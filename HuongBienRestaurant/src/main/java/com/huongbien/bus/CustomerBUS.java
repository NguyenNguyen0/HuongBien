package com.huongbien.bus;


import com.huongbien.dao.CustomerDAO;
import com.huongbien.entity.Customer;

import java.util.List;

public class CustomerBUS {
    private final CustomerDAO customerDao;

    public CustomerBUS() {
        customerDao = CustomerDAO.getInstance();
    }

    public List<Customer> getAllCustomer() {
        return customerDao.getAll();
    }

    public List<Customer> getCustomerByName(String name) {
        return customerDao.getByName(name);
    }

    public List<Customer> getCustomerByPhoneNumber(String phoneNumber) {
        return customerDao.getByManyPhoneNumber(phoneNumber);
    }

    public Customer getCustomer(String customerId) {
        return customerDao.getById(customerId);
    }

    public boolean addCustomer(Customer customer) {
        return customerDao.add(customer);
    }
}
