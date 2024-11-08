package com.huongbien.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huongbien.entity.Order;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GUI_ManageListOrderController implements Initializable {
    private final static String path_paymentQueue = "src/main/resources/com/huongbien/temp/paymentQueue.json";
    @FXML
    private TableColumn<Map<String, Object>, Integer> col_PaymentQueueNumericalOrder;
    @FXML
    private TableColumn<Map<String, Object>, String> col_PaymentQueueCustomer;
    @FXML
    private TableColumn<Map<String, Object>, String> col_PaymentQueuePromotion;
    @FXML
    private TableColumn<Map<String, Object>, Integer> col_PaymentQueueQuantityCuisine;
    @FXML
    private TableColumn<Map<String, Object>, String> col_PaymentQueueTotalAmount;
    @FXML
    private TableView<Map<String, Object>> tabView_PaymentQueue;

    private ObservableList<Map<String, Object>> orderList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadJsonDataPaymentQueue();
    }

    private void loadJsonDataPaymentQueue() {
        //cell
        col_PaymentQueueNumericalOrder.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Integer) cellData.getValue().get("Numerical Order"))
        );

        col_PaymentQueueCustomer.setCellValueFactory(cellData -> {
            String customerId = (String) cellData.getValue().get("Customer ID");
            if (customerId == null || customerId.isEmpty()) {
                customerId = "Khách vãng lai";
            }
            return new SimpleObjectProperty<>(customerId);
        });

        col_PaymentQueuePromotion.setCellValueFactory(cellData -> {
            String promotionId = (String) cellData.getValue().get("Promotion ID");
            if (promotionId == null || promotionId.isEmpty()) {
                promotionId = "Không áp dụng";
            }
            return new SimpleObjectProperty<>(promotionId);
        });

        col_PaymentQueueQuantityCuisine.setCellValueFactory(cellData -> {
            int quantitySum = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToInt(cuisine -> (int) cuisine.get("Cuisine Quantity"))
                    .sum();
            return new SimpleObjectProperty<>(quantitySum);
        });

        col_PaymentQueueTotalAmount.setCellValueFactory(cellData -> {
            double totalAmount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).stream()
                    .mapToDouble(cuisine -> (double) cuisine.get("Cuisine Money"))
                    .sum();
            return new SimpleObjectProperty<>(Utils.formatPrice(totalAmount));
        });
        //
        orderList = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(path_paymentQueue));
            for (JsonNode orderNode : rootNode) {
                Map<String, Object> orderMap = objectMapper.convertValue(orderNode, Map.class);
                orderList.add(orderMap);
            }
            tabView_PaymentQueue.setItems(orderList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
