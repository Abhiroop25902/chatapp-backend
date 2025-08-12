package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.entity.Message;
import com.abhiroop.chatbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.abhiroop.chatbackend.lib.Constants.MESSAGE_PAGE_SIZE;

@Service
public class MessageService {
    final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     *
     * @param roomId     UUID for the chat room
     * @param pageNumber zero indexed
     * @return
     */
    public Page<Message> getMessagesForRoomPaginated(UUID roomId, int pageNumber) {
        return messageRepository.findByRoom_Id(
                roomId,
                PageRequest.of(pageNumber, MESSAGE_PAGE_SIZE, Sort.by("sentAt").descending())
        );
    }
}
