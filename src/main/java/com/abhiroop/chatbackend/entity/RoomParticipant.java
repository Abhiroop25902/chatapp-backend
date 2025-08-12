package com.abhiroop.chatbackend.entity;

import com.abhiroop.chatbackend.lib.RoomParticipantRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "room_participants", schema = "chat_app",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_chat_room_user",
                        columnNames = {"room_id", "user_id"}
                )},
        indexes = {
                @Index(name = "idx_room_id", columnList = "room_id")
        }
)
public class RoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_room_participant_chat_room"))
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_room_participant_user"))
    private User user;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Builder.Default
    @Column(name = "left_at")
    private LocalDateTime leftAt = null;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "room_participant_role", nullable = false)
    private RoomParticipantRole roomParticipantRole = RoomParticipantRole.MEMBER;

    @Builder.Default
    @Column(name = "is_active", nullable = false, columnDefinition = "BIT DEFAULT 1")
    private Boolean isActive = true;

    @PreUpdate
    public void preUpdate() {
        if (this.leftAt == null && Boolean.FALSE.equals(isActive)) {
            this.leftAt = LocalDateTime.now();
        }
    }
}
