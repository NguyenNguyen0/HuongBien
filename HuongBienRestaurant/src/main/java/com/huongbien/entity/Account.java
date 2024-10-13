package com.huongbien.entity;

import java.util.Objects;

public class Account {
    private String userName;
    private String hashcode;
    private String role;
    private String email;
    private Boolean isActive;
    private Employee employee;
    private byte[] avatar;

    public Account() {}

    public Account(String userName, String hashcode, String role, String email, Boolean isActive, Employee employee, byte[] avatar) {
        setUserName(userName);
        setHashcode(hashcode);
        setRole(role);
        setEmail(email);
        setIsActive(isActive);
        setEmployee(employee);
        setAvatar(avatar);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.userName = userName;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        if (hashcode == null || hashcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashcode cannot be empty");
        }
        this.hashcode = hashcode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || (!role.equals("Manager") && !role.equals("Waiter"))) {
            throw new IllegalArgumentException("Role must be either Manager or Waiter");
        }
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        if (isActive == null) {
            throw new IllegalArgumentException("isActive cannot be null");
        }
        this.isActive = isActive;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userName='" + userName + '\'' +
                ", hashcode='" + hashcode + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", employee=" + employee +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(userName, account.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName);
    }
}


