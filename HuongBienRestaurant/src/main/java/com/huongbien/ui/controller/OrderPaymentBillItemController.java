package com.huongbien.ui.controller;

import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderPaymentBillItemController implements Initializable {
    private final static String TEMPORARY_BILL_PATH = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    @FXML
    private Label cuisineIdLabel;
    @FXML
    private Label cuisineNameLabel;
    @FXML
    private Label cuisineNoteLabel;
    @FXML
    private Label cuisineQuantityLabel;
    @FXML
    private Label cuisineSalePriceLabel;
    @FXML
    private Label cuisineTotalPriceLabel;

    private OrderPaymentController orderPaymentController;

    public void setOrderPaymentBillController(OrderPaymentController orderPaymentController) {
        this.orderPaymentController = orderPaymentController;
    }

    private OrderPaymentInvoiceController orderPaymentInvoiceController;

    public void setOrderPaymentInvoiceController(OrderPaymentInvoiceController orderPaymentInvoiceController) {
        this.orderPaymentInvoiceController = orderPaymentInvoiceController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setDataBill(OrderDetail orderDetail) {
        cuisineIdLabel.setText(orderDetail.getCuisine().getCuisineId());
        cuisineNameLabel.setText(orderDetail.getCuisine().getName());
        cuisineSalePriceLabel.setText(Utils.formatPrice(orderDetail.getCuisine().getPrice()));
        cuisineNoteLabel.setText(orderDetail.getNote());
        cuisineQuantityLabel.setText(orderDetail.getQuantity() + "");
        cuisineTotalPriceLabel.setText(Utils.formatPrice(orderDetail.getSalePrice()));
    }
}
