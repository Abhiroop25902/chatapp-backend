package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.entity.ChatRoom;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {
    final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Secured("ROLE_USER")
    public List<ChatRoom> getActiveChatRoomsForUser() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return chatRoomRepository.findActiveChatRoomsByUserId(user.getId());
    }
}
