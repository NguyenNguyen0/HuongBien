package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.dao.DAO_Employee;
import com.huongbien.database.Database;
import com.huongbien.entity.Category;
import com.huongbien.entity.Cuisine;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public TextField txt_empAddress;

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
    private TextField txt_searchEmpID;

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
    private Button btn_empSearch;

    @FXML
    private Button btn_empSub;

    @FXML
    private Button btn_imgChooser;

    @FXML
    private ImageView imgView_emp;
    public byte[] imageEmpByte = null;

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
                    .filter(e -> e.getPosition() != null && !"Quản lý".equals(e.getPosition()))
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

    public GUI_ManageEmployee_DialogAddressController gui_manageEmployee_dialogAddressController;

    public void setController(GUI_ManageEmployee_DialogAddressController gui_manageEmployee_dialogAddressController) {
        this.gui_manageEmployee_dialogAddressController = gui_manageEmployee_dialogAddressController;
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

                DecimalFormat moneyFormat = new DecimalFormat("#,###");
                //--Format
                String formattedHourlyPay = moneyFormat.format(employee.getHourlyPay());
                txt_empHourPay.setText(formattedHourlyPay);
                //---------------
                String formattedSalary = moneyFormat.format(employee.getSalary());
                txt_empSalary.setText(formattedSalary);

                txt_empAddress.setText(employee.getAddress());
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
                } else {
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
            enableInput();
            utilsButton_1();
        }
    }

    @FXML
    void btn_clearSearchEmpID(MouseEvent event) {
        txt_searchEmpID.setText("");
        txt_searchEmpID.requestFocus();
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

    public void clearChooserImage() {
        imageEmpByte = null;
        Image image = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_employee/user-256px.png"));
        imgView_emp.setImage(image);
    }

    public void clear() {
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
        tabViewEmp.getSelectionModel().clearSelection();
        btn_empFired.setVisible(false);
        clearChooserImage();
    }

    public void enableInput() {
        txt_empName.setDisable(false);
        txt_empCitizenID.setDisable(false);
        txt_empEmail.setDisable(false);
        txt_empPhone.setDisable(false);
        radio_empMale.setDisable(false);
        radio_empFemale.setDisable(false);
        date_empBirthDate.setDisable(false);
        txt_empHourPay.setDisable(false);
        txt_empSalary.setDisable(false);
        txt_empAddress.setDisable(false);
        comboBox_empStatus.setDisable(false);
        comboBox_empPostion.setDisable(false);
        btn_imgChooser.setDisable(false);
    }

    public void disableInput() {
        txt_empName.setDisable(true);
        txt_empCitizenID.setDisable(true);
        txt_empPhone.setDisable(true);
        txt_empEmail.setDisable(true);
        radio_empMale.setDisable(true);
        radio_empFemale.setDisable(true);
        date_empBirthDate.setDisable(true);
        txt_empHourPay.setDisable(true);
        txt_empSalary.setDisable(true);
        txt_empAddress.setDisable(true);
        comboBox_empStatus.setDisable(true);
        comboBox_empPostion.setDisable(true);
        btn_imgChooser.setDisable(true);
    }

    public void utilsButton_1() {
        btn_empSub.setText("Thêm");
        btn_empMain.setText("Sửa");
        btn_empSub.setStyle("-fx-background-color:   #1D557E");
        btn_empMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        btn_empSub.setText("Sửa");
        btn_empMain.setText("Thêm");
        btn_empSub.setStyle("-fx-background-color:   #761D7E");
        btn_empMain.setStyle("-fx-background-color:  #1D557E");
    }

    @FXML
    void btn_empSub(ActionEvent event) {
        if (btn_empSub.getText().equals("Sửa")) {
            clearChooserImage();
            utilsButton_1();
        } else if (btn_empSub.getText().equals("Thêm")) {
            btn_empClear.setVisible(true);
            btn_empFired.setVisible(false);
            btn_empSub.setVisible(false);
            btn_empMain.setVisible(true);
            enableInput();
            utilsButton_2();
            clear();
            comboBox_empStatus.setDisable(true);
        }
    }

    @FXML
    void btn_empMain(ActionEvent event) {
        if (btn_empMain.getText().equals("Sửa")) {
            //Lay ID cua table thuc hien chinh sua
            Employee selectedItem = tabViewEmp.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String idSelect = selectedItem.getEmployeeId();
                double workHours = selectedItem.getWorkHours();
                LocalDate hireDate = selectedItem.getHireDate();
                //
                String name = txt_empName.getText();
                String citizenId = txt_empCitizenID.getText();
                String phone = txt_empPhone.getText();
                String email = txt_empEmail.getText();
                boolean gender;
                if (radio_empMale.isSelected()) {
                    gender = true;
                } else if (radio_empFemale.isSelected()) {
                    gender = false;
                } else {
                    gender = false;
                }
                LocalDate birthDate = date_empBirthDate.getValue();
                double hourPay = txt_empHourPay.getText().isEmpty() ? 0.0 : Double.parseDouble(txt_empHourPay.getText().replace(".", ""));
                double salary = txt_empSalary.getText().isEmpty() ? 0.0 : Double.parseDouble(txt_empSalary.getText().replace(".", ""));
                String address = txt_empAddress.getText();
                String status = comboBox_empStatus.getValue();
                String position = comboBox_empPostion.getValue() != null ? comboBox_empPostion.getValue().getPosition() : null;
                try {
                    DAO_Employee dao_employee = new DAO_Employee(Database.getConnection());
                    Employee employee = new Employee(
                            idSelect, name, phone, citizenId, gender, address,
                            birthDate, email, status, hireDate, position, workHours, hourPay, salary, null
                    );
                    if (dao_employee.update(employee)) {
                        System.out.println("Đã câp nhạt thành công");
                    } else {
                        System.out.println("Cập nhật nhân viên không thành công");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    Database.closeConnection();
                }
            }
        } else if (btn_empMain.getText().equals("Thêm")) {
            String name = txt_empName.getText();
            String citizenId = txt_empCitizenID.getText();
            String phone = txt_empPhone.getText();
            String email = txt_empEmail.getText();
            boolean gender;
            if (radio_empMale.isSelected()) {
                gender = true;
            } else if (radio_empFemale.isSelected()) {
                gender = false;
            } else {
                gender = false;
            }
            LocalDate birthDate = date_empBirthDate.getValue();
            double hourPay = txt_empHourPay.getText().isEmpty() ? 0.0 : Double.parseDouble(txt_empHourPay.getText().replace(".", ""));
            double salary = txt_empSalary.getText().isEmpty() ? 0.0 : Double.parseDouble(txt_empSalary.getText().replace(".", ""));
            String address = txt_empAddress.getText();
            String position = comboBox_empPostion.getValue() != null
                    ? comboBox_empPostion.getValue().getPosition()
                    : comboBox_empPostion.getEditor().getText();
            double workHours = 0;
            try {
                DAO_Employee dao_employee = new DAO_Employee(Database.getConnection());
                Employee employee = new Employee(name, phone, citizenId,
                        gender, address, birthDate, email, position, workHours, hourPay, salary, null);
                if (dao_employee.add(employee)) {
                    System.out.println("Thêm nhan vien thành công");
                } else {
                    System.out.println("Thêm nhan vien không thành công");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
            clear();
        }
        disableInput();
        tabViewEmp.getItems().clear();
        setCellValues();
        btn_empClear.setVisible(false);
        btn_empFired.setVisible(false);
        btn_empSub.setVisible(true);
        btn_empMain.setVisible(false);
        utilsButton_1();
    }

    @FXML
    void btn_empClear(ActionEvent event) {
        btn_empClear.setVisible(false);
        btn_empFired.setVisible(false);
        btn_empSub.setVisible(true);
        btn_empMain.setVisible(false);
        tabViewEmp.getSelectionModel().clearSelection();
        utilsButton_1();
        disableInput();
        clear();
    }

    @FXML
    void btn_empFired(ActionEvent event) {
        //processing
        Employee selectedItem = tabViewEmp.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getEmployeeId();
            try {
                DAO_Employee dao_employee = new DAO_Employee(Database.getConnection());
                if (dao_employee.updateStatus(idSelect, "Nghỉ việc")) {
                    System.out.println("Sa thải nhan vien thanh cong");
                } else {
                    System.out.println("Sa thải nhan vien khong thanh cong");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
        }
        tabViewEmp.getItems().clear();
        setCellValues();
        btn_empClear.fire();
        disableInput();
    }

    @FXML
    void btn_empSearch(ActionEvent event) {
        tabViewEmp.getItems().clear();
        String name = txt_searchEmpName.getText();
        String phone = txt_searchEmpPhone.getText();
        String empID = txt_searchEmpID.getText();
        try {
            DAO_Employee dao_Employee = new DAO_Employee(Database.getConnection());
            List<Employee> employeeList = dao_Employee.getByCriteria(phone, name, empID);
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

    @FXML
    void openInputAddress(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageEmployee_DialogAddress.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Diage Address - Huong Bien Restaurant");
        primaryStage.setMaximized(true);
        GUI_ManageEmployee_DialogAddressController dialogController = loader.getController();
        dialogController.setGUIManageEmployeeController(this);
        primaryStage.show();
    }

    @FXML
    void txt_empHourPay_onKeyReleased(KeyEvent event) {
        String input = txt_empHourPay.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            txt_empHourPay.setText(formattedText);
            txt_empHourPay.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            txt_empHourPay.setText(validInput.toString());
            txt_empHourPay.positionCaret(validInput.length());
        }
    }

    @FXML
    void txt_empSalary_onKeyReleased(KeyEvent event) {
        String input = txt_empSalary.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            txt_empSalary.setText(formattedText);
            txt_empSalary.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            txt_empSalary.setText(validInput.toString());
            txt_empSalary.positionCaret(validInput.length());
        }
    }

    @FXML
    void btn_imgChooser(ActionEvent event) {

    }
}