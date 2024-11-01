package com.huongbien.database.Faker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.huongbien.dao.*;
import com.huongbien.database.Database;
import com.huongbien.entity.*;
import com.huongbien.test.ReceivedMoney;
import com.huongbien.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

public class OrderFaker {
    public ArrayList<Table> tables;
    public ArrayList<Employee> receptionist;
    public ArrayList<Promotion> promotions;
    public ArrayList<Customer> customers;
    public ArrayList<Cuisine> cuisines;

    public OrderFaker() throws SQLException {
        Connection instance = Database.getConnection();
        DAO_Table daoTable = new DAO_Table(instance);
        DAO_Employee daoEmployee = new DAO_Employee(instance);
        DAO_Customer daoCustomer = new DAO_Customer(instance);
        DAO_Promotion daoPromotion = new DAO_Promotion(instance);
        DAO_Cuisine daoCuisine = new DAO_Cuisine(instance);

        this.tables = (ArrayList<Table>) daoTable.get();
        this.promotions = (ArrayList<Promotion>) daoPromotion.get();
        this.customers = (ArrayList<Customer>) daoCustomer.get();
        this.receptionist = (ArrayList<Employee>) daoEmployee.getByPosition("Tiếp tân");
        this.cuisines = (ArrayList<Cuisine>) daoCuisine.get();
    }

    public ArrayList<Table> fakeOrderTables() {
        return getRandom(tables, Utils.randomNumber(1, 3));
    }

    public ArrayList<OrderDetail> fakeOrderDetails(String orderId) {
        int orderDetailQuantity = Utils.randomNumber(3, 6);
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        ArrayList<Cuisine> randomCuisines = getRandom(cuisines, orderDetailQuantity);

        for (Cuisine cuisine : randomCuisines) {
            orderDetails.add(
                    new OrderDetail(
                            orderId,
                            Utils.randomNumber(1, 3),
                            "",
                            cuisine.getPrice(),
                            cuisine
                    )
            );
        }

        return orderDetails;
    }

    public Payment fakePayment(double amount, LocalDate paymentDate, LocalTime paymentTime) {
        String[] paymentsMethods = {"Tiền mặt", "Chuyển khoản"};

        return new Payment(amount, paymentDate, paymentTime, paymentsMethods[Utils.randomNumber(0, 1)]);
    }

    public Order fakeOrder(LocalDate orderDate) {
        Order order = new Order();

        order.setNotes("");
        order.setOrderDate(orderDate);
        order.setOrderId(null);
        order.setTables(fakeOrderTables());
        order.setCustomer(getRandomCustomer());
        order.setEmployee(getRandomEmployee());
        order.setOrderDetails(fakeOrderDetails(order.getOrderId()));

        order.setPromotion(getPromotion(order.getCustomer().getMembershipLevel()));
        order.setDiscount(order.getPromotion() == null ? 0 : order.getPromotion().getDiscount());
        order.setTotalAmount(order.getTotalAmount(order.getOrderDetails(), order.getPromotion()));
        order.setPaymentAmount(ReceivedMoney.getSuggestedMoneyReceived((int) order.getTotalAmount())[0]);
        order.setDispensedAmount(order.getPaymentAmount() - order.getTotalAmount());
        order.setPayment(fakePayment(order.getPaymentAmount(), orderDate, fakeTime()));

        return order;
    }

    public LocalTime fakeTime() {
        return LocalTime.of(Utils.randomNumber(0, 23), Utils.randomNumber(0, 59));
    }

    public Customer getRandomCustomer() {
        return customers.get(Utils.randomNumber(0, customers.size() - 1));
    }

    public Employee getRandomEmployee() {
        return receptionist.get(Utils.randomNumber(0, receptionist.size() - 1));
    }

    public Promotion getPromotion(int membershipLevel) {
        for (Promotion promotion : promotions) {
            if (membershipLevel == promotion.getMembershipLevel())
                return promotion;
        }

        return null;
    }

    public void fakingData() throws SQLException, JsonProcessingException {
        Connection instance = Database.getConnection();
        DAO_Order daoOrder = new DAO_Order(instance);
        DAO_Promotion daoPromotion = new DAO_Promotion(instance);

//        System.out.println(fakeOrder(LocalDate.of(2023, 12, 1)));
//        PrettyPrint.objectPrint(fakeOrder(LocalDate.of(2023, 12, 1)));

        ArrayList<Order> orders = new ArrayList<>();
        Set<Order> orderSet = new HashSet<>();

        for (int year = 2021; year <= 2024; year++) {
            for (int month = 1; month <= 12; month++) {
                int dayOfMonth = getDaysOfAMonth(month, year);
                for (int day = 1; day <= dayOfMonth; day++) {
                    int orderQuantity = Utils.randomNumber(0, 3);
                    for (int i = 0; i < orderQuantity; i++) {
                        orderSet.add(fakeOrder(LocalDate.of(year, month, day)));
                        System.out.println("Order added!");
                    }
                }
            }
        }

        System.out.println("Fake success: " + orderSet.size() + " orders, start inserted?");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        int successCount = 0;
        int failCount = 0;
        for (Order order : orderSet) {
            try {
                if (!daoOrder.add(order)) {
                    System.out.println("Add fail object: " + order);
                    failCount++;
                    break;
                } else {
                    successCount++;
                    System.out.println("Add success object: " + order);
                }
            } catch (Exception e) {
                System.out.println("Add fail object: " + order);
                failCount++;
            }
        }

        System.out.println("Total order inserted successfully: " + successCount);
        System.out.println("Total order inserted fail: " + failCount);
    }

    public int getDaysOfAMonth(int month, int year) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public <T> ArrayList<T> getRandom(ArrayList<T> list, int quantity) {
        Collections.shuffle(list);
        return new ArrayList<>(list.subList(0, Math.min(quantity, list.size())));
    }

    public <T> ArrayList<T> getRandom(ArrayList<T> list, int min, int max) {
        Collections.shuffle(list);
        return new ArrayList<>(list.subList(0, Utils.randomNumber(min, Math.min(max, list.size()))));
    }
}
