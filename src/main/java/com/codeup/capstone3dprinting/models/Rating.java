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

}
