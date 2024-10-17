package com.huongbien.entity;

import java.util.Objects;

public class TableType {
    private String tableId;
    private String name;
    private String description;

    TableType(int tableNumber, String name, String description) {
        this.setTableId(tableNumber);
        this.setName(name);
        this.setDescription(description);
    }

    public void setTableId(int tableNumber) {
        this.tableId = String.format("LB%03d", tableNumber);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTableId() {
        return tableId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TableType{" +
                "tableId='" + tableId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableType tableType = (TableType) o;
        return Objects.equals(tableId, tableType.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableId);
    }
}
