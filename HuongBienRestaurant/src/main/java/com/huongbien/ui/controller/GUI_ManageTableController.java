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

public class GUI_ManageTableController implements Initializable {
    @FXML
    private Button btn_tabClear;

    @FXML
    private Button btn_tabMain;

    @FXML
    private Button btn_tabSub;

    @FXML
    private ComboBox<Integer> comboBox_tabFloor;

    @FXML
    private ComboBox<String> comboBox_tabStatus;

    @FXML
    private ComboBox<TableType> comboBox_tabType;

    @FXML
    private ImageView imgView_tab;

    @FXML
    private TableColumn<Table, Integer> tabCol_tabFloor;

    @FXML
    private TableColumn<?, ?> tabCol_tabID;

    @FXML
    private TableColumn<?, ?> tabCol_tabName;

    @FXML
    private TableColumn<?, ?> tabCol_tabSeats;

    @FXML
    private TableColumn<?, ?> tabCol_tabStatus;

    @FXML
    private TableColumn<?, ?> tabCol_tabType;

    @FXML
    private TableView<Table> tabViewTab;

    @FXML
    private TextField txt_tabName;

    @FXML
    private TextField txt_tabSearch;

    @FXML
    private TextField txt_tabSeats;

    private void setCellValues() {
        TableDAO tableDao = TableDAO.getInstance();
        List<Table> tableList = tableDao.getAll();

        ObservableList<Table> listTable = FXCollections.observableArrayList(tableList);
        tabCol_tabID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabCol_tabName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabCol_tabType.setCellValueFactory(new PropertyValueFactory<>("tableTypeName"));
        tabCol_tabSeats.setCellValueFactory(new PropertyValueFactory<>("seats"));
        tabCol_tabStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tabCol_tabFloor.setCellValueFactory(new PropertyValueFactory<>("floor"));

        tabViewTab.setItems(listTable);
    }

    private void setValueCombobox() {
        TableDAO tableDAO = TableDAO.getInstance();
        List<String> statusList = tableDAO.getDistinctStatuses();
        ObservableList<String> statuses = FXCollections.observableArrayList(statusList);
        comboBox_tabStatus.setItems(statuses);

        TableTypeDAO tableTypeDAO = TableTypeDAO.getInstance();
        List<TableType> tableTypeList = tableTypeDAO.getAll();
        ObservableList<TableType> tableTypes = FXCollections.observableArrayList(tableTypeList);
        comboBox_tabType.setItems(tableTypes);
        comboBox_tabType.setConverter(new StringConverter<TableType>() {
            @Override
            public String toString(TableType tableType) {
                return tableType != null ? tableType.getName() : "";
            }

            @Override
            public TableType fromString(String string) {
                return comboBox_tabType.getItems().stream()
                        .filter(item -> item.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        ObservableList<Integer> floorList = FXCollections.observableArrayList(0, 1, 2, 3);
        comboBox_tabFloor.setItems(floorList);

    }

    public void clear() {
        txt_tabName.setText("");
        txt_tabSeats.setText("");
        comboBox_tabType.getSelectionModel().clearSelection();
        comboBox_tabFloor.getSelectionModel().clearSelection();
        comboBox_tabStatus.getSelectionModel().clearSelection();
        tabViewTab.getSelectionModel().clearSelection();
    }

    public void utilsButton_1() {
        btn_tabSub.setText("Thêm bàn");
        btn_tabMain.setText("Sửa bàn");
        btn_tabSub.setStyle("-fx-background-color:   #1D557E");
        btn_tabMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        btn_tabSub.setText("Thêm bàn");
        btn_tabMain.setText("Sửa bàn");
        btn_tabSub.setStyle("-fx-background-color:   #761D7E");
        btn_tabMain.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
        txt_tabName.setDisable(true);
        txt_tabSeats.setDisable(true);
        comboBox_tabType.setDisable(true);
        comboBox_tabStatus.setDisable(true);
        comboBox_tabFloor.setDisable(true);
    }

    void enableInput() {
        txt_tabName.setDisable(false);
        txt_tabSeats.setDisable(false);
        comboBox_tabFloor.setDisable(false);
        comboBox_tabStatus.setDisable(false);
        comboBox_tabType.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        setValueCombobox();
    }

    @FXML
    void btn_clearSearch(MouseEvent event) {
        txt_tabSearch.setText("");
    }

    @FXML
    void btn_tabClear(ActionEvent event) {
        clear();
    }

    @FXML
    void btn_tabMain(ActionEvent event) {
        Table selectedTable = tabViewTab.getSelectionModel().getSelectedItem();

        if (selectedTable == null) {
            System.out.println("Lỗi: Chưa chọn bàn để sửa.");
            return;
        }

        String existingTableId = selectedTable.getId();
        String name = txt_tabName.getText();
        int seats;
        int floor;
        String status = comboBox_tabStatus.getValue();
        TableType selectedTableType = comboBox_tabType.getValue();

        if (txt_tabSeats.getText().isEmpty()) {
            System.out.println("Lỗi: Số ghế không được để trống.");
            return;
        }

        try {
            seats = Integer.parseInt(txt_tabSeats.getText());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị số ghế không hợp lệ.");
            return;
        }

        if (comboBox_tabFloor.getValue() == null) {
            System.out.println("Lỗi: Chưa chọn tầng.");
            return;
        }

        floor = comboBox_tabFloor.getValue();

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

        clear();
        disableInput();
        tabViewTab.getItems().clear();
        setCellValues();
        utilsButton_1();
    }


    @FXML
    void btn_tabSub(ActionEvent event) {
        String name = txt_tabName.getText();
        int seats;
        int floor;
        String status = comboBox_tabStatus.getValue();
        TableType selectedTableType = comboBox_tabType.getValue();

        try {
            seats = Integer.parseInt(txt_tabSeats.getText());
            floor = comboBox_tabFloor.getValue();
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

        clear();
        disableInput();
        tabViewTab.getItems().clear();
        setCellValues();
        utilsButton_1();
    }

    @FXML
    void getTabInfo(MouseEvent event) {
        Table selectedItem = tabViewTab.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getId();
            TableDAO tableDao = TableDAO.getInstance();
            Table table = tableDao.getById(idSelect);

            txt_tabName.setText(table.getName());
            txt_tabSeats.setText(String.valueOf(table.getSeats()));

            comboBox_tabType.getItems().stream()
                    .filter(type -> type.getName().equals(table.getTableTypeName()))
                    .findFirst()
                    .ifPresent(comboBox_tabType.getSelectionModel()::select);

            comboBox_tabStatus.getItems().stream()
                    .filter(status -> status.equals(table.getStatus()))
                    .findFirst()
                    .ifPresent(comboBox_tabStatus.getSelectionModel()::select);

            comboBox_tabFloor.getItems().stream()
                    .filter(floor -> floor.equals(table.getFloor()))
                    .findFirst()
                    .ifPresent(comboBox_tabFloor.getSelectionModel()::select);

            btn_tabMain.setVisible(true);
            btn_tabSub.setVisible(true);
            btn_tabClear.setVisible(true);
            enableInput();
            utilsButton_1();
        }
    }

}