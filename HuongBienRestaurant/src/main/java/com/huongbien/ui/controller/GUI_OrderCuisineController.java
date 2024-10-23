package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Utils;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderCuisineController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/bill.json";

    //cuisine
    @FXML
    private BorderPane compoent_borderCuisine;

    @FXML
    private ScrollPane compoent_scrollCuisine;

    @FXML
    private GridPane compoent_gridCuisine;

    private List<Cuisine> cuisines;
    private List<OrderDetail> orderDetails;

    //bill
    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    public GridPane compoent_gridBill;

    //DAO
    private DAO_Cuisine cuisine_DAO;

    private void loadingCuisine() {
        cuisines = new ArrayList<>(dataCuisine());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < cuisines.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderCuisineItem.fxml"));
                VBox cuisineBox = fxmlLoader.load();
                GUI_OrderCuisineItemController gui_orderCuisineItemController = fxmlLoader.getController();
                gui_orderCuisineItemController.setDataCuisine(cuisines.get(i));
                gui_orderCuisineItemController.setOrderCuisineController(this);

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridCuisine.add(cuisineBox, columns++, rows);
                GridPane.setMargin(cuisineBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compoent_scrollCuisine.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridCuisine.prefWidthProperty().bind(compoent_scrollCuisine.widthProperty());
    }

    public void loadingBill() throws FileNotFoundException {
        orderDetails = new ArrayList<>(dataBill());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderBillItem.fxml"));
                HBox billBox = fxmlLoader.load();
                GUI_OrderBillItemController gui_orderBillItemController = fxmlLoader.getController();
                gui_orderBillItemController.setDataBill(orderDetails.get(i));
                gui_orderBillItemController.setOrderBillController(this);

                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridBill.add(billBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compoent_scrollBill.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridBill.prefWidthProperty().bind(compoent_scrollBill.widthProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingCuisine();
        try {
            loadingBill();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    private List<Cuisine> dataCuisine() {
        try {
            Connection connection = Database.getConnection();
            cuisine_DAO = new DAO_Cuisine(connection);

            List<Cuisine> ls = cuisine_DAO.get();
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }

//        List<Cuisine> ls = new ArrayList<>();
//        Cuisine cuisine = new Cuisine();
//        cuisine.setCuisineId("M001");
//        cuisine.setName("Tôm hùm");
//        cuisine.setPrice(120030);
//        ls.add(cuisine);
//        return ls;
    }

    private List<OrderDetail> dataBill() throws FileNotFoundException {
        List<OrderDetail> ls = readFromJSON();
        return ls;

//        List<OrderDetail> ls = new ArrayList<>();
//        Cuisine cuisine;
//        OrderDetail orderDetail;
//        cuisine = new Cuisine();
//        cuisine.setCuisineId("M001");
//        cuisine.setName("Tôm xú");
//        cuisine.setPrice(500000);
//        orderDetail = new OrderDetail();
//        orderDetail.setCuisine(cuisine);
//        orderDetail.setNote("không ớt");
//        orderDetail.setQuantity(2);
//        orderDetail.setSalePrice(1000000);
//        ls.add(orderDetail);
//
//        return ls;
    }
}
