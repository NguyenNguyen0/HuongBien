package com.huongbien.ui.controller;

import com.huongbien.entity.Table;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderTableItemController implements Initializable {
    @FXML
    private Label lbl_tableName;

    @FXML
    private Label lbl_tableSeats;

    @FXML
    private ImageView imgTable;

    public void setData(Table table) {
        lbl_tableName.setText(table.getName());
        lbl_tableSeats.setText(table.getSeats()+"");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
