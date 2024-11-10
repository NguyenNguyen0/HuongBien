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

public class OrderManagementController implements Initializable {
    @FXML
    public TextField orderIdField;
    @FXML
    public TextField orderDiscountField;
    @FXML
    public TextField orderDispensedAmount;
    @FXML
    public TextField orderVATField;
    @FXML
    public TextField orderTotalAmountField;
    @FXML
    public TextField orderCustomerField;
    @FXML
    public TextField orderEmployeeIdField;
    @FXML
    public TextField orderPromotionIdField;
    @FXML
    public TextField orderPaymentIdField;
    @FXML
    public TextArea orderNoteTextArea;
    @FXML
    public TextField orderSearchField;
    @FXML
    public Button searchOrderButton;
    @FXML
    public ImageView clearSearchButton;
    @FXML
    public TableView<Order> orderTable;
    public TableColumn<Order, String> orderIdColumn;
    @FXML
    public TableColumn<Order, Date> orderCreatedDateColumn;
    @FXML
    public TableColumn<Order, Double> orderTotalAmountColumn;
    @FXML
    public TableColumn<Order, String> orderEmployeeIdColumn;
    @FXML
    public TableColumn<Order, String> customerIdColumn;
    @FXML
    public DatePicker orderDateDatePicker;
    @FXML
    private Label pageIndexLabel;

    //    TODO: viết lại logic cho code đỡ bẩn
    private static final OrderDAO orderDAO = OrderDAO.getInstance();

    private static final Paginator<Order> orderPaginator = new Paginator<>((offset, limit) -> OrderDAO.getInstance().getWithPagination(offset, limit), orderDAO.getTotalOrderCount(), 10, false);

    public void setOrderTableValue() {
        orderTable.getItems().clear();
        try {
            List<Order> orders = orderPaginator.getCurrentPage();
            setPageIndexLabel(orderPaginator.getCurrentPageIndex(), orderPaginator.getTotalPages());
            ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

            orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            orderCreatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            orderTotalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            orderTotalAmountColumn.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
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

            customerIdColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                            cellData.getValue().getCustomer().getCustomerId() : ""));

            orderEmployeeIdColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                            cellData.getValue().getEmployee().getEmployeeId() : ""));

            orderTable.setItems(listOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOrderTableValue();
    }

    //    TODO: xử lý lại phần pagination khi search
    @FXML
    void onSearchOrderButtonClicked(MouseEvent event) {
        String id = orderSearchField.getText();
        List<Order> orders = null;

        if (id.isEmpty() || id.isBlank()) {
            orders = orderPaginator.getCurrentPage();
        } else {
//                TODO: xử lý lại phần search
//                orders = orderDAO.searchOrder(id);
        }

        ObservableList<Order> listOrder = FXCollections.observableArrayList(orders);

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderCreatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        orderTotalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderTotalAmountColumn.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
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

        customerIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                        cellData.getValue().getCustomer().getCustomerId() : ""));

        orderEmployeeIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmployee() != null ?
                        cellData.getValue().getEmployee().getEmployeeId() : ""));
        orderTable.setItems(listOrder);
    }

    @FXML
    void onClearSearchClickedClicked(MouseEvent mouseEvent) {
        orderSearchField.clear();
        setOrderTableValue();
    }

    @FXML
    void onOrderTableClicked(MouseEvent mouseEvent) {
        Order selectedItem = orderTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderIdField.setText(selectedItem.getOrderId());
            orderDateDatePicker.setValue(selectedItem.getOrderDate());

            double vat = selectedItem.getVatTax();
            int vatTax = (int) (vat * 100);
            orderVATField.setText(vatTax + "%");

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedDiscount = priceFormat.format(selectedItem.getDiscount());
            orderDiscountField.setText(formattedDiscount);
            String formattedDispensed = priceFormat.format(selectedItem.getDispensedAmount());
            orderDispensedAmount.setText(formattedDispensed);
            String formattedTotalAmount = priceFormat.format(selectedItem.getTotalAmount());
            orderTotalAmountField.setText(formattedTotalAmount);

            if (selectedItem.getCustomer() == null) {
                orderCustomerField.setText("Vãng lai");
            } else {
                orderCustomerField.setText(selectedItem.getCustomer().getCustomerId());
            }

            orderEmployeeIdField.setText(selectedItem.getEmployee().getEmployeeId());

            if (selectedItem.getPromotion() == null) {
                orderPromotionIdField.setText("Không");
            } else {
                orderPromotionIdField.setText(selectedItem.getPromotion().getPromotionId());
            }

            orderPaymentIdField.setText(selectedItem.getPayment().getPaymentId());
            orderNoteTextArea.setText(selectedItem.getNotes());

        }
    }

    public void setPageIndexLabel(int currentPage, int totalPage) {
        pageIndexLabel.setText(currentPage + "/" + totalPage);
    }

    @FXML
    void onLastPageButtonClicked(MouseEvent event) {
        orderPaginator.goToLastPage();
        setOrderTableValue();
    }

    @FXML
    void onNextPageButtonClicked(MouseEvent event) {
        orderPaginator.goToNextPage();
        setOrderTableValue();
    }

    @FXML
    void onPrevPageButtonClicked(MouseEvent event) {
        orderPaginator.goToPreviousPage();
        setOrderTableValue();
    }

    @FXML
    void onFirstPageButtonClicked(MouseEvent event) {
        orderPaginator.goToFirstPage();
        setOrderTableValue();
    }
}
