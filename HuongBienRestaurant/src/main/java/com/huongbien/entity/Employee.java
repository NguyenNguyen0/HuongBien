package com.huongbien.entity;

import com.huongbien.utils.Utils;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private String employeeId;
    private String name;
    private String phoneNumber;
    private String citizenIDNumber;
    private boolean gender;
    private String address;
    private LocalDate birthday;
    private String email;
    private String status;
    private LocalDate hireDate;
    private String position;
    private double workHours;
    private double hourlyPay;
    private double salary;
    private Employee manager;

    public Employee() {}

    public Employee(String employeeId, String name, String phoneNumber, String citizenIDNumber,
                    boolean gender, String address, LocalDate birthday, String email,
                    String status, LocalDate hireDate, String position, double workHours,
                    double hourlyPay, double salary, Employee manager) {
        setEmployeeId(employeeId);
        setName(name);
        setPhoneNumber(phoneNumber);
        setCitizenIDNumber(citizenIDNumber);
        setGender(gender);
        setAddress(address);
        setBirthday(birthday);
        setEmail(email);
        setStatus(status);
        setHireDate(hireDate);
        setPosition(position);
        setWorkHours(workHours);
        setHourlyPay(hourlyPay);
        setSalary(salary);
        setManager(manager);
    }

    public Employee(String name, String phoneNumber, String citizenIDNumber,
                    boolean gender, String address, LocalDate birthday, String email,
                    String position, double workHours, double hourlyPay, double salary,
                    Employee manager) {
        setEmployeeId(null);
        setName(name);
        setPhoneNumber(phoneNumber);
        setCitizenIDNumber(citizenIDNumber);
        setGender(gender);
        setAddress(address);
        setBirthday(birthday);
        setEmail(email);
        setStatus("đang làm");
        setHireDate(LocalDate.now());
        setPosition(position);
        setWorkHours(workHours);
        setHourlyPay(hourlyPay);
        setSalary(salary);
        setManager(manager);
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null) {
            LocalDate currentDate = LocalDate.now();
            this.employeeId = String.format("NV%02d%02d%02d%03d",
                    currentDate.getYear() % 100,
                    currentDate.getMonthValue(),
                    currentDate.getDayOfMonth(),
                    Utils.randomNumber(1, 999)
            );
            return;
        }

        if (employeeId.matches("^NV\\d{9}$")) {
            this.employeeId = employeeId;
            return;
        }

        throw new IllegalArgumentException("Invalid employeeId format");
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || name.split(" ").length < 2) {
            throw new IllegalArgumentException("Họ tên không không được rỗng và phải có ít nhất hai từ");
        }
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("'phoneNumber' không được rỗng, gồm 10 kí tự và bắt đầu bằng số 0");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setCitizenIDNumber(String citizenIDNumber) {
        if (citizenIDNumber == null || citizenIDNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("'citizenIDNumber' không được rỗng");
        }
        this.citizenIDNumber = citizenIDNumber;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(LocalDate birthday) {
        if (birthday != null && LocalDate.now().minusYears(18).isBefore(birthday)) {
            throw new IllegalArgumentException("Nhân viên phải >=18 tuổi");
        }
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("email phải là đuôi @....com");
        }
        this.email = email;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status không được rỗng");
        }
        this.status = status;
    }

    public void setHireDate(LocalDate hireDate) {
        if (hireDate != null && hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("hireDate phải trước ngày hiện tại");
        }
        this.hireDate = hireDate;
    }

    public void setPosition(String position) {
        if (!"Phục vụ".equals(position) && !"Quản lí".equals(position)) {
            throw new IllegalArgumentException("Position phải là 'Phục vụ' hoặc 'Quản lí'");
        }
        this.position = position;
    }

    public void setWorkHours(double workHours) {
        if (workHours <= 0) {
            throw new IllegalArgumentException("workHours: số giờ làm phải lớn hơn 0");
        }
        this.workHours = workHours;
    }

    public void setHourlyPay(double hourlyPay) {
        if (hourlyPay < 20000) {
            throw new IllegalArgumentException("hourlyPay: Số lương theo giờ tối thiểu là 20k");
        }
        this.hourlyPay = hourlyPay;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCitizenIDNumber() {
        return citizenIDNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getPosition() {
        return position;
    }

    public double getWorkHours() {
        return workHours;
    }

    public double getHourlyPay() {
        return hourlyPay;
    }

    public double getSalary() {
        return salary;
    }

    public Employee getManager() {
        return manager;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", citizenIDNumber='" + citizenIDNumber + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", hireDate=" + hireDate +
                ", position='" + position + '\'' +
                ", workHours=" + workHours +
                ", hourlyPay=" + hourlyPay +
                ", salary=" + salary +
                ", manager=" + (manager != null ? manager.getName() : "Không có quản lý") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(employeeId);
    }
}