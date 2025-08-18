package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.ChatRoomCreateRequestDto;
import com.abhiroop.chatbackend.dto.ChatRoomCreateResponseDto;
import com.abhiroop.chatbackend.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatRoomController {

    final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Secured("ROLE_USER")
    @PutMapping("/api/v1/chatroom")
    ResponseEntity<ChatRoomCreateOrUpdateResponseDto> createNewChatRoom(@RequestBody @Valid ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        final var chatRoom = chatRoomService.createChatRoom(chatRoomCreateRequestDto);

        log.info("Created chat room with id: {}", chatRoom.getId());

        return ResponseEntity.ok().body(
                ChatRoomCreateOrUpdateResponseDto.builder()
                        .chatRoomId(chatRoom.getId())
                        .chatRoomName(chatRoom.getName())
                        .chatRoomDescription(chatRoom.getDescription())
                        .roomType(chatRoom.getType())
                        .createdBy(chatRoom.getCreatedBy().getId())
                        .isActive(chatRoom.isActive())
                        .maxParticipant(chatRoom.getMaxParticipants())
                        .createdAt(chatRoom.getCreatedAt())
                        .updatedAt(chatRoom.getUpdatedAt())
                        .build()
        );
    }


}
