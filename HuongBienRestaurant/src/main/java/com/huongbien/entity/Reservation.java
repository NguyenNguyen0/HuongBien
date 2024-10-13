package com.huongbien.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private String reservationId;
    private String partyType;
    private int partySize;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private LocalDate receiveDate;
    private String status;
    private double deposit;
    private double refundDeposit;
    private Payment payment;
    private Employee employee;
    private Table table;
    private Customer customer;


    public Reservation() {}

    public Reservation(String reservationId, String partyType, int partySize, LocalDate reservationDate,
                       LocalTime reservationTime, LocalDate receiveDate, String status, double deposit,
                       double refundDeposit, Payment payment, Employee employee, Table table, Customer customer) {
        setReservationId(reservationId);
        setPartyType(partyType);
        setPartySize(partySize);
        setReservationDate(reservationDate);
        setReservationTime(reservationTime);
        setReceiveDate(receiveDate);
        setStatus(status);
        setDeposit(deposit);
        setRefundDeposit(refundDeposit);
        setPayment(payment);
        setEmployee(employee);
        setTable(table);
        setCustomer(customer);
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        if (reservationId == null || !reservationId.matches("^DB\\d{6}\\d{3}$")) {
            throw new IllegalArgumentException("Invalid reservation ID format");
        }
        this.reservationId = reservationId;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        if (partyType == null || partyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Party type cannot be empty");
        }
        this.partyType = partyType;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        if (partySize <= 0) {
            throw new IllegalArgumentException("Party size must be greater than 0");
        }
        this.partySize = partySize;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        if (reservationDate == null || reservationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation date must be today or earlier");
        }
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        if (reservationTime == null || (reservationDate.equals(LocalDate.now()) && reservationTime.isAfter(LocalTime.now()))) {
            throw new IllegalArgumentException("Reservation time must be before the current time if the reservation is for today");
        }
        this.reservationTime = reservationTime;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        if (receiveDate == null || receiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Receive date must be today or later");
        }
        this.receiveDate = receiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        if (deposit < 0) {
            throw new IllegalArgumentException("Deposit must be greater than or equal to 0");
        }
        this.deposit = deposit;
    }

    public double getRefundDeposit() {
        return refundDeposit;
    }

    public void setRefundDeposit(double refundDeposit) {
        if (refundDeposit < 0) {
            throw new IllegalArgumentException("Refund deposit must be greater than or equal to 0");
        }
        this.refundDeposit = refundDeposit;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        this.table = table;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", partyType='" + partyType + '\'' +
                ", partySize=" + partySize +
                ", reservationDate=" + reservationDate +
                ", reservationTime=" + reservationTime +
                ", receiveDate=" + receiveDate +
                ", status='" + status + '\'' +
                ", deposit=" + deposit +
                ", refundDeposit=" + refundDeposit +
                ", payment=" + payment +
                ", employee=" + employee +
                ", table=" + table +
                ", customer=" + customer +
                '}';
    }
}
