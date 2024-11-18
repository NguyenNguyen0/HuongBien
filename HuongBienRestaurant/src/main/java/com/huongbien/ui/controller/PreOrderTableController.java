package com.huongbien.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PreOrderTableController implements Initializable {

    public RestaurantMainController restaurantMainController;

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onReservationManagementButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openReservationManagement();
    }

    @FXML
    void onSavePreOrderTableButtonAction(ActionEvent event) {
        //TODO: Viết sự kiện lưu thông tin đặt trước xuống database
        System.out.println("Đã lưu thành công");
    }
}
