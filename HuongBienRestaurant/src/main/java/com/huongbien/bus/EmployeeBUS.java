package com.huongbien.bus;

import com.huongbien.dao.EmployeeDAO;
import com.huongbien.entity.Employee;

import java.util.List;

public class EmployeeBUS {
    private final EmployeeDAO employeeDao;

    public EmployeeBUS() {
        employeeDao = EmployeeDAO.getInstance();
    }

    public List<Employee> getEmployeeList() {
        return employeeDao.getAll();
    }

    public List<Employee> getEmployeeById(String id) {
        if (id.isBlank() || id.isEmpty()) return null;
        return employeeDao.getById(id);
    }

    public List<Employee> getEmployeeByPosition(String position) {
        if (position.isEmpty() || position.isBlank()) return null;
        return employeeDao.getByPosition(position);
    }

    public List<Employee> getEmployeeByCriteria(String phoneNumber, String name, String employeeId) {
        return employeeDao.getByCriteria(phoneNumber, name, employeeId);
    }
}
