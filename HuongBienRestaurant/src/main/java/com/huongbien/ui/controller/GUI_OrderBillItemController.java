package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderBillItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/bill.json";

    @FXML
    private Label lbl_cuisineID;

    @FXML
    private Button btn_cuisineDecrease;

    @FXML
    private Button btn_cuisineDelete;

    @FXML
    private Button btn_cuisineIncrease;

    @FXML
    private Button btn_cuisineNote;

    @FXML
    private Label lbl_cuisineName;

    @FXML
    private Label lbl_cuisineNote;

    @FXML
    private Label lbl_cuisineQuantity;

    @FXML
    private Label lbl_cuisineSalePrice;

    @FXML
    private Label lbl_salePrice;

    private GUI_OrderCuisineController gui_orderCuisineController;

    public void setOrderBillController(GUI_OrderCuisineController gui_orderCuisineController) {
        this.gui_orderCuisineController = gui_orderCuisineController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDataBill(OrderDetail orderDetail) {
        lbl_cuisineID.setText(orderDetail.getCuisine().getCuisineId());
        lbl_cuisineName.setText(orderDetail.getCuisine().getName());
        lbl_cuisineSalePrice.setText(orderDetail.getCuisine().getPrice()+"");
        lbl_cuisineNote.setText(orderDetail.getNote());
        lbl_cuisineQuantity.setText(orderDetail.getQuantity()+"");
        lbl_salePrice.setText(orderDetail.getSalePrice()+"");
    }

    private void removeFromJson(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path);
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
        Utils.writeJsonToFile(jsonArray, path);
    }

    private void increaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path);
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

                System.out.println("Increased quantity for Cuisine ID: " + cuisineID + ". New Quantity: " + newQuantity);
                break;
            }
        }
        Utils.writeJsonToFile(jsonArray, path);
    }

    private void decreaseQuantityInJSON(String cuisineID) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path); // Đọc file JSON
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
        Utils.writeJsonToFile(jsonArray, path);
    }

    private void updateNoteInJSON(String cuisineID, String newNote) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path); // Đọc file JSON
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
        Utils.writeJsonToFile(jsonArray, path);
    }



    @FXML
    void btn_cuisineDecrease(ActionEvent event) throws FileNotFoundException {
        String cuisineID = lbl_cuisineID.getText();
        decreaseQuantityInJSON(cuisineID);
        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
    }

    @FXML
    void btn_cuisineDelete(ActionEvent event) throws FileNotFoundException {
        String cuisineID = lbl_cuisineID.getText();
        removeFromJson(cuisineID);
        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
    }

    @FXML
    void btn_cuisineIncrease(ActionEvent event) throws FileNotFoundException {
        String cuisineID = lbl_cuisineID.getText();
        increaseQuantityInJSON(cuisineID);
        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
    }

    @FXML
    void btn_cuisineNote(ActionEvent event) throws FileNotFoundException {
        String cuisineID = lbl_cuisineID.getText();
        String newNote = "Không ớt";
        updateNoteInJSON(cuisineID, newNote);
        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
    }
}
