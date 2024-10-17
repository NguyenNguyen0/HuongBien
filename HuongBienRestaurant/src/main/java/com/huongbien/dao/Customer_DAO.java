package com.huongbien.dao;

import com.huongbien.entity.Customer;

import java.util.List;

public class Customer_DAO extends Base_DAO<Customer> {
    @Override
    public boolean add(Customer object) {
        return false;
    }

    @Override
    public boolean update(Customer object) {
        return false;
    }

    @Override
    public List<Customer> get() {
        return List.of();
    }

    @Override
    public Customer get(String id) {
        return null;
    }
}
