package com.huongbien.entity;

import java.util.Objects;

public class FoodOrder {
    private String foodOrderId;
    private int quantity;
    private String note;
    private double salePrice;
    private final Cuisine cuisine;

    public FoodOrder(double salePrice, String note, int quantity, String foodOrderId, Cuisine cuisine) {
        this.salePrice = salePrice;
        this.note = note;
        this.quantity = quantity;
        this.foodOrderId = foodOrderId;
        this.cuisine = cuisine;
    }

    public void setFoodOrderId(String foodOrderId) {
        if (foodOrderId == null || !foodOrderId.matches("^FO\\d{13}$")) {
            throw new IllegalArgumentException("Invalid food order ID format");
        }

        this.foodOrderId = foodOrderId;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        this.quantity = quantity;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSalePrice(double salePrice) {
        if (salePrice <= 0) {
            throw new IllegalArgumentException("Sale price must be greater than 0");
        }

        this.salePrice = salePrice;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public String getFoodOrderId() {
        return foodOrderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public double getSalePrice() {
        return salePrice;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "foodOrderId='" + foodOrderId + '\'' +
                ", quantity=" + quantity +
                ", note='" + note + '\'' +
                ", salePrice=" + salePrice +
                ", cuisine=" + cuisine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodOrder foodOrder = (FoodOrder) o;
        return Objects.equals(foodOrderId, foodOrder.foodOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(foodOrderId);
    }
}
