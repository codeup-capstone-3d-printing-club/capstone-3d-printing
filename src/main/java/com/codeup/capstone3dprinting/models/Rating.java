package com.codeup.capstone3dprinting.models;

import com.mysql.cj.Session;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

    @ManyToOne
    private File file;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

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
}
