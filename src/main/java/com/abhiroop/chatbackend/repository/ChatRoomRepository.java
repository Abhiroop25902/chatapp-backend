package com.abhiroop.chatbackend.repository;

import com.abhiroop.chatbackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {


    @Query("""
            select rp.chatRoom
            from RoomParticipant rp
            where rp.user.id = :userId
                and rp.isActive = true
                and rp.leftAt is null
                and rp.chatRoom.isActive = true
            """)
    List<ChatRoom> findActiveChatRoomsByUserId(UUID userId);
}
