package com.huongbien.ui.controller;

import com.huongbien.dao.EmployeeDAO;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeManagementController implements Initializable {
    @FXML private TableColumn<Employee, String> employeeGenderColumn;
    @FXML private TableColumn<Employee, String> employeeIdColumn;
    @FXML private TableColumn<Employee, String> employeeNameColumn;
    @FXML private TableColumn<Employee, String> employeePhoneColumn;
    @FXML private TableColumn<Employee, String> employeePositionColumn;
    @FXML private TableColumn<Employee, String> employeeStatusColumn;
    @FXML private TableView<Employee> employeeTable;
    @FXML private ComboBox<String> employeeStatusComboBox;
    @FXML private ComboBox<Employee> employeePositionComboBox;
    @FXML private DatePicker employeeBirthdayDatePicker;
    @FXML private RadioButton femaleRadioButton;
    @FXML private RadioButton maleRadioButton;
    @FXML public TextField employeeAddressField;
    @FXML private TextField employeeCitizenIdField;
    @FXML private TextField employeeEmailField;
    @FXML private DatePicker employeeHiredateDatePicker;
    @FXML private TextField employeeNameField;
    @FXML private TextField employeePhoneField;
    @FXML private TextField employeeSalaryField;
    @FXML private TextField employeeHourlyPayField;
    @FXML private ToggleGroup genderGroup;
    @FXML private TextField employeeIdSearchField;
    @FXML private TextField employeeNameSearchField;
    @FXML private TextField employeePhoneSearchField;
    @FXML private Button employeeClearFormButton;
    @FXML private Button fireEmployeeButton;
    @FXML private Button handleActionEmployeeButton;
    @FXML private Button searchEmployeeButton;
    @FXML private Button swapModeEmployeeButton;
    @FXML private Button chooseImageButton;
    @FXML private ImageView employeeAvatar;

    public byte[] employeeImageBytes = null;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPromotionTableValue();
        setComboBoxValue();
    }

    private void setPromotionTableValue() {
        EmployeeDAO employeeDao = EmployeeDAO.getInstance();
        List<Employee> employeeList = employeeDao.getAll();
        ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeeList);
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeGenderColumn.setCellValueFactory(cellData -> {
            boolean gender = cellData.getValue().isGender();
            String genderText = gender ? "Nam" : "Nữ";
            return new SimpleStringProperty(genderText);
        });
        employeePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeePositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        employeeTable.setItems(listEmployee);
    }

    private void setComboBoxValue() {
        //Status
        ObservableList<String> statusList = FXCollections.observableArrayList("Đang làm", "Nghỉ phép", "Nghỉ việc");
        employeeStatusComboBox.setItems(statusList);
        employeeStatusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return employeeStatusComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
//        employeeStatusComboBox.getSelectionModel().selectFirst();
        employeeStatusComboBox.setCellFactory(lv -> new ListCell<>() {
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
        EmployeeDAO employeeDao = EmployeeDAO.getInstance();
        List<Employee> employeeList = employeeDao.getAll();
        List<Employee> distinctEmployees = new ArrayList<>(employeeList.stream()
                .filter(e -> e.getPosition() != null && !"Quản lý".equals(e.getPosition()))
                .collect(Collectors.toMap(Employee::getPosition, e -> e, (e1, e2) -> e1))
                .values());
        ObservableList<Employee> employees = FXCollections.observableArrayList(distinctEmployees);
        employeePositionComboBox.setItems(employees);
        employeePositionComboBox.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee != null ? employee.getPosition() : "";
            }

            @Override
            public Employee fromString(String string) {
                return employeePositionComboBox.getItems().stream()
                        .filter(item -> item.getPosition().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public EmployeeAddressDialogController _manageEmployee_dialogAddressController;

    public void setController(EmployeeAddressDialogController _manageEmployee_dialogAddressController) {
        this._manageEmployee_dialogAddressController = _manageEmployee_dialogAddressController;
    }

    @FXML
    void getEmpInfo(MouseEvent event) {
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getEmployeeId();
            EmployeeDAO employeeDao = EmployeeDAO.getInstance();
            Employee employee = employeeDao.getById(idSelect).getFirst();
            employeeNameField.setText(employee.getName());
            employeeCitizenIdField.setText(employee.getCitizenIDNumber());
            employeePhoneField.setText(employee.getPhoneNumber());
            employeeEmailField.setText(employee.getEmail());
            employeeEmailField.setText(employee.getEmail());

            DecimalFormat moneyFormat = new DecimalFormat("#,###");
            //--Format
            String formattedHourlyPay = moneyFormat.format(employee.getHourlyPay());
            employeeHourlyPayField.setText(formattedHourlyPay);
            //---------------
            String formattedSalary = moneyFormat.format(employee.getSalary());
            employeeSalaryField.setText(formattedSalary);

            employeeAddressField.setText(employee.getAddress());
            if (employee.getHireDate() != null) {
                employeeHiredateDatePicker.setValue(employee.getHireDate());
            }
            employeeStatusComboBox.getSelectionModel().select(employee.getStatus());
            employeePositionComboBox.getSelectionModel().select(employee);
            if (employee.getBirthday() != null) {
                employeeBirthdayDatePicker.setValue(employee.getBirthday());
            }
            if (employee.isGender()) {
                genderGroup.selectToggle(maleRadioButton);
            } else {
                genderGroup.selectToggle(femaleRadioButton);
            }
            fireEmployeeButton.setVisible(true);
            employeeClearFormButton.setVisible(true);
            swapModeEmployeeButton.setVisible(true);
            handleActionEmployeeButton.setVisible(true);
            enableInput();
            utilsButton_1();
        }
    }

    @FXML
    void onClearSearchClickedEmpID(MouseEvent event) {
        employeeIdSearchField.setText("");
        employeeIdSearchField.requestFocus();
    }

    @FXML
    void onClearSearchClickedEmpName(MouseEvent event) {
        employeeNameSearchField.setText("");
        employeeNameSearchField.requestFocus();
    }

    @FXML
    void onClearSearchClickedEmpPhone(MouseEvent event) {
        employeePhoneSearchField.setText("");
        employeePhoneSearchField.requestFocus();
    }

    public void clearChooserImage() {
        employeeImageBytes = null;
        Image image = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_employee/user-256px.png"));
        employeeAvatar.setImage(image);
    }

    public void clear() {
        employeeNameField.setText("");
        employeeCitizenIdField.setText("");
        employeePhoneField.setText("");
        employeeEmailField.setText("");
        genderGroup.selectToggle(null);
        employeeBirthdayDatePicker.setValue(null);
        employeeHourlyPayField.setText("");
        employeeSalaryField.setText("");
        employeeAddressField.setText("");
        employeeHiredateDatePicker.setValue(null);
        employeeStatusComboBox.getSelectionModel().clearSelection();
        employeePositionComboBox.getSelectionModel().clearSelection();
        employeeTable.getSelectionModel().clearSelection();
        fireEmployeeButton.setVisible(false);
        clearChooserImage();
    }

    public void enableInput() {
        employeeNameField.setDisable(false);
        employeeCitizenIdField.setDisable(false);
        employeeEmailField.setDisable(false);
        employeePhoneField.setDisable(false);
        maleRadioButton.setDisable(false);
        femaleRadioButton.setDisable(false);
        employeeBirthdayDatePicker.setDisable(false);
        employeeHourlyPayField.setDisable(false);
        employeeSalaryField.setDisable(false);
        employeeAddressField.setDisable(false);
        employeeStatusComboBox.setDisable(false);
        employeePositionComboBox.setDisable(false);
        chooseImageButton.setDisable(false);
    }

    public void disableInput() {
        employeeNameField.setDisable(true);
        employeeCitizenIdField.setDisable(true);
        employeePhoneField.setDisable(true);
        employeeEmailField.setDisable(true);
        maleRadioButton.setDisable(true);
        femaleRadioButton.setDisable(true);
        employeeBirthdayDatePicker.setDisable(true);
        employeeHourlyPayField.setDisable(true);
        employeeSalaryField.setDisable(true);
        employeeAddressField.setDisable(true);
        employeeStatusComboBox.setDisable(true);
        employeePositionComboBox.setDisable(true);
        chooseImageButton.setDisable(true);
    }

    public void utilsButton_1() {
        swapModeEmployeeButton.setText("Thêm");
        handleActionEmployeeButton.setText("Sửa");
        swapModeEmployeeButton.setStyle("-fx-background-color:   #1D557E");
        handleActionEmployeeButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        swapModeEmployeeButton.setText("Sửa");
        handleActionEmployeeButton.setText("Thêm");
        swapModeEmployeeButton.setStyle("-fx-background-color:   #761D7E");
        handleActionEmployeeButton.setStyle("-fx-background-color:  #1D557E");
    }

    @FXML
    void swapModeEmployeeButton(ActionEvent event) {
        if (swapModeEmployeeButton.getText().equals("Sửa")) {
            clearChooserImage();
            utilsButton_1();
        } else if (swapModeEmployeeButton.getText().equals("Thêm")) {
            employeeClearFormButton.setVisible(true);
            fireEmployeeButton.setVisible(false);
            swapModeEmployeeButton.setVisible(false);
            handleActionEmployeeButton.setVisible(true);
            enableInput();
            utilsButton_2();
            clear();
            employeeStatusComboBox.setDisable(true);
        }
    }

    @FXML
    void handleActionEmployeeButton(ActionEvent event) {
        if (handleActionEmployeeButton.getText().equals("Sửa")) {
            //Lay ID cua table thuc hien chinh sua
            Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String idSelect = selectedItem.getEmployeeId();
                double workHours = selectedItem.getWorkHours();
                LocalDate hireDate = selectedItem.getHireDate();
                //
                String name = employeeNameField.getText();
                String citizenId = employeeCitizenIdField.getText();
                String phone = employeePhoneField.getText();
                String email = employeeEmailField.getText();
                boolean gender;
                if (maleRadioButton.isSelected()) {
                    gender = true;
                } else if (femaleRadioButton.isSelected()) {
                    gender = false;
                } else {
                    gender = false;
                }
                LocalDate birthDate = employeeBirthdayDatePicker.getValue();
                double hourPay = employeeHourlyPayField.getText().isEmpty() ? 0.0 : Double.parseDouble(employeeHourlyPayField.getText().replace(".", ""));
                double salary = employeeSalaryField.getText().isEmpty() ? 0.0 : Double.parseDouble(employeeSalaryField.getText().replace(".", ""));
                String address = employeeAddressField.getText();
                String status = employeeStatusComboBox.getValue();
                String position = employeePositionComboBox.getValue() != null ? employeePositionComboBox.getValue().getPosition() : null;
                EmployeeDAO employeeDao = EmployeeDAO.getInstance();
                Employee employee = new Employee(
                        idSelect, name, phone, citizenId, gender, address,
                        birthDate, email, status, hireDate, position, workHours, hourPay, salary, null
                );
                if (employeeDao.updateEmployeeInfo(employee)) {
                    System.out.println("Đã câp nhạt thành công");
                } else {
                    System.out.println("Cập nhật nhân viên không thành công");
                }
            }
        } else if (handleActionEmployeeButton.getText().equals("Thêm")) {
            String name = employeeNameField.getText();
            String citizenId = employeeCitizenIdField.getText();
            String phone = employeePhoneField.getText();
            String email = employeeEmailField.getText();
            boolean gender;
            if (maleRadioButton.isSelected()) {
                gender = true;
            } else if (femaleRadioButton.isSelected()) {
                gender = false;
            } else {
                gender = false;
            }
            LocalDate birthDate = employeeBirthdayDatePicker.getValue();
            double hourPay = employeeHourlyPayField.getText().isEmpty() ? 0.0 : Double.parseDouble(employeeHourlyPayField.getText().replace(".", ""));
            double salary = employeeSalaryField.getText().isEmpty() ? 0.0 : Double.parseDouble(employeeSalaryField.getText().replace(".", ""));
            String address = employeeAddressField.getText();
            String position = employeePositionComboBox.getValue() != null
                    ? employeePositionComboBox.getValue().getPosition()
                    : employeePositionComboBox.getEditor().getText();
            double workHours = 0;
            EmployeeDAO employeeDao = EmployeeDAO.getInstance();
            Employee employee = new Employee(name, phone, citizenId,
                    gender, address, birthDate, email, position, workHours, hourPay, salary, null);
            if (employeeDao.add(employee)) {
                System.out.println("Thêm nhan vien thành công");
            } else {
                System.out.println("Thêm nhan vien không thành công");
            }
            clear();
        }
        disableInput();
        employeeTable.getItems().clear();
        setPromotionTableValue();
        employeeClearFormButton.setVisible(false);
        fireEmployeeButton.setVisible(false);
        swapModeEmployeeButton.setVisible(true);
        handleActionEmployeeButton.setVisible(false);
        utilsButton_1();
    }

    @FXML
    void employeeClearFormButton(ActionEvent event) {
        employeeClearFormButton.setVisible(false);
        fireEmployeeButton.setVisible(false);
        swapModeEmployeeButton.setVisible(true);
        handleActionEmployeeButton.setVisible(false);
        employeeTable.getSelectionModel().clearSelection();
        utilsButton_1();
        disableInput();
        clear();
    }

    @FXML
    void fireEmployeeButton(ActionEvent event) {
        //processing
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getEmployeeId();
            EmployeeDAO employeeDao = EmployeeDAO.getInstance();
            if (employeeDao.updateStatus(idSelect, "Nghỉ việc")) {
                System.out.println("Sa thải nhan vien thanh cong");
            } else {
                System.out.println("Sa thải nhan vien khong thanh cong");
            }
        }
        employeeTable.getItems().clear();
        setPromotionTableValue();
        employeeClearFormButton.fire();
        disableInput();
    }

    @FXML
    void searchEmployeeButton(ActionEvent event) {
        employeeTable.getItems().clear();
        String name = employeeNameSearchField.getText();
        String phone = employeePhoneSearchField.getText();
        String empID = employeeIdSearchField.getText();
        EmployeeDAO employeeDao = EmployeeDAO.getInstance();
        List<Employee> employeeList = employeeDao.getByCriteria(phone, name, empID);
        ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeeList);
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeGenderColumn.setCellValueFactory(cellData -> {
            boolean gender = cellData.getValue().isGender();
            String genderText = gender ? "Nam" : "Nữ";
            return new SimpleStringProperty(genderText);
        });
        employeePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeePositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        employeeTable.setItems(listEmployee);
    }

    @FXML
    void openInputAddress(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/EmployeeAddressDialog.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        EmployeeAddressDialogController employeeAddressDialogController = loader.getController();
        employeeAddressDialogController.setEmployeeManagementController(this);
        primaryStage.show();
    }

    @FXML
    void employeeHourlyPayField_onKeyReleased(KeyEvent event) {
        String input = employeeHourlyPayField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            employeeHourlyPayField.setText(formattedText);
            employeeHourlyPayField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            employeeHourlyPayField.setText(validInput.toString());
            employeeHourlyPayField.positionCaret(validInput.length());
        }
    }

    @FXML
    void employeeSalaryField_onKeyReleased(KeyEvent event) {
        String input = employeeSalaryField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            employeeSalaryField.setText(formattedText);
            employeeSalaryField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            employeeSalaryField.setText(validInput.toString());
            employeeSalaryField.positionCaret(validInput.length());
        }
    }

    @FXML
    void onImageChooserButtonClicked(ActionEvent event) {

    }
}