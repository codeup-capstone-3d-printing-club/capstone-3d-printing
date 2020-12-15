package com.codeup.capstone3dprinting.models;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Comment(){}

    public Comment(long id, String comment, Timestamp createdAt, File file, User owner){
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.file = file;
        this.owner = owner;
    }
    public Comment(String comment, Timestamp createdAt, File file, User owner){
        this.comment = comment;
        this.createdAt = createdAt;
        this.file = file;
        this.owner = owner;
    }

    public Comment(Comment copy){
        id = copy.id;
        comment = copy.comment;
        createdAt = copy.createdAt;
        file = copy.file;
        owner = copy.owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
