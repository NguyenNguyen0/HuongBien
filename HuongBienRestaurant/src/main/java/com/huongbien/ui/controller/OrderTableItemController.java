package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class OrderTableItemController {
    @FXML
    private ImageView tableImageView;
    @FXML
    private ImageView checkedImageView;
    @FXML
    private Label tableIdLabel; // Table ID displayed in the table item, get from the database !important
    @FXML
    private Label tableNameLabel;
    @FXML
    private Label tableSeatLabel;
    @FXML
    private Label tableTypeLabel;

    private boolean isCheck = false;

    public OrderTableController orderTableController;

    // Set the orderTableController
    public void setOrderTableController(OrderTableController orderTableController) {
        this.orderTableController = orderTableController;
    }

    public void setTableItemData(Table table) {
        tableIdLabel.setText(table.getId());
        tableNameLabel.setText(table.getName());
        tableTypeLabel.setText(table.getTableType().getName());
        tableSeatLabel.setText("Số chỗ: " + table.getSeats());
        setTableImage(table.getStatus());
        setCheckedTableFromJSON();
    }

    private void setTableImage(String tableStatus) {
        switch (tableStatus) {
            case "Bàn trống" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-empty-512px.png"));
            case "Đặt trước" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-preOrder-512px.png"));
            case "Phục vụ" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-ordered-512px.png"));
            case "Bàn đóng" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-stop-512px.png"));
        }
    }

    private void setCheckedTableFromJSON() {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableIdLabel.getText())) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    public void updateCheckedIcon(String tableId) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    private void writeDataToJSONFile(String tableId) {
        Table table = TableDAO.getInstance().getById(tableId);
        if (table != null) {
            JsonArray jsonArray;
            try {
                jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }
            boolean tableExists = false;
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject existingTable = jsonArray.get(i).getAsJsonObject();
                if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                    jsonArray.remove(i);
                    tableExists = true;
                    break;
                }
            }
            if (!tableExists) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Table ID", table.getId());
                jsonArray.add(jsonObject);
            }
            Utils.writeJsonToFile(jsonArray, Constants.TEMPORARY_TABLE_PATH);
            updateCheckedIcon(tableId);
        } else {
            System.out.println("Table with ID " + tableId + " not found in the database.");
        }
    }

    @FXML
    void onTableItemComponentClicked(MouseEvent event) throws FileNotFoundException, SQLException {
        //check status of table
        String tableId = tableIdLabel.getText();
        Table table = TableDAO.getInstance().getById(tableId);
        switch (table.getStatus()) {
            case "Đặt trước" -> Utils.showAlert("Bàn đang được đặt trước, không thể chọn bàn này.", "Đặt trước");
            case "Phục vụ" -> Utils.showAlert("Bàn đang được phục vụ, không thể chọn bàn này.", "Phục vụ");
            case "Bàn đóng" -> Utils.showAlert("Bàn đã đóng, không thể chọn bàn này.", "Bàn đóng");
            case "Bàn trống" -> {
                writeDataToJSONFile(tableId);
                orderTableController.tableInfoTabPane.getTabs().clear();
                orderTableController.readTableDataFromJSON();
            }
        }
    }
}
