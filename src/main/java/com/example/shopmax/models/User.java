package com.example.shopmax.models;

import com.example.shopmax.helper.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Name field is required!")
    @Size(min = 2, max = 12, message = "character should be between 2 and 12")
    private String name;
    //    @NotBlank(message = "password field is required!")
    private String password;


    private String email;
    private String role;
    private String imageUrl;

    public User() {

    }

    public User(String name, String password, String email, String role, String imageUrl) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
