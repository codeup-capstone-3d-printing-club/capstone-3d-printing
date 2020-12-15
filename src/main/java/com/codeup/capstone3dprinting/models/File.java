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

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "file_category",
            joinColumns = { @JoinColumn(name = "file_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private List<Category> categories;

   public File() {}
  
    //Create
    public File(long id, String fileUrl, String title, Timestamp createdAt,
                Timestamp updatedAt, String description, String imgUrl, boolean isPrivate, User owner) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.imgUrl = imgUrl;
        this.isPrivate = isPrivate;
        this.owner = owner;
    }

    //Read
    public File(String fileUrl, String title, Timestamp createdAt,
                Timestamp updatedAt, String description, String imgUrl, boolean isPrivate, User owner) {
        this.fileUrl = fileUrl;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.imgUrl = imgUrl;
        this.isPrivate = isPrivate;
        this.owner = owner;
    }

    public File(File copy) {
        id = copy.id;
        fileUrl = copy.fileUrl;
        title = copy.title;
        createdAt = copy.createdAt;
        updatedAt = copy.updatedAt;
        description = copy.description;
        imgUrl = copy.imgUrl;
        isPrivate = copy.isPrivate;
        owner = copy.owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
