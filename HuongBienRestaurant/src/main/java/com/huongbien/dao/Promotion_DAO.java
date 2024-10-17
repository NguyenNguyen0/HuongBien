package com.huongbien.dao;

import com.huongbien.entity.Promotion;

import java.util.List;

public class Promotion_DAO extends Base_DAO<Promotion> {

    @Override
    public boolean add(Promotion object) {
        return false;
    }

    @Override
    public boolean update(Promotion object) {
        return false;
    }

    @Override
    public List<Promotion> get() {
        return List.of();
    }

    @Override
    public Promotion get(String id) {
        return null;
    }
}
