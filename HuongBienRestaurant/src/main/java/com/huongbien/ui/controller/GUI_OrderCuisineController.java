package com.huongbien.ui.controller;

import com.huongbien.dao.Cuisine_DAO;
import com.huongbien.dao.Table_DAO;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Table;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderCuisineController implements Initializable {
    //cuisine
    @FXML
    private BorderPane compoent_borderCuisine;

    @FXML
    private ScrollPane compoent_scrollCuisine;

    @FXML
    private GridPane compoent_gridCuisine;

    private List<Cuisine> cuisines;

    //bill
    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    private GridPane compoent_gridBill;

    //DAO
    private Cuisine_DAO cuisine_DAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //cuisine
        cuisines = new ArrayList<>(data());

        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < cuisines.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderCuisineItem.fxml"));
                VBox cuisineBox = fxmlLoader.load();
                GUI_OrderCuisineItemController gui_orderCuisineItemController = fxmlLoader.getController();
                gui_orderCuisineItemController.setData(cuisines.get(i));
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

        compoent_scrollBill.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridBill.prefWidthProperty().bind(compoent_scrollBill.widthProperty());

    }

    private List<Cuisine> data() {
        try {
            Connection connection = Database.getConnection();
            cuisine_DAO = new Cuisine_DAO(connection);

            List<Cuisine> ls = cuisine_DAO.get();
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }

//        List<Cuisine> ls = new ArrayList<>();
//
//        Cuisine cuisine = new Cuisine();
//        cuisine.setName("Tôm hùm");
//        cuisine.setPrice(120030);
//        ls.add(cuisine);
//
//        return ls;
    }
}
