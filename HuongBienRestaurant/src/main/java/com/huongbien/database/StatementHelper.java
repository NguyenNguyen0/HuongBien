package com.huongbien.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class StatementHelper {
    private final Connection connection;
    private static StatementHelper instance;

    private StatementHelper() {
        try {
            this.connection = Database.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static StatementHelper getInstances() {
        if (instance == null) {
            instance = new StatementHelper();
        }
        return instance;
    }


    public CallableStatement callProcedure(String callProcedure, Object... args) {
        try {
            CallableStatement callStatement = connection.prepareCall(callProcedure);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof LocalDate) {
                    callStatement.setDate(i + 1, Date.valueOf((LocalDate) args[i]));
                } else if (args[i] instanceof LocalTime) {
                    callStatement.setObject(i + 1, Time.valueOf((LocalTime) args[i]));
                } else {
                    callStatement.setObject(i + 1, args[i]);
                }
            }

            return callStatement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, Object... args) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof LocalDate) {
                    statement.setDate(i + 1, Date.valueOf((LocalDate) args[i]));
                } else if (args[i] instanceof LocalTime) {
                    statement.setObject(i + 1, Time.valueOf((LocalTime) args[i]));
                } else {
                    statement.setObject(i + 1, args[i]);
                }
            }
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
 }