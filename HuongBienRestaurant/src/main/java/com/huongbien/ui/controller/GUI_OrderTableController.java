package com.huongbien.ui.controller;

import com.huongbien.entity.TableItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderTableController implements Initializable {
    @FXML
    private ScrollPane compoent_scrollPane;

    @FXML
    private HBox compoent_HB_Table;

    @FXML
    private GridPane compoent_gridTable;
    private List<TableItem> tables;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tables = new ArrayList<>(data());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < tables.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderTableItem.fxml"));
                VBox tableBox = fxmlLoader.load();
                GUI_OrderTableItemController gui_orderTableItemController = fxmlLoader.getController();
                gui_orderTableItemController.setData(tables.get(i));

                if (columns == 5) {
                    columns = 0;
                    ++rows;
                }

                compoent_gridTable.add(tableBox, columns++, rows);
                GridPane.setMargin(tableBox, new Insets(10));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        compoent_scrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridTable.prefWidthProperty().bind(compoent_scrollPane.widthProperty());
    }

    private List<TableItem> data() {
        List<TableItem> ls = new ArrayList<>();

        TableItem tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-vip-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-vip-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-vip-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        tableItem = new TableItem();
        tableItem.setImg("/com/huongbien/icon/order/table-normal-open-64px.png");
        ls.add(tableItem);

        return ls;
    }
}
