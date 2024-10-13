package com.huongbien.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private String customerId;
    private String name;
    private String address;
    private Boolean gender;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private LocalDate registrationDate;
    private int accumulatedPoints;
    private String memberShip;

    public Customer() {}

    public Customer(String customerId, String name, String address, Boolean gender,
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null || !customerId.matches("^KH\\d{6}\\d{3}$")) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        if (birthday == null || birthday.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Customer must be at least 18 years old");
        }
        this.birthday = birthday;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        if (registrationDate == null || registrationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Registration date must be before or equal to today");
        }
        this.registrationDate = registrationDate;
    }

    public int getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public void setAccumulatedPoints(int accumulatedPoints) {
        if (accumulatedPoints < 0) {
            throw new IllegalArgumentException("Accumulated points must be non-negative");
        }
        this.accumulatedPoints = accumulatedPoints;
    }

    public String getMemberShip() {
        return memberShip;
    }

    public void setMemberShip(String memberShip) {
        if (memberShip == null || (!memberShip.equals("VIP") && !memberShip.equals("Regular"))) {
            throw new IllegalArgumentException("Membership must be either VIP or Regular");
        }
        this.memberShip = memberShip;
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

