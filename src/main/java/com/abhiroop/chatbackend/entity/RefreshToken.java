package com.abhiroop.chatbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import static com.abhiroop.chatbackend.lib.Constants.REFRESH_TOKENS_TABLE_NAME;
import static com.abhiroop.chatbackend.lib.Constants.SCHEMA_NAME;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = REFRESH_TOKENS_TABLE_NAME, schema = SCHEMA_NAME)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_refresh_token_user"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(name = "expire_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expireAt;

}
