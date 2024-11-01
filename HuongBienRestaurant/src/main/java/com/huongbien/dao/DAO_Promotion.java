package com.huongbien.dao;

import com.huongbien.entity.Promotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_Promotion extends DAO_Base<Promotion> {
    private final Connection connection;

    public DAO_Promotion(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean add(Promotion promotion) {
        String sql = "INSERT INTO promotion (id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, promotion.getPromotionId());
            stmt.setString(2, promotion.getName());
            stmt.setDate(3, java.sql.Date.valueOf(promotion.getStartDate()));
            stmt.setDate(4, java.sql.Date.valueOf(promotion.getEndDate()));
            stmt.setDouble(5, promotion.getDiscount());
            stmt.setString(6, promotion.getDescription());
            stmt.setDouble(7, promotion.getMinimumOrderAmount());
            stmt.setInt(8, promotion.getMembershipLevel());
            stmt.setString(9, promotion.getStatus());

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Promotion promotion) {
        String sql = "UPDATE promotion SET name = ?, startDate = ?, endDate = ?, discount = ?, description = ?, minimumOrderAmount = ?, membershipLevel = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, promotion.getName());
            stmt.setDate(2, java.sql.Date.valueOf(promotion.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(promotion.getEndDate()));
            stmt.setDouble(4, promotion.getDiscount());
            stmt.setString(5, promotion.getDescription());
            stmt.setDouble(6, promotion.getMinimumOrderAmount());
            stmt.setInt(7, promotion.getMembershipLevel());
            stmt.setString(8, promotion.getStatus());
            stmt.setString(9, promotion.getPromotionId());

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
        String sql = "SELECT id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status FROM Promotion WHERE status LIKE N'Còn hiệu lực'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                String promotionId = rs.getString("id");
                promotion.setPromotionId(promotionId);
                promotion.setName(rs.getString("name"));
                promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                promotion.setDiscount(rs.getDouble("discount"));
                promotion.setDescription(rs.getString("description"));
                promotion.setMembershipLevel(rs.getInt("membershipLevel"));
                promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                promotion.setStatus(rs.getString("status"));
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    public List<Promotion> search(String id) {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM Promotion WHERE id LIKE N'%"+id+"%'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                String promotionId = rs.getString("id");
                promotion.setPromotionId(promotionId);
                promotion.setName(rs.getString("name"));
                promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                promotion.setDiscount(rs.getDouble("discount"));
                promotion.setDescription(rs.getString("description"));
                promotion.setMembershipLevel(rs.getInt("membershipLevel"));
                promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                promotion.setStatus(rs.getString("status"));
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    public List<Promotion> getExpired() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status FROM Promotion WHERE status LIKE N'Hết hiệu lực'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = new Promotion();
                String promotionId = rs.getString("id");
                promotion.setPromotionId(promotionId);
                promotion.setName(rs.getString("name"));
                promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                promotion.setDiscount(rs.getDouble("discount"));
                promotion.setDescription(rs.getString("description"));
                promotion.setMembershipLevel(rs.getInt("membershipLevel"));
                promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                promotion.setStatus(rs.getString("status"));
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
        String sql = "SELECT id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status FROM promotion WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    promotion = new Promotion();
                    promotion.setPromotionId(rs.getString("id"));
                    promotion.setName(rs.getString("name"));
                    promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                    promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                    promotion.setDiscount(rs.getDouble("discount"));
                    promotion.setDescription(rs.getString("description"));
                    promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                    promotion.setMinimumOrderAmount(rs.getInt("membershipLevel"));
                    promotion.setMinimumOrderAmount(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotion;
    }

    public List<Promotion> getForCustomer(int customerMembershipLevel, double orderAmount) {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status FROM Promotion WHERE membershipLevel <= ? AND minimumOrderAmount <= ? ORDER BY membershipLevel DESC;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerMembershipLevel);
            stmt.setDouble(2, orderAmount);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Promotion promotion = new Promotion();
                String promotionId = rs.getString("id");
                promotion.setPromotionId(promotionId);
                promotion.setName(rs.getString("name"));
                promotion.setStartDate(rs.getDate("startDate").toLocalDate());
                promotion.setEndDate(rs.getDate("endDate").toLocalDate());
                promotion.setDiscount(rs.getDouble("discount"));
                promotion.setDescription(rs.getString("description"));
                promotion.setMembershipLevel(rs.getInt("membershipLevel"));
                promotion.setMinimumOrderAmount(rs.getDouble("minimumOrderAmount"));
                promotion.setStatus(rs.getString("status"));
                promotions.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promotions;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM promotion WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
//            e.printStackTrace();
            return false;
        }
    }
}
