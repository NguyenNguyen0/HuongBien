package com.huongbien.dao;

import com.huongbien.entity.Order;

import java.util.List;

public class Order_DAO extends Base_DAO<Order>{

    @Override
    public boolean add(Order object) {
        return false;
    }

    @Override
    public boolean update(Order object) {
        return false;
    }

    @Override
    public List<Order> get() {
        return List.of();
    }

    @Override
    public Order get(String id) {
        return null;
    }
}
