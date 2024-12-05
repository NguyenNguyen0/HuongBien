package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.config.Variable;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Table;
import com.huongbien.utils.ToastsMessage;
import com.huongbien.utils.Utils;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PreOrderController implements Initializable {
    @FXML private TextField numOfAttendeesField;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private TextField tableInfoField;
    @FXML private Label tableAmountLabel;
    @FXML private Label cuisineAmountLabel;
    @FXML private Label totalAmoutLabel;
    @FXML private Label preOrderCuisineLabel;
    @FXML private DatePicker preOrderDatePicker;
    @FXML private ComboBox<String> preOrderPartyTypeComboBox;
    @FXML private TextField phoneNumField;
    @FXML private TextField emailField;
    @FXML private TextField cusIDField;
    @FXML private TextField nameField;
    @FXML private TextField noteField;

    //Controller area
    public RestaurantMainController restaurantMainController;
    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }
    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTimeComboBox();
        try {
            setInfoFromJSON();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //function area
    private void setTimeComboBox() {
        hourComboBox.getItems().clear();
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        hourComboBox.setValue("23");
        minuteComboBox.getItems().clear();
        for (int i = 0; i < 60; i+=5) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
        minuteComboBox.setValue("55");
    }

    private void setInfoFromJSON() throws FileNotFoundException {
        //Set Current date
        preOrderDatePicker.setValue(LocalDate.now());
        //get table info from json file
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        StringBuilder tableInfoBuilder = new StringBuilder();
        double tableAmount = 0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            if (table != null) {
                //set table text
                String floorStr = (table.getFloor() == 0) ? "Tầng trệt" : "Tầng " + table.getFloor();
                tableInfoBuilder.append(table.getName()).append(" (").append(floorStr).append("), ");
            } else {
                tableInfoBuilder.append("Thông tin bàn không xác định, ");
            }
            //calculate table fee
            assert table != null;
            tableAmount += (table.getTableType().getTableId().equals(Variable.tableVipID)) ? Variable.tablePrice : 0;
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        int cuisineQuantity = 0;
        double cuisineAmount = 0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            cuisineQuantity += quantity;
            cuisineAmount += money;
        }
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Customer ID").getAsString();
            Customer customer = CustomerDAO.getInstance().getById(id);
            if (customer != null) {
                cusIDField.setText(customer.getCustomerId());
                nameField.setText(customer.getName());
                phoneNumField.setText(customer.getPhoneNumber());
                emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
            } else {
                cusIDField.setText("");
                nameField.setText("");
                phoneNumField.setText("");
                emailField.setText("");
            }
        }
        tableInfoField.setText(tableInfoBuilder.toString());
        preOrderCuisineLabel.setText(cuisineQuantity + " món");
        //Set Type Party
        ObservableList<String> partyTypes = FXCollections.observableArrayList(Variable.partyTypesArray);
        preOrderPartyTypeComboBox.setItems(partyTypes);
        preOrderPartyTypeComboBox.getSelectionModel().selectLast();
        //setLabel
        tableAmountLabel.setText(String.format("%,.0f VNĐ", tableAmount));
        cuisineAmountLabel.setText(String.format("%,.0f VNĐ", cuisineAmount));
        totalAmoutLabel.setText(String.format("%,.0f VNĐ", tableAmount + cuisineAmount));
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onReservationManagementButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openReservationManagement();
    }

    @FXML
    void onDecreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        if (currentValue > 1) {
            numOfAttendeesField.setText(String.valueOf(currentValue - 1));
        }
    }

    @FXML
    void onIncreaseButtonAction(ActionEvent event) {
        int currentValue = Integer.parseInt(numOfAttendeesField.getText());
        numOfAttendeesField.setText(String.valueOf(currentValue + 1));
    }

    @FXML
    void onEditTableButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openOrderTable();
    }

    @FXML
    void onPreOrderCuisineButtonAction(ActionEvent event) throws IOException {
        restaurantMainController.openPreOrderCuisine();
    }

    @FXML
    void onSearchCustomerExisKeyType(KeyEvent event) {
        String phone = phoneNumField.getText();
        if (!phone.matches("\\d*")) {
            phoneNumField.setText(phone.replaceAll("[^\\d]", ""));
            phoneNumField.positionCaret(phoneNumField.getText().length());
            ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Chỉ nhập số, vui lòng không nhập ký tự khác");
            return;
        }

        if(phone.length() > 10) {
            ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Số điện thoại sai định dạng, phải bao gồm 10 số");
            phoneNumField.setText(phone.substring(0, 10));
            phoneNumField.positionCaret(10);
            return;
        }

        if (phone.length() < 10) {
            cusIDField.setText("");
            nameField.setText("");
            emailField.setText("");
        } else {
            Customer customer = CustomerDAO.getInstance().getByOnePhoneNumber(phone);
            if(customer != null) {
                cusIDField.setText(customer.getCustomerId());
                nameField.setText(customer.getName());
                emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
            } else {
                ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Không tìm thấy khách hàng, nhập tên khách hàng để đăng ký mới");
            }
        }
    }

    @FXML
    void onSavePreOrderTableButtonAction(ActionEvent event) throws IOException {
        //TODO: chưa ràng buộc regex (biểu thức chính quy) cho số điện thoại và email
        if(phoneNumField.getText().isEmpty()) {
            ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Vui lòng nhập số điện thoại để kiểm tra khách hàng");
            return;
        }

        if(phoneNumField.getText().length() != 10) {
            ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Số điện thoại không hợp lệ, phải bao gồm 10 số");
            return;
        }

        if(nameField.getText().isEmpty() || phoneNumField.getText().isEmpty()) {
            ToastsMessage.showToastsMessage("Thông báo", "Vui lòng nhập đầy đủ thông tin khách hàng để thực hiện đăng ký");
            return;
        }
        if(cusIDField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Thông báo");
            alert.setContentText("Không tìm thấy khách hàng trong hệ thống, bạn có muốn đăng ký khách hàng mới không?");
            ButtonType btn_ok = new ButtonType("Đăng ký");
            ButtonType btn_cancel = new ButtonType("Không");
            alert.getButtonTypes().setAll(btn_ok, btn_cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btn_ok) {
                String phone = phoneNumField.getText();
                String name = nameField.getText();
                String email = emailField.getText().isEmpty() ? null : emailField.getText();
                CustomerDAO customerDAO = CustomerDAO.getInstance();
                customerDAO.add(new Customer(name, null, 0, phone, email, null));
                Customer customer = customerDAO.getByOnePhoneNumber(phone);
                cusIDField.setText(customer.getCustomerId());
                ToastsMessage.showToastsMessage("Thông báo", "Đăng ký khách hàng mới thành công, nhấn LƯU để tạo đơn đặt mới");
                return;
            }
        }
        //TODO: Xử lý database ghi đơn đặt taị đây
        //---write here---


        //Delay để thông báo thao tác hợp lệ rồi mới chuyển trang
        ToastsMessage.showToastsMessage("Thông báo)", "Tạo đơn đặt trước thành công, hệ thống sẽ chuyển đến trang quản lý đặt bàn sau 3 giây");

        //TODO: Xoá Alert này đi khi đã ghi database
        Utils.showAlert("Chưa có sự kiện ghi đơn dặt trước vào database", "Lưu Ý");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            try {
                restaurantMainController.openReservationManagement();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
    }

    @FXML
    void onChangeValueQuantityKeyTyped(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (!Character.isDigit(c) || c == '0') {
            numOfAttendeesField.setText("1");
            ToastsMessage.showToastsMessage("Sai định dạng", "Vui lòng chỉ nhập số, từ 1 người trở lên");
        }
    }

    @FXML
    void onClearPhoneNumFieldButton(ActionEvent event) {
        phoneNumField.setText("");
    }
}
