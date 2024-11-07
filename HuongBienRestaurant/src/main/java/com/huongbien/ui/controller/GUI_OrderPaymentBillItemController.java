package com.huongbien.ui.controller;

import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderPaymentBillItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    @FXML
    private Label lbl_cuisineID;
    @FXML
    private Label lbl_cuisineName;
    @FXML
    private Label lbl_cuisineNote;
    @FXML
    private Label lbl_cuisineQuantity;
    @FXML
    private Label lbl_cuisineSalePrice;
    @FXML
    private Label lbl_salePrice;

    private GUI_OrderPaymentController gui_OrderPaymentController;

    public void setOrderPaymentBillController(GUI_OrderPaymentController gui_OrderPaymentController) {
        this.gui_OrderPaymentController = gui_OrderPaymentController;
    }

    private GUI_OrderPaymentInvoiceController gui_OrderPaymentInvoiceController;

    public void setOrderPaymentInvoiceController(GUI_OrderPaymentInvoiceController gui_OrderPaymentInvoiceController) {
        this.gui_OrderPaymentInvoiceController = gui_OrderPaymentInvoiceController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setDataBill(OrderDetail orderDetail) {
        lbl_cuisineID.setText(orderDetail.getCuisine().getCuisineId());
        lbl_cuisineName.setText(orderDetail.getCuisine().getName());
        lbl_cuisineSalePrice.setText(Utils.formatPrice(orderDetail.getCuisine().getPrice()));
        lbl_cuisineNote.setText(orderDetail.getNote());
        lbl_cuisineQuantity.setText(orderDetail.getQuantity() + "");
        lbl_salePrice.setText(Utils.formatPrice(orderDetail.getSalePrice()));
    }
}
