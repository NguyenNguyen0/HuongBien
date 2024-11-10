package com.huongbien.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {
    private final static String PAYMENT_QUEUE_PATH = "src/main/resources/com/huongbien/temp/paymentQueue.json";
    @FXML
    private TableColumn<Map<String, Object>, Integer> paymentQueueNumericalOrderColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> paymentQueueCustomerColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> paymentQueuePromotionColumn;
    @FXML
    private TableColumn<Map<String, Object>, Integer> paymentQueueQuantityCuisineColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> paymentQueueTotalAmountColumn;
    @FXML
    private TableView<Map<String, Object>> paymentQueueTable;

    private ObservableList<Map<String, Object>> orderList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPaymentQueueDataFromJSON();
    }

    private void loadPaymentQueueDataFromJSON() {
        //cell
        paymentQueueNumericalOrderColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Integer) cellData.getValue().get("Numerical Order"))
        );

        paymentQueueCustomerColumn.setCellValueFactory(cellData -> {
            String customerId = (String) cellData.getValue().get("Customer ID");
            if (customerId == null || customerId.isEmpty()) {
                customerId = "Khách vãng lai";
            }
            return new SimpleObjectProperty<>(customerId);
        });

        paymentQueuePromotionColumn.setCellValueFactory(cellData -> {
            String promotionId = (String) cellData.getValue().get("Promotion ID");
            if (promotionId == null || promotionId.isEmpty()) {
                promotionId = "Không áp dụng";
            }
            return new SimpleObjectProperty<>(promotionId);
        });

        paymentQueueQuantityCuisineColumn.setCellValueFactory(cellData -> {
            int quantitySum = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToInt(cuisine -> (int) cuisine.get("Cuisine Quantity"))
                    .sum();
            return new SimpleObjectProperty<>(quantitySum);
        });

        paymentQueueTotalAmountColumn.setCellValueFactory(cellData -> {
            double totalAmount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToDouble(cuisine -> (double) cuisine.get("Cuisine Money"))
                    .sum();
            return new SimpleObjectProperty<>(Utils.formatPrice(totalAmount));
        });
        //
        orderList = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(PAYMENT_QUEUE_PATH));
            for (JsonNode orderNode : rootNode) {
                Map<String, Object> orderMap = objectMapper.convertValue(orderNode, Map.class);
                orderList.add(orderMap);
            }
            paymentQueueTable.setItems(orderList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
