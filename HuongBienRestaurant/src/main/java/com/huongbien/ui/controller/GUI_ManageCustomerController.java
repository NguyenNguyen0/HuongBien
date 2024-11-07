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
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        List<Customer> customerList = customerDAO.getAll();
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
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        tabCol_customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tabCol_customerAccumulatedPoint.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        tabViewCustomer.setItems(listCustomer);
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

    public boolean checkData() {
        if (txt_customerName.getText().trim().isEmpty()) {
            return false;
        }
        if (!txt_customerPhone.getText().trim().isEmpty()) {
            CustomerDAO customerDAO = CustomerDAO.getInstance();
            List<String> customerList = customerDAO.getPhoneNumber();
            for (String phone : customerList) {
                if (txt_customerPhone.getText().equals(phone)) {
                    System.out.println("Số điện thoại đã được đăng kí thành viên");
                    return false;
                }
            }
        } else {
            return false;
        }
        if (date_customerBirthDate.getValue() == null) {
            return false;
        }
        return genderGroup.getSelectedToggle() != null;
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
        if (btn_customerMain.getText().equals("Thêm")) {
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
                CustomerDAO customerDAO = CustomerDAO.getInstance();
                if (customerDAO.add(customer)) {
                    setCellValues();
                }
            }
            clear();
        } else if (btn_customerMain.getText().equals("Sửa")) {
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
                String id = tabViewCustomer.getSelectionModel().getSelectedItem().getCustomerId();
                CustomerDAO customerDAO = CustomerDAO.getInstance();
                customer = customerDAO.getById(id);
                customer.setName(name);
                customer.setEmail(email);
                customer.setPhoneNumber(phone);
                customer.setAddress(address);
                customer.setGender(gender);
                if (customerDAO.updateCustomerInfo(customer)) {
                    tabViewCustomer.getItems().clear();
                    setCellValues();
                }
            }
            clear();
        }
    }

    @FXML
    void btn_customerSub(ActionEvent event) {
        if (btn_customerMain.getText().equals("Thêm")) {
            utilsButton_1();
            disableInput();
        } else {
            utilsButton_2();
            clear();
            enableInput();
        }
    }

    @FXML
    void getCustomerInfo(MouseEvent event) {
        utilsButton_1();
        Customer selectedItem = tabViewCustomer.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            txt_customerName.setText(selectedItem.getName());
            txt_customerPhone.setText(selectedItem.getPhoneNumber());
            txt_customerAddress.setText(selectedItem.getAddress());
            txt_customerEmail.setText(selectedItem.getEmail());
            txt_customerAccumulatedPoints.setText(selectedItem.getAccumulatedPoints() + "");
            txt_customerMembershipLevel.setText(Utils.toStringMembershipLevel(selectedItem.getMembershipLevel()));
            date_customerBirthDate.setValue(selectedItem.getBirthday());
            date_registrationDate.setValue(selectedItem.getRegistrationDate());

            if (selectedItem.isGender()) {
                genderGroup.selectToggle(radio_customerMale);
            } else {
                genderGroup.selectToggle(radio_customerFemale);
            }
        }

    }

    @FXML
    void txt_searchCustomerPhone(KeyEvent event) {
        String phone = txt_searchCustomerPhone.getText();
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        List<Customer> customerList = customerDAO.getByPhoneNumber(phone);
        ObservableList<Customer> listCustomer = FXCollections.observableArrayList(customerList);

        tabCol_customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tabCol_customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabCol_customerGender.setCellValueFactory(cellData -> {
            boolean gender = cellData.getValue().isGender();
            String genderText = gender ? "Nam" : "Nữ";
            return new SimpleStringProperty(genderText);
        });
        tabCol_customerMemLevel.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        tabCol_customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tabCol_customerAccumulatedPoint.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        tabViewCustomer.setItems(listCustomer);
    }

    @FXML
    public void btn_customerQR(ActionEvent actionEvent) {
        Customer selectedCustomer = tabViewCustomer.getSelectionModel().getSelectedItem();

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