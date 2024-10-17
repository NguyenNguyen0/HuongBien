package com.huongbien.dao;

import com.huongbien.entity.Payment;

import java.util.List;

public class Payment_DAO extends Base_DAO<Payment> {
    @Override
    public boolean add(Payment object) {
        return false;
    }

    @Override
    public boolean update(Payment object) {
        return false;
    }

    @Override
    public List<Payment> get() {
        return List.of();
    }

    @Override
    public Payment get(String id) {
        return null;
    }
}
