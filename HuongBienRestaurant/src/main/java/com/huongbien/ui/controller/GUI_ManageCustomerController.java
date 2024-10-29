package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Customer;
import com.huongbien.dao.DAO_Employee;
import com.huongbien.database.Database;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Employee;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GUI_ManageCustomerController implements Initializable {
    @FXML
    private Button btn_customerClear;

    @FXML
    private Button btn_customerMain;

    @FXML
    private Button btn_customerSub;

    @FXML
    private DatePicker date_customerBirthDate;

    @FXML
    private DatePicker date_registrationDate;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private RadioButton radio_customerFemale;

    @FXML
    private RadioButton radio_customerMale;

    @FXML
    private TableColumn<Customer, Integer> tabCol_customerAccumulatedPoint;

    @FXML
    private TableColumn<Customer, String> tabCol_customerAddress;

    @FXML
    private TableColumn<Customer, String> tabCol_customerGender;

    @FXML
    private TableColumn<Customer, String> tabCol_customerID;

    @FXML
    private TableColumn<Customer, String> tabCol_customerMemLevel;

    @FXML
    private TableColumn<Customer, String> tabCol_customerName;

    @FXML
    private TableView<Customer> tabViewCustomer;

    @FXML
    private TextField txt_customerAccumulatedPoints;

    @FXML
    private TextField txt_customerAddress;

    @FXML
    private TextField txt_customerEmail;

    @FXML
    private TextField txt_customerMembershipLevel;

    @FXML
    private TextField txt_customerName;

    @FXML
    private TextField txt_customerPhone;

    @FXML
    private TextField txt_searchCustomerPhone;

    private Utils utils;


    private void setCellValues() {
        try {
            Connection connection = Database.getConnection();
            DAO_Customer dao_customer = new DAO_Customer(connection);
            List<Customer> customerList = dao_customer.get();
            ObservableList<Customer> listCustomer = FXCollections.observableArrayList(customerList);

            txt_customerAddress.setEditable(true);
            txt_customerName.setEditable(true);

            tabCol_customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            tabCol_customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tabCol_customerGender.setCellValueFactory(cellData -> {
                boolean gender = cellData.getValue().isGender();
                String genderText = gender ? "Nam" : "Nữ";
                return new SimpleStringProperty(genderText);
            });
            tabCol_customerMemLevel.setCellValueFactory(cellData -> {
                int memberShip = cellData.getValue().getMembershipLevel();
                String memberShipLevel = utils.toStringMembershipLevel(memberShip);
                return new SimpleStringProperty(memberShipLevel);
            });
            tabCol_customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            tabCol_customerAccumulatedPoint.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
            tabViewCustomer.setItems(listCustomer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }


    public void clear() {
        txt_customerName.setText("");
        txt_customerPhone.setText("");
        txt_customerEmail.setText("");
        date_customerBirthDate.setValue(null);
        genderGroup.selectToggle(null);
        date_registrationDate.setValue(null);
        txt_customerAddress.setText("");
        txt_customerMembershipLevel.setText("");
        txt_customerAccumulatedPoints.setText("");
        tabViewCustomer.getSelectionModel().clearSelection();
    }

    public void enableInput() {
        txt_customerName.setDisable(false);
        txt_customerEmail.setDisable(false);
        txt_customerAddress.setDisable(false);
        txt_customerPhone.setDisable(false);
        date_customerBirthDate.setDisable(false);
    }

    public void disableInput() {
        txt_customerName.setDisable(true);
        txt_customerEmail.setDisable(true);
        txt_customerAddress.setDisable(true);
        txt_customerPhone.setDisable(true);
        date_customerBirthDate.setDisable(true);

    }

    public void utilsButton_1() {
        btn_customerSub.setText("Thêm");
        btn_customerMain.setText("Sửa");
        btn_customerSub.setStyle("-fx-background-color:   #1D557E");
        btn_customerMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        btn_customerSub.setText("Sửa");
        btn_customerMain.setText("Thêm");
        btn_customerSub.setStyle("-fx-background-color:   #761D7E");
        btn_customerMain.setStyle("-fx-background-color:  #1D557E");
    }

    public boolean checkData(){
        if (txt_customerName.getText().trim().equals("")){
            return false;
        }
        if (txt_customerPhone.getText().trim().equals("")){
            return false;
        }
        if (date_customerBirthDate.getValue() == null){
            return false;
        }
        if (genderGroup.getSelectedToggle() == null){
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
    }

    @FXML
    void btn_clearSearchCusPhone(MouseEvent event) {
        txt_searchCustomerPhone.setText("");
        setCellValues();
    }

    @FXML
    void btn_customerClear(ActionEvent event) {
        clear();
        txt_customerName.requestFocus();
    }

    @FXML
    void btn_customerMain(ActionEvent event) {
        if(btn_customerMain.getText() == "Thêm") {
            enableInput();
            Customer customer = null;
            boolean gender = true;
            if(checkData()) {
                String name = txt_customerName.getText();
                String phone = txt_customerPhone.getText();
                String email = txt_customerEmail.getText();
                String address = txt_customerAddress.getText();
                LocalDate birthday = date_customerBirthDate.getValue();
                if (radio_customerFemale.isSelected()) {
                    gender = false;
                }
                customer = new Customer(name, address, gender, phone, email, birthday);
                try {
                    Connection connection = Database.getConnection();
                    DAO_Customer dao_customer = new DAO_Customer(connection);
                    if (dao_customer.add(customer)){
                        setCellValues();
                    }
                    connection.close();
                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
            clear();
        }
        else if (btn_customerMain.getText().equals("Sửa")){
            enableInput();
            Customer customer = null;
            boolean gender = true;
            if (checkData()) {
                String name = txt_customerName.getText();
                String phone = txt_customerPhone.getText();
                String email = txt_customerEmail.getText();
                String address = txt_customerAddress.getText();
                LocalDate birthday = date_customerBirthDate.getValue();
                if (radio_customerFemale.isSelected()) {
                    gender = false;
                }
                customer = new Customer(name, address, gender, phone, email, birthday);
                try {
                    Connection connection = Database.getConnection();
                    DAO_Customer dao_customer = new DAO_Customer(connection);
                    if (dao_customer.update(customer)) {
                        setCellValues();
                    }
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            clear();
        }
    }

    @FXML
    void btn_customerSub(ActionEvent event) {
        if (btn_customerMain.getText().equals("Thêm")) {
            utilsButton_1();
        }
        else {
            utilsButton_2();
            clear();
        }
    }

    @FXML
    void getCustomerInfo(MouseEvent event) {
        utilsButton_1();
        Customer selectedItem = tabViewCustomer.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            txt_customerName.setText(selectedItem.getName());
            txt_customerPhone.setText(selectedItem.getPhoneNumber());
            txt_customerAddress.setText(selectedItem.getAddress());
            txt_customerEmail.setText(selectedItem.getEmail());
            txt_customerAccumulatedPoints.setText(selectedItem.getAccumulatedPoints()+"");
            txt_customerMembershipLevel.setText(utils.toStringMembershipLevel(selectedItem.getMembershipLevel()));
            date_customerBirthDate.setValue(selectedItem.getBirthday());
            date_registrationDate.setValue(selectedItem.getRegistrationDate());

            if(selectedItem.isGender()){
                genderGroup.selectToggle(radio_customerMale);
            }
            else {
                genderGroup.selectToggle(radio_customerFemale);
            }
        }

    }

    @FXML
    void txt_searchCustomerPhone(KeyEvent event) {
        String phone = txt_searchCustomerPhone.getText();
        try {
            Connection connection = Database.getConnection();
            DAO_Customer dao_customer = new DAO_Customer(connection);
            List<Customer> customerList = dao_customer.searchCustomerPhone(phone);
            ObservableList<Customer> listCustomer = FXCollections.observableArrayList(customerList);

            txt_customerAddress.setEditable(true);
            txt_customerName.setEditable(true);

            tabCol_customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            tabCol_customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tabCol_customerGender.setCellValueFactory(cellData -> {
                boolean gender = cellData.getValue().isGender();
                String genderText = gender ? "Nam" : "Nữ";
                return new SimpleStringProperty(genderText);
            });
            tabCol_customerMemLevel.setCellValueFactory(cellData -> {
                int memberShip = cellData.getValue().getMembershipLevel();
                String memberShipLevel = utils.toStringMembershipLevel(memberShip);
                return new SimpleStringProperty(memberShipLevel);
            });
            tabCol_customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            tabCol_customerAccumulatedPoint.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
            tabViewCustomer.setItems(listCustomer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

}