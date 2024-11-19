package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
import com.huongbien.service.QRCodeHandler;
import com.huongbien.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentController implements Initializable {
    @FXML
    public TableView<Promotion> promotionTableView;
    @FXML
    private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML
    private TableColumn<Promotion, String> promotionIdColumn;
    @FXML
    private TableColumn<Promotion, String> promotionNameColumn;
    @FXML
    private Label paymentEmployeeLabel;
    @FXML
    private Label paymentCuisineQuantityLabel;
    @FXML
    private Label paymentTotalAmountLabel;
    @FXML
    private Label paymentVATLabel;
    @FXML
    private Label paymentDiscountLabel;
    @FXML
    private Label paymentFinalAmountLabel;
    @FXML
    private Label tableInfoLabel;
    @FXML
    private ScrollPane billScrollPane;
    @FXML
    public GridPane billGridPane;
    @FXML
    private TextField searchCustomerField;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerRankField;
    @FXML
    private Button searchCustomerButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button createCustomerQRButton;
    //---
    private VideoCapture capture;
    private Timer timer;
    private QRCodeHandler qrCodeHandler;
    //---
    public RestaurantMainController restaurantMainController;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    public void loadBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getBillData());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                OrderPaymentBillItemController _OrderPaymentBillItemController = fxmlLoader.getController();
                _OrderPaymentBillItemController.setDataBill(orderDetails.get(i));
                _OrderPaymentBillItemController.setOrderPaymentBillController(this);
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

    public List<OrderDetail> readFromBillJSON() throws FileNotFoundException {
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

    private List<OrderDetail> getBillData() throws FileNotFoundException {
        return readFromBillJSON();
    }

    public void setPaymentInfo() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayBill = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        JsonArray jsonArrayTab = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayEmp = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        //Emp Session
        for (JsonElement element : jsonArrayEmp) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO dao_employee = EmployeeDAO.getInstance();
            Employee employee = dao_employee.getById(id).get(0);
            paymentEmployeeLabel.setText(employee.getName());
        }
        //table
        StringBuilder tabInfoBuilder = new StringBuilder();
        for (JsonElement element : jsonArrayTab) {
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
        for (JsonElement element : jsonArrayBill) {
            totalQuantityCuisine++;
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        //set discount
        double discount = 0.0;
        double discountMoney = totalAmount * discount;
        paymentDiscountLabel.setText(String.format("%,.0f VNĐ", discountMoney));
        //set VAT
        double vat = totalAmount * 0.1;
        paymentCuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        paymentTotalAmountLabel.setText(String.format("%,.0f VNĐ", totalAmount));
        paymentVATLabel.setText(String.format("%,.0f VNĐ", vat));
        //FinalAmount
        double finalAmount = totalAmount - discountMoney + vat;
        paymentFinalAmountLabel.setText(String.format("%,.0f VNĐ", finalAmount));
        readCumtomerExistsFromJSON();
    }

    public void setPromotionTableValue() {
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getAll();
        promotionList.sort(Comparator.comparing(Promotion::getDiscount).reversed());

        ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        promotionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        promotionDiscountColumn.setCellFactory(col -> new TableCell<Promotion, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });
        promotionTableView.setItems(listPromotion);
    }

    private void readCumtomerExistsFromJSON() throws FileNotFoundException {
        JsonArray customerArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        if (!customerArray.isEmpty()) {
            JsonObject customerObject = customerArray.get(0).getAsJsonObject();
            String idCustomer = customerObject.get("Customer ID").getAsString();
            CustomerDAO customerDAO = CustomerDAO.getInstance();
            Customer customer = customerDAO.getById(idCustomer);
            searchCustomerField.setText((customer != null ? customer.getPhoneNumber() : ""));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.qrCodeHandler = new QRCodeHandler();
            loadBill();
            setPaymentInfo();
            setPromotionTableValue();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        searchCustomerButton.fire();
        promotionTableView.setMouseTransparent(true);
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderCuisine();
    }

    @FXML
    void addCustomerButton(ActionEvent event) throws IOException {
        restaurantMainController.openCustomerManagement();
    }

    private void setDiscountFromPromotionSearch() throws FileNotFoundException {
        double totalAmount = 0;
        JsonArray jsonArrayBill = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        for (JsonElement element : jsonArrayBill) {
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        double discount = 0.0;
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getAll();
        ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
        if (!listPromotion.isEmpty()) {
            Promotion maxDiscountPromotion = listPromotion.stream()
                    .max(Comparator.comparingDouble(Promotion::getDiscount))
                    .orElse(null);

            promotionTableView.getSelectionModel().select(maxDiscountPromotion);
            discount = maxDiscountPromotion.getDiscount();
        } else {
            //Not promotion cause, I set default 0
            discount = 0.0;
        }
        double discountMoney = totalAmount * discount;
        paymentDiscountLabel.setText(String.format("- %,.0f VNĐ", discountMoney));
        //set VAT
        double vat = totalAmount * 0.1;
        paymentTotalAmountLabel.setText(String.format("%,.0f VNĐ", totalAmount));
        paymentVATLabel.setText(String.format("%,.0f VNĐ", vat));
        //FinalAmount
        double finalAmount = totalAmount - discountMoney + vat;
        paymentFinalAmountLabel.setText(String.format("%,.0f VNĐ", finalAmount));
    }

    @FXML
    void onSearchCustomerButtonClicked(ActionEvent event) throws FileNotFoundException, SQLException {
        String inputPhone = searchCustomerField.getText().trim();
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        Customer customer = customerDAO.getByOnePhoneNumber(inputPhone);
        if (customer != null) {
            // Set discount
            setDiscountFromPromotionSearch();
            //Write Down JSON
            String customerID = customer.getCustomerId();
            String promotionID = promotionTableView.getSelectionModel().getSelectedItem().getPromotionId();
            JsonArray customerArray = new JsonArray();
            JsonObject customerObject = new JsonObject();
            customerObject.addProperty("Customer ID", customerID);
            customerObject.addProperty("Promotion ID", promotionID);
            customerArray.add(customerObject);
            Utils.writeJsonToFile(customerArray, Constants.TEMPORARY_CUSTOMER_PATH);
            //display label
            customerIdField.setText(customerID);
            customerNameField.setText(customer.getName());
            customerRankField.setText(Utils.toStringMembershipLevel(customer.getMembershipLevel()));
            //enable table
            promotionTableView.setDisable(false);
        } else {
            customerIdField.setText("");
            customerNameField.setText("");
            customerRankField.setText("");
            promotionTableView.setDisable(true);
            promotionTableView.getSelectionModel().clearSelection();
            //clear JSON if exists
            Utils.writeJsonToFile(new JsonArray(), Constants.TEMPORARY_CUSTOMER_PATH);
            setPaymentInfo();
        }
    }

    @FXML
    void onCreateCustomerQRButtonClickedClicked(ActionEvent event) {
        openSwingWindow();
    }

    private void openSwingWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cửa sổ quét mã QR");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);
            JLabel cameraLabel = new JLabel();
            frame.add(cameraLabel, BorderLayout.CENTER);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    stopCamera();
                }
            });

            frame.setVisible(true);
            readQRCode(cameraLabel, frame);
        });
    }

    private void readQRCode(JLabel cameraLabel, JFrame frame) {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            Utils.showAlert("Không thể mở camera!", "Lỗi");
            return;
        }

        timer = new Timer(100, e -> {
            Mat frameMat = new Mat();
            if (capture != null && capture.read(frameMat)) {
                if (!frameMat.empty()) {
                    BufferedImage bufferedImage = qrCodeHandler.matToBufferedImage(frameMat);
                    String qrCodeContent = qrCodeHandler.decodeQRCode(bufferedImage);
                    if (qrCodeContent != null) {
                        try {
                            updateCustomerFields(qrCodeContent);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        stopCamera();
                        capture.release();
                        cameraLabel.setIcon(null);
                        frame.dispose();
                    } else {
                        ImageIcon icon = new ImageIcon(bufferedImage);
                        cameraLabel.setIcon(icon);
                    }
                }
            } else {
                System.err.println("Không thể đọc frame từ camera.");
            }
        });

        timer.start();
    }

    private void stopCamera() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (capture != null && capture.isOpened()) {
            capture.release();
        }
    }

    private void updateCustomerFields(String qrCodeContent) throws SQLException, FileNotFoundException {
        String[] parts = qrCodeContent.split(",");
        if (parts.length >= 4) {
            Platform.runLater(() -> {
//              No Delete that code, it's for testing
//                customerIdField.setText(parts[0]);
//                customerNameField.setText(parts[1]);
//                customerRankField.setText(Utils.toStringMembershipLevel(Integer.parseInt(parts[2])));
                String inputPhone = parts[3];
                searchCustomerField.setText(inputPhone);
                searchCustomerButton.fire();
            });
        }
    }

    public void addNewPaymentQueue() throws FileNotFoundException {
        JsonArray paymentQueueArray;
        JsonArray tableArray;
        JsonArray cuisineArray;

        try {
            paymentQueueArray = Utils.readJsonFromFile(Constants.PAYMENT_QUEUE_PATH);
            tableArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
            cuisineArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        } catch (FileNotFoundException e) {
            paymentQueueArray = new JsonArray();
            tableArray = new JsonArray();
            cuisineArray = new JsonArray();
        }

        int newNumericalOrder = !paymentQueueArray.isEmpty()
                ? paymentQueueArray.get(paymentQueueArray.size() - 1).getAsJsonObject().get("Numerical Order").getAsInt() + 1
                : 1;

        JsonObject newPaymentQueue = new JsonObject();
        newPaymentQueue.addProperty("Numerical Order", newNumericalOrder);
        //
        JsonArray customerArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        if (!customerArray.isEmpty()) {
            JsonObject customerObject = customerArray.get(0).getAsJsonObject();
            newPaymentQueue.addProperty("Customer ID", customerObject.get("Customer ID").getAsString());
            newPaymentQueue.addProperty("Promotion ID", customerObject.get("Promotion ID").getAsString());
        } else {
            newPaymentQueue.addProperty("Customer ID", "");
            newPaymentQueue.addProperty("Promotion ID", "");
        }
        //
        JsonArray tableIDs = new JsonArray();
        for (int i = 0; i < tableArray.size(); i++) {
            JsonObject tableObject = tableArray.get(i).getAsJsonObject();
            tableIDs.add(tableObject.get("Table ID").getAsString());
        }
        newPaymentQueue.add("Table ID", tableIDs);

        JsonArray cuisineOrderArray = new JsonArray();
        for (int i = 0; i < cuisineArray.size(); i++) {
            JsonObject billItem = cuisineArray.get(i).getAsJsonObject();
            JsonObject cuisineOrderItem = new JsonObject();
            //
            cuisineOrderItem.addProperty("Cuisine ID", billItem.get("Cuisine ID").getAsString());
            cuisineOrderItem.addProperty("Cuisine Name", billItem.get("Cuisine Name").getAsString());
            cuisineOrderItem.addProperty("Cuisine Price", billItem.get("Cuisine Price").getAsDouble());
            cuisineOrderItem.addProperty("Cuisine Note", billItem.get("Cuisine Note").getAsString());
            cuisineOrderItem.addProperty("Cuisine Quantity", billItem.get("Cuisine Quantity").getAsInt());
            cuisineOrderItem.addProperty("Cuisine Money", billItem.get("Cuisine Money").getAsDouble());
            cuisineOrderArray.add(cuisineOrderItem);
        }
        newPaymentQueue.add("Cuisine Order", cuisineOrderArray);

        paymentQueueArray.add(newPaymentQueue);

        Utils.writeJsonToFile(paymentQueueArray, Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TEMPORARY_CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TEMPORARY_TABLE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TEMPORARY_CUSTOMER_PATH);
    }

    //save payment queue
    @FXML
    void onSavePaymentQueueButtonClicked(ActionEvent event) throws IOException {
        addNewPaymentQueue();
        restaurantMainController.openReservationManagement();
    }

    @FXML
    void onSearchCustomerFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchCustomerButton.fire();
        }
    }

    @FXML
    void onPaymentButtonClicked(ActionEvent event) throws IOException {
        Utils.showAlert("Chức năng đang phát triển", "Thông báo gián đoạn");
        restaurantMainController.openOrderPaymentFinal();
    }
}