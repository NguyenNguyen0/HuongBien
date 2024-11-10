package com.huongbien.dao;

import com.huongbien.entity.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PromotionDAO extends GenericDAO<Promotion> {
    private static final PromotionDAO instance = new PromotionDAO();

    private PromotionDAO() {
        super();
    }

    public static PromotionDAO getInstance() {
        return instance;
    }

    @Override
    public Promotion resultMapper(ResultSet resultSet) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(resultSet.getString("id"));
        promotion.setName(resultSet.getString("name"));
        promotion.setStartDate(resultSet.getDate("startDate").toLocalDate());
        promotion.setEndDate(resultSet.getDate("endDate").toLocalDate());
        promotion.setDiscount(resultSet.getDouble("discount"));
        promotion.setDescription(resultSet.getString("description"));
        promotion.setMembershipLevel(resultSet.getInt("membershipLevel"));
        promotion.setMinimumOrderAmount(resultSet.getDouble("minimumOrderAmount"));
        promotion.setStatus(resultSet.getString("status"));
        return promotion;
    }

    public List<Promotion> getAll() {
        return getMany("SELECT * FROM Promotion");
    }

    public List<Promotion> getAllById(String id) {
        return getMany("SELECT * FROM Promotion WHERE id LIKE ?", "%" + id + "%");
    }

    public List<Promotion> getForCustomer(int customerMembershipLevel, double orderAmount) {
        String sql = "SELECT * FROM Promotion WHERE membershipLevel <= ? AND minimumOrderAmount <= ? AND status = N'Còn hiệu lực' ORDER BY discount DESC;";
        return getMany(sql, customerMembershipLevel, orderAmount);
    }

    public List<Promotion> getExpired() {
        return getMany("SELECT id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status FROM Promotion WHERE status LIKE N'Hết hiệu lực'");
    }

    public Promotion getById(String id) {
        return getOne("SELECT * FROM Promotion WHERE id = ?", id);
    }

    public boolean updateInfo(Promotion promotion) {
        return update("UPDATE promotion SET name = ?, startDate = ?, endDate = ?, discount = ?, description = ?, minimumOrderAmount = ?, membershipLevel = ?, status = ? WHERE id = ?",
                promotion.getName(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getDiscount(),
                promotion.getDescription(),
                promotion.getMinimumOrderAmount(),
                promotion.getMembershipLevel(),
                promotion.getStatus(),
                promotion.getPromotionId()
        );
    }

    @Override
    public boolean add(Promotion promotion) {
        String sql = "INSERT INTO promotion (id, name, startDate, endDate, discount, description, minimumOrderAmount, membershipLevel, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(
                    sql,
                    promotion.getPromotionId(),
                    promotion.getName(),
                    promotion.getStartDate(),
                    promotion.getEndDate(),
                    promotion.getDiscount(),
                    promotion.getDescription(),
                    promotion.getMinimumOrderAmount(),
                    promotion.getMembershipLevel(),
                    promotion.getStatus()
            );
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM promotion WHERE id = ?";
        try (PreparedStatement statement = statementHelper.prepareStatement(sql)) {
            statement.setString(1, id);

            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}