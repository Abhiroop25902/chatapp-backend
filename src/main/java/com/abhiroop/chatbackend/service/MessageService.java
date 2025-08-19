package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.dto.MessageCreateRequestDto;
import com.abhiroop.chatbackend.entity.Message;
import com.abhiroop.chatbackend.exception.InvalidMessageException;
import com.abhiroop.chatbackend.lib.enums.MessageType;
import com.abhiroop.chatbackend.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.abhiroop.chatbackend.lib.Constants.MESSAGE_PAGE_SIZE;

@Service
public class MessageService {
    final MessageRepository messageRepository;
    final UserService userService;
    final ChatRoomService chatRoomService;
    final RoomParticipantService roomParticipantService;
    private final UrlValidatorService urlValidatorService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService, ChatRoomService chatRoomService, RoomParticipantService roomParticipantService, UrlValidatorService urlValidatorService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.roomParticipantService = roomParticipantService;
        this.urlValidatorService = urlValidatorService;
    }

    /**
     *
     * @param roomId     UUID for the chat room
     * @param pageNumber zero indexed
     * @return
     */
    public Page<Message> getMessagesForRoomPaginated(UUID roomId, int pageNumber) {
        return messageRepository.findByRoom_Id(
                roomId,
                PageRequest.of(pageNumber, MESSAGE_PAGE_SIZE, Sort.by("sentAt").descending())
        );
    }

    @Secured("ROLE_USER")
    @Transactional
    public Message addMessage(MessageCreateRequestDto messageCreateRequestDto) {
        //user must be valid via jwt
        final var sender = userService.getCurrentUser();

        //chatroom must be valid
        final var chatRoom = chatRoomService.getChatRoomOrThrowException(messageCreateRequestDto.chatRoomId());

        if (messageCreateRequestDto.messageType() == MessageType.SYSTEM) {
            throw new InvalidMessageException("System Messages cannot be sent by users, this will be reported", sender.getId(), chatRoom.getId());
        }

        String messageContent = messageCreateRequestDto.content();

        // Not TEXT means it will either be IMAGE or FILE
        if (messageCreateRequestDto.messageType() != MessageType.TEXT && !urlValidatorService.isValid(messageContent)) {
            throw new InvalidMessageException("Invalid message content: " + messageContent, sender.getId(), chatRoom.getId());
        }

        //user must have chat room participation
        //any ParticipantRole can add their message
        if (!roomParticipantService.canUserSendMessageInChatRoom(sender.getId(), chatRoom.getId())) {
            throw new InvalidMessageException("User is not part of this chat room, this will be reported", sender.getId(), chatRoom.getId());
        }

        final var replyToMessage = Optional.ofNullable(messageCreateRequestDto.replyMessageId())
                .flatMap(messageRepository::findById)
                .filter(m -> m.getRoom().getId().equals(chatRoom.getId()))
                .orElse(null);

        return messageRepository.save(
                Message.builder()
                        .room(chatRoom)
                        .sender(sender)
                        .replyToMessage(replyToMessage)
                        .messageType(messageCreateRequestDto.messageType())
                        .content(messageContent)
                        .build()
        );
    }
}
