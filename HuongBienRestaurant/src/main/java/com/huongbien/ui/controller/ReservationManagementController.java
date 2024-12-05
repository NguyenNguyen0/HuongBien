package com.huongbien.ui.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.ReservationDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
import com.huongbien.utils.RefreshJSON;
import com.huongbien.utils.ToastsMessage;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {
    //Payment-Queue
    @FXML
    private TableView<Map<String, Object>> paymentQueueTableView;
    @FXML
    private TableColumn<Map<String, Object>, Integer> numericalPaymentQueueOrderColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> customerPaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, Integer> quantityCuisinePaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> promotionPaymentQueueColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> totalAmountPaymentQueueColumn;
    @FXML
    private Button toOrderPaymentButton;
    @FXML
    private Button deletePaymentQueueButton;
    @FXML
    private Label countPaymentQueueLabel;
    @FXML
    private Label customerNamePaymentQueueLabel;
    @FXML
    private Label tableAreaPaymentQueueLabel;
    @FXML
    private Label promotionNamePaymentQueueLabel;
    @FXML
    private Label cuisineQuantityPaymentQueueLabel;
    @FXML
    private Label totalAmountPaymentQueueLabel;
    //Pre-Order
    @FXML
    private TableView<Reservation> preOrderTableView;
    @FXML
    private TableColumn<Reservation, String> idPreOrderColumn;
    @FXML
    private TableColumn<Reservation, String> customerPreOrderColumn;
    @FXML
    private TableColumn<Reservation, Integer> partySizePreOrderColumn;
    @FXML
    private TableColumn<Reservation, LocalTime> receiveTimePreOrderColumn;
    @FXML
    private TableColumn<Reservation, String> partyTypePreOrderColumn;
    @FXML
    private DatePicker receivePreOrderDatePicker;
    @FXML
    private Button editPreOrderButton;
    @FXML
    private Button confirmTablePreOrderButton;
    @FXML
    private Button deletePreOrderButton;
    @FXML
    private Label countPreOrderLabel;
    @FXML
    private Label customerPreOrderLabel;
    @FXML
    private Label tablePreOrderLabel;
    @FXML
    private Label cuisinePreOrderLabel;
    @FXML
    private Label depositPreOrderLabel;
    @FXML
    private Label notePreOrderLabel;

    //Controller area
    public RestaurantMainController restaurantMainController;

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Payment Queue
        try {
            setUIDefault();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Payment Queue
        loadPaymentQueueDataFromJSON();
        //Pre-Order
        setPreOrderTableViewColumn();
    }

    private void disablePayQueueButton() {
        deletePaymentQueueButton.setVisible(false);
        toOrderPaymentButton.setVisible(false);
    }

    private void enablePayQueueButton() {
        deletePaymentQueueButton.setVisible(true);
        toOrderPaymentButton.setVisible(true);
    }

    private void disablePreOrderButton() {
        editPreOrderButton.setVisible(false);
        deletePreOrderButton.setVisible(false);
        confirmTablePreOrderButton.setVisible(false);
    }

    private void enablePreOrderButton() {
        editPreOrderButton.setVisible(true);
        deletePreOrderButton.setVisible(true);
        confirmTablePreOrderButton.setVisible(true);
    }

    private void setUIDefault() throws FileNotFoundException {
        //Payment Queue
        customerNamePaymentQueueLabel.setText("");
        tableAreaPaymentQueueLabel.setText("");
        cuisineQuantityPaymentQueueLabel.setText("");
        promotionNamePaymentQueueLabel.setText("");
        totalAmountPaymentQueueLabel.setText("");
        countPaymentQueueLabel.setText("( " + Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH).size() + " )");
        disablePayQueueButton();
        //Pre-Order
        receivePreOrderDatePicker.setValue(LocalDate.now());
        customerPreOrderLabel.setText("");
        tablePreOrderLabel.setText("");
        cuisinePreOrderLabel.setText("");
        depositPreOrderLabel.setText("");
        notePreOrderLabel.setText("");
        disablePreOrderButton();
    }

    //TODO: Payment Queue
    //function area
    private void loadPaymentQueueDataFromJSON() {
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        paymentQueueTableView.setPlaceholder(new Label("Không có dữ liệu"));
        numericalPaymentQueueOrderColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Integer) cellData.getValue().get("Numerical Order"))
        );
        customerPaymentQueueColumn.setCellValueFactory(cellData -> {
            String customerId = (String) cellData.getValue().get("Customer ID");
            if (customerId == null || customerId.isEmpty()) {
                return new SimpleObjectProperty<>("Khách vãng lai");
            }
            Customer customer = customerDAO.getById(customerId);
            String customerName = (customer != null) ? customer.getName() : "Không xác định";
            return new SimpleObjectProperty<>(customerName);
        });
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        promotionPaymentQueueColumn.setCellValueFactory(cellData -> {
            String promotionId = (String) cellData.getValue().get("Promotion ID");
            if (promotionId == null || promotionId.isEmpty()) {
                return new SimpleObjectProperty<>("Không áp dụng");
            }
            Promotion promotion = promotionDAO.getById(promotionId);
            String promotionName = (promotion != null) ? promotion.getName() : "Không xác định";
            return new SimpleObjectProperty<>(promotionName);
        });
        quantityCuisinePaymentQueueColumn.setCellValueFactory(cellData -> {
            int cuisineCount = ((List<Map<String, Object>>) cellData.getValue().get("Cuisine Order")).size();
            return new SimpleObjectProperty<>(cuisineCount);
        });
        totalAmountPaymentQueueColumn.setCellValueFactory(cellData -> {
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
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
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
            //cuisine JSON
            assert selectedPaymentQueue != null;
            JsonArray cuisineOrderArray = selectedPaymentQueue.getAsJsonArray("Cuisine Order");
            Utils.writeJsonToFile(cuisineOrderArray, Constants.CUISINE_PATH);
            //Table JSON
            JsonArray tableIDArray = selectedPaymentQueue.getAsJsonArray("Table ID");
            JsonArray tableArray = new JsonArray();
            for (int i = 0; i < tableIDArray.size(); i++) {
                JsonObject tableObject = new JsonObject();
                tableObject.addProperty("Table ID", tableIDArray.get(i).getAsString());
                tableArray.add(tableObject);
            }
            Utils.writeJsonToFile(tableArray, Constants.TABLE_PATH);
            // Customer JSON
            JsonArray customerArray = new JsonArray();
            JsonObject customerObject = new JsonObject();
            customerObject.addProperty("Customer ID", selectedPaymentQueue.get("Customer ID").getAsString());
            customerObject.addProperty("Promotion ID", selectedPaymentQueue.get("Promotion ID").getAsString());
            customerArray.add(customerObject);
            Utils.writeJsonToFile(customerArray, Constants.CUSTOMER_PATH);
            //
            //remove after write CUISINE.json and TABLE.json
            paymentQueueArray.remove(selectedPaymentQueueIndex);
            Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
            restaurantMainController.openOrderPayment();
        } else {
            System.out.println("Chưa chọn hàng trong bảng!");
        }
    }

    private void deleteInfoFromPaymentQueueJsonHandlers() throws FileNotFoundException {
        int selectedIndex = paymentQueueTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
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
            Integer numericalOrder = numericalPaymentQueueOrderColumn.getCellData(selectedIndex);
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
                    cuisineQuantityPaymentQueueLabel.setText(cuisineQuantity + " món");
                    promotionNamePaymentQueueLabel.setText(promotionName);
                    totalAmountPaymentQueueLabel.setText(String.format("%,.0f VNĐ", totalAmount));
                    break;
                }
            }
            enablePayQueueButton();
        }
    }

    @FXML
    void onOrderPaymentButtonAction(ActionEvent event) throws IOException {
        writeInfoToTempJsonHandlers();
    }

    @FXML
    void onDeletePaymentQueueButtonClicked(ActionEvent event) throws FileNotFoundException {
        deleteInfoFromPaymentQueueJsonHandlers();
        paymentQueueTableView.getItems().clear();
        loadPaymentQueueDataFromJSON();
        setUIDefault();
    }

    //Pre-Order here
    private void setPreOrderTableViewColumn() {
        ReservationDAO reservationDAO = ReservationDAO.getInstance();
        //set Quantity Pre Order
        int quantity = reservationDAO.getCountReservationNotReceiveByDate(receivePreOrderDatePicker.getValue());
        countPreOrderLabel.setText("( " + quantity + " )");
        //table view
        preOrderTableView.getItems().clear();
        preOrderTableView.setPlaceholder(new Label("Không có dữ liệu"));
        List<Reservation> reservationList = reservationDAO.getReservationNotReceiveByDate(receivePreOrderDatePicker.getValue());
        idPreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        customerPreOrderColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return new SimpleStringProperty(customer.getName());
        });
        partySizePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("partySize"));
        receiveTimePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("receiveTime"));
        partyTypePreOrderColumn.setCellValueFactory(new PropertyValueFactory<>("partyType"));
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList(reservationList);
        preOrderTableView.setItems(reservationObservableList);
    }

    @FXML
    void receivePreOrderDatePickerAction(ActionEvent event) {
        setPreOrderTableViewColumn();
    }

    @FXML
    void onPreOrderTableViewClicked(MouseEvent event) throws FileNotFoundException {
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            customerPreOrderLabel.setText(reservation.getCustomer().getName());
            //Format Table label
            List<Table> tables = reservation.getTables();
            StringBuilder tableInfo = new StringBuilder();
            for (Table table : tables) {
                String tableFloorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                tableInfo.append(table.getName())
                        .append(" (")
                        .append(tableFloorStr)
                        .append(" - ")
                        .append(table.getTableType().getName())
                        .append("), ");
            }
            if (!tableInfo.isEmpty()) {
                tableInfo.setLength(tableInfo.length() - 2);
            }
            tablePreOrderLabel.setText(tableInfo.toString());
            cuisinePreOrderLabel.setText(reservation.getFoodOrders().size() + " món");
            depositPreOrderLabel.setText(String.format("%,.0f VNĐ", reservation.getDeposit()));
            notePreOrderLabel.setText(reservation.getNote() != null ? reservation.getNote() : "");
            enablePreOrderButton();
        }
    }

    @FXML
    void onPreOrderButtonAction(ActionEvent event) throws IOException {
        RefreshJSON.clearAllJSONWithoutLoginSession();
        restaurantMainController.openPreOrder();
    }

    @FXML
    void onEditPreOrderButtonAction(ActionEvent event) throws IOException {
        int selectedIndex = preOrderTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Reservation reservation = preOrderTableView.getSelectionModel().getSelectedItem();
            String id = reservation.getReservationId();
            ToastsMessage.showMessage("Đang cập nhật bàn cho đơn hàng đặt trước " + id, "success");

            //reservation
            JsonArray jsonArrayReservation = new JsonArray();
            JsonObject jsonObjectReservation = new JsonObject();
            jsonObjectReservation.addProperty("Reservation ID", id);
            jsonArrayReservation.add(jsonObjectReservation);
            Utils.writeJsonToFile(jsonArrayReservation, Constants.RESERVATION_PATH);

            //Convert Reservation Database to JSON
            //cuisine
            List<FoodOrder> foodOrders = reservation.getFoodOrders();
            JsonArray jsonArrayCuisine = new JsonArray();
            for (FoodOrder foodOrder : foodOrders) {
                JsonObject jsonObjectCuisine = new JsonObject();
                jsonObjectCuisine.addProperty("Cuisine ID", foodOrder.getCuisine().getCuisineId());
                jsonObjectCuisine.addProperty("Cuisine Name", foodOrder.getCuisine().getName());
                jsonObjectCuisine.addProperty("Cuisine Price", foodOrder.getCuisine().getPrice());
                jsonObjectCuisine.addProperty("Cuisine Note", foodOrder.getNote());
                jsonObjectCuisine.addProperty("Cuisine Quantity", foodOrder.getQuantity());
                jsonObjectCuisine.addProperty("Cuisine Money", foodOrder.getQuantity() * foodOrder.getCuisine().getPrice());
                jsonArrayCuisine.add(jsonObjectCuisine);
            }
            Utils.writeJsonToFile(jsonArrayCuisine, Constants.CUISINE_PATH);

            //table
            List<Table> tables = reservation.getTables();
            JsonArray jsonArrayTable = new JsonArray();
            for (Table table : tables) {
                JsonObject jsonObjectTable = new JsonObject();
                jsonObjectTable.addProperty("Table ID", table.getId());
                jsonArrayTable.add(jsonObjectTable);
            }
            Utils.writeJsonToFile(jsonArrayTable, Constants.TABLE_PATH);

            //customer
            JsonArray jsonArrayCustomer = new JsonArray();
            JsonObject jsonObjectCustomer = new JsonObject();
            jsonObjectCustomer.addProperty("Customer ID", reservation.getCustomer().getCustomerId());
            jsonArrayCustomer.add(jsonObjectCustomer);
            Utils.writeJsonToFile(jsonArrayCustomer, Constants.CUSTOMER_PATH);
        }
        restaurantMainController.openPreOrder();
    }

    @FXML
    void onConfirmTablePreOrderButtonAction(ActionEvent event) throws IOException {
        //TODO: Receive Pre-Order
        restaurantMainController.openOrderPayment();
    }

    @FXML
    void onDeletePreOrderButtonClicked(ActionEvent event) {

    }
}