package com.abhiroop.chatbackend.repository;

import com.abhiroop.chatbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // db contains fulltext index on message content
    @Query(nativeQuery = true,
            value = """
                    select m.*
                    from chat_app.messages m
                    where m.chat_room_id = :room_id
                        and CONTAINS(content, :keyword)
                    """)
    List<Message> searchByKeywordInChatRoom(@Param("room_id") UUID roomId, @Param("keyword") String keyword);


}
