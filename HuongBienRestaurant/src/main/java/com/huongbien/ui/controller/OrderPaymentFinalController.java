package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentFinalController implements Initializable {
    @FXML private VBox screenDefaultMethodVBox;
    @FXML private VBox screenCashMethodVBox;
    @FXML private VBox screenBankingMethodVBox;
    @FXML private VBox screenEWalletMethodVBox;
    @FXML private Label paymentEmployeeLabel;
    @FXML private Label paymentCuisineQuantityLabel;
    @FXML private Label paymentTotalAmountLabel;
    @FXML private Label paymentVATLabel;
    @FXML private Label paymentDiscountLabel;
    @FXML private Label paymentFinalAmountLabel;
    @FXML private Label tableInfoLabel;
    @FXML private ScrollPane billScrollPane;
    @FXML public GridPane billGridPane;

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setInfo();
            loadCuisine();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCuisine() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getCuisineData());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                OrderPaymentBillItemController orderPaymentBillItemController = fxmlLoader.getController();
                orderPaymentBillItemController.setDataBill(orderDetails.get(i));
                orderPaymentBillItemController.setOrderPaymentFinalController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                billGridPane.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        billScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        billGridPane.prefWidthProperty().bind(billScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromCuisineJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<OrderDetail> getCuisineData() throws FileNotFoundException {
        return readFromCuisineJSON();
    }

    public void setInfo() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayLoginSession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        //Set screen method default
        screenDefaultMethodVBox.setVisible(true);
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
        //Session
        for (JsonElement element : jsonArrayLoginSession) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO dao_employee = EmployeeDAO.getInstance();
            Employee employee = dao_employee.getById(id).get(0);
            paymentEmployeeLabel.setText(employee.getName());
        }
        //table
        StringBuilder tabInfoBuilder = new StringBuilder();
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            String floorStr = (table.getFloor() == 0 ? "Tầng trệt" : "Tầng " + table.getFloor());
            tabInfoBuilder.append(table.getName()).append(" (").append(floorStr).append(") ").append(", ");
        }
        if (!tabInfoBuilder.isEmpty()) {
            tabInfoBuilder.setLength(tabInfoBuilder.length() - 2);
        }
        tableInfoLabel.setText(tabInfoBuilder.toString());
        //
        int totalQuantityCuisine = 0;
        double totalAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            totalQuantityCuisine++;
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        //set discount
        double discount = 0.0;
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Promotion ID").getAsString();
            PromotionDAO promotionDAO = PromotionDAO.getInstance();
            Promotion promotion = promotionDAO.getById(id);
            if (promotion != null) {
                discount = promotion.getDiscount();
            }
        }
        double discountMoney = totalAmount * discount;
        if (discountMoney > 0) {
            paymentDiscountLabel.setText(String.format("- %,.0f VNĐ", discountMoney));
        } else {
            paymentDiscountLabel.setText("0 VNĐ");
        }
        //set VAT
        double vat = totalAmount * 0.1;
        paymentCuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        paymentTotalAmountLabel.setText(String.format("%,.0f VNĐ", totalAmount));
        paymentVATLabel.setText(String.format("%,.0f VNĐ", vat));
        //FinalAmount
        double finalAmount = totalAmount - discountMoney + vat;
        paymentFinalAmountLabel.setText(String.format("%,.0f VNĐ", finalAmount));
    }

    public void openCashMethod() {
        screenDefaultMethodVBox.setVisible(false);
        screenCashMethodVBox.setVisible(true);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openBankingMethod() {
        screenDefaultMethodVBox.setVisible(false);
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(true);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openEWalletMethod() {
        screenDefaultMethodVBox.setVisible(false);
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(true);
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderPayment();
    }

    @FXML
    void onCashButtonAction(ActionEvent event) {
        openCashMethod();
    }

    @FXML
    void onBankingButtonAction(ActionEvent event) {
        openBankingMethod();
    }

    @FXML
    void onEWalletButtonAction(ActionEvent event) {
        openEWalletMethod();
    }
}