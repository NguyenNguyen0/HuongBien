package com.huongbien.dao;

import com.huongbien.entity.FoodOrder;

import java.util.List;

public class FoodOrder_DAO extends Base_DAO<FoodOrder>{
    @Override
    public boolean add(FoodOrder object) {
        return false;
    }

    @Override
    public boolean update(FoodOrder object) {
        return false;
    }

    @Override
    public List<FoodOrder> get() {
        return List.of();
    }

    @Override
    public FoodOrder get(String id) {
        return null;
    }
}
