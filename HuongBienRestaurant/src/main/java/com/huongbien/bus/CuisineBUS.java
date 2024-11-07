package com.huongbien.bus;


import com.huongbien.dao.CuisineDAO;
import com.huongbien.entity.Cuisine;

import java.util.List;

public class CuisineBUS {
    private final CuisineDAO cuisineDao;

    public CuisineBUS() {
        cuisineDao = CuisineDAO.getInstance();
    }

    public List<Cuisine> getAllCuisine() {
        return cuisineDao.getAll();
    }

    public List<Cuisine> getCuisineByName(String name) {
        return cuisineDao.getByName(name);
    }

    public Cuisine getCuisineById(String cuisineId) {
        return cuisineDao.getById(cuisineId);
    }
}
