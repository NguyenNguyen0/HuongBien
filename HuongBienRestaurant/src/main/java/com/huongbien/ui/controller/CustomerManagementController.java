package com.huongbien.ui.controller;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.entity.Customer;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomerManagementController implements Initializable {
    @FXML
    public Button createCustomerQRButton;
    @FXML
    private Button clearCustomerFormButton;

    @FXML
    private Button handleActionCustomerButton;

    @FXML
    private Button swapModeCustomerButton;

    @FXML
    private DatePicker customerBirthdayDatePicker;

    @FXML
    private DatePicker registrationDateDatePicker;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TableColumn<Customer, Integer> customerAccumulatedPointColumn;

    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerGenderColumn;

    @FXML
    private TableColumn<Customer, String> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> customerMembershipLevelColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TextField customerAccumulatedPointsField;

    @FXML
    private TextField customerAddressField;

    @FXML
    private TextField customerEmailField;

    @FXML
    private TextField customerMembershipLevelField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerPhoneField;

    @FXML
    private TextField searchCustomerPhoneField;

    private Utils utils;

    private void setCustomerTableValues() {
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        List<Customer> customerList = customerDAO.getAll();
        ObservableList<Customer> listCustomer = FXCollections.observableArrayList(customerList);

        customerAddressField.setEditable(true);
        customerNameField.setEditable(true);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerGenderColumn.setCellValueFactory(cellData -> {
            boolean gender = cellData.getValue().isGender();
            String genderText = gender ? "Nam" : "Nữ";
            return new SimpleStringProperty(genderText);
        });
        customerMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAccumulatedPointColumn.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        customerTable.setItems(listCustomer);
    }


    public void clearCustomerForm() {
        customerNameField.setText("");
        customerPhoneField.setText("");
        customerEmailField.setText("");
        customerBirthdayDatePicker.setValue(null);
        genderGroup.selectToggle(null);
        registrationDateDatePicker.setValue(null);
        customerAddressField.setText("");
        customerMembershipLevelField.setText("");
        customerAccumulatedPointsField.setText("");
        customerTable.getSelectionModel().clearSelection();
    }

    public void enableInput() {
        customerNameField.setDisable(false);
        customerEmailField.setDisable(false);
        customerAddressField.setDisable(false);
        customerPhoneField.setDisable(false);
        customerBirthdayDatePicker.setDisable(false);
    }

    public void disableInput() {
        customerNameField.setDisable(true);
        customerEmailField.setDisable(true);
        customerAddressField.setDisable(true);
        customerPhoneField.setDisable(true);
        customerBirthdayDatePicker.setDisable(true);

    }

    public void setHandleActionButtonToEditCustomer() {
        swapModeCustomerButton.setText("Thêm");
        handleActionCustomerButton.setText("Sửa");
        swapModeCustomerButton.setStyle("-fx-background-color:   #1D557E");
        handleActionCustomerButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void setHandleActionButtonToAddCustomer() {
        swapModeCustomerButton.setText("Sửa");
        handleActionCustomerButton.setText("Thêm");
        swapModeCustomerButton.setStyle("-fx-background-color:   #761D7E");
        handleActionCustomerButton.setStyle("-fx-background-color:  #1D557E");
    }

    public boolean validateCustomerData() {
        if (customerNameField.getText().trim().isEmpty()) {
            return false;
        }
        if (!customerPhoneField.getText().trim().isEmpty()) {
            CustomerDAO customerDAO = CustomerDAO.getInstance();
            List<String> customerList = customerDAO.getPhoneNumber();
            for (String phone : customerList) {
                if (customerPhoneField.getText().equals(phone)) {
                    System.out.println("Số điện thoại đã được đăng kí thành viên");
                    return false;
                }
            }
        } else {
            return false;
        }
        if (customerBirthdayDatePicker.getValue() == null) {
            return false;
        }
        return genderGroup.getSelectedToggle() != null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCustomerTableValues();
    }

    @FXML
    void onClearSearchFieldButtonClicked(MouseEvent event) {
        searchCustomerPhoneField.setText("");
        setCustomerTableValues();
    }

    @FXML
    void onClearCustomerFormButtonClicked(ActionEvent event) {
        clearCustomerForm();
        customerNameField.requestFocus();
    }

    @FXML
    void onCustomerHandleActionButtonClicked(ActionEvent event) {
        if (handleActionCustomerButton.getText().equals("Thêm")) {
            enableInput();
            Customer customer = null;
            boolean gender = true;
            if (validateCustomerData()) {
                String name = customerNameField.getText();
                String phone = customerPhoneField.getText();
                String email = customerEmailField.getText();
                String address = customerAddressField.getText();
                LocalDate birthday = customerBirthdayDatePicker.getValue();
                if (femaleRadioButton.isSelected()) {
                    gender = false;
                }
                customer = new Customer(name, address, gender, phone, email, birthday);
                CustomerDAO customerDAO = CustomerDAO.getInstance();
                if (customerDAO.add(customer)) {
                    setCustomerTableValues();
                }
            }
            clearCustomerForm();
        } else if (handleActionCustomerButton.getText().equals("Sửa")) {
            enableInput();
            Customer customer = null;
            boolean gender = true;
            if (validateCustomerData()) {
                String name = customerNameField.getText();
                String phone = customerPhoneField.getText();
                String email = customerEmailField.getText();
                String address = customerAddressField.getText();
                LocalDate birthday = customerBirthdayDatePicker.getValue();
                if (femaleRadioButton.isSelected()) {
                    gender = false;
                }
                String id = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
                CustomerDAO customerDAO = CustomerDAO.getInstance();
                customer = customerDAO.getById(id);
                customer.setName(name);
                customer.setEmail(email);
                customer.setPhoneNumber(phone);
                customer.setAddress(address);
                customer.setGender(gender);
                if (customerDAO.updateCustomerInfo(customer)) {
                    customerTable.getItems().clear();
                    setCustomerTableValues();
                }
            }
            clearCustomerForm();
        }
    }

    @FXML
    void onCustomerSwapModeButtonClicked(ActionEvent event) {
        if (handleActionCustomerButton.getText().equals("Thêm")) {
            setHandleActionButtonToEditCustomer();
            disableInput();
        } else {
            setHandleActionButtonToAddCustomer();
            clearCustomerForm();
            enableInput();
        }
    }

    @FXML
    void onCustomerTableClicked(MouseEvent event) {
        setHandleActionButtonToEditCustomer();
        Customer selectedItem = customerTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            customerNameField.setText(selectedItem.getName());
            customerPhoneField.setText(selectedItem.getPhoneNumber());
            customerAddressField.setText(selectedItem.getAddress());
            customerEmailField.setText(selectedItem.getEmail());
            customerAccumulatedPointsField.setText(selectedItem.getAccumulatedPoints() + "");
            customerMembershipLevelField.setText(Utils.toStringMembershipLevel(selectedItem.getMembershipLevel()));
            customerBirthdayDatePicker.setValue(selectedItem.getBirthday());
            registrationDateDatePicker.setValue(selectedItem.getRegistrationDate());

            if (selectedItem.isGender()) {
                genderGroup.selectToggle(maleRadioButton);
            } else {
                genderGroup.selectToggle(femaleRadioButton);
            }
        }

    }

    @FXML
    void onSearchCustomerPhoneFieldKeyReleased(KeyEvent event) {
        String phone = searchCustomerPhoneField.getText();
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        List<Customer> customerList = customerDAO.getByPhoneNumber(phone);
        ObservableList<Customer> listCustomer = FXCollections.observableArrayList(customerList);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerGenderColumn.setCellValueFactory(cellData -> {
            boolean gender = cellData.getValue().isGender();
            String genderText = gender ? "Nam" : "Nữ";
            return new SimpleStringProperty(genderText);
        });
        customerMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAccumulatedPointColumn.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        customerTable.setItems(listCustomer);
    }

    @FXML
    void onCreateCustomerQRButton(ActionEvent actionEvent) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            String qrContent = selectedCustomer.getCustomerId() + ","
                    + selectedCustomer.getName() + ","
                    + selectedCustomer.getMembershipLevel() + ","
                    + selectedCustomer.getPhoneNumber();

            createQRCode(selectedCustomer, qrContent);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một khách hàng để tạo mã QR!");
            alert.showAndWait();
        }
    }

    private void createQRCode(Customer selectedCustomer, String qrContent) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 400, 400, hints);

            String customerId = selectedCustomer.getCustomerId();
            String outputFile = "src/main/resources/com/huongbien/img/qr/QrCode_MaKH" + customerId + ".png";
            Path path = Paths.get(outputFile);

            Files.createDirectories(path.getParent());

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "QR code được tạo thành công tại: " + outputFile);
            alert.showAndWait();
        } catch (WriterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi viết mã QR: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi ghi tệp: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi không xác định: " + e.getMessage());
            alert.showAndWait();
        }
    }
}