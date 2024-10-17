package com.huongbien.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private String customerId;
    private String name;
    private String address;
    private boolean gender;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private LocalDate registrationDate;
    private int accumulatedPoints;
    private String memberShip;

//  Dành cho khách vãng lai 
    public Customer() {
        setCustomerId("KH0000000");
        setName("Khách vãng lai");
    }

    public Customer(String customerId, String name, String address, boolean gender,
                    String phoneNumber, String email, LocalDate birthday,
                    LocalDate registrationDate, int accumulatedPoints, String memberShip) {
        setCustomerId(customerId);
        setName(name);
        setAddress(address);
        setGender(gender);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setBirthday(birthday);
        setRegistrationDate(registrationDate);
        setAccumulatedPoints(accumulatedPoints);
        setMemberShip(memberShip);
    }

    public void setCustomerId(String customerId) {
        if (customerId == null || !customerId.matches("^KH\\d{6}\\d{3}$")) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        this.customerId = customerId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.matches("^0\\d{9}$")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public void setEmail(String email) {
        if (email == null || email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setAccumulatedPoints(int accumulatedPoints) {
        if (accumulatedPoints < 0) {
            throw new IllegalArgumentException("Accumulated points must be non-negative");
        }
        this.accumulatedPoints = accumulatedPoints;
    }

    public void setMemberShip(String memberShip) {
        if (memberShip == null || memberShip.equals("VIP") || memberShip.equals("Thường")) {
            this.memberShip = memberShip;
        } else {
            throw new IllegalArgumentException("Membership must be either VIP or Thường");
        }
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public String getMemberShip() {
        return memberShip;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", registrationDate=" + registrationDate +
                ", accumulatedPoints=" + accumulatedPoints +
                ", memberShip='" + memberShip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerId);
    }
}

