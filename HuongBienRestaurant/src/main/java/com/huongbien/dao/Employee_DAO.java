package com.huongbien.dao;

import com.huongbien.entity.Employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Employee_DAO extends Base_DAO<Employee> {
    private Connection connection = null;

    public Employee_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Employee object) {
        String sql = "INSERT INTO Employee (id, name, phoneNumber, citizenIDNumber, gender, address, birthday, email, status, hireDate, position, workHours, hourlyPay, salary, managerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, object.getEmployeeId());
            stmt.setString(2, object.getName());
            stmt.setString(3, object.getPhoneNumber());
            stmt.setString(4, object.getCitizenIDNumber());
            stmt.setBoolean(5, object.isGender());
            stmt.setString(6, object.getAddress());
            stmt.setDate(7, Date.valueOf(object.getBirthday()));
            stmt.setString(8, object.getEmail());
            stmt.setString(9, object.getStatus());
            stmt.setDate(10, Date.valueOf(object.getHireDate()));
            stmt.setString(11, object.getPosition());
            stmt.setDouble(12, object.getWorkHours());
            stmt.setDouble(13, object.getHourlyPay());
            stmt.setDouble(14, object.getSalary());
            stmt.setString(15, object.getManager() != null ? object.getManager().getEmployeeId() : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Employee object) {
        return false;
    }

    @Override
    public List<Employee> get() {
        return List.of();
    }

    @Override
    public Employee get(String id) {
        return null;
    }
}
