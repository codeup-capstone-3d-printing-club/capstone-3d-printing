package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "files")
public class File implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "file_category",
            joinColumns = { @JoinColumn(name = "file_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private File file;

   public File() {}
  
    //Create
    public File(long id, String file_url, String file_title, Timestamp created_at,
                Timestamp updated_at, String description, String img_url, boolean is_private, File file) {
        this.id = id;
        this.file_url = file_url;
        this.file_title = file_title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.img_url = img_url;
        this.is_private = is_private;
        this.file = file;
    }

    //Read
    public File(String file_url, String file_title, Timestamp created_at,
                Timestamp updated_at, String description, String img_url, boolean is_private, File file) {
        this.file_url = file_url;
        this.file_title = file_title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.img_url = img_url;
        this.is_private = is_private;
        this.file = file;
    }

    public File(File copy) {
        id = copy.id;
        file_url = copy.file_url;
        file_title = copy.file_title;
        created_at = copy.created_at;
        updated_at = copy.updated_at;
        description = copy.description;
        img_url = copy.img_url;
        is_private = copy.is_private;
        file = copy.file;
    }

    public long getId() {
        return id;
    }

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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
