package com.huongbien.ui.controller;

import com.huongbien.bus.EmployeeBUS;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.entity.Employee;
import com.huongbien.utils.Converter;
import com.huongbien.utils.Pagination;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeManagementController implements Initializable {
    private static final String DEFAULT_IMAGE_PATH = "/com/huongbien/icon/mg_employee/user-256px.png";
    private static final String[] STATUS_OPTIONS = {"Đang làm", "Nghỉ phép", "Nghỉ việc"};
    private static final String[] SEARCH_METHODS = {"Còn làm việc", "Tất cả", "Tìm theo tên", "Tìm theo số điện thoại", "Tìm theo chức vụ"};
    private static final String ADD_MODE = "Thêm";
    private static final String EDIT_MODE = "Sửa";
    public TextField employeeWorkingHourField;

    @FXML
    private TableColumn<Employee, String> employeeGenderColumn;
    @FXML
    private TableColumn<Employee, String> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> employeeNameColumn;
    @FXML
    private TableColumn<Employee, String> employeePhoneColumn;
    @FXML
    private TableColumn<Employee, String> employeePositionColumn;
    @FXML
    private TableColumn<Employee, String> employeeStatusColumn;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private ComboBox<String> employeeStatusComboBox;
    @FXML
    private ComboBox<Employee> employeePositionComboBox;
    @FXML
    private DatePicker employeeBirthdayDatePicker;
    @FXML
    private DatePicker employeeHiredateDatePicker;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    public TextField employeeAddressField;
    @FXML
    private TextField employeeCitizenIdField;
    @FXML
    private ComboBox<Employee> employeeManagerIdComboBox;
    @FXML
    private TextField employeeEmailField;
    @FXML
    private TextField employeeNameField;
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField employeePhoneField;
    @FXML
    private TextField employeeSalaryField;
    @FXML
    private TextField employeeHourlyPayField;
    @FXML
    private ImageView clearSearchButton;
    @FXML
    private ComboBox<String> searchMethodComboBox;
    @FXML
    private TextField employeeSearchField;
    @FXML
    public Label pageIndexLabel;
    @FXML
    private Button searchEmployeeButton;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private Button employeeClearFormButton;
    @FXML
    private Button fireEmployeeButton;
    @FXML
    private Button handleActionEmployeeButton;
    @FXML
    private Button swapModeEmployeeButton;
    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView employeeAvatar;

    private byte[] employeeImageBytes = null;

    private Pagination<Employee> employeePagination;

    private final EmployeeBUS employeeBUS = new EmployeeBUS();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearSearchButton.setVisible(false);
        employeeSearchField.setDisable(true);
        setComboBoxValue();
        changeHandleActionButtonToAddEmployee();
        setEmployeePaginationGetStillWorking();
        setEmployeeTable();
        setEmployeeTableValue();
    }

    public void setEmployeePaginationGetAll() {
        int itemPerPage = 10;
        int totalItem = employeeBUS.countAllEmployees();
        boolean isRollBack = false;
        employeePagination = new Pagination<>(
                employeeBUS::getAllEmployeesWithPagination,
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setEmployeePaginationGetStillWorking() {
        int itemPerPage = 10;
        int totalItem = employeeBUS.countStillWorkingEmployees();
        boolean isRollBack = false;
        employeePagination = new Pagination<>(
                employeeBUS::getStillWorkingEmployeesWithPagination,
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setEmployeePaginationGetByPhone(String phoneNumber) {
        int itemPerPage = 10;
        int totalItem = employeeBUS.countEmployeesByPhoneNumber(phoneNumber);
        boolean isRollBack = false;
        employeePagination = new Pagination<>(
                (offset, limit) -> employeeBUS.getEmployeesByPhoneNumberWithPagination(phoneNumber, offset, limit),
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setEmployeePaginationGetByName(String name) {
        int itemPerPage = 10;
        int totalItem = employeeBUS.countEmployeesByName(name);
        boolean isRollBack = false;
        employeePagination = new Pagination<>(
                (offset, limit) -> employeeBUS.getEmployeesByNameWithPagination(name, offset, limit),
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setEmployeePaginationGetByPosition(String name) {
        int itemPerPage = 10;
        int totalItem = employeeBUS.countEmployeesByPosition(name);
        boolean isRollBack = false;
        employeePagination = new Pagination<>(
                (offset, limit) -> employeeBUS.getEmployeesByPositionWithPagination(name, offset, limit),
                itemPerPage,
                totalItem,
                isRollBack
        );
    }

    public void setEmployeeTable() {
        employeeTable.setPlaceholder(new Label("Không có dữ liệu"));

        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeGenderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender() ? "Nam" : "Nữ"));
        employeePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeePositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setEmployeeTableValue() {
        employeeTable.getItems().clear();
        setPageIndexLabel();
        ObservableList<Employee> listEmployee = FXCollections.observableArrayList(employeePagination.getCurrentPage());
        employeeTable.setItems(listEmployee);
    }

    public void setPageIndexLabel() {
        pageIndexLabel.setText(employeePagination.getCurrentPageIndex() + "/" + employeePagination.getTotalPages());
    }

    public void setComboBoxValue() {
        setStatusComboBox();
        setPositionComboBox();
        setSearchMethodComboBox();
        setEmployeeManagerIdComboBox();
    }

    public void setEmployeeManagerIdComboBox() {
    List<Employee> managers = employeeBUS.getEmployeeByPosition("Quản lý");
    employeeManagerIdComboBox.getItems().add(null);
    employeeManagerIdComboBox.getItems().addAll(managers);
    employeeManagerIdComboBox.setConverter(new StringConverter<>() {
        @Override
        public String toString(Employee employee) {
            return employee != null ? employee.getEmployeeId() : "Không có";
        }

        @Override
        public Employee fromString(String string) {
            return employeeManagerIdComboBox.getItems().stream()
                    .filter(item -> item.getEmployeeId().equals(string))
                    .findFirst()
                    .orElse(null);
        }
    });
}

    public void setSearchMethodComboBox() {
        ObservableList<String> searchMethodList = FXCollections.observableArrayList(SEARCH_METHODS);
        searchMethodComboBox.setItems(searchMethodList);
        searchMethodComboBox.setValue(SEARCH_METHODS[0]);
    }

    public void setStatusComboBox() {
        ObservableList<String> statusList = FXCollections.observableArrayList(STATUS_OPTIONS);
        employeeStatusComboBox.setItems(statusList);
        employeeStatusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return employeeStatusComboBox.getItems().stream().filter(item -> item.equals(string)).findFirst().orElse(null);
            }
        });
        employeeStatusComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setDisable(item != null && item.equals("Nghỉ việc"));
            }
        });
    }

    public void setPositionComboBox() {
        EmployeeDAO employeeDao = EmployeeDAO.getInstance();
        List<Employee> employeeList = employeeDao.getAll();
        List<Employee> distinctEmployees = new ArrayList<>(employeeList.stream()
                .filter(e -> e.getPosition() != null && !"Quản lý".equals(e.getPosition()))
                .collect(Collectors.toMap(Employee::getPosition, e -> e, (e1, e2) -> e1))
                .values());
        ObservableList<Employee> employees = FXCollections.observableArrayList(distinctEmployees);
        employeePositionComboBox.setItems(employees);
        employeePositionComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Employee employee) {
                return employee != null ? employee.getPosition() : "";
            }

            @Override
            public Employee fromString(String string) {
                return employeePositionComboBox.getItems().stream().filter(item -> item.getPosition().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void clearChooserImage() {
        employeeImageBytes = null;
        employeeAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH))));
    }

    public void clearEmployeeForm() {
        employeeIdField.clear();
        employeeNameField.clear();
        employeeCitizenIdField.clear();
        employeePhoneField.clear();
        employeeEmailField.clear();
        genderGroup.selectToggle(null);
        employeeManagerIdComboBox.getSelectionModel().clearSelection();
        employeeBirthdayDatePicker.setValue(null);
        employeeHourlyPayField.clear();
        employeeSalaryField.clear();
        employeeAddressField.clear();
        employeeHiredateDatePicker.setValue(null);
        employeeWorkingHourField.clear();
        employeeStatusComboBox.getSelectionModel().clearSelection();
        employeePositionComboBox.getSelectionModel().clearSelection();
        employeeTable.getSelectionModel().clearSelection();
        fireEmployeeButton.setVisible(false);
        clearChooserImage();
    }

    public void enableInput() {
        setInputFieldsDisable(false);
    }

    public void disableInput() {
        setInputFieldsDisable(true);
    }

    public void setInputFieldsDisable(boolean disable) {
        employeeNameField.setDisable(disable);
        employeeCitizenIdField.setDisable(disable);
        employeePhoneField.setDisable(disable);
        employeeEmailField.setDisable(disable);
        maleRadioButton.setDisable(disable);
        femaleRadioButton.setDisable(disable);
        employeeWorkingHourField.setDisable(disable);
        employeeEmailField.setDisable(disable);
        employeeBirthdayDatePicker.setDisable(disable);
        employeeHourlyPayField.setDisable(disable);
        employeeHiredateDatePicker.setDisable(disable);
        employeeSalaryField.setDisable(disable);
        employeeAddressField.setDisable(disable);
        employeeManagerIdComboBox.setDisable(disable);
        employeeStatusComboBox.setDisable(disable);
        employeePositionComboBox.setDisable(disable);
        chooseImageButton.setDisable(disable);
    }

    public void changeHandleActionButtonToUpdateEmployee() {
        fireEmployeeButton.setVisible(true);
        setButtonMode(ADD_MODE, EDIT_MODE, "#1D557E", "#761D7E");
    }

    public void changeHandleActionButtonToAddEmployee() {
        fireEmployeeButton.setVisible(false);
        setButtonMode(EDIT_MODE, ADD_MODE, "#761D7E", "#1D557E");
    }

    public void setButtonMode(String swapModeText, String handleActionText, String swapModeColor, String handleActionColor) {
        swapModeEmployeeButton.setText(swapModeText);
        handleActionEmployeeButton.setText(handleActionText);
        swapModeEmployeeButton.setStyle("-fx-background-color: " + swapModeColor);
        handleActionEmployeeButton.setStyle("-fx-background-color: " + handleActionColor);
    }

    @FXML
    public void onEmployeeTableClicked(MouseEvent event) {
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            populateEmployeeForm(selectedItem);
            changeHandleActionButtonToUpdateEmployee();
        }
    }

    public void populateEmployeeForm(Employee employee) {
        if (employee.getProfileImage() == null || employee.getProfileImage().length == 0) {
            employeeAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH))));
        } else {
            employeeAvatar.setImage(new Image(Converter.bytesToInputStream(employee.getProfileImage())));
        }
        employeeIdField.setText(employee.getEmployeeId());
        employeeNameField.setText(employee.getName());
        employeeCitizenIdField.setText(employee.getCitizenIDNumber());
        employeePhoneField.setText(employee.getPhoneNumber());
        employeeEmailField.setText(employee.getEmail());
        employeeManagerIdComboBox.setValue(employee.getManager());
        employeeHourlyPayField.setText(formatMoney(employee.getHourlyPay()));
        employeeSalaryField.setText(formatMoney(employee.getSalary()));
        employeeAddressField.setText(employee.getAddress());
        employeeWorkingHourField.setText(formatMoney(employee.getWorkHours()));
        employeeHiredateDatePicker.setValue(employee.getHireDate());
        employeeStatusComboBox.getSelectionModel().select(employee.getStatus());
        employeePositionComboBox.getSelectionModel().select(employee);
        employeeBirthdayDatePicker.setValue(employee.getBirthday());
        genderGroup.selectToggle(employee.getGender() ? maleRadioButton : femaleRadioButton);
    }

    public String formatMoney(double amount) {
        return new DecimalFormat("#,###").format(amount);
    }

    @FXML
    public void onClearSearchButtonClicked(MouseEvent event) {
        clearSearchField(employeeSearchField);
        clearSearchButton.setVisible(false);
    }

    public void clearSearchField(TextField searchField) {
        searchField.clear();
        searchField.requestFocus();
    }

    @FXML
    public void onSwapModeEmployeeButtonClicked(MouseEvent event) {
        if (swapModeEmployeeButton.getText().equals(EDIT_MODE)) {
            disableInput();
            clearChooserImage();
            changeHandleActionButtonToUpdateEmployee();
        } else if (swapModeEmployeeButton.getText().equals(ADD_MODE)) {
            enableInput();
            clearChooserImage();
            clearEmployeeForm();
            changeHandleActionButtonToAddEmployee();
        }
    }

    @FXML
    public void onHandleActionEmployeeButtonClicked(ActionEvent event) {
        if (handleActionEmployeeButton.getText().equals(EDIT_MODE)) {
            updateEmployee();
        } else if (handleActionEmployeeButton.getText().equals(ADD_MODE)) {
            addEmployee();
        }
        clearEmployeeForm();
    }

    public boolean validateEmployeeInfo() {
        if (employeeNameField.getText().isEmpty()) {
            employeeNameField.setStyle("-fx-border-color: red");
            return false;
        } else {
            employeeNameField.setStyle(null);
        }
        if (employeePhoneField.getText().isEmpty()) {
            employeePhoneField.setStyle("-fx-border-color: red");
            return false;
        } else {
            employeePhoneField.setStyle(null);
        }
        if (employeePositionComboBox.getValue() == null) {
            employeePositionComboBox.setStyle("-fx-border-color: red");
            return false;
        } else {
            employeePositionComboBox.setStyle(null);
        }
        if (employeeStatusComboBox.getValue() == null) {
            employeeStatusComboBox.setStyle("-fx-border-color: red");
            return false;
        } else {
            employeeStatusComboBox.setStyle(null);
        }
//
        return true;
    }

    public void updateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) return;
        if (!validateEmployeeInfo()) return;

        if (employeeImageBytes == null) {
            employeeImageBytes = selectedEmployee.getProfileImage();
        }
        Employee updatedEmployee = createEmployeeFromForm(selectedEmployee.getEmployeeId(), selectedEmployee.getWorkHours(), selectedEmployee.getHireDate(), employeeImageBytes);
        if (employeeBUS.updateEmployeeInfo(updatedEmployee)) {
            setEmployeeTableValue();
            System.out.println("Đã câp nhạt thành công");
        } else {
            System.out.println("Cập nhật nhân viên không thành công");
        }
    }

    public void addEmployee() {
        Employee newEmployee = createEmployeeFromForm(null, 0, LocalDate.now(), employeeImageBytes);
        if (employeeBUS.addEmployee(newEmployee)) {
            clearEmployeeForm();
            setEmployeeTableValue();
            System.out.println("Thêm nhan vien thành công");
        } else {
            System.out.println("Thêm nhan vien không thành công");
        }
    }

    public Employee createEmployeeFromForm(String employeeId, double workHours, LocalDate hireDate, byte[] profileImage) {
        String name = employeeNameField.getText();
        String citizenId = employeeCitizenIdField.getText();
        String phone = employeePhoneField.getText();
        String email = employeeEmailField.getText();
        boolean gender = maleRadioButton.isSelected();
        LocalDate birthDate = employeeBirthdayDatePicker.getValue();
        double hourPay = parseMoney(employeeHourlyPayField.getText());
        double salary = parseMoney(employeeSalaryField.getText());
        Employee managerId = employeeManagerIdComboBox.getValue();
        String address = employeeAddressField.getText();
        String status = employeeStatusComboBox.getValue();
        String position = employeePositionComboBox.getValue() != null ? employeePositionComboBox.getValue().getPosition() : employeePositionComboBox.getEditor().getText();
        return new Employee(employeeId, name, phone, citizenId, gender, address, birthDate, email, status, hireDate, position, workHours, hourPay, salary, managerId, profileImage);
    }

//    TODO move to Utils
    public double parseMoney(String text) {
        return text.isEmpty() ? 0.0 : Double.parseDouble(text.replace(",", ""));
    }

    @FXML
    public void onEmployeeClearFormButtonClicked(ActionEvent event) {
        clearEmployeeForm();
    }

    @FXML
    public void onFireEmployeeButtonClicked(ActionEvent event) {
        Employee selectedItem = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        if (employeeBUS.updateEmployeeStatus(selectedItem.getEmployeeId(), "Nghỉ việc")) {
            System.out.println("Sa thải nhan vien thanh cong");
        } else {
            System.out.println("Sa thải nhan vien khong thanh cong");
        }
        setEmployeeTableValue();
        clearEmployeeForm();
    }

    @FXML
    public void onSearchEmployeeButtonClicked(ActionEvent event) {
        String selectedMethod = searchMethodComboBox.getValue();
        String searchValue = employeeSearchField.getText();
        switch (selectedMethod) {
            case "Tìm theo tên" -> setEmployeePaginationGetByName(searchValue);
            case "Tìm theo số điện thoại" -> setEmployeePaginationGetByPhone(searchValue);
            case "Tìm theo chức vụ" -> setEmployeePaginationGetByPosition(searchValue);
        }

        setEmployeeTableValue();
    }

    @FXML
    public void onOpenAddressEditingDialogButtonClicked(MouseEvent event) throws IOException {
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
    public void onEmployeeHourlyPayFieldKeyReleased(KeyEvent event) {
        formatMoneyField(employeeHourlyPayField);
    }

    @FXML
    public void onEmployeeSalaryFieldKeyReleased(KeyEvent event) {
        formatMoneyField(employeeSalaryField);
    }

    public void formatMoneyField(TextField moneyField) {
        String input = moneyField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            String formattedText = NumberFormat.getInstance().format(Long.parseLong(input));
            moneyField.setText(formattedText);
            moneyField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            moneyField.setText(validInput.toString());
            moneyField.positionCaret(validInput.length());
        }
    }

    @FXML
    public void onImageChooserButtonClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                employeeImageBytes = Converter.fileToBytes(selectedFile);
                Image image = new Image(Converter.bytesToInputStream(employeeImageBytes));
                employeeAvatar.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onEmployeeSearchFieldKeyReleased(KeyEvent keyEvent) {
        String searchValue = employeeSearchField.getText();
        clearSearchButton.setVisible(!searchValue.isEmpty());
    }

    @FXML
    public void onSearchMethodComboBoxSelected(ActionEvent actionEvent) {
        String selectedMethod = searchMethodComboBox.getValue();
        switch (selectedMethod) {
            case "Còn làm việc":
                employeeSearchField.setDisable(true);
                setEmployeePaginationGetStillWorking();
                break;
            case "Tất cả":
                employeeSearchField.setDisable(true);
                setEmployeePaginationGetAll();
                break;
            case "Tìm theo tên", "Tìm theo số điện thoại", "Tìm theo chức vụ":
                employeeSearchField.setDisable(false);
                break;
        }
    }

    @FXML
    public void onFirstPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToFirstPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onPrevPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToPreviousPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onNextPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToNextPage();
        setEmployeeTableValue();
    }

    @FXML
    public void onLastPageButtonClicked(MouseEvent mouseEvent) {
        employeePagination.goToLastPage();
        setEmployeeTableValue();
    }
}