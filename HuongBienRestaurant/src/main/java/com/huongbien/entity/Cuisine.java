package com.huongbien.entity;

import java.util.Objects;

public class Cuisine {
    private String cuisineId;
    private String name;
    private double price;
    private String description;
    private byte[] image;
    private Category category;
    //
    private String imgCuisineImg; //test

    public Cuisine() {}

    public Cuisine(String cuisineId, String name, double price, String description, byte[] image, Category category, String imgCuisineImg) {
        setCuisineId(cuisineId);
        setName(name);
        setPrice(price);
        setDescription(description);
        setImage(image);
        setCategory(category);
        this.imgCuisineImg = imgCuisineImg;
    }

    //test data
    public String getImgCuisineImg() {
        return imgCuisineImg;
    }

    public void setImgCuisineImg(String imgCuisineImg) {
        this.imgCuisineImg = imgCuisineImg;
    }
    //test-data

    public void setCuisineId(String cuisineId) {
        if (cuisineId == null || !cuisineId.matches("^M\\d{3}$")) {
            throw new IllegalArgumentException("Invalid cuisine ID format");
        }
        this.cuisineId = cuisineId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = price;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.description = description;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCuisineId() {
        return cuisineId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Cuisine{" +
                "cuisineId='" + cuisineId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuisine cuisine = (Cuisine) o;
        return Objects.equals(cuisineId, cuisine.cuisineId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cuisineId);
    }
}

