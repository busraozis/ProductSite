package com.dolap.casestudy.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String explanation;

    private double price;

    @ManyToOne
    private User seller;

    @ManyToOne
    private Category category;

    Product(){}

    public Product(String name, String explanation, double price, User seller, Category category){
        this.name = name;
        this.explanation = explanation;
        this.price = price;
        this.seller = seller;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
