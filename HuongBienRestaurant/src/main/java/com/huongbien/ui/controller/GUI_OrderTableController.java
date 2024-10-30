package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Category;
import com.huongbien.dao.DAO_Table;
import com.huongbien.database.Database;
import com.huongbien.entity.Category;
import com.huongbien.entity.Table;
import com.huongbien.entity.TableType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class GUI_OrderTableController implements Initializable {
    @FXML
    private ScrollPane compoent_scrollPane;

    @FXML
    private GridPane compoent_gridTable;
    private List<Table> tables;

    @FXML
    public ComboBox<Table> comboBox_tabFloor;

    @FXML
    public ComboBox<Table> comboBox_tabStatus;

    @FXML
    public ComboBox<Table> comboBox_tabType;

    //DAO
    private DAO_Table dao_table;

    @FXML
    public TabPane tabPane_infoTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGridPane();
        setValueCombobox();
        setTableTab();
        setTableTab();
        setTableTab();
    }

    private void setGridPane() {
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
                gui_orderTableItemController.setGui_orderTableController(this);
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
        try {
            Connection connection = Database.getConnection();
            dao_table = new DAO_Table(connection);

            List<Table> ls = dao_table.get();
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    private void setValueCombobox() {
        try {
            DAO_Table dao_table = new DAO_Table(Database.getConnection());
            List<Table> tableList = dao_table.get();

            // Xử lý ComboBox cho Floor
            Set<Integer> uniqueFloors = new HashSet<>();
            List<Table> uniqueFloorTables = new ArrayList<>();
            for (Table table : tableList) {
                if (uniqueFloors.add(table.getFloor())) {
                    uniqueFloorTables.add(table);
                }
            }
            uniqueFloorTables.sort(Comparator.comparingInt(Table::getFloor));
            ObservableList<Table> floorTables = FXCollections.observableArrayList(uniqueFloorTables);
            comboBox_tabFloor.setItems(floorTables);
            comboBox_tabFloor.setConverter(new StringConverter<Table>() {
                @Override
                public String toString(Table table) {
                    if (table == null) {
                        return "";
                    }
                    int floor = table.getFloor();
                    switch (floor) {
                        case 0:
                            return "Tầng trệt";
                        default:
                            return "Tầng " + floor;
                    }
                }
                @Override
                public Table fromString(String string) {
                    int floorValue = Integer.parseInt(string.replace("Tầng ", "").trim());
                    return comboBox_tabFloor.getItems().stream()
                            .filter(item -> item.getFloor() == floorValue)
                            .findFirst()
                            .orElse(null);
                }
            });
            comboBox_tabFloor.getSelectionModel().selectFirst();
            // Xử lý ComboBox cho Status
            Set<String> uniqueStatuses = new HashSet<>();
            List<Table> uniqueStatusTables = new ArrayList<>();
            Table allStatusOption = new Table();
            allStatusOption.setStatus("Tất cả trạng thái");
            uniqueStatusTables.add(allStatusOption);
            for (Table table : tableList) {
                if (uniqueStatuses.add(table.getStatus())) {
                    uniqueStatusTables.add(table);
                }
            }
            ObservableList<Table> statusTables = FXCollections.observableArrayList(uniqueStatusTables);
            comboBox_tabStatus.setItems(statusTables);
            comboBox_tabStatus.setConverter(new StringConverter<Table>() {
                @Override
                public String toString(Table table) {
                    return table != null ? table.getStatus() : "";
                }

                @Override
                public Table fromString(String string) {
                    return comboBox_tabStatus.getItems().stream()
                            .filter(item -> item.getStatus().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            comboBox_tabStatus.getSelectionModel().selectFirst();
            // Xử lý ComboBox cho TableType
            Set<TableType> uniqueTableTypes = new HashSet<>();
            List<Table> uniqueTypeTables = new ArrayList<>();
            Table allTypeOption = new Table();
            TableType allType = new TableType();
            allType.setName("Tất cả loại bàn");
            allTypeOption.setTableType(allType);
            uniqueTypeTables.add(allTypeOption);
            for (Table table : tableList) {
                if (uniqueTableTypes.add(table.getTableType())) {
                    uniqueTypeTables.add(table);
                }
            }
            ObservableList<Table> typeTables = FXCollections.observableArrayList(uniqueTypeTables);
            comboBox_tabType.setItems(typeTables);
            comboBox_tabType.setConverter(new StringConverter<Table>() {
                @Override
                public String toString(Table table) {
                    return table != null ? table.getTableType().getName() : "";
                }
                @Override
                public Table fromString(String string) {
                    return comboBox_tabType.getItems().stream()
                            .filter(item -> item.getTableType().getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            comboBox_tabType.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    public void setTableTab() {
        String tableName = "01";
        String tableType = "STANDARD";
        int seatCount = 5;
        String floorName = "Tầng trệt";

        Tab newTab = new Tab();
        newTab.setText(floorName);

        HBox tabContentHBox = new HBox(10);
        tabContentHBox.setPadding(new Insets(20, 0, 0, 20));
        tabContentHBox.setAlignment(Pos.CENTER_LEFT);

        // Table name
        HBox tableNameHBox = new HBox(10);
        Label tableNameLabel = new Label("Tên bàn:");
        tableNameLabel.setFont(new Font("System Bold", 20));
        Label tableNameValue = new Label(tableName);
        tableNameValue.setFont(new Font("System Bold", 20));
        tableNameHBox.getChildren().addAll(tableNameLabel, tableNameValue);

        // Separator
        HBox tableseparator1 = new HBox(10);
        Label separator1 = new Label("|");
        separator1.setFont(new Font("System Bold", 20));
        tableseparator1.getChildren().addAll(separator1);

        // Seat count
        HBox seatCountHBox = new HBox(10);
        Label seatCountLabel = new Label("Số chỗ:");
        seatCountLabel.setFont(new Font("System Bold", 20));
        Label seatCountValue = new Label(String.valueOf(seatCount));
        seatCountValue.setFont(new Font("System Bold", 20));
        seatCountHBox.getChildren().addAll(seatCountLabel, seatCountValue);

        // Separator
        HBox tableseparator2 = new HBox(10);
        Label separator2 = new Label("|");
        separator2.setFont(new Font("System Bold", 20));
        tableseparator2.getChildren().addAll(separator2);

        // Table type
        HBox tableTypeHBox = new HBox(10);
        Label tableTypeLabel = new Label("Loại bàn:");
        tableTypeLabel.setFont(new Font("System Bold", 20));
        Label tableTypeValue = new Label(tableType);
        tableTypeValue.setFont(new Font("System Bold", 20));
        tableTypeHBox.getChildren().addAll(tableTypeLabel, tableTypeValue);

        // Add all elements to the main HBox
        tabContentHBox.getChildren().addAll(tableNameHBox, tableseparator1, seatCountHBox, tableseparator2, tableTypeHBox);
        newTab.setContent(tabContentHBox);
        tabPane_infoTable.getTabs().add(newTab);
        tabPane_infoTable.getSelectionModel().select(newTab);
    }
}
