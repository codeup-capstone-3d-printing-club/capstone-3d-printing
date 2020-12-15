package com.codeup.capstone3dprinting.models;

import javax.persistence.*;

@Entity
@Table(name = "reasons")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;
  
    public Reason(){}

    public Reason(long id, String description){
        this.id = id;
        this.description = description;
    }

    public Reason(String description){
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
  
   
}
