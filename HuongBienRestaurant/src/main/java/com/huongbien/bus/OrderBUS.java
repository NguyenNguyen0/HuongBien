package com.huongbien.bus;

import com.huongbien.dao.OrderDAO;
import com.huongbien.entity.Order;

import java.util.List;

public class OrderBUS {
    private final OrderDAO orderDao;

    public OrderBUS() {
        orderDao = OrderDAO.getInstance();
    }

    public List<Order> getAllOrder() {
        return orderDao.getAll();
    }

    public boolean addOrder(Order order) {
        if (order == null) return false;
        return orderDao.add(order);
    }
}
