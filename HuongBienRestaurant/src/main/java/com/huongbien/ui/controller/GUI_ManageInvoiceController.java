package com.huongbien.ui.controller;

import com.huongbien.dao.OrderDAO;
import com.huongbien.entity.Order;
import com.huongbien.utils.Paginator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
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
    public Button searchOrderButton;
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
    private Label pageIndexLabel;

    //    TODO: viết lại logic cho code đỡ bẩn
    private static final OrderDAO orderDAO = OrderDAO.getInstance();

    private static final Paginator<Order> orderPaginator = new Paginator<>((offset, limit) -> OrderDAO.getInstance().getWithPagination(offset, limit), orderDAO.getTotalOrderCount(), 10, false);

    public void setCellValues() {
        tabViewInvoice.getItems().clear();
        try {
            List<Order> orders = orderPaginator.getCurrentPage();
            setPageIndexLabel(orderPaginator.getCurrentPageIndex(), orderPaginator.getTotalPages());
            ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

            tabCol_invoiceID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            tabCol_dateCreateInvoice.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            tabCol_totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            tabCol_totalAmount.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
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
            }));

            tabCol_customer.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                            cellData.getValue().getCustomer().getCustomerId() : ""));

            tabCol_employeeId.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                            cellData.getValue().getEmployee().getEmployeeId() : ""));

            tabViewInvoice.setItems(listOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
    }

    //    TODO: xử lý lại phần pagination khi search
    @FXML
    void searchButtonOnClick(MouseEvent event) {
        String id = txt_invoiceSearch.getText();
        List<Order> orders = null;

        if (id.isEmpty() || id.isBlank()) {
            orders = orderPaginator.getCurrentPage();
        } else {
//                TODO: xử lý lại phần search
//                orders = orderDAO.searchOrder(id);
        }

        ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

        tabCol_invoiceID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tabCol_dateCreateInvoice.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        tabCol_totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        tabCol_totalAmount.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
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
        }));

        tabCol_customer.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                        cellData.getValue().getCustomer().getCustomerId() : ""));

        tabCol_employeeId.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                        cellData.getValue().getEmployee().getEmployeeId() : ""));
        tabViewInvoice.setItems(listOrder);
    }

    @FXML
    void btn_clearSearchClicked(MouseEvent mouseEvent) {
        txt_invoiceSearch.clear();
        setCellValues();
    }

    @FXML
    void getInvoiceInfo(MouseEvent mouseEvent) {
        Order selectedItem = tabViewInvoice.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txt_invoiceId.setText(selectedItem.getOrderId());
            date_orderDate.setValue(selectedItem.getOrderDate());

            double vat = selectedItem.getVatTax();
            int vatTax = (int) (vat * 100);
            txt_VAT.setText(vatTax + "%");

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedDiscount = priceFormat.format(selectedItem.getDiscount());
            txt_discount.setText(formattedDiscount);
            String formattedDispensed = priceFormat.format(selectedItem.getDispensedAmount());
            txt_dispensedAmount.setText(formattedDispensed);
            String formattedTotalAmount = priceFormat.format(selectedItem.getTotalAmount());
            txt_totalAmount.setText(formattedTotalAmount);

            if (selectedItem.getCustomer() == null) {
                txt_customer.setText("Vãng lai");
            } else {
                txt_customer.setText(selectedItem.getCustomer().getCustomerId());
            }

            txt_employeeId.setText(selectedItem.getEmployee().getEmployeeId());

            if (selectedItem.getPromotion() == null) {
                txt_promotionId.setText("Không");
            } else {
                txt_promotionId.setText(selectedItem.getPromotion().getPromotionId());
            }

            txt_paymentId.setText(selectedItem.getPayment().getPaymentId());
            txtArea_notes.setText(selectedItem.getNotes());

        }
    }

    public void setPageIndexLabel(int currentPage, int totalPage) {
        pageIndexLabel.setText(currentPage + "/" + totalPage);
    }

    @FXML
    void lastPageButtonOnClick(MouseEvent event) {
        orderPaginator.goToLastPage();
        setCellValues();
    }

    @FXML
    void nextPageButtonOnClick(MouseEvent event) {
        orderPaginator.goToNextPage();
        setCellValues();
    }

    @FXML
    void prevPageButtonOnClick(MouseEvent event) {
        orderPaginator.goToPreviousPage();
        setCellValues();
    }

    @FXML
    void firstPageButtonOnClick(MouseEvent event) {
        orderPaginator.goToFirstPage();
        setCellValues();
    }
}
