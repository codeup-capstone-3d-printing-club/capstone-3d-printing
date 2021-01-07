package com.codeup.capstone3dprinting.models;

import com.codeup.capstone3dprinting.repos.CommentRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @Autowired
    @Transient
    private CommentRepository commentRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner;

    @ManyToOne
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    public void setChildren() {
        this.children = commentRepository.findByParent(this);
    }
}
