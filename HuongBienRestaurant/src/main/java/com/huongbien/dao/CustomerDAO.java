package com.huongbien.dao;

import com.huongbien.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.huongbien.database.Database.connection;

public class CustomerDAO extends GenericDAO<Customer> {
    private static final CustomerDAO instance = new CustomerDAO();

    private CustomerDAO() {
        super();
    }

    public static CustomerDAO getInstance() {
        return instance;
    }

    @Override
    public Customer resultMapper(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getString("id"));
        customer.setName(resultSet.getString("name"));
        customer.setAddress(resultSet.getString("address"));
        customer.setGender(resultSet.getBoolean("gender"));
        customer.setPhoneNumber(resultSet.getString("phoneNumber"));
        customer.setEmail(resultSet.getString("email"));
        customer.setBirthday(resultSet.getDate("birthday").toLocalDate());
        customer.setRegistrationDate(resultSet.getDate("registrationDate").toLocalDate());
        customer.setAccumulatedPoints(resultSet.getInt("accumulatedPoints"));
        customer.setMembershipLevel(resultSet.getInt("membershipLevel"));
        return customer;
    }

    public List<Customer> getAll() {
        return getMany("SELECT * FROM Customer");
    }

    public List<Customer> getByPhoneNumber(String phoneNumber) {
        return getMany("SELECT * FROM Customer WHERE phoneNumber LIKE ?", phoneNumber + "%");
    }

    public List<Customer> getByName(String name) {
        return getMany("SELECT * FROM Customer WHERE name LIKE ?", "%" + name + "%");
    }

    //  TODO: update this method
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

    public Customer getById(String customerId) {
        return getOne("SELECT * FROM Customer WHERE id = ?", customerId);
    }

    public boolean updateCustomerInfo(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, address = " +
                "?, phoneNumber = ?, email = ?, birthday = ?, accumulatedPoints = ?, membershipLevel = ? WHERE id = ?";
        return update(sql, customer.getName(), customer.getAddress(), customer.getPhoneNumber(), customer.getEmail(), customer.getBirthday(), customer.getAccumulatedPoints(), customer.getMembershipLevel(), customer.getCustomerId());
    }

    @Override
    public boolean add(Customer customer) {
        String sql = "INSERT INTO Customer (id, name, address, gender, phoneNumber, email, birthday, registrationDate, accumulatedPoints, membershipLevel) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql, customer.getCustomerId(), customer.getName(), customer.getAddress(),
                    customer.isGender(), customer.getPhoneNumber(), customer.getEmail(), customer.getBirthday(), customer.getRegistrationDate(),
                    customer.getAccumulatedPoints(), customer.getMembershipLevel());
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
