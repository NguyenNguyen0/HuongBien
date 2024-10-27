package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Employee;
import com.huongbien.database.Database;
import com.huongbien.entity.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GUI_ManageEmployeeController implements Initializable {
    @FXML
    private TableColumn<Employee, String> tabCol_empGender;

    @FXML
    private TableColumn<Employee, String> tabCol_empID;

    @FXML
    private TableColumn<Employee, String> tabCol_empName;

    @FXML
    private TableColumn<Employee, String> tabCol_empPhone;

    @FXML
    private TableColumn<Employee, String> tabCol_empPosition;

    @FXML
    private TableColumn<Employee, String> tabCol_empStatus;

    @FXML
    private TableView<Employee> tabViewEmp;

    @FXML
    private ComboBox<String> comboBox_empStatus;

    @FXML
    private ComboBox<Employee> comboBox_empPostion;

    @FXML
    private DatePicker date_empBirthDate;

    @FXML
    private RadioButton radio_empFemale;

    @FXML
    private RadioButton radio_empMale;

    @FXML
    private TextField txt_empAddress;

    @FXML
    private TextField txt_empCitizenID;

    @FXML
    private TextField txt_empEmail;

    @FXML
    private DatePicker date_empHireDate;

    @FXML
    private TextField txt_empName;

    @FXML
    private TextField txt_empPhone;

    @FXML
    private TextField txt_empSalary;

    @FXML
    private TextField txt_empHourPay;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private TextField txt_searchEmpCitizenID;

    @FXML
    private TextField txt_searchEmpName;

    @FXML
    private TextField txt_searchEmpPhone;

    @FXML
    private Button btn_empClear;

    @FXML
    private Button btn_empFired;

    @FXML
    private Button btn_empMain;

    @FXML
    private Button btn_empRefresh;

    @FXML
    private Button btn_empSearch;

    @FXML
    private Button btn_empSub;

    private void setCellValues() {
        try {
            Connection connection = Database.getConnection();
            DAO_Employee dao_Employee = new DAO_Employee(connection);
            List<Employee> employeeList = dao_Employee.get();
            ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeeList);
            tabCol_empID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
            tabCol_empName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tabCol_empGender.setCellValueFactory(cellData -> {
                boolean gender = cellData.getValue().isGender();
                String genderText = gender ? "Nam" : "Nữ";
                return new SimpleStringProperty(genderText);
            });
            tabCol_empPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            tabCol_empPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
            tabCol_empStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            tabViewEmp.setItems(listEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    private void setValueCombobox() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Đang làm", "Nghỉ phép", "Nghỉ việc");
        comboBox_empStatus.setItems(statusList);
        comboBox_empStatus.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return comboBox_empStatus.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
//        comboBox_empStatus.getSelectionModel().selectFirst();
        comboBox_empStatus.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setDisable(false);
                } else {
                    setText(item);
                    if (item.equals("Nghỉ việc")) {
                        setDisable(true);
                    }
                }
            }
        });
        //Position
        try {
            Connection connection = Database.getConnection();
            DAO_Employee dao_employee = new DAO_Employee(connection);
            List<Employee> employeeList = dao_employee.get();
            List<Employee> distinctEmployees = new ArrayList<>(employeeList.stream()
                    .filter(e -> e.getPosition() != null)
                    .collect(Collectors.toMap(Employee::getPosition, e -> e, (e1, e2) -> e1))
                    .values());
            ObservableList<Employee> employees = FXCollections.observableArrayList(distinctEmployees);
            comboBox_empPostion.setItems(employees);
            comboBox_empPostion.setConverter(new StringConverter<Employee>() {
                @Override
                public String toString(Employee employee) {
                    return employee != null ? employee.getPosition() : "";
                }
                @Override
                public Employee fromString(String string) {
                    return comboBox_empPostion.getItems().stream()
                            .filter(item -> item.getPosition().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        setValueCombobox();
    }

    @FXML
    void getEmpInfo(MouseEvent event) {
        Employee selectedItem = tabViewEmp.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getEmployeeId();
            try {
                Connection connection = Database.getConnection();
                DAO_Employee dao_employee = new DAO_Employee(connection);
                Employee employee = dao_employee.get(idSelect);

                txt_empName.setText(employee.getName());
                txt_empCitizenID.setText(employee.getCitizenIDNumber());
                txt_empPhone.setText(employee.getPhoneNumber());
                txt_empEmail.setText(employee.getEmail());
                txt_empEmail.setText(employee.getEmail());
                txt_empHourPay.setText(employee.getHourlyPay()+"");
                txt_empSalary.setText(employee.getSalary()+"");
                txt_empAddress.setText(employee.getAddress());
//
                if (employee.getHireDate() != null) {
                    date_empHireDate.setValue(employee.getHireDate());
                }

                comboBox_empStatus.getSelectionModel().select(employee.getStatus());
                comboBox_empPostion.getSelectionModel().select(employee);

                if (employee.getBirthday() != null) {
                    date_empBirthDate.setValue(employee.getBirthday());
                }

                if (employee.isGender()) {
                    genderGroup.selectToggle(radio_empMale);
                }else {
                    genderGroup.selectToggle(radio_empFemale);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }

            btn_empFired.setVisible(true);
            btn_empClear.setVisible(true);
            btn_empSub.setVisible(true);
            btn_empMain.setVisible(true);
            btn_empSub.setText("Thêm");
            btn_empMain.setText("Sửa");
            btn_empSub.setStyle("-fx-background-color:   #1D557E");
            btn_empMain.setStyle("-fx-background-color:  #761D7E");
            enableInput();
        }
    }

    @FXML
    void btn_clearSearchEmpCitizenID(MouseEvent event) {
        txt_searchEmpCitizenID.setText("");
        txt_searchEmpCitizenID.requestFocus();
    }

    @FXML
    void btn_clearSearchEmpName(MouseEvent event) {
        txt_searchEmpName.setText("");
        txt_searchEmpName.requestFocus();
    }

    @FXML
    void btn_clearSearchEmpPhone(MouseEvent event) {
        txt_searchEmpPhone.setText("");
        txt_searchEmpPhone.requestFocus();
    }

    public void clearTxt() {
        txt_empName.setText("");
        txt_empCitizenID.setText("");
        txt_empPhone.setText("");
        txt_empEmail.setText("");
        genderGroup.selectToggle(null);
        date_empBirthDate.setValue(null);
        txt_empHourPay.setText("");
        txt_empSalary.setText("");
        txt_empAddress.setText("");
        date_empHireDate.setValue(null);
        comboBox_empStatus.getSelectionModel().clearSelection();
        comboBox_empPostion.getSelectionModel().clearSelection();
        btn_empFired.setVisible(false);
    }

    public void enableInput() {
        txt_empName.setEditable(true);
        txt_empCitizenID.setEditable(true);
        txt_empPhone.setEditable(true);
        txt_empEmail.setEditable(true);
        radio_empMale.setDisable(false);
        radio_empFemale.setDisable(false);
        date_empBirthDate.setDisable(false);
        txt_empHourPay.setEditable(true);
        txt_empSalary.setEditable(true);
        txt_empAddress.setEditable(true);
        comboBox_empStatus.setDisable(false);
        comboBox_empPostion.setDisable(false);
    }

    public void disableInput() {
        txt_empName.setEditable(false);
        txt_empCitizenID.setEditable(false);
        txt_empPhone.setEditable(false);
        txt_empEmail.setEditable(false);
        radio_empMale.setDisable(true);
        radio_empFemale.setDisable(true);
        date_empBirthDate.setDisable(true);
        txt_empHourPay.setEditable(false);
        txt_empSalary.setEditable(false);
        txt_empAddress.setEditable(false);
        comboBox_empStatus.setDisable(true);
        comboBox_empPostion.setDisable(true);
    }

    @FXML
    void btn_empSub(ActionEvent event) {
        if (btn_empSub.getText().equals("Sửa")) {
            btn_empSub.setText("Thêm");
            btn_empMain.setText("Sửa");
            btn_empSub.setStyle("-fx-background-color:   #1D557E");
            btn_empMain.setStyle("-fx-background-color:  #761D7E");
        } else if (btn_empSub.getText().equals("Thêm")) {
            btn_empSub.setText("Sửa");
            btn_empMain.setText("Thêm");
            btn_empSub.setStyle("-fx-background-color:   #761D7E");
            btn_empMain.setStyle("-fx-background-color:  #1D557E");
            btn_empSub.setVisible(false);
            btn_empMain.setVisible(true);
            btn_empClear.setVisible(true);
            enableInput();
            clearTxt();
        }
    }

    @FXML
    void btn_empMain(ActionEvent event) {
        if (btn_empMain.getText().equals("Sửa")) {
            btn_empMain.setText("Sửa");
            btn_empSub.setText("Thêm");
            btn_empMain.setStyle("-fx-background-color: #761D7E;");
            btn_empSub.setStyle("-fx-background-color: #1D557E;");
            btn_empSub.setVisible(true);
            btn_empMain.setVisible(false);
            disableInput();
        } else if (btn_empMain.getText().equals("Thêm")) {
            btn_empMain.setText("Sửa");
            btn_empSub.setText("Thêm");
            btn_empMain.setStyle("-fx-background-color: #761D7E;");
            btn_empSub.setStyle("-fx-background-color: #1D557E;");
            btn_empSub.setVisible(true);
            btn_empMain.setVisible(false);
            clearTxt();
            tabViewEmp.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void btn_empClear(ActionEvent event) {
        btn_empSub.setText("Thêm");
        btn_empMain.setText("Sửa");
        btn_empSub.setStyle("-fx-background-color:   #1D557E");
        btn_empMain.setStyle("-fx-background-color:  #761D7E");
        btn_empSub.setVisible(true);
        btn_empMain.setVisible(false);
        tabViewEmp.getSelectionModel().clearSelection();
        clearTxt();
    }

    @FXML
    void btn_empFired(ActionEvent event) {

    }

    @FXML
    void btn_empRefresh(ActionEvent event) {

    }

    @FXML
    void btn_empSearch(ActionEvent event) {

    }
}
