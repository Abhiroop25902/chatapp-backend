package com.abhiroop.chatbackend.repository;

import com.abhiroop.chatbackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
                        select distinct cr
                        from RoomParticipant a
                            inner join RoomParticipant b on a.chatRoom = b.chatRoom
                            inner join ChatRoom cr on a.chatRoom.id = cr.id
                        where a.isActive = true
                            and a.leftAt is null
                            and b.isActive = true
                            and b.leftAt is null
                            and cr.isActive = true
                            and cr.type = 'DIRECT_MESSAGE'
                            and a.user <> b.user
                            and a.user.id = :a_user_id
            
            """)
    List<ChatRoom> findActiveDmChatRoomByUserId(@Param("a_user_id") UUID userId);
}
