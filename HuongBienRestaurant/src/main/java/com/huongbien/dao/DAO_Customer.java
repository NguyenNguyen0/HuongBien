package com.huongbien.dao;

import com.huongbien.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_Customer extends DAO_Base<Customer> {
    private final Connection connection;

    public DAO_Customer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Customer customer) {
        String sql = "INSERT INTO Customer (id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setBoolean(4, customer.isGender());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.setString(6, customer.getEmail());
            stmt.setDate(7, Date.valueOf(customer.getBirthday()));
            stmt.setDate(8, Date.valueOf(customer.getRegistrationDate()));
            stmt.setInt(9, customer.getAccumulatedPoints());
            stmt.setInt(10, customer.getMembershipLevel());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, address = ?, gender = ?, phoneNumber = ?, email = ?, birthday = ?, registrationDate = ?, "
                + "accumulatedPoints = ?, membershipLevel = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setBoolean(3, customer.isGender());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getEmail());
            stmt.setDate(6, Date.valueOf(customer.getBirthday()));
            stmt.setDate(7, Date.valueOf(customer.getRegistrationDate()));
            stmt.setInt(8, customer.getAccumulatedPoints());
            stmt.setInt(9, customer.getMembershipLevel());
            stmt.setString(10, customer.getCustomerId());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Customer> get() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel FROM Customer";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setGender(rs.getBoolean("gender"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
                customer.setBirthday(rs.getDate("birthday").toLocalDate());
                customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                customer.setMembershipLevel(rs.getInt("membershipLevel"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
    @Override
    public Customer get(String id) {
        Customer customer = null;
        String sql = "SELECT id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel "
                + "FROM Customer WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setCustomerId(rs.getString("id"));
                    customer.setName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setGender(rs.getBoolean("gender"));
                    customer.setPhoneNumber(rs.getString("phoneNumber"));
                    customer.setEmail(rs.getString("email"));
                    customer.setBirthday(rs.getDate("birthday").toLocalDate());
                    customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                    customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                    customer.setMembershipLevel(rs.getInt("membershipLevel"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public Customer getByPhone(String phone) {
        Customer customer = null;
        String sql = "SELECT id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel "
                + "FROM Customer WHERE phoneNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setCustomerId(rs.getString("id"));
                    customer.setName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setGender(rs.getBoolean("gender"));
                    customer.setPhoneNumber(rs.getString("phoneNumber"));
                    customer.setEmail(rs.getString("email"));
                    customer.setBirthday(rs.getDate("birthday").toLocalDate());
                    customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                    customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                    customer.setMembershipLevel(rs.getInt("membershipLevel"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public List<String> getPhoneNumber() {
        List<String> customersPhone = new ArrayList<>();
        String sql = "SELECT phoneNumber FROM Customer";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String phone = rs.getString("phoneNumber");
                customersPhone.add(phone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customersPhone;
    }

    public List<Customer> getByName(String name) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel FROM Customer WHERE name LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setGender(rs.getBoolean("gender"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
                customer.setBirthday(rs.getDate("birthday").toLocalDate());
                customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                customer.setMembershipLevel(rs.getInt("membershipLevel"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<Customer> searchCustomerPhone(String phone){
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE phoneNumber LIKE N'%" + phone + "%'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setGender(rs.getBoolean("gender"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
                customer.setBirthday(rs.getDate("birthday").toLocalDate());
                customer.setRegistrationDate(rs.getDate("registrationDate").toLocalDate());
                customer.setAccumulatedPoints(rs.getInt("accumulatedPoints"));
                customer.setMembershipLevel(rs.getInt("membershipLevel"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
