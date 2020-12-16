package com.codeup.capstone3dprinting.models;

import com.mysql.cj.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

    @ManyToOne
    private File file;

    @Column(nullable = false)
    private int rating;

    public Rating(){}

    public Rating(long id, int rating, File file){
        this.id = id;
        this.rating = rating;
        this.file = file;
    }

    public Rating(int rating, File file){
        this.rating = rating;
        this.file = file;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}
