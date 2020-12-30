package com.codeup.capstone3dprinting.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 45, unique = true)
    private String username;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "joined_at", nullable = false)
    private Timestamp joinedAt;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_flagged")
    private boolean isFlagged;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "owner")
    private List<File> files;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "follows",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "follow_id")})
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "follows",
            joinColumns = {@JoinColumn(name = "follow_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "favorites",
            joinColumns = {@JoinColumn(name = "liker_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id")})
    private List<File> favorites = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "user_settings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "setting_id")})
    private List<Setting> settings = new ArrayList<>();

    public User() {
    }

    public User(User copy) {
        id = copy.id;
        avatarUrl = copy.avatarUrl;
        email = copy.email;
        firstName = copy.firstName;
        isAdmin = copy.isAdmin;
        isVerified = copy.isVerified;
        joinedAt = copy.joinedAt;
        lastName = copy.lastName;
        password = copy.password;
        username = copy.username;
        isFlagged = copy.isFlagged;
        isActive = copy.isActive;
        files = copy.files;
        users = copy.users;
        favorites = copy.favorites;
        settings = copy.settings;
        followers = copy.followers;
    }
}
