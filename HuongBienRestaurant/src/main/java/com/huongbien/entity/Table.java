package com.huongbien.entity;

import java.util.Objects;

public class Table {
    private String id;
    private String name;
    private int floor;
    private int seats;
    private boolean isAvailable;
    private TableType tableType;

    public Table() {
    }

    public Table(String id, String name, int floor, int seats, boolean isAvailable, TableType tableType) {
        setId(id);
        setName(name);
        setFloor(floor);
        setSeats(seats);
        setIsAvailable(isAvailable);
        setTableType(tableType);
    }

    public void setId(String id) {
        if (id == null || !id.matches("^T\\dB\\d{3}$")) {
            throw new IllegalArgumentException("Invalid table ID format");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void setFloor(int floor) {
        if (floor < 0) {
            throw new IllegalArgumentException("Floor must be 0 or greater");
        }
        this.floor = floor;
    }

    public void setSeats(int seats) {
        if (seats < 2) {
            throw new IllegalArgumentException("Seats must be 2 or greater");
        }
        this.seats = seats;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFloor() {
        return floor;
    }

    public int getSeats() {
        return seats;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public TableType getTableType() {
        return tableType;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", floor=" + floor +
                ", seats=" + seats +
                ", isAvailable=" + isAvailable +
                ", tableType=" + tableType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
