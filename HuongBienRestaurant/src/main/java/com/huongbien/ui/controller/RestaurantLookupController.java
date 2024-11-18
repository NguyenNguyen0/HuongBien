package com.huongbien.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantLookupController implements Initializable {

    @FXML
    private ComboBox<String> choiceComboBox;
    @FXML
    private TableView<Objects> lookupTable;
    @FXML
    private TableColumn<Objects, ?> column1;
    @FXML
    private TableColumn<Objects, ?> column2;
    @FXML
    private TableColumn<Objects, ?> column3;
    @FXML
    private TableColumn<Objects, ?> column4;
    @FXML
    private TableColumn<Objects, ?> column5;
    @FXML
    private TableColumn<Objects, ?> column6;
    @FXML
    private TableColumn<Objects, ?> column7;
    @FXML
    private TableColumn<Objects, ?> column8;
    @FXML
    private TableColumn<Objects, ?> column9;

    private final DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

    private void setChoiceComboBoxValue() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Đơn đặt", "Hóa đơn", "Món ăn", "Bàn ăn", "Khách hàng", "Nhân viên", "Khuyến mãi");
        choiceComboBox.setItems(statusList);
        choiceComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return choiceComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void autoResizeColumns(TableView<Objects> tableView) {
        for (TableColumn<Objects, ?> column : tableView.getColumns()) {
            column.setPrefWidth(150);
            column.setResizable(true);
            column.setMinWidth(50);
            column.setMaxWidth(250);
            column.setStyle("-fx-alignment: CENTER;");
        }
        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = tableView.getWidth();
            for (TableColumn<Objects, ?> column : tableView.getColumns()) {
                column.setPrefWidth(totalWidth / tableView.getColumns().size());
            }
        });
    }

    private void setPromotionTableValue() {

    }

    private void setColumnsName() {
        if (choiceComboBox.getSelectionModel().isSelected(0)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã đơn");
            column2.setText("Ngày đặt");
            column3.setText("Khách hàng");
            column4.setText("SDT");
            column5.setText("Giờ nhận");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(1)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã HD");
            column2.setText("Ngày lập");
            column3.setText("Khách hàng");
            column4.setText("Nhân viên");
            column5.setText("Tổng tiền");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(2)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã món");
            column2.setText("Tên món");
            column3.setText("Giá tiền");
            column4.setText("Loại món");
            column5.setText("Mô tả");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(3)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã bàn");
            column2.setText("Tên bàn");
            column3.setText("Số chỗ");
            column4.setText("Loại bàn");
            column5.setText("Tầng");
            column6.setText("Trạng thái");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            lookupTable.getColumns().add(column6);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(4)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã KH");
            column2.setText("Họ tên");
            column3.setText("Giới tính");
            column4.setText("Địa chỉ");
            column5.setText("Điểm");
            column6.setText("Hạng thành viên");
            column7.setText("Ngày sinh");
            column8.setText("Ngày tham gia");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            lookupTable.getColumns().add(column6);
            lookupTable.getColumns().add(column7);
            lookupTable.getColumns().add(column8);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(5)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã NV");
            column2.setText("Họ tên");
            column3.setText("Giới tính");
            column4.setText("Địa chỉ");
            column5.setText("CCCD");
            column6.setText("SDT");
            column7.setText("Ngày sinh");
            column8.setText("Chức vụ");
            column9.setText("Ngày vào làm");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            lookupTable.getColumns().add(column6);
            lookupTable.getColumns().add(column7);
            lookupTable.getColumns().add(column8);
            lookupTable.getColumns().add(column9);
            autoResizeColumns(lookupTable);
        } else if (choiceComboBox.getSelectionModel().isSelected(6)) {
            lookupTable.getColumns().clear();
            column1.setText("Mã KM");
            column2.setText("Tên KM");
            column3.setText("Ngày bắt đầu");
            column4.setText("Ngày kết thúc");
            column5.setText("Giảm giá");
            column6.setText("Hạng áp dụng");
            column7.setText("Hóa đơn tối thiểu");
            column8.setText("Trạng thái");
            column9.setText("Mô tả");
            lookupTable.getColumns().add(column1);
            lookupTable.getColumns().add(column2);
            lookupTable.getColumns().add(column3);
            lookupTable.getColumns().add(column4);
            lookupTable.getColumns().add(column5);
            lookupTable.getColumns().add(column6);
            lookupTable.getColumns().add(column7);
            lookupTable.getColumns().add(column8);
            lookupTable.getColumns().add(column9);
            autoResizeColumns(lookupTable);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lookupTable.getColumns().clear();
        setChoiceComboBoxValue();
    }

    @FXML
    void onOrderSearchFieldKeyReleased(KeyEvent keyEvent) {
    }

    @FXML
    void onClearSearchButtonClicked(MouseEvent mouseEvent) {
    }

    @FXML
    void onOrderTableClicked(MouseEvent mouseEvent) {
    }

    @FXML
    void onChoiceComboBoxSelected(ActionEvent actionEvent) {
        //Chọn Đơn đặt
        if (choiceComboBox.getSelectionModel().isSelected(0)) {
            setColumnsName();
            setPromotionTableValue();
        }
        //Chọn Hóa đơn
        if (choiceComboBox.getSelectionModel().isSelected(1)) {
            setColumnsName();
            setPromotionTableValue();
        }
        //Chọn món ăn
        if (choiceComboBox.getSelectionModel().isSelected(2)) {
            setColumnsName();
            setPromotionTableValue();

        }
        //Chọn Bàn ăn
        if (choiceComboBox.getSelectionModel().isSelected(3)) {
            setColumnsName();
            setPromotionTableValue();
        }
        //Chọn Khách hàng
        if (choiceComboBox.getSelectionModel().isSelected(4)) {
            setColumnsName();
            setPromotionTableValue();
        }
        //Chọn Nhân viên
        if (choiceComboBox.getSelectionModel().isSelected(5)) {
            setColumnsName();
            setPromotionTableValue();
        }
        //Chọn Khuyến mãi
        if (choiceComboBox.getSelectionModel().isSelected(6)) {
            setColumnsName();
            setPromotionTableValue();
        }
    }
}
