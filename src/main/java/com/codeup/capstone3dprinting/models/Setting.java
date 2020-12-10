package com.codeup.capstone3dprinting.models;

import javax.persistence.*;

@Entity
@Table(name="settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String setting_description;

}
