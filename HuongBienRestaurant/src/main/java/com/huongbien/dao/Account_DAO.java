package com.huongbien.dao;

import com.huongbien.entity.Account;

import java.sql.*;
import java.util.List;

public class Account_DAO extends Base_DAO<Account> {
    private Connection connection = null;

    public Account_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Account object) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO account (username, hashcode, role, email, isActive) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, object.getUsername());
            stmt.setString(2, object.getHashcode());
            stmt.setString(3, object.getRole());
            stmt.setString(4, object.getEmail());
            stmt.setBoolean(5, object.getIsActive());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Account object) {
        return false;
    }

    @Override
    public List<Account> get() {
        return List.of();
    }

    @Override
    public Account get(String id) {
//        TODO: trường employeeInfo phải bổ sung sau khi hoàn thành Employee_DAO
        Account account = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT username, hashcode, role, email, isActive FROM account WHERE username = ?"
            );
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setUsername(rs.getString("username"));
                account.setHashcode(rs.getString("hashcode"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setIsActive(rs.getBoolean("isActive"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
