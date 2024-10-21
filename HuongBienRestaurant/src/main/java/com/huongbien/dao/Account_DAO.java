package com.huongbien.dao;

import com.huongbien.database.Database;
import com.huongbien.entity.Account;
import com.huongbien.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
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
                    "INSERT INTO account (username, hashcode, role, email, isActive, avatar) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, object.getUsername());
            stmt.setString(2, object.getHashcode());
            stmt.setString(3, object.getRole());
            stmt.setString(4, object.getEmail());
            stmt.setBoolean(5, object.getIsActive());
            stmt.setBytes(6, object.getAvatar());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Invalid account object");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE Account SET hashcode = ?, role = ?, email = ?, isActive = ?, avatar = ? WHERE username = ?"
            );
            stmt.setString(1, account.getHashcode());
            stmt.setString(2, account.getRole());
            stmt.setString(3, account.getEmail());
            stmt.setBoolean(4, account.getIsActive());
            stmt.setBytes(5, account.getAvatar());
            stmt.setString(6, account.getUsername());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Account> get() {
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM account");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account();
                account.setUsername(rs.getString("username"));
                account.setHashcode(rs.getString("hashcode"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setIsActive(rs.getBoolean("isActive"));
                account.setEmployeeInfo(new Employee_DAO(connection).get(account.getUsername()));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account get(String id) {
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
                account.setEmployeeInfo(new Employee_DAO(connection).get(account.getUsername()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean changePassword(String username, String oldHashCode, String newPassword) {
        Account account = this.get(username);

        if (account == null) {
            throw new IllegalArgumentException(String.format("Username: %s is not found!", username));
        }

        if (account.getHashcode().equals(oldHashCode)) {
            account.setHashcode(Utils.hashPassword(newPassword));
            this.update(account);
            return true;
        }

        return false;
    }
}
