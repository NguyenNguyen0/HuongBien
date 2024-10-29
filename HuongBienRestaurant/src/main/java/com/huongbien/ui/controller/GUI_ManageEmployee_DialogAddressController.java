package com.huongbien.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI_ManageEmployee_DialogAddressController implements Initializable {
    @FXML
    private TextField txt_addressDetail;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_save;

    @FXML
    private ComboBox<String> comboBox_city;

    @FXML
    private ComboBox<String> comboBox_district;

    @FXML
    private ComboBox<String> comboBox_ward;

    public void setValuesCombox_ward() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Phường 1",
                "Phường 2", "Phường 3", "Phường 4", "Phường 5", "Phường 6",
                "Phường 7", "Phường 8", "Phường 9", "Phường 10", "Phường 11",
                "Phường 12", "Phường 13", "Phường 14", "Phường 15");
        comboBox_ward.setItems(statusList);
        comboBox_ward.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }
            @Override
            public String fromString(String s) {
                return "";
            }
        });
//        comboBox_ward.getSelectionModel().selectFirst();
    }

    public void setValuesCombox_district() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Quận 1",
                "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 10", "Quận 11",
                "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh",
                "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình",
                "Quận Tân Phú", "Huyện Bình Chánh", "Huyện Cần Giờ",
                "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè");
        comboBox_district.setItems(statusList);
        comboBox_district.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }
            @Override
            public String fromString(String s) {
                return "";
            }
        });
//        comboBox_district.getSelectionModel().selectFirst();
    }

    public void setValuesCombox_city() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Thành phố Hồ Chí Minh");
        comboBox_city.setItems(statusList);
        comboBox_city.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }
            @Override
            public String fromString(String s) {
                return "";
            }
        });
        comboBox_city.getSelectionModel().selectFirst();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValuesCombox_ward();
        setValuesCombox_district();
        setValuesCombox_city();
    }

    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void btn_exit(MouseEvent event) {
        closeWindow(event);
    }

    @FXML
    void overlay(MouseEvent event) {
        closeWindow(event);
    }

    @FXML
    void btn_clear(ActionEvent event) {
        txt_addressDetail.setText("");
        txt_addressDetail.requestFocus();
        comboBox_ward.getSelectionModel().clearSelection();
        comboBox_district.getSelectionModel().clearSelection();
    }

    private GUI_ManageEmployeeController gui_manageEmployeeController;

    public void setGUIManageEmployeeController(GUI_ManageEmployeeController gui_manageEmployeeController) {
        this.gui_manageEmployeeController = gui_manageEmployeeController;
    }

    @FXML
    void btn_save(ActionEvent event) {
        String address = txt_addressDetail.getText() + ", "
                + comboBox_ward.getValue() + ", "
                + comboBox_district.getValue() + ", "
                + comboBox_city.getValue();
        System.out.println(address);
        gui_manageEmployeeController.txt_empAddress.setText(address);
        //close()
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
