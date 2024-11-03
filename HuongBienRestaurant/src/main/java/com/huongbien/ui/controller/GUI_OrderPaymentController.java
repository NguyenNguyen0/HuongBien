package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.dao.DAO_Customer;
import com.huongbien.dao.DAO_Promotion;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Customer;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Promotion;
import com.huongbien.utils.Converter;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderPaymentController implements Initializable {
    private final static String path_bill = "src/main/resources/com/huongbien/temp/bill.json";
    private final static String path_table = "src/main/resources/com/huongbien/temp/table.json";
    @FXML
    private TableView<Promotion> tabView_promotion;

    @FXML
    private TableColumn<Promotion, Double> col_proDiscount;

    @FXML
    private TableColumn<Promotion, String> col_proID;

    @FXML
    private TableColumn<Promotion, String> col_proName;

    @FXML
    private Label lbl_payEmp;

    @FXML
    private Label lbl_payCuisineQuantity;

    @FXML
    private Label lbl_payTotalAmount;

    @FXML
    private Label lbl_payVAT;

    @FXML
    private Label lbl_payDiscount;

    @FXML
    private Label lbl_payFinalAmount;

    @FXML
    private Label lbl_payTabFloor;

    @FXML
    private Label lbl_payTabName;

    @FXML
    private Label lbl_payTabTypeName;

    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    public GridPane compoent_gridBill;

    @FXML
    private TextField txt_searchCustomer;

    @FXML
    private TextField txt_idCustomer;

    @FXML
    private TextField txt_nameCustomer;

    @FXML
    private TextField txt_rankCustomer;

    @FXML
    private Button btn_addCustomer;

    @FXML
    private Button btn_qrCustomer;

    public GUI_MainController gui_mainController;

    public void setGUI_MainController(GUI_MainController gui_mainController) {
        this.gui_mainController = gui_mainController;
    }

    public void loadingBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(dataBill());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                GUI_OrderPaymentBillItemController gui_OrderPaymentBillItemController = fxmlLoader.getController();
                gui_OrderPaymentBillItemController.setDataBill(orderDetails.get(i));
                gui_OrderPaymentBillItemController.setOrderPaymnetBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridBill.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compoent_scrollBill.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridBill.prefWidthProperty().bind(compoent_scrollBill.widthProperty());
    }

    public List<OrderDetail> readFromJSON_bill() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(path_bill);

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

    private List<OrderDetail> dataBill() throws FileNotFoundException {
        List<OrderDetail> ls = readFromJSON_bill();
        return ls;
    }

    public void setInfoPayment() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayBill = Utils.readJsonFromFile(path_bill);
        JsonArray jsonArrayTab = Utils.readJsonFromFile(path_table);
        //get Employee
        for (JsonElement element : jsonArrayTab) {
            JsonObject jsonObject = element.getAsJsonObject();
            //
            String name = jsonObject.get("Table Name").getAsString();
            int floor = jsonObject.get("Table Floor").getAsInt();
            JsonObject tableTypeObject = jsonObject.getAsJsonObject("Table Type");
            String typeName = tableTypeObject.get("Table Type Name").getAsString();
            //
            String floorStr = (floor == 0 ? "Tầng trệt" : "Tầng "+floor);
            lbl_payTabFloor.setText(floorStr);
            lbl_payTabName.setText(name);
            lbl_payTabTypeName.setText(typeName);
        }

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
        lbl_payDiscount.setText(Utils.formatPrice(discountMoney)+" VNĐ");
        //set VAT
        double vat = totalAmount * 0.1;
        lbl_payCuisineQuantity.setText(totalQuantityCuisine+ " món");
        lbl_payTotalAmount.setText(Utils.formatPrice(totalAmount)+ " VNĐ");
        lbl_payVAT.setText("- "+ Utils.formatPrice(vat)+ " VNĐ");
        //FinalAmount
        double finalAmount = totalAmount - discountMoney - vat;
        lbl_payFinalAmount.setText(Utils.formatPrice(finalAmount)+ " VNĐ");
    }

    public void setCellValues() {
        try {
            DAO_Promotion dao_promotion = new DAO_Promotion(Database.getConnection());
            List<Promotion> promotionList = dao_promotion.get();
            promotionList.sort(Comparator.comparing(Promotion::getDiscount).reversed());

            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            col_proID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
            col_proName.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_proDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            col_proDiscount.setCellFactory(col -> new TableCell<Promotion, Double>() {
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

            tabView_promotion.setItems(listPromotion);
            tabView_promotion.setDisable(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadingBill();
            setInfoPayment();
            setCellValues();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btn_back(ActionEvent event) throws IOException {
        gui_mainController.openCuisine();
    }

    @FXML
    void btn_addCustomer(ActionEvent event) throws IOException {
        gui_mainController.openManageCustomer();
    }

    @FXML
    void btn_searchCustomer(MouseEvent event) throws SQLException, FileNotFoundException {
        String inputPhone = txt_searchCustomer.getText().trim();
        DAO_Customer dao_customer = new DAO_Customer(Database.getConnection());
        Customer customer = dao_customer.getByPhone(inputPhone);
        if(customer != null) {
            txt_idCustomer.setText(customer.getCustomerId());
            txt_nameCustomer.setText(customer.getName());
            txt_rankCustomer.setText(Utils.toStringMembershipLevel(customer.getMembershipLevel()));
            tabView_promotion.setDisable(false);
            // Set discount
            double totalAmount = 0;
            JsonArray jsonArrayBill = Utils.readJsonFromFile(path_bill);
            for (JsonElement element : jsonArrayBill) {
                JsonObject jsonObject = element.getAsJsonObject();
                double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
                totalAmount += cuisineMoney;
            }
            double discount = 0.0;
            DAO_Promotion dao_promotion = new DAO_Promotion(Database.getConnection());
            List<Promotion> promotionList = dao_promotion.get();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            if (!listPromotion.isEmpty()) {
                Promotion maxDiscountPromotion = listPromotion.stream()
                        .max(Comparator.comparingDouble(Promotion::getDiscount))
                        .orElse(null);

                tabView_promotion.getSelectionModel().select(maxDiscountPromotion);
                discount = maxDiscountPromotion.getDiscount();
            } else {
                discount = 0.0;
                System.out.println("No promotions available.");
            }
            double discountMoney = totalAmount * discount;
            lbl_payDiscount.setText(" - "+ Utils.formatPrice(discountMoney)+" VNĐ");
            //set VAT
            double vat = totalAmount * 0.1;
            lbl_payTotalAmount.setText(Utils.formatPrice(totalAmount)+ " VNĐ");
            lbl_payVAT.setText("- "+ Utils.formatPrice(vat)+ " VNĐ");
            //FinalAmount
            double finalAmount = totalAmount - discountMoney - vat;
            lbl_payFinalAmount.setText(Utils.formatPrice(finalAmount)+ " VNĐ");
        } else {
            System.out.println("Không tìm thấy khách hàng, vui lòng đăng ký thành viên");
            txt_idCustomer.setText("");
            txt_nameCustomer.setText("");
            txt_rankCustomer.setText("");
            tabView_promotion.setDisable(true);
            tabView_promotion.getSelectionModel().clearSelection();
            setInfoPayment();
        }
    }

    @FXML
    void btn_qrCustomer(ActionEvent event) {

    }

    @FXML
    void tabView_promotion(MouseEvent event) throws FileNotFoundException, SQLException {
        Promotion selectedItem = tabView_promotion.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getPromotionId();
            DAO_Promotion dao_promotion = new DAO_Promotion(Database.getConnection());
            Promotion promotion = dao_promotion.get(idSelect);

            double totalAmount = 0;
            JsonArray jsonArrayBill = Utils.readJsonFromFile(path_bill);
            for (JsonElement element : jsonArrayBill) {
                JsonObject jsonObject = element.getAsJsonObject();
                double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
                totalAmount += cuisineMoney;
            }

            double discount = promotion.getDiscount();
            double discountMoney = totalAmount * discount;
            lbl_payDiscount.setText(" - "+ Utils.formatPrice(discountMoney)+" VNĐ");
            //set VAT
            double vat = totalAmount * 0.1;
            lbl_payTotalAmount.setText(Utils.formatPrice(totalAmount)+ " VNĐ");
            lbl_payVAT.setText("- "+ Utils.formatPrice(vat)+ " VNĐ");
            //FinalAmount
            double finalAmount = totalAmount - discountMoney - vat;
            lbl_payFinalAmount.setText(Utils.formatPrice(finalAmount)+ " VNĐ");
        }
    }
}