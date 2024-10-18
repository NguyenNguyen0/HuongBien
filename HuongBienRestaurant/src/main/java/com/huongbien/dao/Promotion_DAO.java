package com.huongbien.dao;

import com.huongbien.entity.Promotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Promotion_DAO extends Base_DAO<Promotion> {
    private Connection connection;

    public Promotion_DAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Promotion promotion) {
        String sql = "INSERT INTO promotion (promotionId, name, startDate, endDate, isUsed, discount, description, minimumOrderAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, promotion.getPromotion_id());
            stmt.setString(2, promotion.getName());
            stmt.setDate(3, java.sql.Date.valueOf(promotion.getStartDate()));
            stmt.setDate(4, java.sql.Date.valueOf(promotion.getEndDate()));
            stmt.setBoolean(5, promotion.isUsed());
            stmt.setDouble(6, promotion.getDiscount());
            stmt.setString(7, promotion.getDescription());
            stmt.setDouble(8, promotion.getMinimumOrderAmount());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Promotion promotion) {
        String sql = "UPDATE promotion SET name = ?, startDate = ?, endDate = ?, isUsed = ?, discount = ?, description = ?, minimumOrderAmount = ? WHERE promotionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, promotion.getName());
            stmt.setDate(2, java.sql.Date.valueOf(promotion.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(promotion.getEndDate()));
            stmt.setBoolean(4, promotion.isUsed());
            stmt.setDouble(5, promotion.getDiscount());
            stmt.setString(6, promotion.getDescription());
            stmt.setDouble(7, promotion.getMinimumOrderAmount());
            stmt.setString(8, promotion.getPromotion_id());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Promotion> get() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT promotionId, name, startDate, endDate, isUsed, discount, description, minimumOrderAmount FROM promotion";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setPromotion_id(rs.getString("promotionId"));
                promotion.setName(rs.getString("name"));
                promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                promotion.setIsUsed(rs.getBoolean("isUsed"));
                promotion.setDiscount(rs.getDouble("discount"));
                promotion.setDescription(rs.getString("description"));
                promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                promotions.add(promotion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    @Override
    public Promotion get(String id) {
        Promotion promotion = null;
        String sql = "SELECT promotionId, name, startDate, endDate, isUsed, discount, description, minimumOrderAmount FROM promotion WHERE promotionId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    promotion = new Promotion();
                    promotion.setPromotion_id(rs.getString("promotionId"));
                    promotion.setName(rs.getString("name"));
                    promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                    promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                    promotion.setIsUsed(rs.getBoolean("isUsed"));
                    promotion.setDiscount(rs.getDouble("discount"));
                    promotion.setDescription(rs.getString("description"));
                    promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotion;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM promotion WHERE promotionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
