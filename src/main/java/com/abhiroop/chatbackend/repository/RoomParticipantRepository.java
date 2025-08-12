package com.abhiroop.chatbackend.repository;

import com.abhiroop.chatbackend.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, UUID> {
}
