package com.huongbien.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Order {
    private String orderId;
    private LocalDate orderDate;
    private String notes;
    private final double vatTax = 0.1;
    private double paymentAmount;
    private double dispensedAmount;
    private double totalAmount;
    private double discount;
    private ArrayList<OrderDetail> orderDetails = new ArrayList<>();
    private Promotion promotion;
    private Payment payment;
    private ArrayList<Table> tables = new ArrayList<>();
    private Customer customer;
    private Employee employee;

    public Order() {}

    public Order(String orderId, LocalDate orderDate, String notes, double paymentAmount,
                 double dispensedAmount, double totalAmount, double discount,
                 ArrayList<OrderDetail> orderDetails, Promotion promotion, Payment payment,
                 ArrayList<Table> tables, Customer customer, Employee employee) {
        setOrderId(orderId);
        setOrderDate(orderDate);
        setNotes(notes);
        setPaymentAmount(paymentAmount);
        setDispensedAmount(dispensedAmount);
        setTotalAmount(totalAmount);
        setDiscount(discount);
        setOrderDetails(orderDetails);
        setPromotion(promotion);
        setPayment(payment);
        setTables(tables);
        setCustomer(customer);
        setEmployee(employee);
    }

    public void setOrderId(String orderId) {
        if (orderId == null || !orderId.matches("^HD\\d{4}\\d{4}\\d{3}$")) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
        this.orderId = orderId;
    }

    public void setOrderDate(LocalDate orderDate) {
        if (orderDate == null || !orderDate.equals(LocalDate.now())) {
            throw new IllegalArgumentException("Order date must be today's date");
        }
        this.orderDate = orderDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPaymentAmount(double paymentAmount) {
        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        this.paymentAmount = paymentAmount;
    }

    public void setDispensedAmount(double dispensedAmount) {
        if (dispensedAmount <= 0) {
            throw new IllegalArgumentException("Dispensed amount must be greater than 0");
        }
        this.dispensedAmount = dispensedAmount;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }
        this.totalAmount = totalAmount;
    }

    public void setDiscount(double discount) {
        if (discount < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        this.discount = discount;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null");
        }
        this.orderDetails = orderDetails;
    }

    public void setPromotion(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
        this.promotion = promotion;
    }

    public void setPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        this.payment = payment;
    }

    public void setTables(ArrayList<Table> tables) {
        if (tables == null) {
            throw new IllegalArgumentException("Tables cannot be null");
        }
        this.tables = tables;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public double getVatTax() {
        return vatTax;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public double getDispensedAmount() {
        return dispensedAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Payment getPayment() {
        return payment;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", notes='" + notes + '\'' +
                ", vatTax=" + vatTax +
                ", paymentAmount=" + paymentAmount +
                ", dispensedAmount=" + dispensedAmount +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", orderDetails=" + orderDetails +
                ", promotion=" + promotion +
                ", payment=" + payment +
                ", tables=" + tables +
                ", customer=" + customer +
                ", employee=" + employee +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }
}