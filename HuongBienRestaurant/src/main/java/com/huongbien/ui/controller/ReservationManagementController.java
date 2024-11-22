package com.huongbien.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Promotion;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {
    //Payment Queue
    @FXML private TableColumn<Map<String, Object>, Integer> paymentQueueNumericalOrderColumn;
    @FXML private TableColumn<Map<String, Object>, String> paymentQueueCustomerColumn;
    @FXML private TableColumn<Map<String, Object>, String> paymentQueuePromotionColumn;
    @FXML private TableColumn<Map<String, Object>, Integer> paymentQueueQuantityCuisineColumn;
    @FXML private TableColumn<Map<String, Object>, String> paymentQueueTotalAmountColumn;
    @FXML private TableView<Map<String, Object>> paymentQueueTableView;
    @FXML private Label customerNamePaymentQueueLabel;
    @FXML private Label tableAreaPaymentQueueLabel;
    @FXML private Label promotionNamePaymentQueueLabel;
    @FXML private Label cuisineQuantityPaymentQueueLabel;
    @FXML private Label totalAmountPaymentQueueLabel;
    @FXML private Button deletePaymentQueueButton;
    @FXML private Button orderPaymentButton;
    //Pre-Order
    //TODO: Add Pre-Order UI components here

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Payment Queue
        setUIDefault();
        loadPaymentQueueDataFromJSON();
        //Pre-Order
        //TODO:
    }

    private void disablePayQueueButton() {
        deletePaymentQueueButton.setVisible(false);
        orderPaymentButton.setVisible(false);
    }

    private void enablePayQueueButton() {
        deletePaymentQueueButton.setVisible(true);
        orderPaymentButton.setVisible(true);
    }

    private void setUIDefault() {
        //Payment Queue
        customerNamePaymentQueueLabel.setText("");
        tableAreaPaymentQueueLabel.setText("");
        cuisineQuantityPaymentQueueLabel.setText("");
        promotionNamePaymentQueueLabel.setText("");
        totalAmountPaymentQueueLabel.setText("");
        disablePayQueueButton();
        //Pre-Order

    }

    //function area
    private void loadPaymentQueueDataFromJSON() {
        //cell
        paymentQueueNumericalOrderColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Integer) cellData.getValue().get("Numerical Order"))
        );

        CustomerDAO customerDAO = CustomerDAO.getInstance();
        paymentQueueCustomerColumn.setCellValueFactory(cellData -> {
            String customerId = (String) cellData.getValue().get("Customer ID");
            if (customerId == null || customerId.isEmpty()) {
                return new SimpleObjectProperty<>("Khách vãng lai");
            }
            Customer customer = customerDAO.getById(customerId);
            String customerName = (customer != null) ? customer.getName() : "Không xác định";
            return new SimpleObjectProperty<>(customerName);
        });

        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        paymentQueuePromotionColumn.setCellValueFactory(cellData -> {
            String promotionId = (String) cellData.getValue().get("Promotion ID");
            if (promotionId == null || promotionId.isEmpty()) {
                return new SimpleObjectProperty<>("Không áp dụng");
            }
            Promotion promotion = promotionDAO.getById(promotionId);
            String promotionName = (promotion != null) ? promotion.getName() : "Không xác định";
            return new SimpleObjectProperty<>(promotionName);
        });

        paymentQueueQuantityCuisineColumn.setCellValueFactory(cellData -> {
            int cuisineCount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).size();
            return new SimpleObjectProperty<>(cuisineCount);
        });

        paymentQueueTotalAmountColumn.setCellValueFactory(cellData -> {
            double totalAmount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToDouble(cuisine -> (double) cuisine.get("Cuisine Money"))
                    .sum();
            return new SimpleObjectProperty<>(String.format("%,.0f VNĐ", totalAmount));
        });

        ObservableList<Map<String, Object>> paymentQueueObservableList = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(Constants.PAYMENT_QUEUE_PATH));
            for (JsonNode orderNode : rootNode) {
                Map<String, Object> orderMap = objectMapper.convertValue(orderNode, Map.class);
                paymentQueueObservableList.add(orderMap);
            }
            paymentQueueTableView.setItems(paymentQueueObservableList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInfoToTempJsonHandlers() throws IOException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = paymentQueueNumericalOrderColumn.getCellData(selectedIndex);

            JsonArray paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);

            JsonObject selectedPaymentQueue = null;
            int selectedPaymentQueueIndex = -1;
            for (int i = 0; i < paymentQueueArray.size(); i++) {
                JsonObject paymentQueue = paymentQueueArray.get(i).getAsJsonObject();
                if (paymentQueue.get("Numerical Order").getAsInt() == numericalOrder) {
                    selectedPaymentQueue = paymentQueue;
                    selectedPaymentQueueIndex = i;
                    break;
                }
            }

            if (selectedPaymentQueue != null) {
                //cuisine JSON
                JsonArray cuisineOrderArray = selectedPaymentQueue.getAsJsonArray("Cuisine Order");
                Utils.writeJsonToFile(cuisineOrderArray, Constants.TEMPORARY_CUISINE_PATH);
                //Table JSON
                JsonArray tableIDArray = selectedPaymentQueue.getAsJsonArray("Table ID");
                JsonArray tableArray = new JsonArray();
                for (int i = 0; i < tableIDArray.size(); i++) {
                    JsonObject tableObject = new JsonObject();
                    tableObject.addProperty("Table ID", tableIDArray.get(i).getAsString());
                    tableArray.add(tableObject);
                }
                Utils.writeJsonToFile(tableArray, Constants.TEMPORARY_TABLE_PATH);
                // Customer JSON
                JsonArray customerArray = new JsonArray();
                JsonObject customerObject = new JsonObject();
                customerObject.addProperty("Customer ID", selectedPaymentQueue.get("Customer ID").getAsString());
                customerObject.addProperty("Promotion ID", selectedPaymentQueue.get("Promotion ID").getAsString());
                customerArray.add(customerObject);
                Utils.writeJsonToFile(customerArray, Constants.TEMPORARY_CUSTOMER_PATH);
                //
                //remove after write tempBill.json and tempTab.json
                paymentQueueArray.remove(selectedPaymentQueueIndex);
                Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);

                restaurantMainController.openOrderPayment();
            } else {
                System.out.println("Không tìm thấy mục thanh toán với số thứ tự: " + numericalOrder);
            }
        } else {
            System.out.println("Chưa chọn hàng trong bảng!");
        }
    }

    private void deleteInfoFromPaymentQueueJsonHandlers() throws FileNotFoundException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = paymentQueueNumericalOrderColumn.getCellData(selectedIndex);
            System.out.println("Xóa Numerical Order: " + numericalOrder);

            JsonArray paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);

            int selectedPaymentQueueIndex = -1;
            for (int i = 0; i < paymentQueueArray.size(); i++) {
                JsonObject paymentQueue = paymentQueueArray.get(i).getAsJsonObject();
                if (paymentQueue.get("Numerical Order").getAsInt() == numericalOrder) {
                    selectedPaymentQueueIndex = i;
                    break;
                }
            }

            if (selectedPaymentQueueIndex != -1) {
                paymentQueueArray.remove(selectedPaymentQueueIndex);
                Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
                System.out.println("Đã xóa mục thanh toán có Numerical Order: " + numericalOrder);
            } else {
                System.out.println("Không tìm thấy mục thanh toán với Numerical Order: " + numericalOrder);
            }
        } else {
            System.out.println("Chưa chọn hàng trong bảng!");
        }
    }

    //Event area
    @FXML
    void onPaymentQueueTableViewClicked(MouseEvent event) throws FileNotFoundException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = paymentQueueNumericalOrderColumn.getCellData(selectedIndex);
            JsonArray jsonArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            for (JsonElement element : jsonArray) {
                JsonObject order = element.getAsJsonObject();
                if (order.get("Numerical Order").getAsInt() == numericalOrder) {
                    // Lấy thông tin khách hàng
                    String customerID = order.has("Customer ID") ? order.get("Customer ID").getAsString() : "";
                    Customer customer = customerID.isEmpty() ? null : CustomerDAO.getInstance().getById(customerID);
                    String customerName = (customer != null) ? customer.getName() : "Khách vãng lai";
                    // Lấy thông tin khu vực bàn
                    TableDAO tableDAO = TableDAO.getInstance();
                    JsonArray tableIDArray = order.getAsJsonArray("Table ID");
                    StringBuilder tableArea = new StringBuilder();
                    for (int j = 0; j < tableIDArray.size(); j++) {
                        String tableId = tableIDArray.get(j).getAsString();
                        Table table = tableDAO.getById(tableId);
                        if (table != null) {
                            String tableName = table.getName();
                            int tableFloor = table.getFloor();
                            String tableFloorStr = (tableFloor == 0) ? "Tầng trệt" : "Tầng " + tableFloor;
                            tableArea.append(tableName).append(" (").append(tableFloorStr).append(")");
                        } else {
                            tableArea.append("Tên bàn không xác định");
                        }
                        if (j < tableIDArray.size() - 1) {
                            tableArea.append(", ");
                        }
                    }
                    // Lấy thông tin khuyến mãi
                    String promotionID = order.has("Promotion ID") ? order.get("Promotion ID").getAsString() : "";
                    Promotion promotion = promotionID.isEmpty() ? null : PromotionDAO.getInstance().getById(promotionID);
                    String promotionName = (promotion != null) ? promotion.getName() : "Không áp dụng";

                    JsonArray cuisineOrderArray = order.getAsJsonArray("Cuisine Order");
                    int cuisineQuantity = cuisineOrderArray.size();
                    double totalAmount = 0;
                    for (JsonElement cuisineElement : cuisineOrderArray) {
                        JsonObject cuisine = cuisineElement.getAsJsonObject();
                        double money = cuisine.get("Cuisine Money").getAsDouble();
                        totalAmount += money;
                    }
                    //setLabel
                    customerNamePaymentQueueLabel.setText(customerName);
                    tableAreaPaymentQueueLabel.setText(tableArea.toString());
                    cuisineQuantityPaymentQueueLabel.setText(cuisineQuantity+" món");
                    promotionNamePaymentQueueLabel.setText(promotionName);
                    totalAmountPaymentQueueLabel.setText(String.format("%,.0f VNĐ", totalAmount));
                    break;
                }
            }
            enablePayQueueButton();
        }
    }

    @FXML
    void onOrderPaymentButtonClicked(ActionEvent event) throws IOException {
        writeInfoToTempJsonHandlers();
    }

    @FXML
    void onDeletePaymentQueueButtonClicked(ActionEvent event) throws FileNotFoundException {
        deleteInfoFromPaymentQueueJsonHandlers();
        paymentQueueTableView.getItems().clear();
        loadPaymentQueueDataFromJSON();
        setUIDefault();
    }

    @FXML
    void onPreOrderTableButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openPreOrderTable();
    }
}