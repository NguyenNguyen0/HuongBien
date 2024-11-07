package com.huongbien.bus;


import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Table;

import java.time.LocalDate;
import java.util.List;

public class TableBUS {
    private final TableDAO tableDao;

    public TableBUS() {
        tableDao = TableDAO.getInstance();
    }

    public List<Table> getAllTable() {
        return tableDao.getAll();
    }

    public Table getTable(String tableId) {
        if (tableId.isBlank() || tableId.isEmpty()) return null;
        return tableDao.getById(tableId);
    }

    public List<Table> getAllTableByOrderId(String orderId) {
        if (orderId.isBlank() || orderId.isEmpty()) return null;
        return tableDao.getAllByOrderId(orderId);
    }

    public List<Table> getAllTableByReservationId(String reservationId) {
        if (reservationId.isBlank() || reservationId.isEmpty()) return null;
        return tableDao.getAllByReservationId(reservationId);
    }

    public List<Table> getReservedTables(LocalDate date) {
        if (date == null) return null;
        return tableDao.getReservedTables(date);
    }

    public List<String> getDistinctStatuses() {
        return tableDao.getDistinctStatuses();
    }

    public boolean updateTableStatus(String tableId, String status) {
        if (tableId.isBlank() || tableId.isEmpty() || status.isBlank() || status.isEmpty()) return false;
        return tableDao.updateStatus(tableId, status);
    }

    public boolean addTable(Table table) {
        if (table == null) return false;
        return tableDao.add(table);
    }

    public boolean addTablesToOrder(String orderId, List<Table> tables) {
        if (tables.isEmpty() || orderId.isBlank() || orderId.isEmpty()) return false;
        return tableDao.addTablesToOrder(orderId, tables);
    }

    public boolean addTablesToReservation(String reservationId, List<Table> tables) {
        if (tables.isEmpty() || reservationId.isBlank() || reservationId.isEmpty()) return false;
        return tableDao.addTablesToReservation(reservationId, tables);
    }
}
