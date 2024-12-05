package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.config.Variable;
import com.huongbien.dao.CuisineDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

public class OrderCuisineController implements Initializable {
    //cuisine
    @FXML private ScrollPane cuisineScrollPane;
    @FXML private GridPane cuisineGridPane;
    @FXML private ScrollPane billScrollPane;
    @FXML public GridPane billGridPane;
    @FXML private Label tableInfoLabel;
    @FXML private Label cuisineAmountLabel;
    @FXML private Label cuisineQuantityLabel;
    @FXML private Label tableAmountLabel;
    @FXML private Label totalAmountLabel;

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCuisine();
        try {
            loadBill();
            setCuisinesInfoFromJSON();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCuisine() {
        List<Cuisine> cuisines = new ArrayList<>(getCuisineData());
        int columns = 0;
        int rows = 1;
        try {
            for (Cuisine cuisine : cuisines) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderCuisineItem.fxml"));
                VBox cuisineBox = fxmlLoader.load();
                OrderCuisineItemController orderCuisineItemController = fxmlLoader.getController();
                orderCuisineItemController.setCuisineData(cuisine);
                orderCuisineItemController.setOrderCuisineController(this);
                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }
                cuisineGridPane.add(cuisineBox, columns++, rows);
                GridPane.setMargin(cuisineBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cuisineScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cuisineGridPane.prefWidthProperty().bind(cuisineScrollPane.widthProperty());
    }

    public void loadBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getBillData());
        int columns = 0;
        int rows = 1;
        try {
            for (OrderDetail orderDetail : orderDetails) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderCuisineBillItem.fxml"));
                HBox billBox = fxmlLoader.load();
                OrderCuisineBillItemController orderCuisineBillItemController = fxmlLoader.getController();
                orderCuisineBillItemController.setDataBill(orderDetail);
                orderCuisineBillItemController.setOrderBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                billGridPane.add(billBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        billScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        billGridPane.prefWidthProperty().bind(billScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromBillJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            //set item cuisine bill
            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<Cuisine> getCuisineData() {
        CuisineDAO cuisineDAO = CuisineDAO.getInstance();
        return cuisineDAO.getAll();
    }

    private List<OrderDetail> getBillData() throws FileNotFoundException {
        return readFromBillJSON();
    }

    public void setCuisinesInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);

        int totalQuantityCuisine = 0;
        double cuisineAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int cuisineQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalQuantityCuisine += cuisineQuantity;
            cuisineAmount += cuisineMoney;
        }
        //table
        double tableAmount = 0.0;
        StringBuilder tableInfoBuilder = new StringBuilder();
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            if (table != null) {
                //set table text
                String floorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                tableInfoBuilder.append(table.getName()).append(" (").append(floorStr).append("), ");
                //calculator table amount
                tableAmount += table.getTableType().getTableId().equals(Variable.tableVipID) ? Variable.tableVipPrice : 0;
            } else {
                tableInfoBuilder.append("Thông tin bàn không xác định, ");
            }
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        //setLabel
        tableInfoLabel.setText(tableInfoBuilder.toString());
        cuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount));
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        totalAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount + tableAmount));
    }

    @FXML
    void onOrderPaymentButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        if (!jsonArray.isEmpty()) {
            restaurantMainController.openOrderPayment();
        } else {
            Utils.showAlert("Vui lòng chọn món", "Reminder");
        }
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onClearCuisineButtonAction(ActionEvent event) throws FileNotFoundException, SQLException {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        billGridPane.getChildren().clear();
        loadBill();
        setCuisinesInfoFromJSON();
    }
}
