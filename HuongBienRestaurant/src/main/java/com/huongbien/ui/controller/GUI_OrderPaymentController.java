package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderPaymentController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/bill.json";
    @FXML
    private ScrollPane compoent_scrollCuisine;

    @FXML
    private GridPane compoent_gridCuisine;

    private List<Cuisine> cuisines;

    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    public GridPane compoent_gridBill;

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

    public List<OrderDetail> readFromJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(path);

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
        List<OrderDetail> ls = readFromJSON();
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

    @FXML
    void btn_back(ActionEvent event) throws IOException {
        gui_mainController.openCuisine();
    }
}
