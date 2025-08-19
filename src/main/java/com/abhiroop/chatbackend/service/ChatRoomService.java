package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.dto.ChatRoomCreateRequestDto;
import com.abhiroop.chatbackend.dto.ChatRoomPatchRequestDto;
import com.abhiroop.chatbackend.entity.ChatRoom;
import com.abhiroop.chatbackend.entity.RoomParticipant;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.exception.ChatRoomEditNotAuthorizedException;
import com.abhiroop.chatbackend.exception.ChatRoomNotFoundException;
import com.abhiroop.chatbackend.lib.enums.ParticipantRole;
import com.abhiroop.chatbackend.lib.enums.RoomType;
import com.abhiroop.chatbackend.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatRoomService {
    final ChatRoomRepository chatRoomRepository;
    final RoomParticipantService roomParticipantService;
    final UserService userService;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository, RoomParticipantService roomParticipantService, UserService userService) {
        this.chatRoomRepository = chatRoomRepository;
        this.roomParticipantService = roomParticipantService;
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    public List<ChatRoom> getActiveChatRoomsForUser() {
        final var user = userService.getCurrentUser();
        return chatRoomRepository.findActiveChatRoomsByUserId(user.getId());
    }

    @Secured("ROLE_USER")
    public List<ChatRoom> getActiveDmRoomsForCurrentUser() {
        final var user = userService.getCurrentUser();
        return chatRoomRepository.findActiveDmChatRoomByUserId(user.getId());
    }

    @Secured("ROLE_USER")
    public ChatRoom createChatRoom(ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        final var user = userService.getCurrentUser();

        final var chatRoom = ChatRoom.builder()
                .name(chatRoomCreateRequestDto.roomName())
                .description(chatRoomCreateRequestDto.roomDescription())
                .createdBy(user)
                .type(chatRoomCreateRequestDto.roomType())
                .maxParticipants(chatRoomCreateRequestDto.roomType() == RoomType.DIRECT_MESSAGE ? 2 : 50)
                .build();

        final var savedChatRoom = chatRoomRepository.save(chatRoom);

        // will also have to create a Room participant instance to define that current user is owner of the Chat Room
        roomParticipantService.save(
                RoomParticipant.builder()
                        .chatRoom(savedChatRoom)
                        .user(user)
                        .participantRole(ParticipantRole.OWNER)
                        .build()
        );

        return savedChatRoom;
    }

    @Transactional
    @Secured("ROLE_USER")
    public ChatRoom updateChatRoomDetails(ChatRoomPatchRequestDto chatRoomPatchRequestDto) {
        final var user = userService.getCurrentUser();

        final var presentChatRoom = getChatRoomOrThrowException(chatRoomPatchRequestDto.chatRoomId());

        verifyEditAccessOfUserForChatRoom(user, presentChatRoom);

        boolean updated = false;
        if (chatRoomPatchRequestDto.newRoomDescription() != null) {
            presentChatRoom.setDescription(chatRoomPatchRequestDto.newRoomDescription());
            updated = true;
        }

        if (chatRoomPatchRequestDto.newRoomName() != null) {
            presentChatRoom.setName(chatRoomPatchRequestDto.newRoomName());
            updated = true;
        }

        return updated ? chatRoomRepository.save(presentChatRoom) : presentChatRoom;
    }

    @Transactional
    @Secured("ROLE_USER")
    public void deleteChatRoom(UUID chatRoomId) {
        final var user = userService.getCurrentUser();
        final var chatRoom = getChatRoomOrThrowException(chatRoomId);

        verifyEditAccessOfUserForChatRoom(user, chatRoom);

        if (!chatRoom.isActive()) return;

        chatRoom.setActive(false);
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoomOrThrowException(UUID chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new ChatRoomNotFoundException("Chat Room Not Found", chatRoomId)
        );
    }


    private void verifyEditAccessOfUserForChatRoom(User user, ChatRoom room) {
        // only owner and admin are allowed to update
        final var roomParticipantOptional = roomParticipantService.getParticipantRole(user.getId(), room.getId());

        if (roomParticipantOptional.isEmpty() || !roomParticipantOptional.get().hasEditAccess())
            throw new ChatRoomEditNotAuthorizedException("This User is not authorized to edit this chat room", user.getId());
    }
}
