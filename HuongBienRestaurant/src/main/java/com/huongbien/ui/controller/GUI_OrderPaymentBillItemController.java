package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderPaymentBillItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/bill.json";
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

    public void setOrderPaymnetBillController(GUI_OrderPaymentController gui_OrderPaymentController) {
        this.gui_OrderPaymentController = gui_OrderPaymentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setDataBill(OrderDetail orderDetail) {
        lbl_cuisineID.setText(orderDetail.getCuisine().getCuisineId());
        lbl_cuisineName.setText(orderDetail.getCuisine().getName());
        lbl_cuisineSalePrice.setText(Utils.formatPrice(orderDetail.getCuisine().getPrice()));
        lbl_cuisineNote.setText(orderDetail.getNote());
        lbl_cuisineQuantity.setText(orderDetail.getQuantity()+"");
        lbl_salePrice.setText(Utils.formatPrice(orderDetail.getSalePrice()));
    }
}
