package com.codeup.capstone3dprinting.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "sent_at", nullable = false)
    private Timestamp sentAt;

    @Column(nullable = false)
    private boolean unread;

    public Message() {

    }

    public Message(String message, Timestamp timestamp, User recipient, User sender) {
        this.message = message;
        this.sentAt = timestamp;
        this.recipient = recipient;
        this.sender = sender;
        this.unread = true;
    }
}
