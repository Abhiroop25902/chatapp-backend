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
            value = "SELECT * FROM chat_app.messages WHERE CONTAINS(content, :keyword)")
    List<Message> searchByKeyword(@Param("keyword") String keyword);
}
