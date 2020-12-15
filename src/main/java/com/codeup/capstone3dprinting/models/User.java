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

    @Column(length = 45)
    private String first_name;

    @Column(length = 45)
    private String last_name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean is_verified;

    @Column(nullable = false)
    private Timestamp joined_at;

    @Column(nullable = false)
    private boolean is_admin;

    @Column
    private String avatar_url;

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
    private List<File> favoriteFiles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_settings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "setting_id")})
    private List<Setting> settings;

    public List<File> getFavoriteFiles() {
        return favoriteFiles;
    }

    public void setFavoriteFiles(List<File> favoriteFiles) {
        this.favoriteFiles = favoriteFiles;
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

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public User() {
    }

    //Create
    public User(String email, String first_name, boolean is_admin, boolean is_verified,
                Timestamp joined_at, String last_name, String password, String username) {
        this.email = email;
        this.first_name = first_name;
        this.is_verified = false;
        this.is_admin = false;
        this.joined_at = joined_at;
        this.last_name = last_name;
        this.password = password;
        this.username = username;

    }
    //Read
    public User(long id, String email, String first_name, boolean is_admin, boolean is_verified,
                Timestamp joined_at, String last_name, String password, String username) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.is_verified = false;
        this.is_admin = false;
        this.joined_at = joined_at;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
    }


    public User(User copy) {
        id = copy.id;
        avatar_url = copy.avatar_url;
        email = copy.email;
        first_name = copy.first_name;
        is_admin = copy.is_admin;
        is_verified = copy.is_verified;
        joined_at = copy.joined_at;
        last_name = copy.last_name;
        password = copy.password;
        username = copy.username;
    }

    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }


}
