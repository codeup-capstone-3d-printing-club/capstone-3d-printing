package com.codeup.capstone3dprinting.models;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File file_id;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private Timestamp created_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(User owner_id) {
        this.owner_id = owner_id;
    }

    public File getFile_id() {
        return file_id;
    }

    public void setFile_id(File file_id) {
        this.file_id = file_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
