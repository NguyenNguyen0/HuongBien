package com.huongbien.ui.controller;

import com.huongbien.entity.Table;
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
    private List<Table> tables;

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

                if (columns == 4) {
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

    private List<Table> data() {
        List<Table> ls = new ArrayList<>();

        Table table = new Table();
        table.setName("Bàn 01");
        table.setSeats(5);
        ls.add(table);

        table = new Table();
        table.setName("Bàn 02");
        table.setSeats(10);
        ls.add(table);

        table = new Table();
        table.setName("Bàn 03");
        table.setSeats(15);
        ls.add(table);

        return ls;
    }
}
