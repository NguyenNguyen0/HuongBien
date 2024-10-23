package com.huongbien.entity;

import com.huongbien.utils.Utils;

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

    public Payment() {}

    public Payment(String paymentId, double amount, LocalDate paymentDate, String paymentMethod, LocalTime paymentTime) {
        setPaymentId(paymentId);
        setAmount(amount);
        setPaymentDate(paymentDate);
        setPaymentMethod(paymentMethod);
        setPaymentTime(paymentDate, paymentTime);
    }

    public Payment(double amount, LocalDate paymentDate, String paymentMethod, LocalTime paymentTime) {
        setPaymentId(null);
        setAmount(amount);
        setPaymentDate(paymentDate);
        setPaymentMethod(paymentMethod);
        setPaymentTime(paymentDate, paymentTime);
    }

    public void setPaymentId(String paymentId) {
//        if (paymentId == null) {
//            LocalDate currentDate = LocalDate.now();
//            LocalTime currentTime = LocalTime.now();
//            this.paymentId = String.format("TT%02d%02d%02d%02d%02d%02d%03d",
//                    currentDate.getYear() % 100,
//                    currentDate.getMonthValue(),
//                    currentDate.getDayOfMonth(),
//                    currentTime.getHour(),
//                    currentTime.getMinute(),
//                    currentTime.getSecond(),
//                    Utils.randomNumber(1, 999)
//            );
//            return;
//        }
//        if (paymentId.matches("^TT\\d{15}$")) {
//            this.paymentId = paymentId;
//            return;
//        }
//        throw new IllegalArgumentException("Invalid paymentId format. Expected format: TT-yy-mm-dd-hh-MM-ss-xxx");
        this.paymentId = paymentId;
    }

    public void setAmount(double amount) {
//        if (amount > 0) {
//            this.amount = amount;
//        } else {
//            throw new IllegalArgumentException("Amount must be greater than 0.");
//        }
        this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
//        if (paymentDate == null) {
//            throw new IllegalArgumentException("Payment date cannot be null.");
//        }
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethod(String paymentMethod) {
//        if (paymentMethod.equalsIgnoreCase("Chuyển khoản") ||
//                paymentMethod.equalsIgnoreCase("Tiền mặt")) {
//            this.paymentMethod = paymentMethod;
//            return;
//        }
//        throw new IllegalArgumentException("Payment method must be either 'Chuyển khoản' or 'Tiền mặt'.");
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentTime(LocalDate paymentDate, LocalTime paymentTime) {
//        if (paymentDate == null) {
//            throw new IllegalArgumentException("Payment date cannot be null.");
//        }
//        if (paymentTime == null) {
//            throw new IllegalArgumentException("Payment time cannot be null.");
//        }
        this.paymentTime = paymentTime;
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
