package com.huongbien.ui.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_ManageListOrderController implements Initializable {
    //    private final String path_paymentQueue = "src/main/resources/com/huongbien/temp/paymentQueue.json";
//    @FXML
//    private TableColumn<Map<String, Object>, Integer> col_PaymentQueueNumericalOrder;
//    @FXML
//    private TableColumn<Map<String, Object>, String> col_PaymentQueueCustomer;
//    @FXML
//    private TableColumn<Map<String, Object>, String> col_PaymentQueuePromotion;
//    @FXML
//    private TableColumn<Map<String, Object>, Integer> col_PaymentQueueQuantityCuisine;
//    @FXML
//    private TableColumn<Map<String, Object>, Double> col_PaymentQueueTotalAmount;
//    @FXML
//    private TableView<Map<String, Object>> tabView_PaymentQueue;
//
//    private ObservableList<Map<String, Object>> orderList;
//
//    // Function to load orders from JSON
//    public List<Map<String, Object>> loadOrdersFromJson(String filePath) throws IOException {
//        List<Order> orders = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode rootNode = mapper.readTree(new File(filePath));
//        for (JsonNode node : rootNode) {
//            Map<String, Object> orderMap = new HashMap<>();
//            orderMap.put("numericalOrder", node.get("Numerical Order").asInt());
//            orderMap.put("customerID", node.get("Customer ID").asText());
//            orderMap.put("promotionID", node.get("Promotion ID").asText());
//
//            // Calculate total amount and quantity of cuisines
//            double totalAmount = 0;
//            int quantityCuisine = 0;
//            for (JsonNode cuisineNode : node.get("Cuisine Order")) {
//                totalAmount += cuisineNode.get("Cuisine Money").asDouble();
//                quantityCuisine += 1;
//            }
//            orderMap.put("totalAmount", totalAmount);
//            orderMap.put("quantityCuisine", quantityCuisine);
//
//            orders.add(orderMap);
//        }
//        return orders;
//    }
//
//    // Method to load data into the TableView
//    private void loadData() throws IOException {
//        col_PaymentQueueNumericalOrder.setCellValueFactory(new PropertyValueFactory<>("numericalOrder"));
//        col_PaymentQueueCustomer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
//        col_PaymentQueuePromotion.setCellValueFactory(new PropertyValueFactory<>("promotionID"));
//        col_PaymentQueueQuantityCuisine.setCellValueFactory(new PropertyValueFactory<>("quantityCuisine"));
//        col_PaymentQueueTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//
//        List<Map<String, Object>> orders = loadOrdersFromJson(path_paymentQueue);
//        orderList = FXCollections.observableArrayList(orders);
//        tabView_PaymentQueue.setItems(orderList);
//    }
//
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            loadData();
//        } catch (IOException e) {
//            throw new RuntimeException("Error loading data from JSON file", e);
//        }
    }
}
