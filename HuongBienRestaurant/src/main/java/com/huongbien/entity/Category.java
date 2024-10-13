package com.huongbien.entity;

import java.util.Objects;

public class Category {
    private String categoryId;
    private String name;
    private String description;

    public Category(String categoryId, String name, String description) {
        setCategoryId(categoryId);
        setName(name);
        setDescription(description);
    }

    public void setCategoryId(String categoryId) {
        if (categoryId != null && categoryId.matches("^CG\\d{3}$")) {
            this.categoryId = categoryId;
        } else {
            throw new IllegalArgumentException("Invalid categoryId format. Expected format: CGxxx (e.g., CG002).");
        }
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static String generateCategoryId(int number) {
        return String.format("CG%03d", number);
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }
}
