package com.donkeys_today.server.domain.reply;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.donkeys_today.server.domain.user.User;
import com.donkeys_today.server.support.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "replies")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean is_read;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
