package com.codeup.capstone3dprinting.models;

import javax.persistence.*;

@Entity
@Table(name = "reasons")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String reason_description;
  
    public Reason(){}

    public Reason(long id, String reason_description){
        this.id = id;
        this.reason_description = reason_description;
    }

    public Reason(String reason_description){
        this.reason_description = reason_description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getReason_description() {
        return reason_description;
    }

    public void setReason_description(String reason_description) {
        this.reason_description = reason_description;
    }
  
   
}
