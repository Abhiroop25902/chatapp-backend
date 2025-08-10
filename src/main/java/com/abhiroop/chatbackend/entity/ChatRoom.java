package com.abhiroop.chatbackend.entity;

import com.abhiroop.chatbackend.lib.ChatRoomType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_rooms", schema = "chat_app")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_name")
    private String name;

    @Column(name = "room_description")
    private String description;


    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_chat_room_user"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User createdBy;

    // NOTE: adding column definition makes this app bound to Azure SQL

    @Column(name = "max_participants", nullable = false, columnDefinition = "INT CHECK (max_participants IN (2, 50))")
    private int maxParticipants;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
