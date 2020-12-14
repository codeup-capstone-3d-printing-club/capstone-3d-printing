package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
@Entity
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

    @ManyToOne
    private User rater_id;

    @ManyToOne
    private File file_id;

    @Column(nullable = false)
    private int rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRater_id() {
        return rater_id;
    }

    public void setRater_id(User rater_id) {
        this.rater_id = rater_id;
    }

    public File getFile_id() {
        return file_id;
    }

    public void setFile_id(File file_id) {
        this.file_id = file_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
