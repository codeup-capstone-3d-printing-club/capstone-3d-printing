package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 45)
    private String username;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(nullable = false)
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<File> files;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "follows",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "follow_id")})
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "favorites",
            joinColumns = {@JoinColumn(name = "liker_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id")})
    private List<File> favorites;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_settings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "setting_id")})
    private List<Setting> settings;

    public List<File> getFavoriteFiles() {
        return favorites;
    }

    public void setFavoriteFiles(List<File> favoriteFiles) {
        this.favorites = favoriteFiles;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public List<File> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<File> favorites) {
        this.favorites = favorites;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public User() {
    }

    //Create
    public User(String email, String firstName, boolean isAdmin, boolean isVerified,
                Timestamp joinedAt, String lastName, String password, String username, boolean isFlagged, boolean isActive) {
        this.email = email;
        this.firstName = firstName;
        this.isVerified = false;
        this.isAdmin = false;
        this.joinedAt = joinedAt;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.isFlagged = isFlagged;
        this.isActive = isActive;
    }

    //Read
    public User(long id, String email, String firstName, boolean isAdmin, boolean isVerified,
                Timestamp joinedAt, String lastName, String password, String username, boolean isFlagged, boolean isActive) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.isVerified = isVerified;
        this.isAdmin = isAdmin;
        this.joinedAt = joinedAt;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.isFlagged = isFlagged;
        this.isActive = isActive;
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
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isFlagged(){
        return this.isFlagged;
    }

    public void setFlagged(boolean isFlagged){
        this.isFlagged = isFlagged;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
}
