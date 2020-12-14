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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSetting_description() {
        return setting_description;
    }

    public void setSetting_description(String setting_description) {
        this.setting_description = setting_description;
    }
}
