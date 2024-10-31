package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Order;
import com.huongbien.dao.DAO_Promotion;
import com.huongbien.database.Database;
import com.huongbien.entity.Order;
import com.huongbien.entity.Promotion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_ManageInvoiceController implements Initializable {
    @FXML
    public TextField txt_invoiceId;
    @FXML
    public TextField txt_discount;
    public TextField txt_dispensedAmount;
    @FXML
    public TextField txt_VAT;
    @FXML
    public TextField txt_totalAmount;
    @FXML
    public TextField txt_customer;
    @FXML
    public TextField txt_employeeId;
    @FXML
    public TextField txt_promotionId;
    @FXML
    public TextField txt_paymentId;
    @FXML
    public TextArea txtArea_notes;
    @FXML
    public TextField txt_invoiceSearch;
    @FXML
    public ImageView btn_clearSearch;
    @FXML
    public TableView<Order> tabViewInvoice;
    public TableColumn<Order, String> tabCol_invoiceID;
    @FXML
    public TableColumn<Order, Date> tabCol_dateCreateInvoice;
    @FXML
    public TableColumn<Order, Double> tabCol_totalAmount;
    @FXML
    public TableColumn<Order, String> tabCol_employeeId;
    @FXML
    public TableColumn<Order, String> tabCol_customer;
    @FXML
    public DatePicker date_orderDate;

    @FXML

    public void setCellValues(){
        try {
            Connection connection = Database.getConnection();
            DAO_Order dao_order = new DAO_Order(connection);
            List<Order> orders = dao_order.get();
            ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

            tabCol_invoiceID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            tabCol_dateCreateInvoice.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            tabCol_totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            tabCol_totalAmount.setCellFactory(column -> {
                return new TextFieldTableCell<>(new StringConverter<Double>() {
                    @Override
                    public String toString(Double price) {
                        return price != null ? priceFormat.format(price) : "";
                    }

                    @Override
                    public Double fromString(String string) {
                        try {
                            return priceFormat.parse(string).doubleValue();
                        } catch (Exception e) {
                            return 0.0;
                        }
                    }
                });
            });

            tabCol_customer.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                            cellData.getValue().getCustomer().getCustomerId() : ""));

            tabCol_employeeId.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                            cellData.getValue().getEmployee().getEmployeeId() : ""));
            tabViewInvoice.setItems(listOrder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
    }
    @FXML
    void txt_invoiceSearchKeyReleased(KeyEvent keyEvent) {
        String id = txt_invoiceSearch.getText();
        try {
            Connection connection = Database.getConnection();
            DAO_Order dao_order = new DAO_Order(connection);
            List<Order> orders = dao_order.getSearch(id);
            ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

            tabCol_invoiceID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            tabCol_dateCreateInvoice.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            tabCol_totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            tabCol_totalAmount.setCellFactory(column -> {
                return new TextFieldTableCell<>(new StringConverter<Double>() {
                    @Override
                    public String toString(Double price) {
                        return price != null ? priceFormat.format(price) : "";
                    }

                    @Override
                    public Double fromString(String string) {
                        try {
                            return priceFormat.parse(string).doubleValue();
                        } catch (Exception e) {
                            return 0.0;
                        }
                    }
                });
            });

            tabCol_customer.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                            cellData.getValue().getCustomer().getCustomerId() : ""));

            tabCol_employeeId.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                            cellData.getValue().getEmployee().getEmployeeId() : ""));
            tabViewInvoice.setItems(listOrder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }
    @FXML
    void btn_clearSearchClicked(MouseEvent mouseEvent) {
        txt_invoiceSearch.clear();
        setCellValues();
    }
    @FXML
    void getInvoiceInfo(MouseEvent mouseEvent) {
        Order selectedItem = tabViewInvoice.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            txt_invoiceId.setText(selectedItem.getOrderId());
            date_orderDate.setValue(selectedItem.getOrderDate());

            double vat = selectedItem.getVatTax();
            int vatTax = (int) (vat * 100);
            txt_VAT.setText(vatTax+"%");

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedDiscount = priceFormat.format(selectedItem.getDiscount());
            txt_discount.setText(formattedDiscount);
            String formattedDispensed = priceFormat.format(selectedItem.getDispensedAmount());
            txt_dispensedAmount.setText(formattedDispensed);
            String formattedTotalAmount = priceFormat.format(selectedItem.getTotalAmount());
            txt_totalAmount.setText(formattedTotalAmount);

            if (selectedItem.getCustomer() == null){
                txt_customer.setText("Vãng lai");
            }
            else {
                txt_customer.setText(selectedItem.getCustomer().getCustomerId());
            }

            txt_employeeId.setText(selectedItem.getEmployee().getEmployeeId());

            if (selectedItem.getPromotion() == null){
                txt_promotionId.setText("Không");
            }
            else{
                txt_promotionId.setText(selectedItem.getPromotion().getPromotionId());
            }

            txt_paymentId.setText(selectedItem.getPayment().getPaymentId());
            txtArea_notes.setText(selectedItem.getNotes());

        }
    }
}
