package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSent_at(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
