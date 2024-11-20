package com.huongbien.ui.controller;

import com.huongbien.dao.TableDAO;
import com.huongbien.dao.TableTypeDAO;
import com.huongbien.entity.Table;
import com.huongbien.entity.TableType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TableManagementController implements Initializable {
    @FXML private Button clearTableFormButton;
    @FXML private Button handleActionTableButton;
    @FXML private Button swapModeTableButton;
    @FXML private ComboBox<Integer> tableFloorComboBox;
    @FXML private ComboBox<String> tableStatusComboBox;
    @FXML private ComboBox<TableType> tableTypeComboBox;
    @FXML private ImageView tableImageView;
    @FXML private TableColumn<Table, Integer> tableFloorColumn;
    @FXML private TableColumn<?, ?> tableIdColumn;
    @FXML private TableColumn<?, ?> tableNameColumn;
    @FXML private TableColumn<?, ?> tableSeatsColumn;
    @FXML private TableColumn<?, ?> tableStatusColumn;
    @FXML private TableColumn<?, ?> tableTypeColumn;
    @FXML private TableView<Table> tableTableView;
    @FXML private TextField tableNameField;
    @FXML private TextField tableSearchField;
    @FXML private TextField tableSeatsField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPromotionTableValue();
        setComboBoxValue();
    }

    private void setPromotionTableValue() {
        TableDAO tableDao = TableDAO.getInstance();
        List<Table> tableList = tableDao.getAll();

        ObservableList<Table> listTable = FXCollections.observableArrayList(tableList);
        tableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tableTypeName"));
        tableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
        tableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableFloorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));

        tableTableView.setItems(listTable);
    }

    private void setComboBoxValue() {
        TableDAO tableDAO = TableDAO.getInstance();
        List<String> statusList = tableDAO.getDistinctStatuses();
        ObservableList<String> statuses = FXCollections.observableArrayList(statusList);
        tableStatusComboBox.setItems(statuses);

        TableTypeDAO tableTypeDAO = TableTypeDAO.getInstance();
        List<TableType> tableTypeList = tableTypeDAO.getAll();
        ObservableList<TableType> tableTypes = FXCollections.observableArrayList(tableTypeList);
        tableTypeComboBox.setItems(tableTypes);
        tableTypeComboBox.setConverter(new StringConverter<TableType>() {
            @Override
            public String toString(TableType tableType) {
                return tableType != null ? tableType.getName() : "";
            }

            @Override
            public TableType fromString(String string) {
                return tableTypeComboBox.getItems().stream()
                        .filter(item -> item.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        ObservableList<Integer> floorList = FXCollections.observableArrayList(0, 1, 2, 3);
        tableFloorComboBox.setItems(floorList);
    }

    public void clearTableForm() {
        tableNameField.setText("");
        tableSeatsField.setText("");
        tableTypeComboBox.getSelectionModel().clearSelection();
        tableFloorComboBox.getSelectionModel().clearSelection();
        tableStatusComboBox.getSelectionModel().clearSelection();
        tableTableView.getSelectionModel().clearSelection();
    }

    public void changeHandleButtonModeToEditTable() {
        swapModeTableButton.setText("Thêm bàn");
        handleActionTableButton.setText("Sửa bàn");
        swapModeTableButton.setStyle("-fx-background-color:   #1D557E");
        handleActionTableButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void changeHandleButtonModeToAddTable() {
        swapModeTableButton.setText("Sửa bàn");
        handleActionTableButton.setText("Thêm bàn");
        swapModeTableButton.setStyle("-fx-background-color:   #761D7E");
        handleActionTableButton.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
        tableNameField.setDisable(true);
        tableSeatsField.setDisable(true);
        tableTypeComboBox.setDisable(true);
        tableStatusComboBox.setDisable(true);
        tableFloorComboBox.setDisable(true);
    }

    void enableInput() {
        tableNameField.setDisable(false);
        tableSeatsField.setDisable(false);
        tableFloorComboBox.setDisable(false);
        tableStatusComboBox.setDisable(false);
        tableTypeComboBox.setDisable(false);
    }

    @FXML
    void onClearSearchClicked(MouseEvent event) {
        tableSearchField.setText("");
    }

    @FXML
    void onClearTableFormButtonClicked(ActionEvent event) {
        clearTableForm();
    }

    @FXML
    void onHandleActionTableButtonClicked(ActionEvent event) {
        Table selectedTable = tableTableView.getSelectionModel().getSelectedItem();

        if (selectedTable == null) {
            System.out.println("Lỗi: Chưa chọn bàn để sửa.");
            return;
        }

        String existingTableId = selectedTable.getId();
        String name = tableNameField.getText();
        int seats;
        int floor;
        String status = tableStatusComboBox.getValue();
        TableType selectedTableType = tableTypeComboBox.getValue();

        if (tableSeatsField.getText().isEmpty()) {
            System.out.println("Lỗi: Số ghế không được để trống.");
            return;
        }

        try {
            seats = Integer.parseInt(tableSeatsField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị số ghế không hợp lệ.");
            return;
        }

        if (tableFloorComboBox.getValue() == null) {
            System.out.println("Lỗi: Chưa chọn tầng.");
            return;
        }

        floor = tableFloorComboBox.getValue();

        if (selectedTableType == null) {
            System.out.println("Lỗi: Chưa chọn loại bàn.");
            return;
        }

        TableDAO tableDao = TableDAO.getInstance();

        Table table = new Table(existingTableId, name, floor, seats, status, selectedTableType);

        if (tableDao.updateTableInfo(table)) {
            System.out.println("Sửa bàn thành công");
        } else {
            System.out.println("Sửa bàn không thành công");
        }

        clearTableForm();
        disableInput();
        tableTableView.getItems().clear();
        setPromotionTableValue();
        changeHandleButtonModeToEditTable();
    }


    @FXML
    void onSwapModeTableButtonClicked(ActionEvent event) {
        String name = tableNameField.getText();
        int seats;
        int floor;
        String status = tableStatusComboBox.getValue();
        TableType selectedTableType = tableTypeComboBox.getValue();

        try {
            seats = Integer.parseInt(tableSeatsField.getText());
            floor = tableFloorComboBox.getValue();
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị số ghế hoặc tầng không hợp lệ.");
            return;
        }

        if (selectedTableType == null) {
            System.out.println("Lỗi: Chưa chọn loại bàn.");
            return;
        }

        TableDAO tableDao = TableDAO.getInstance();

        Table table = new Table(name, floor, seats, status, selectedTableType);

        if (tableDao.add(table)) {
            System.out.println("Thêm bàn thành công");
        } else {
            System.out.println("Thêm bàn không thành công");
        }

        clearTableForm();
        disableInput();
        tableTableView.getItems().clear();
        setPromotionTableValue();
        changeHandleButtonModeToEditTable();
    }

    @FXML
    void onTableTableViewClicked(MouseEvent event) {
        Table selectedItem = tableTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getId();
            TableDAO tableDao = TableDAO.getInstance();
            Table table = tableDao.getById(idSelect);

            tableNameField.setText(table.getName());
            tableSeatsField.setText(String.valueOf(table.getSeats()));

            tableTypeComboBox.getItems().stream()
                    .filter(type -> type.getName().equals(table.getTableTypeName()))
                    .findFirst()
                    .ifPresent(tableTypeComboBox.getSelectionModel()::select);

            tableStatusComboBox.getItems().stream()
                    .filter(status -> status.equals(table.getStatus()))
                    .findFirst()
                    .ifPresent(tableStatusComboBox.getSelectionModel()::select);

            tableFloorComboBox.getItems().stream()
                    .filter(floor -> floor.equals(table.getFloor()))
                    .findFirst()
                    .ifPresent(tableFloorComboBox.getSelectionModel()::select);

            handleActionTableButton.setVisible(true);
            swapModeTableButton.setVisible(true);
            clearTableFormButton.setVisible(true);
            enableInput();
            changeHandleButtonModeToEditTable();
        }
    }

}