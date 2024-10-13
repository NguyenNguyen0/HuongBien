package com.huongbien.entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private LocalDate orderDate;
    private String notes;
    private double vatTax;
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

    public Order(String orderId, LocalDate orderDate, String notes, double vatTax,
                 double paymentAmount, double dispensedAmount, double totalAmount, double discount,
                 ArrayList<OrderDetail> orderDetails, Promotion promotion, Payment payment,
                 ArrayList<Table> tables, Customer customer, Employee employee) {
        setOrderId(orderId);
        setOrderDate(orderDate);
        setNotes(notes);
        setVatTax(vatTax);
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || !orderId.matches("^HD\\d{4}\\d{4}\\d{3}$")) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        if (orderDate == null || !orderDate.equals(LocalDate.now())) {
            throw new IllegalArgumentException("Order date must be today's date");
        }
        this.orderDate = orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getVatTax() {
        return vatTax;
    }

    public void setVatTax(double vatTax) {
        if (vatTax < 0) {
            throw new IllegalArgumentException("VAT tax cannot be negative");
        }
        this.vatTax = vatTax;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        this.paymentAmount = paymentAmount;
    }

    public double getDispensedAmount() {
        return dispensedAmount;
    }

    public void setDispensedAmount(double dispensedAmount) {
        if (dispensedAmount <= 0) {
            throw new IllegalArgumentException("Dispensed amount must be greater than 0");
        }
        this.dispensedAmount = dispensedAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }
        this.totalAmount = totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        this.discount = discount;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null");
        }
        this.orderDetails = orderDetails;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
        this.promotion = promotion;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        this.payment = payment;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        if (tables == null) {
            throw new IllegalArgumentException("Tables cannot be null");
        }
        this.tables = tables;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
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
}
