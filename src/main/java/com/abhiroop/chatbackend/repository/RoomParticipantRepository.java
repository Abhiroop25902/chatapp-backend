package com.abhiroop.chatbackend.repository;

import com.abhiroop.chatbackend.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, UUID> {

    Optional<RoomParticipant> findByUser_IdAndChatRoom_Id(UUID userId, UUID roomId);
}
