package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GUI_LookupController implements Initializable {

    @FXML
    private ComboBox<String> comboBox_choice;
    @FXML
    private TableView<Objects> tabViewLookUp;
    @FXML
    private TableColumn<Objects, ?> tabCol_1;
    @FXML
    private TableColumn<Objects, ?> tabCol_2;
    @FXML
    private TableColumn<Objects, ?> tabCol_3;
    @FXML
    private TableColumn<Objects, ?> tabCol_4;
    @FXML
    private TableColumn<Objects, ?> tabCol_5;
    @FXML
    private TableColumn<Objects, ?> tabCol_6;
    @FXML
    private TableColumn<Objects, ?> tabCol_7;
    @FXML
    private TableColumn<Objects, ?> tabCol_8;
    @FXML
    private TableColumn<Objects, ?> tabCol_9;

    private final DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

    private void setValueComboboxChoice() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Đơn đặt", "Hóa đơn", "Món ăn", "Bàn ăn", "Khách hàng", "Nhân viên", "Khuyến mãi");
        comboBox_choice.setItems(statusList);
        comboBox_choice.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return comboBox_choice.getItems().stream()
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

    private void setCellValues(){

    }



    private void setColumnsName (){
        if (comboBox_choice.getSelectionModel().isSelected(0)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã đơn");
            tabCol_2.setText("Ngày đặt");
            tabCol_3.setText("Khách hàng");
            tabCol_4.setText("SDT");
            tabCol_5.setText("Giờ nhận");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(1)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã HD");
            tabCol_2.setText("Ngày lập");
            tabCol_3.setText("Khách hàng");
            tabCol_4.setText("Nhân viên");
            tabCol_5.setText("Tổng tiền");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(2)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã món");
            tabCol_2.setText("Tên món");
            tabCol_3.setText("Giá tiền");
            tabCol_4.setText("Loại món");
            tabCol_5.setText("Mô tả");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(3)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã bàn");
            tabCol_2.setText("Tên bàn");
            tabCol_3.setText("Số chỗ");
            tabCol_4.setText("Loại bàn");
            tabCol_5.setText("Tầng");
            tabCol_6.setText("Trạng thái");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            tabViewLookUp.getColumns().add(tabCol_6);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(4)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã KH");
            tabCol_2.setText("Họ tên");
            tabCol_3.setText("Giới tính");
            tabCol_4.setText("Địa chỉ");
            tabCol_5.setText("Điểm");
            tabCol_6.setText("Hạng thành viên");
            tabCol_7.setText("Ngày sinh");
            tabCol_8.setText("Ngày tham gia");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            tabViewLookUp.getColumns().add(tabCol_6);
            tabViewLookUp.getColumns().add(tabCol_7);
            tabViewLookUp.getColumns().add(tabCol_8);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(5)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã NV");
            tabCol_2.setText("Họ tên");
            tabCol_3.setText("Giới tính");
            tabCol_4.setText("Địa chỉ");
            tabCol_5.setText("CCCD");
            tabCol_6.setText("SDT");
            tabCol_7.setText("Ngày sinh");
            tabCol_8.setText("Chức vụ");
            tabCol_9.setText("Ngày vào làm");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            tabViewLookUp.getColumns().add(tabCol_6);
            tabViewLookUp.getColumns().add(tabCol_7);
            tabViewLookUp.getColumns().add(tabCol_8);
            tabViewLookUp.getColumns().add(tabCol_9);
            autoResizeColumns(tabViewLookUp);
        }
        else if (comboBox_choice.getSelectionModel().isSelected(6)){
            tabViewLookUp.getColumns().clear();
            tabCol_1.setText("Mã KM");
            tabCol_2.setText("Tên KM");
            tabCol_3.setText("Giới tính");
            tabCol_4.setText("Địa chỉ");
            tabCol_5.setText("CCCD");
            tabCol_6.setText("SDT");
            tabCol_7.setText("Ngày sinh");
            tabCol_8.setText("Chức vụ");
            tabCol_9.setText("Ngày vào làm");
            tabViewLookUp.getColumns().add(tabCol_1);
            tabViewLookUp.getColumns().add(tabCol_2);
            tabViewLookUp.getColumns().add(tabCol_3);
            tabViewLookUp.getColumns().add(tabCol_4);
            tabViewLookUp.getColumns().add(tabCol_5);
            tabViewLookUp.getColumns().add(tabCol_6);
            tabViewLookUp.getColumns().add(tabCol_7);
            tabViewLookUp.getColumns().add(tabCol_8);
            tabViewLookUp.getColumns().add(tabCol_9);
            autoResizeColumns(tabViewLookUp);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabViewLookUp.getColumns().clear();
        setValueComboboxChoice();
    }

    @FXML
    void txt_invoiceSearchKeyReleased(KeyEvent keyEvent) {
    }

    @FXML
    void btn_clearSearchClicked(MouseEvent mouseEvent) {
    }

    @FXML
    void getInvoiceInfo(MouseEvent mouseEvent) {
    }
    @FXML
    void comboBox_choose(ActionEvent actionEvent) {
        //Chọn Đơn đặt
        if (comboBox_choice.getSelectionModel().isSelected(0)){
            setColumnsName();
            setCellValues();
        }
        //Chọn Hóa đơn
        if (comboBox_choice.getSelectionModel().isSelected(1)){
            setColumnsName();
            setCellValues();
        }
        //Chọn món ăn
        if (comboBox_choice.getSelectionModel().isSelected(2)){
            setColumnsName();
            setCellValues();

        }
        //Chọn Bàn ăn
        if (comboBox_choice.getSelectionModel().isSelected(3)){
            setColumnsName();
            setCellValues();
        }
        //Chọn Khách hàng
        if (comboBox_choice.getSelectionModel().isSelected(4)){
            setColumnsName();
            setCellValues();
        }
        //Chọn Nhân viên
        if (comboBox_choice.getSelectionModel().isSelected(5)){
            setColumnsName();
            setCellValues();
        }
        //Chọn Khuyến mãi
        if (comboBox_choice.getSelectionModel().isSelected(6)){
            setColumnsName();
            setCellValues();
        }

    }
}
