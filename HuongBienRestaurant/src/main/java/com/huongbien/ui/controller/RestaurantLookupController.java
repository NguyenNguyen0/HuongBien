package com.huongbien.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantLookupController implements Initializable {
    @FXML
    private ComboBox<String> restaurantLookupComboBox;
    @FXML
    private VBox restaurantLookupCuisineVBox;
    @FXML
    private VBox restaurantLookupPreOrderTableVBox;
    @FXML
    private VBox restaurantLookupPromotionVBox;
    @FXML
    private VBox restaurantLookupTableVBox;

    public RestaurantMainController restaurantMainController;

    //function area--------------------------------------------------------------------------
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    private void setDefaultTableView() {
        restaurantLookupCuisineVBox.setVisible(true);
        restaurantLookupPreOrderTableVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupTableVBox.setVisible(false);
    }

    private void setDefaultRestaurantLookupComboBox() {
        ObservableList<String> restaurantLookupList = FXCollections.observableArrayList();
        restaurantLookupList.add("Bàn");
        restaurantLookupList.add("Món ăn");
        restaurantLookupList.add("Khuyến mãi");
        restaurantLookupList.add("Đơn đặt trước");
        restaurantLookupComboBox.setItems(restaurantLookupList);
        restaurantLookupComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }
            @Override
            public String fromString(String string) {
                return string;
            }
        });
        restaurantLookupComboBox.getSelectionModel().selectFirst();
    }
    //---TODO: add more function

    //event area--------------------------------------------------------------------------
    //---
    @FXML
    void onRestaurantLookupComboBoxAction(ActionEvent event) {
        String selectedItem = restaurantLookupComboBox.getValue();
        restaurantLookupCuisineVBox.setVisible(false);
        restaurantLookupPreOrderTableVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupTableVBox.setVisible(false);
        switch (selectedItem) {
            case "Bàn":
                restaurantMainController.featureTitleLabel.setText("Tra cứu bàn");
                restaurantLookupTableVBox.setVisible(true);
                break;
            case "Món ăn":
                restaurantMainController.featureTitleLabel.setText("Tra cứu món ăn");
                restaurantLookupCuisineVBox.setVisible(true);
                break;
            case "Khuyến mãi":
                restaurantMainController.featureTitleLabel.setText("Tra cứu khuyến mãi");
                restaurantLookupPromotionVBox.setVisible(true);
                break;
            case "Đơn đặt trước":
                restaurantMainController.featureTitleLabel.setText("Tra cứu đơn đặt trước");
                restaurantLookupPreOrderTableVBox.setVisible(true);
                break;
        }
    }
    //---TODO: add more event

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultRestaurantLookupComboBox();
        setDefaultTableView(); //set default table view display table first

    }
}
