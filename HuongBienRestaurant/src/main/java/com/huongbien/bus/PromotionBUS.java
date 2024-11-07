package com.huongbien.bus;


import com.huongbien.dao.PromotionDAO;
import com.huongbien.entity.Promotion;

import java.util.List;

public class PromotionBUS {
    private final PromotionDAO promotionDao;

    public PromotionBUS() {
        promotionDao = PromotionDAO.getInstance();
    }

    public List<Promotion> getAllPromotion() {
        return promotionDao.getAll();
    }

    public Promotion getPromotion(String promotionId) {
        if (promotionId.isBlank() || promotionId.isEmpty()) return null;
        return promotionDao.getById(promotionId);
    }

    public List<Promotion> getPromotionForCustomer(int customerMembershipLevel, double orderAmount) {
        if (orderAmount < 0) return null;
        if (customerMembershipLevel < 0 || customerMembershipLevel > 3) return null;
        return promotionDao.getForCustomer(customerMembershipLevel, orderAmount);
    }
}
