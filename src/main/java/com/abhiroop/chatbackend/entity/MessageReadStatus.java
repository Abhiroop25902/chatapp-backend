package com.abhiroop.chatbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "message_read_status", schema = "chat_app",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_message_user", columnNames = {"message_id", "user_id"})
        }
)
public class MessageReadStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_message_read_status_message"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_message_read_status_user"))
    private User user;

    @Builder.Default
    @Column(name = "read_at")
    private LocalDateTime readAt = null;
}
