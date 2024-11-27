package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Optional;

public class PreOrderCuisineBillItemController {
    @FXML
    private Label cuisineIdLabel;
    @FXML
    private Label cuisineNameLabel;
    @FXML
    private Label cuisineNoteLabel;
    @FXML
    private Label cuisineQuantityLabel;
    @FXML
    private Label cuisineSalePriceLabel;
    @FXML
    private Label cuisineTotalPriceLabel;

    //Controller area
    public PreOrderCuisineController preOrderCuisineController;
    public void setPreOrderBillController(PreOrderCuisineController preOrderCuisineController) {
        this.preOrderCuisineController = preOrderCuisineController;
    }


    public void setDataBill(OrderDetail orderDetail) {
        cuisineIdLabel.setText(orderDetail.getCuisine().getCuisineId());
        cuisineNameLabel.setText(orderDetail.getCuisine().getName());
        cuisineSalePriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getCuisine().getPrice()));
        cuisineNoteLabel.setText(orderDetail.getNote());
        cuisineQuantityLabel.setText(orderDetail.getQuantity() + "");
        cuisineTotalPriceLabel.setText(String.format("%,.0f VNĐ", orderDetail.getSalePrice()));
    }

    private void removeFromJson(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to remove item.");
            return;
        }
        for (int i = jsonArray.size() - 1; i >= 0; i--) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();
            if (existingCuisineID.equals(cuisineID)) {
                jsonArray.remove(i);
                System.out.println("Removed item with Cuisine ID: " + cuisineID);
                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.TEMPORARY_CUISINE_PATH);
    }

    private void increaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to increase quantity.");
            return;
        }

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

            if (existingCuisineID.equals(cuisineID)) {
                int currentQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
                double price = jsonObject.get("Cuisine Price").getAsDouble();

                int newQuantity = currentQuantity + 1;
                jsonObject.addProperty("Cuisine Quantity", newQuantity);

                double newMoney = price * newQuantity;
                jsonObject.addProperty("Cuisine Money", newMoney);

                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.TEMPORARY_CUISINE_PATH);
    }

    private void decreaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH); // Đọc file JSON
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to decrease quantity.");
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

            if (existingCuisineID.equals(cuisineID)) {
                int currentQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
                double price = jsonObject.get("Cuisine Price").getAsDouble();

                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    jsonObject.addProperty("Cuisine Quantity", newQuantity);

                    double newMoney = price * newQuantity;
                    jsonObject.addProperty("Cuisine Money", newMoney);

                    System.out.println("Decreased quantity for Cuisine ID: " + cuisineID + ". New Quantity: " + newQuantity);
                } else {
                    jsonArray.remove(i);
                    System.out.println("Removed item with Cuisine ID: " + cuisineID + " due to zero quantity.");
                }
                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.TEMPORARY_CUISINE_PATH);
    }

    private void updateNoteInJSON(String cuisineID, String newNote) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH); // Đọc file JSON
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Unable to update note.");
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();

            if (existingCuisineID.equals(cuisineID)) {
                jsonObject.addProperty("Cuisine Note", newNote);
                System.out.println("Updated note for Cuisine ID: " + cuisineID + ". New Note: " + newNote);
                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, Constants.TEMPORARY_CUISINE_PATH);
    }


    @FXML
    void onDecreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        decreaseQuantityInJSON(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onDeleteCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        removeFromJson(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onIncreaseCuisineButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String cuisineID = cuisineIdLabel.getText();
        increaseQuantityInJSON(cuisineID);
        preOrderCuisineController.billGridPane.getChildren().clear();
        preOrderCuisineController.loadBill();
        //update lbl
        preOrderCuisineController.setCuisinesInfoFromJSON();
    }

    @FXML
    void onNoteCuisineButtonClicked(ActionEvent event) throws FileNotFoundException {
        // Hiển thị dialog để người dùng nhập ghi chú mới
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Nhập ghi chú cho món ăn:");
        inputDialog.setContentText("Ghi chú:");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        // Lấy giá trị ghi chú từ dialog nếu người dùng nhấn OK
        Optional<String> result = inputDialog.showAndWait();
        result.ifPresent(newNote -> {
            String cuisineID = cuisineIdLabel.getText();
            try {
                updateNoteInJSON(cuisineID, newNote);
                preOrderCuisineController.billGridPane.getChildren().clear();
                preOrderCuisineController.loadBill();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Không thể cập nhật ghi chú.");
                alert.setContentText("Đã xảy ra lỗi khi cập nhật ghi chú vào JSON.");
                alert.showAndWait();
            }
        });
    }
}
