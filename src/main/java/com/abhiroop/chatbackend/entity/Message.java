package com.abhiroop.chatbackend.entity;

import com.abhiroop.chatbackend.lib.ChatMessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages", schema = "chat_app")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_chat_room"), updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_sender"), updatable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private ChatMessageType messageType;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "reply_to_message_id", foreignKey = @ForeignKey(name = "fk_message_replyTo_message"))
    @Builder.Default
    private Message replyToMessage = null;

    @Builder.Default
    @Column(name = "is_deleted", updatable = false, nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;


    @UpdateTimestamp
    @Column(name = "edited_at", nullable = false)
    private LocalDateTime editedAt;

}
