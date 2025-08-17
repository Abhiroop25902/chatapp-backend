package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.repository.RoomParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomParticipantService {

    private final RoomParticipantRepository roomParticipantRepository;

    public RoomParticipantService(RoomParticipantRepository roomParticipantRepository) {
        this.roomParticipantRepository = roomParticipantRepository;
    }

    public boolean validateUserRoomParticipation(UUID userId, UUID roomId) {
        return roomParticipantRepository.findByUser_IdAndChatRoom_Id(userId, roomId)
                .map(roomParticipant -> roomParticipant.getParticipantRole() != null)
                .orElse(false);
    }
}
