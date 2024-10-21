package com.huongbien.ui.controller;

import com.huongbien.entity.Cuisine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderCuisineItemController implements Initializable {
    @FXML
    private ImageView imgCuisine;

    @FXML
    private Label lbl_cuisineName;

    @FXML
    private Label lbl_cuisinePrice;

    private GUI_OrderCuisineController orderCuisineController;

    public void setOrderCuisineController(GUI_OrderCuisineController orderCuisineController) {
        this.orderCuisineController = orderCuisineController;
    }

    public void setData(Cuisine cuisine) {
        lbl_cuisineName.setText(cuisine.getName());
        lbl_cuisinePrice.setText(cuisine.getPrice()+"");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Circle clip = new Circle(imgCuisine.getFitWidth() / 2, imgCuisine.getFitHeight() / 2, Math.min(imgCuisine.getFitWidth(), imgCuisine.getFitHeight()) / 2);
        imgCuisine.setClip(clip);
    }

    @FXML
    void ml_getInfoCuisine(MouseEvent event) {

    }
}