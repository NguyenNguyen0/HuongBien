package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.TableDAO;
import com.huongbien.dao.TableTypeDAO;
import com.huongbien.entity.Table;
import com.huongbien.entity.TableType;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderTableController implements Initializable {
    @FXML private ScrollPane orderTableScrollPane;
    @FXML private GridPane orderTableGridPane;
    @FXML public ComboBox<String> tableFloorComboBox;
    @FXML public ComboBox<String> tableStatusComboBox;
    @FXML public ComboBox<String> tableTypeComboBox;
    @FXML public TabPane tableInfoTabPane;
    @FXML public Label tableQuantityLabel;
    @FXML public Label seatTotalLabel;
    @FXML public Label tableAmountLabel;

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTablesToGridPane("0", "Tất cả trạng thái", "Tất cả loại bàn");
        setComboBoxValue();
        try {
            readTableDataFromJSON();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //---
    private void loadTablesToGridPane(String floor, String status, String type) {
        List<Table> tables = new ArrayList<>(getTableDataByCriteria(floor, status, type));
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < tables.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderTableItem.fxml"));
                VBox tableBox = fxmlLoader.load();
                OrderTableItemController orderTableItemController = fxmlLoader.getController();
                orderTableItemController.setTableItemData(tables.get(i));
                orderTableItemController.setOrderTableController(this);
                if (columns == 4) {
                    columns = 0;
                    ++rows;
                }
                orderTableGridPane.add(tableBox, columns++, rows);
                GridPane.setMargin(tableBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        orderTableScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        orderTableGridPane.prefWidthProperty().bind(orderTableScrollPane.widthProperty());
    }

    private List<Table> getTableDataByCriteria(String floor, String status, String type) {
        TableDAO tableDAO = TableDAO.getInstance();
        List<Table> ls = tableDAO.getByCriteria(floor, status, type);
        return ls;
    }

    private void setComboBoxValue() {
        TableDAO tableDAO = TableDAO.getInstance();
        TableTypeDAO tableTypeDAO = TableTypeDAO.getInstance();
        //floor
        List<String> floors = tableDAO.getDistinctFloor();
        ObservableList<String> floorOptions = FXCollections.observableArrayList(floors);
        tableFloorComboBox.setItems(floorOptions);
        tableFloorComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String floor) {
                return floor.equals("0") ? "Tầng trệt" : "Tầng " + floor;
            }

            @Override
            public String fromString(String string) {
                return string.replace("Tầng ", "").trim();
            }
        });
        tableFloorComboBox.getSelectionModel().selectFirst();
        //Status
        List<String> statuses = tableDAO.getDistinctStatuses();
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Tất cả trạng thái");
        statusOptions.addAll(statuses);
        tableStatusComboBox.setItems(statusOptions);
        tableStatusComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        tableStatusComboBox.getSelectionModel().selectFirst();
        //Type
        List<String> tableTypes = tableTypeDAO.getDistinctTableType();
        ObservableList<String> typeOptions = FXCollections.observableArrayList("Tất cả loại bàn");
        typeOptions.addAll(tableTypes);
        tableTypeComboBox.setItems(typeOptions);
        tableTypeComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String tableType) {
                return tableType != null ? tableType : "";
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        tableTypeComboBox.getSelectionModel().selectFirst();
    }

    public void setTableTabPane(String name, int floor, int seats, String typeName) {
        String floorString;
        if (floor == 0) {
            floorString = "Tầng trệt";
        } else {
            floorString = "Tầng " + floor;
        }

        Tab newTab = new Tab();
        newTab.setText(floorString);

        HBox tabContentHBox = new HBox(10);
        tabContentHBox.setPadding(new Insets(10, 0, 0, 10));
        tabContentHBox.setAlignment(Pos.CENTER_LEFT);
        tabContentHBox.setStyle("-fx-background-color: white");

        HBox tableNameHBox = new HBox(10);
        Label tableNameValue = new Label(name);
        tableNameValue.setFont(new Font("System Bold", 15));
        tableNameHBox.getChildren().addAll(tableNameValue);

        HBox tableseparator1 = new HBox(10);
        Label separator1 = new Label("-");
        separator1.setFont(new Font("System Bold", 15));
        tableseparator1.getChildren().addAll(separator1);

        HBox seatCountHBox = new HBox(10);
        Label seatCountLabel = new Label("Số chỗ:");
        seatCountLabel.setFont(new Font("System Bold", 15));
        Label seatCountValue = new Label(String.valueOf(seats));
        seatCountValue.setFont(new Font("System Bold", 15));
        seatCountHBox.getChildren().addAll(seatCountLabel, seatCountValue);

        HBox tableseparator2 = new HBox(10);
        Label separator2 = new Label("-");
        separator2.setFont(new Font("System Bold", 15));
        tableseparator2.getChildren().addAll(separator2);

        HBox tableTypeHBox = new HBox(10);
        Label tableTypeLabel = new Label("Loại bàn:");
        tableTypeLabel.setFont(new Font("System Bold", 15));
        Label tableTypeValue = new Label(typeName);
        tableTypeValue.setFont(new Font("System Bold", 15));
        tableTypeHBox.getChildren().addAll(tableTypeLabel, tableTypeValue);

        tabContentHBox.getChildren().addAll(tableNameHBox, tableseparator1, seatCountHBox, tableseparator2, tableTypeHBox);
        newTab.setContent(tabContentHBox);
        tableInfoTabPane.getTabs().add(newTab);
        tableInfoTabPane.getSelectionModel().select(newTab);
    }

    public void readTableDataFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        int seatTotal = 0;
        double tableAmount = 0;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO tableDAO = TableDAO.getInstance();
            Table table = tableDAO.getById(id);
            setTableTabPane(table.getName(), table.getFloor(), table.getSeats(), table.getTableType().getName());
            //calculate seat total
            seatTotal += table.getSeats();
            //calculate table amount
            tableAmount += table.getTableType().getTableId().equals("LB002") ? Constants.TABLE_PRICE : 0;
        }
        tableQuantityLabel.setText(String.valueOf(jsonArray.size()));
        seatTotalLabel.setText(seatTotal + " chỗ");
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
    }

    public void handleLoadTableFromComboBoxSelection() throws SQLException {
        String floor = tableFloorComboBox.getValue();
        String status = tableStatusComboBox.getValue();
        String tableTypeName = tableTypeComboBox.getValue();
        TableTypeDAO tableTypeDAO = TableTypeDAO.getInstance();
        TableType tableType = tableTypeDAO.getByName(tableTypeName);
        String tableTypeId = (tableType != null) ? tableType.getTableId() : "";
        orderTableGridPane.getChildren().clear();
        loadTablesToGridPane(floor, status, tableTypeId);
    }

    //comboBox
    @FXML
    void onTableFloorComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onTableStatusComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onTableTypeComboBoxSelected(ActionEvent event) throws SQLException {
        handleLoadTableFromComboBoxSelection();
    }

    @FXML
    void onChooseCuisineButtonClicked(ActionEvent event) throws IOException {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File không tồn tại.");
            return;
        }
        if (jsonArray.isEmpty()) {
            Utils.showAlert("Vui lòng chọn bàn", "Reminder");
            return;
        }
        restaurantMainController.openOrderCuisine();
    }

    @FXML
    void onPreOrderButtonClicked(ActionEvent event) throws IOException {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File không tồn tại.");
            return;
        }
        if (jsonArray.isEmpty()) {
            Utils.showAlert("Vui lòng chọn bàn", "Reminder");
            return;
        }
        restaurantMainController.openPreOrderTable();
    }
}
