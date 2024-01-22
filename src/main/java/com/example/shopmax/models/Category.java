package com.example.shopmax.models;


import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;


    // Constructors, getters, setters, etc.


    public Category() {

    }

    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
