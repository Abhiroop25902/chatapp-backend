package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.entity.RoomParticipant;
import com.abhiroop.chatbackend.lib.enums.ParticipantRole;
import com.abhiroop.chatbackend.repository.RoomParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoomParticipantService {

    private final RoomParticipantRepository roomParticipantRepository;

    public RoomParticipantService(RoomParticipantRepository roomParticipantRepository) {
        this.roomParticipantRepository = roomParticipantRepository;
    }

    public boolean canUserSendMessageInChatRoom(UUID userId, UUID roomId) {
        return roomParticipantRepository.findByUser_IdAndChatRoom_Id(userId, roomId)
                .map(roomParticipant -> roomParticipant.getParticipantRole() != null)
                .orElse(false);
    }

    public Optional<ParticipantRole> getParticipantRole(UUID userId, UUID roomId) {
        final var roomParticipantOptional = roomParticipantRepository.findByUser_IdAndChatRoom_Id(userId, roomId);

        return roomParticipantOptional.map(RoomParticipant::getParticipantRole);
    }

    public RoomParticipant save(RoomParticipant roomParticipant) {
        return roomParticipantRepository.save(roomParticipant);
    }
}
