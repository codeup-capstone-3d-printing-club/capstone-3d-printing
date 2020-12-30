package com.codeup.capstone3dprinting.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter @Setter
@Table(name="flags")
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private File file;

    @ManyToOne
    private User reporter;

    @ManyToOne
    private Reason reason;

}
