package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PreOrderController implements Initializable {
    @FXML private TextField numOfAttendeesField;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private TextField tableInfoField;
    @FXML private Label tableFeeLabel;

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }
    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTimeComboBox();
        try {
            setTableInfoFromJSON();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //function area
    private void setTimeComboBox() {
        hourComboBox.getItems().clear();
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        hourComboBox.setValue("23");
        minuteComboBox.getItems().clear();
        for (int i = 0; i < 60; i+=5) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
        minuteComboBox.setValue("55");
    }

    private void setTableInfoFromJSON() throws FileNotFoundException {
        //get table info from json file
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        StringBuilder tableInfoBuilder = new StringBuilder();
        double totalTableFee = 0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            if (table != null) {
                //set table text
                String floorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                tableInfoBuilder.append(table.getName()).append(" (").append(floorStr).append("), ");
            } else {
                tableInfoBuilder.append("Thông tin bàn không xác định, ");
            }
            //calculate table fee
            assert table != null;
            totalTableFee += (table.getTableType().getTableId().equals("LB002")) ? Constants.TABLE_PRICE : 0;
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        tableInfoField.setText(tableInfoBuilder.toString());

        //set table price
        tableFeeLabel.setText(String.format("%,.0f VNĐ", totalTableFee));

    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onReservationManagementButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openReservationManagement();
    }

    @FXML
    void onDecreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        if (currentValue > 1) {
            numOfAttendeesField.setText(String.valueOf(currentValue - 1));
        }
    }

    @FXML
    void onIncreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        numOfAttendeesField.setText(String.valueOf(currentValue + 1));
    }

    @FXML
    void onEditTableButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onPreOrderCuisineButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openPreOrderCuisine();
    }

    @FXML
    void onSavePreOrderTableButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openReservationManagement();
        //TODO: Viết sự kiện lưu thông tin đặt trước xuống database
        System.out.println("Đã lưu thành công");
    }
}
