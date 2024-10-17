package com.huongbien.entity;

import java.util.Objects;

public class Account {
    private String username;
    private String hashcode;
    private String role;
    private String email;
    private Boolean isActive;
    private Employee employeeInfo;
    private byte[] avatar;

    public Account() {}

    public Account(String username, String hashcode, String role, String email, Boolean isActive, Employee employeeInfo, byte[] avatar) {
        setUsername(username);
        setHashcode(hashcode);
        setRole(role);
        setEmail(email);
        setIsActive(isActive);
        setEmployeeInfo(employeeInfo);
        setAvatar(avatar);
    }

    public void setUsername(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.username = employeeId;
    }

    public void setHashcode(String hashcode) {
        if (hashcode == null || hashcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashcode cannot be empty");
        }
        this.hashcode = hashcode;
    }

    public void setRole(String role) {
        if (role == null || (!role.equals("Manager") && !role.equals("Waiter"))) {
            throw new IllegalArgumentException("Role must be either Manager or Waiter");
        }
        this.role = role;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setIsActive(Boolean isActive) {
        if (isActive == null) {
            throw new IllegalArgumentException("isActive cannot be null");
        }
        this.isActive = isActive;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setEmployeeInfo(Employee employeeInfo) {
        this.employeeInfo = employeeInfo;
    }

    public String getUsername() {
        return username;
    }

    public String getHashcode() {
        return hashcode;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Employee getEmployeeInfo() {
        return employeeInfo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", hashcode='" + hashcode + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", employeeInfo=" + employeeInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}


