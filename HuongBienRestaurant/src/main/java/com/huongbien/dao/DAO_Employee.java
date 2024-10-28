package com.huongbien.dao;

import com.huongbien.entity.Employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Employee extends DAO_Base<Employee> {
    private final Connection connection;

    public DAO_Employee(Connection connection) {
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
        String sql = "UPDATE Employee SET name = ?, phoneNumber = ?, citizenIDNumber = ?, gender = ?, address = ?, birthday = ?, email = ?, status = ?, hireDate = ?, position = ?, workHours = ?, hourlyPay = ?, salary = ?, managerId = ? WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getPhoneNumber());
            stmt.setString(3, object.getCitizenIDNumber());
            stmt.setBoolean(4, object.isGender());
            stmt.setString(5, object.getAddress());
            stmt.setDate(6, Date.valueOf(object.getBirthday()));
            stmt.setString(7, object.getEmail());
            stmt.setString(8, object.getStatus());
            stmt.setDate(9, Date.valueOf(object.getHireDate()));
            stmt.setString(10, object.getPosition());
            stmt.setDouble(11, object.getWorkHours());
            stmt.setDouble(12, object.getHourlyPay());
            stmt.setDouble(13, object.getSalary());
            stmt.setString(14, object.getManager() != null ? object.getManager().getEmployeeId() : null);
            stmt.setString(15, object.getEmployeeId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> get() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee get(String id) {
        if (id == null) {
            return null;
        }

        String sql = "SELECT * FROM Employee WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                return employee;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Employee> getByPhoneNumber(String phoneNumber) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE phoneNumber LIKE ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, phoneNumber + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    public List<Employee> getByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE name LIKE ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    public List<Employee> getEmpID(String EmpID) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE citizenIDNumber LIKE ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, EmpID + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    public List<Employee> getByCriteria(String phoneNumber, String name, String empID) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Employee WHERE 1=1");
        List<String> parameters = new ArrayList<>();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            sqlBuilder.append(" AND phoneNumber LIKE ?");
            parameters.add(phoneNumber + "%");
        }
        if (name != null && !name.isEmpty()) {
            sqlBuilder.append(" AND name LIKE ?");
            parameters.add("%" + name + "%");
        }
        if (empID != null && !empID.isEmpty()) {
            sqlBuilder.append(" AND id LIKE ?");
            parameters.add(empID + "%");
        }
        String sql = sqlBuilder.toString();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setCitizenIDNumber(rs.getString("citizenIDNumber"));
                employee.setGender(rs.getBoolean("gender"));
                employee.setAddress(rs.getString("address"));
                employee.setBirthday(rs.getDate("birthday").toLocalDate());
                employee.setEmail(rs.getString("email"));
                employee.setStatus(rs.getString("status"));
                employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                employee.setPosition(rs.getString("position"));
                employee.setWorkHours(rs.getDouble("workHours"));
                employee.setHourlyPay(rs.getDouble("hourlyPay"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setManager(this.get(rs.getString("managerId")));
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    public boolean updateStatus(String id, String status) {
        String sql = "UPDATE Employee SET status = ? WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}