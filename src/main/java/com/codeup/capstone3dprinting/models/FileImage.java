package com.codeup.capstone3dprinting.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="file_imgs")
public class FileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(length=255)
    private String img_url;

    public FileImage(){}

    public FileImage(long id, File file, String img_url){
        this.id = id;
        this.file = file;
        this.img_url = img_url;
    }

    public FileImage(File file, String img_url){
        this.file = file;
        this.img_url = img_url;
    }

    public FileImage(FileImage copy){
        id = copy.id;
        file = copy.file;
        img_url = copy.img_url;
    }
}
