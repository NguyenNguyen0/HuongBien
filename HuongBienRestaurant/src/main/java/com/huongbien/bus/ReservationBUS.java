package com.huongbien.bus;

import com.huongbien.dao.ReservationDAO;
import com.huongbien.entity.Reservation;

import java.util.List;

public class ReservationBUS {
    private final ReservationDAO reservationDao;

    public ReservationBUS() {
        reservationDao = ReservationDAO.getInstance();
    }

    public List<Reservation> getReservationsByCustomerId(String customerId) {
        if (customerId.isBlank() || customerId.isEmpty()) return null;
        return reservationDao.getAllByCustomerId(customerId);
    }

    public Reservation getReservationById(String reservationId) {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return reservationDao.getById(reservationId);
    }

    public boolean addReservation(Reservation reservation) {
        if (reservation == null) return false;
        return reservationDao.add(reservation);
    }
}
