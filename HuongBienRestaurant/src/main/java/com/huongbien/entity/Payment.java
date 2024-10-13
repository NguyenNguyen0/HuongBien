package com.huongbien.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Payment {
    private String paymentId;
    private double amount;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private String paymentMethod;

    public Payment(String paymentId, double amount, LocalDate paymentDate, String paymentMethod, LocalTime paymentTime) {
        setPaymentId(paymentId);
        setAmount(amount);
        setPaymentDate(paymentDate);
        setPaymentMethod(paymentMethod);
        setPaymentTime(paymentDate, paymentTime);
    }

    public void setPaymentId(String paymentId) {
        if (paymentId != null && paymentId.matches("^P\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{3}$")) {
            this.paymentId = paymentId;
        } else {
            throw new IllegalArgumentException("Invalid paymentId format. Expected format: P-yy-mm-dd-hh-MM-xxx");
        }
    }

    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate.isBefore(LocalDate.now()) || paymentDate.isEqual(LocalDate.now())) {
            this.paymentDate = paymentDate;
        } else {
            throw new IllegalArgumentException("Payment date must be today or in the past.");
        }
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod.equalsIgnoreCase("Chuyển khoản") ||
                paymentMethod.equalsIgnoreCase("Tiền mặt")) {
            this.paymentMethod = paymentMethod;
        } else {
            throw new IllegalArgumentException("Payment method must be either 'Chuyển khoản' or 'Tiền mặt'.");
        }
    }

    public void setPaymentTime(LocalDate paymentDate, LocalTime paymentTime) {
        LocalTime tenAM = LocalTime.of(10, 0);
        LocalTime tenPM = LocalTime.of(22, 0);

        if (paymentDate.isEqual(LocalDate.now())) {
            if (paymentTime.isBefore(LocalTime.now()) || paymentTime.equals(LocalTime.now())) {
                this.paymentTime = paymentTime;
            } else {
                throw new IllegalArgumentException("Payment time for today must be before or equal to the current time.");
            }
        } else if (paymentDate.isBefore(LocalDate.now())) {
            if (paymentTime.isAfter(tenAM) && paymentTime.isBefore(tenPM)) {
                this.paymentTime = paymentTime;
            } else {
                throw new IllegalArgumentException("Payment time for past dates must be between 10:00 and 22:00.");
            }
        } else {
            throw new IllegalArgumentException("Payment date cannot be in the future.");
        }
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalTime getPaymentTime() {
        return paymentTime;
    }

    public static String generatePaymentId(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm");
        String datePart = dateTime.format(formatter);
        String randomPart = String.format("%03d", (int) (Math.random() * 1000));
        return "P-" + datePart + "-" + randomPart;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentTime=" + paymentTime +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(paymentId);
    }
}
