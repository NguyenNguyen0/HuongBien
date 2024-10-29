package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Employee;
import com.huongbien.database.Database;
import com.huongbien.entity.Employee;
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
    private TableColumn<?, ?> tabCol_customerAccumulatedPoint;

    @FXML
    private TableColumn<?, ?> tabCol_customerAddress;

    @FXML
    private TableColumn<?, ?> tabCol_customerGender;

    @FXML
    private TableColumn<?, ?> tabCol_customerID;

    @FXML
    private TableColumn<?, ?> tabCol_customerMemLevel;

    @FXML
    private TableColumn<?, ?> tabCol_customerName;

    @FXML
    private TableView<?> tabViewCustomer;

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

    private void setCellValues() {
//        try {
//            Connection connection = Database.getConnection();
//            DAO_Employee dao_Employee = new DAO_Employee(connection);
//            List<Employee> employeeList = dao_Employee.get();
//            ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeeList);
//            tabCol_empID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
//            tabCol_empName.setCellValueFactory(new PropertyValueFactory<>("name"));
//            tabCol_empGender.setCellValueFactory(cellData -> {
//                boolean gender = cellData.getValue().isGender();
//                String genderText = gender ? "Nam" : "Nữ";
//                return new SimpleStringProperty(genderText);
//            });
//            tabCol_empPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
//            tabCol_empPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
//            tabCol_empStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//            tabViewEmp.setItems(listEmployee);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            Database.closeConnection();
//        }
    }

    private void setValueCombobox() {
        //Status
//        ObservableList<String> statusList = FXCollections.observableArrayList("Đang làm", "Nghỉ phép", "Nghỉ việc");
//        comboBox_empStatus.setItems(statusList);
//        comboBox_empStatus.setConverter(new StringConverter<String>() {
//            @Override
//            public String toString(String status) {
//                return status != null ? status : "";
//            }
//
//            @Override
//            public String fromString(String string) {
//                return comboBox_empStatus.getItems().stream()
//                        .filter(item -> item.equals(string))
//                        .findFirst()
//                        .orElse(null);
//            }
//        });
////        comboBox_empStatus.getSelectionModel().selectFirst();
//        comboBox_empStatus.setCellFactory(lv -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null || empty) {
//                    setText(null);
//                    setDisable(false);
//                } else {
//                    setText(item);
//                    if (item.equals("Nghỉ việc")) {
//                        setDisable(true);
//                    }
//                }
//            }
//        });
        //Position
//        try {
//            Connection connection = Database.getConnection();
//            DAO_Employee dao_employee = new DAO_Employee(connection);
//            List<Employee> employeeList = dao_employee.get();
//            List<Employee> distinctEmployees = new ArrayList<>(employeeList.stream()
//                    .filter(e -> e.getPosition() != null && !"Quản lý".equals(e.getPosition()))
//                    .collect(Collectors.toMap(Employee::getPosition, e -> e, (e1, e2) -> e1))
//                    .values());
//            ObservableList<Employee> employees = FXCollections.observableArrayList(distinctEmployees);
//            comboBox_empPostion.setItems(employees);
//            comboBox_empPostion.setConverter(new StringConverter<Employee>() {
//                @Override
//                public String toString(Employee employee) {
//                    return employee != null ? employee.getPosition() : "";
//                }
//
//                @Override
//                public Employee fromString(String string) {
//                    return comboBox_empPostion.getItems().stream()
//                            .filter(item -> item.getPosition().equals(string))
//                            .findFirst()
//                            .orElse(null);
//                }
//            });
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            Database.closeConnection();
//        }
//    }
//
//    public void clearChooserImage() {
//        imageEmpByte = null;
//        Image image = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_employee/user-256px.png"));
//        imgView_emp.setImage(image);
    }

    public void clear() {
//        txt_empName.setText("");
//        txt_empCitizenID.setText("");
//        txt_empPhone.setText("");
//        txt_empEmail.setText("");
//        genderGroup.selectToggle(null);
//        date_empBirthDate.setValue(null);
//        txt_empHourPay.setText("");
//        txt_empSalary.setText("");
//        txt_empAddress.setText("");
//        date_empHireDate.setValue(null);
//        comboBox_empStatus.getSelectionModel().clearSelection();
//        comboBox_empPostion.getSelectionModel().clearSelection();
//        tabViewEmp.getSelectionModel().clearSelection();
//        btn_empFired.setVisible(false);
//        clearChooserImage();
    }

    public void enableInput() {
//        txt_empName.setDisable(false);
//        txt_empCitizenID.setDisable(false);
//        txt_empEmail.setDisable(false);
//        txt_empPhone.setDisable(false);
//        radio_empMale.setDisable(false);
//        radio_empFemale.setDisable(false);
//        date_empBirthDate.setDisable(false);
//        txt_empHourPay.setDisable(false);
//        txt_empSalary.setDisable(false);
//        txt_empAddress.setDisable(false);
//        comboBox_empStatus.setDisable(false);
//        comboBox_empPostion.setDisable(false);
//        btn_imgChooser.setDisable(false);
    }

    public void disableInput() {
//        txt_empName.setDisable(true);
//        txt_empCitizenID.setDisable(true);
//        txt_empPhone.setDisable(true);
//        txt_empEmail.setDisable(true);
//        radio_empMale.setDisable(true);
//        radio_empFemale.setDisable(true);
//        date_empBirthDate.setDisable(true);
//        txt_empHourPay.setDisable(true);
//        txt_empSalary.setDisable(true);
//        txt_empAddress.setDisable(true);
//        comboBox_empStatus.setDisable(true);
//        comboBox_empPostion.setDisable(true);
//        btn_imgChooser.setDisable(true);
    }

    public void utilsButton_1() {
//        btn_empSub.setText("Thêm");
//        btn_empMain.setText("Sửa");
//        btn_empSub.setStyle("-fx-background-color:   #1D557E");
//        btn_empMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
//        btn_empSub.setText("Sửa");
//        btn_empMain.setText("Thêm");
//        btn_empSub.setStyle("-fx-background-color:   #761D7E");
//        btn_empMain.setStyle("-fx-background-color:  #1D557E");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        setValueCombobox();
    }

    @FXML
    void btn_clearSearchEmpName(MouseEvent event) {

    }

    @FXML
    void btn_customerClear(ActionEvent event) {

    }

    @FXML
    void btn_customerMain(ActionEvent event) {

    }

    @FXML
    void btn_customerSub(ActionEvent event) {

    }

    @FXML
    void getCustomerInfo(MouseEvent event) {

    }

    @FXML
    void txt_empHourPay_onKeyReleased(KeyEvent event) {

    }
}