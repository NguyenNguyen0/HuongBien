package com.huongbien.dao;

import com.huongbien.entity.OrderDetail;

import java.util.List;

public class OrderDetail_DAO extends Base_DAO<OrderDetail>{
    @Override
    public boolean add(OrderDetail object) {
        return false;
    }

    @Override
    public boolean update(OrderDetail object) {
        return false;
    }

    @Override
    public List<OrderDetail> get() {
        return List.of();
    }

    @Override
    public OrderDetail get(String id) {
        return null;
    }
}
