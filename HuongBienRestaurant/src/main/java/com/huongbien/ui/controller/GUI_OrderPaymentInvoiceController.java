package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.huongbien.dao.DAO_Customer;
import com.huongbien.dao.DAO_Promotion;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Customer;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Promotion;
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
import javafx.scene.input.MouseEvent;
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

public class GUI_OrderPaymentInvoiceController implements Initializable {
    private final static String path_bill = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    public GridPane compoent_gridBill;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
                gui_OrderPaymentBillItemController.setOrderPaymentInvoiceController(this);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadingBill();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}