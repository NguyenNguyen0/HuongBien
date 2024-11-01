package com.huongbien.database.Faker;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public class TestFaker {
    public static void main(String[] args) throws SQLException, JsonProcessingException {
        OrderFaker orderFaker = new OrderFaker();
        orderFaker.fakingData();

//        ReservationFaker reservationFaker = new ReservationFaker();
//        reservationFaker.fakingData();
    }
}
