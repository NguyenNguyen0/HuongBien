package com.huongbien.dao;

import com.huongbien.entity.Reservation;

import java.util.List;

public class Reservation_DAO extends Base_DAO<Reservation>{
    @Override
    public boolean add(Reservation object) {
        return false;
    }

    @Override
    public boolean update(Reservation object) {
        return false;
    }

    @Override
    public List<Reservation> get() {
        return List.of();
    }

    @Override
    public Reservation get(String id) {
        return null;
    }
}
