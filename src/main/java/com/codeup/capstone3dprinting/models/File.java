package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 500)
    private String file_url;

    @Column(nullable = false, length = 100)
    private String file_title;

    @Column(nullable = false)
    private Timestamp created_at;

    @Column(nullable = false)
    private Timestamp updated_at;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column
    private String img_url;

    @Column(nullable = false)
    private boolean is_private;

    @OneToOne
    private User owner;

    public long getId() {
        return id;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "file_category",
            joinColumns = { @JoinColumn(name = "file_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private List<Category> categories;

    public void setId(long id) {
        this.id = id;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_title() {
        return file_title;
    }

    public void setFile_title(String file_title) {
        this.file_title = file_title;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
