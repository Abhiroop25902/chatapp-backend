package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.MessageCreateOrUpdateResponseDto;
import com.abhiroop.chatbackend.dto.MessageCreateRequestDto;
import com.abhiroop.chatbackend.entity.Message;
import com.abhiroop.chatbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @Secured("ROLE_USER")
    @PutMapping("/api/v1/message")
    ResponseEntity<MessageCreateOrUpdateResponseDto> addMessage(@RequestBody MessageCreateRequestDto requestDto) {
        final var message = messageService.addMessage(requestDto);

        return ResponseEntity.ok().body(
                MessageCreateOrUpdateResponseDto.builder()
                        .messageId(message.getId())
                        .chatRoomId(message.getRoom().getId())
                        .senderId(message.getSender().getId())
                        .messageType(message.getMessageType())
                        .content(message.getContent())
                        .replyToMessageId(
                                Optional.ofNullable(message.getReplyToMessage())
                                        .map(Message::getId)
                                        .orElse(null)
                        )
                        .sentAt(message.getSentAt())
                        .editedAt(message.getEditedAt())
                        .build()
        );
    }

}
