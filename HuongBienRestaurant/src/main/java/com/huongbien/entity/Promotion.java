package com.huongbien.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {
    private String promotionId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isUsed;
    private double discount;
    private String description;
    private double minimumOrderAmount;

    public Promotion() {}

    public Promotion(String promotionId, String name, LocalDate startDate, LocalDate endDate,
                     boolean isUsed, double discount, String description, double minimumOrderAmount) {
        setPromotion_id(promotionId);
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setIsUsed(isUsed);
        setDiscount(discount);
        setDescription(description);
        setMinimumOrderAmount(minimumOrderAmount);
    }

    public void setPromotion_id(String promotionId) {
        if (promotionId == null || promotionId.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            String dateStr = String.format("%02d%02d%02d", currentDate.getYear() % 100,
                    currentDate.getMonthValue(), currentDate.getDayOfMonth());
            this.promotionId = "KM" + dateStr + String.format("%03d", 1);
        } else {
            this.promotionId = promotionId;
        }
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("'name': tên khuyến mãi không được để trống");
        }
        this.name = name;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate != null && startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("startDate phải trước hoặc là ngày hiện tại");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate phải sau ngày bắt đầu mở giảm giá");
        }
        this.endDate = endDate;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void setDiscount(double discount) {
        if (discount <= 0) {
            throw new IllegalArgumentException("discount phải lớn hơn 0");
        }
        this.discount = discount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinimumOrderAmount(double minimumOrderAmount) {
        if (minimumOrderAmount <= 0) {
            throw new IllegalArgumentException("MinimumOrderAmount phải lớn hơn 0");
        }
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public String getPromotion_id() {
        return promotionId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public double getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId='" + promotionId + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isUsed=" + isUsed +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", minimumOrderAmount=" + minimumOrderAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promotion promotion = (Promotion) o;
        return Objects.equals(promotionId, promotion.promotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(promotionId);
    }
}
