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
    private ImageView imgView_table;

    @FXML
    private Label lbl_tableName;

    @FXML
    private Label lbl_tableSeats;

    @FXML
    private Label lbl_tableType;

    public void setData(Table table) {
        lbl_tableName.setText(table.getName());
        lbl_tableType.setText(table.getTableType().getName());
        lbl_tableSeats.setText("Số chỗ: "+ table.getSeats());
        String tableStatus = table.getStatus();
        if(tableStatus.equals("Bàn trống")) {
            imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-empty-512px.png"));
        }else if(tableStatus.equals("Đặt trước")) {
            imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-preOrder-512px.png"));
        } else if(tableStatus.equals("Phục vụ")) {
            imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-ordered-512px.png"));
        } else if(tableStatus.equals("Bàn đóng")) {
            imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-stop-512px.png"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
