package com.huongbien.ui.controller;

import com.huongbien.entity.TableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_OrderTableItemController implements Initializable {
    @FXML
    private ImageView imgTable;

    public void setData(TableItem tableItem) {
        Image image = new Image(getClass().getResourceAsStream(tableItem.getImg()));
        imgTable.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
