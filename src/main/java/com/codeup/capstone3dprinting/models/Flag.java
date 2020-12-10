package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="flags")
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private File file_id;

    @ManyToOne
    private User reporter_id;

    @ManyToOne
    private Reason reason;

}
