package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.DAO_Category;
import com.huongbien.dao.DAO_Table;
import com.huongbien.dao.DAO_TableType;
import com.huongbien.database.Database;
import com.huongbien.entity.*;
import com.huongbien.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class GUI_OrderTableController implements Initializable {
    private final static String path_table = "src/main/resources/com/huongbien/temp/temporaryTable.json";

    @FXML
    private ScrollPane compoent_scrollPane;

    @FXML
    private GridPane compoent_gridTable;

    @FXML
    public ComboBox<String> comboBox_tabFloor;

    @FXML
    public ComboBox<String> comboBox_tabStatus;

    @FXML
    public ComboBox<String> comboBox_tabType;

    @FXML
    public TabPane tabPane_infoTable;

    public GUI_MainController gui_mainController;

    public void setGUI_MainController(GUI_MainController gui_mainController) {
        this.gui_mainController = gui_mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGridPane("0", "Tất cả trạng thái", "Tất cả loại bàn");
        setValueCombobox();
        try {
            readFromJSON();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setGridPane(String floor, String status, String type) {
        List<Table> tables = new ArrayList<>(data(floor, status, type));
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

    private List<Table> data(String floor, String status, String type) {
        try {
            DAO_Table dao_table = new DAO_Table(Database.getConnection());
            List<Table> ls = dao_table.getByCriteria(floor, status, type);
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
            DAO_TableType dao_tableType = new DAO_TableType(Database.getConnection());
            //floor
            List<String> floors = dao_table.getDistinctFloor();
            ObservableList<String> floorOptions = FXCollections.observableArrayList(floors);
            comboBox_tabFloor.setItems(floorOptions);
            comboBox_tabFloor.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String floor) {
                    return floor.equals("0") ? "Tầng trệt" : "Tầng " + floor;
                }
                @Override
                public String fromString(String string) {
                    return string.replace("Tầng ", "").trim();
                }
            });
            comboBox_tabFloor.getSelectionModel().selectFirst();
            //Status
            List<String> statuses = dao_table.getDistinctStatuses();
            ObservableList<String> statusOptions = FXCollections.observableArrayList("Tất cả trạng thái");
            statusOptions.addAll(statuses);
            comboBox_tabStatus.setItems(statusOptions);
            comboBox_tabStatus.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String status) {
                    return status != null ? status : "";
                }
                @Override
                public String fromString(String string) {
                    return string;
                }
            });
            comboBox_tabStatus.getSelectionModel().selectFirst();
            //Type
            List<String> tableTypes = dao_tableType.getDistinctTableType();
            ObservableList<String> typeOptions = FXCollections.observableArrayList("Tất cả loại bàn");
            typeOptions.addAll(tableTypes);
            comboBox_tabType.setItems(typeOptions);
            comboBox_tabType.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String tableType) {
                    return tableType != null ? tableType : "";
                }
                @Override
                public String fromString(String string) {
                    return string;
                }
            });
            comboBox_tabType.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    public void setTableTab(String name, int floor, int seats, String typeName) {
        String floorString;
        if (floor == 0) {
            floorString = "Tầng trệt";
        } else {
            floorString = "Tầng " + floor;
        }

        Tab newTab = new Tab();
        newTab.setText(floorString);

        HBox tabContentHBox = new HBox(10);
        tabContentHBox.setPadding(new Insets(20, 0, 0, 20));
        tabContentHBox.setAlignment(Pos.CENTER_LEFT);
        tabContentHBox.setStyle("-fx-background-color: white");

        HBox tableNameHBox = new HBox(10);
        Label tableNameValue = new Label(name);
        tableNameValue.setFont(new Font("System Bold", 20));
        tableNameHBox.getChildren().addAll(tableNameValue);

        HBox tableseparator1 = new HBox(10);
        Label separator1 = new Label("|");
        separator1.setFont(new Font("System Bold", 20));
        tableseparator1.getChildren().addAll(separator1);

        HBox seatCountHBox = new HBox(10);
        Label seatCountLabel = new Label("Số chỗ:");
        seatCountLabel.setFont(new Font("System Bold", 20));
        Label seatCountValue = new Label(String.valueOf(seats));
        seatCountValue.setFont(new Font("System Bold", 20));
        seatCountHBox.getChildren().addAll(seatCountLabel, seatCountValue);

        HBox tableseparator2 = new HBox(10);
        Label separator2 = new Label("|");
        separator2.setFont(new Font("System Bold", 20));
        tableseparator2.getChildren().addAll(separator2);

        HBox tableTypeHBox = new HBox(10);
        Label tableTypeLabel = new Label("Loại bàn:");
        tableTypeLabel.setFont(new Font("System Bold", 20));
        Label tableTypeValue = new Label(typeName);
        tableTypeValue.setFont(new Font("System Bold", 20));
        tableTypeHBox.getChildren().addAll(tableTypeLabel, tableTypeValue);

        tabContentHBox.getChildren().addAll(tableNameHBox, tableseparator1, seatCountHBox, tableseparator2, tableTypeHBox);
        newTab.setContent(tabContentHBox);
        tabPane_infoTable.getTabs().add(newTab);
        tabPane_infoTable.getSelectionModel().select(newTab);
    }

    public void readFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(path_table);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            DAO_Table dao_table = new DAO_Table(Database.getConnection());
            Table table = dao_table.get(id);
            setTableTab(table.getName(), table.getFloor(), table.getSeats(), table.getTableType().getName());
        }
    }


    public void handleComboBox() throws SQLException {
        String floor = comboBox_tabFloor.getValue();
        String status = comboBox_tabStatus.getValue();
        String tableTypeName = comboBox_tabType.getValue();
        DAO_TableType dao_tableType = new DAO_TableType(Database.getConnection());
        TableType tableType = dao_tableType.getByName(tableTypeName);
        String tableTypeId = (tableType != null) ? tableType.getTableId() : "";
        compoent_gridTable.getChildren().clear();
        setGridPane(floor, status, tableTypeId);
    }

    //comboBox
    @FXML
    void comboBox_tabFloor(ActionEvent event) throws SQLException {
        handleComboBox();
    }

    @FXML
    void comboBox_tabStatus(ActionEvent event) throws SQLException {
        handleComboBox();
    }

    @FXML
    void comboBox_tabType(ActionEvent event) throws SQLException {
        handleComboBox();
    }

    @FXML
    void btn_chooseCuisine(ActionEvent event) throws IOException {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path_table);
        } catch (FileNotFoundException e) {
            System.out.println("File không tồn tại.");
            return;
        }
        if (jsonArray.isEmpty()) {
            System.out.println("Vui lòng chọn bàn");
            return;
        }

        gui_mainController.openCuisine();
    }

}
