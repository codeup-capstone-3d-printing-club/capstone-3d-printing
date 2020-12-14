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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public File getFile_id() {
        return file_id;
    }

    public void setFile_id(File file_id) {
        this.file_id = file_id;
    }

    public User getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(User reporter_id) {
        this.reporter_id = reporter_id;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }
}
