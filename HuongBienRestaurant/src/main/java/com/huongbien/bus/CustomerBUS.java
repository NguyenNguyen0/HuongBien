package com.huongbien.bus;


import com.huongbien.dao.CustomerDAO;
import com.huongbien.entity.Customer;

import java.time.LocalDate;
import java.util.List;

public class CustomerBUS {
    private final CustomerDAO customerDao;

    public CustomerBUS() {
        customerDao = CustomerDAO.getInstance();
    }

    public int getTotalCustomerCount() {
        return customerDao.countTotal();
    }

    public int getTotalCustomersCountByPhoneNumber(String phoneNumber) {
        return customerDao.countTotalByPhoneNumber(phoneNumber);
    }

    public int getTotalCustomersCountByName(String name) {
        return customerDao.countTotalByName(name);
    }

    public int getTotalCustomersCountById(String id) {
        return customerDao.countTotalById(id);
    }

    public List<Customer> getCustomersByIdWithPagination(int offset, int limit, String id) {
        return customerDao.getAllWithPaginationById(id, offset, limit);
    }

    public List<Customer> getCustomersByNameWithPagination(int offset, int limit, String name) {
        return customerDao.getAllWithPaginationByName(name, offset, limit);
    }

    public List<Customer> getCustomersByPhoneNumberWithPagination(int offset, int limit, String phoneNumber) {
        return customerDao.getAllWithPaginationByPhoneNumber(phoneNumber, offset, limit);
    }

    public List<Customer> getAllCustomersWithPagination(int offset, int limit) {
        return customerDao.getAllWithPagination(offset, limit);
    }

    public List<Customer> getCustomerInDay(LocalDate date) {
        return customerDao.getCustomerInDay(date);
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

    public boolean updateCustomerInfo(Customer customer) {
        return customerDao.updateCustomerInfo(customer);
    }

    public boolean addCustomer(Customer customer) {
        return customerDao.add(customer);
    }
}
