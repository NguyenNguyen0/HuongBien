package com.huongbien.entity;

import java.util.Objects;

public class Table {
    private String tableId;
    private int seats;
    private String location;
    private boolean isAvailable;
    private TableType tableType;

    public Table() {}

    public Table(String tableId, int seats, String location, boolean isAvailable, TableType tableType) {
        setTableId(tableId);
        setSeats(seats);
        setLocation(location);
        setIsAvailable(isAvailable);
        setTableType(tableType);
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        if (tableId == null || !tableId.matches("^T\\dB\\d{3}$")) {
            throw new IllegalArgumentException("Invalid table ID format");
        }
        this.tableId = tableId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        if (seats < 2) {
            throw new IllegalArgumentException("Seats must be 2 or greater");
        }
        this.seats = seats;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableId='" + tableId + '\'' +
                ", seats=" + seats +
                ", location='" + location + '\'' +
                ", isAvailable=" + isAvailable +
                ", tableType=" + tableType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(tableId, table.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableId);
    }
}

