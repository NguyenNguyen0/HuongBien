package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.bus.ReservationBUS;
import com.huongbien.config.Constants;
import com.huongbien.config.Variable;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.ReservationDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PreOrderController implements Initializable {
    @FXML private Label reservationIDLabel;
    @FXML private TextField numOfAttendeesField;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private TextField tableInfoField;
    @FXML private Label tableAmountLabel;
    @FXML private Label cuisineAmountLabel;
    @FXML private Label totalAmoutLabel;
    @FXML private Label preOrderCuisineLabel;
    @FXML private DatePicker receiveDatePicker;
    @FXML private ComboBox<String> partyTypeComboBox;
    @FXML private TextField phoneNumField;
    @FXML private TextField emailField;
    @FXML private TextField customerIDField;
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
        try {
            setInfoPreOrder();
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
        minuteComboBox.getItems().clear();
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
        hourComboBox.setValue(String.format("%02d", LocalTime.now().getHour()));
        minuteComboBox.setValue(String.format("%02d", LocalTime.now().getMinute()));
    }

    private void setInfoPreOrder() throws FileNotFoundException {
        receiveDatePicker.setValue(LocalDate.now());
        setTimeComboBox();
        ObservableList<String> partyTypes = FXCollections.observableArrayList(Variable.partyTypesArray);
        partyTypeComboBox.setItems(partyTypes);
        partyTypeComboBox.getSelectionModel().selectLast();
        //get table info from json file
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.CUSTOMER_PATH);
        JsonArray jsonArrayReservation = Utils.readJsonFromFile(Constants.RESERVATION_PATH);
        //table
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
            tableAmount += (table.getTableType().getTableId().equals(Variable.tableVipID)) ? Variable.tableVipPrice : 0;
        }
        if (!tableInfoBuilder.isEmpty()) {
            tableInfoBuilder.setLength(tableInfoBuilder.length() - 2);
        }
        //Cuisine
        int cuisineQuantity = 0;
        double cuisineAmount = 0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            cuisineQuantity += quantity;
            cuisineAmount += money;
        }
        //customer
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Customer ID").getAsString();
            Customer customer = CustomerDAO.getInstance().getById(id);
            if (customer != null) {
                customerIDField.setText(customer.getCustomerId());
                nameField.setText(customer.getName());
                phoneNumField.setText(customer.getPhoneNumber());
                emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
            } else {
                customerIDField.setText("");
                nameField.setText("");
                phoneNumField.setText("");
                emailField.setText("");
            }
        }
        for (JsonElement element : jsonArrayReservation) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.has("Reservation ID") ? jsonObject.get("Reservation ID").getAsString() : "";
            reservationIDLabel.setText("Mã đặt bàn: "+id);
            Reservation reservation = ReservationDAO.getInstance().getById(id);
            hourComboBox.setValue((reservation.getReceiveTime() != null) ? String.format("%02d", reservation.getReceiveTime().getHour()) : "23");
            minuteComboBox.setValue((reservation.getReceiveTime() != null) ? String.format("%02d", reservation.getReceiveTime().getMinute()) : "55");
            numOfAttendeesField.setText((reservation.getPartySize() != 0) ? String.valueOf(reservation.getPartySize()) : "1");
            receiveDatePicker.setValue((reservation.getReceiveDate() != null) ? reservation.getReceiveDate() : LocalDate.now());
            noteField.setText((reservation.getNote() != null) ? reservation.getNote() : "");
            partyTypeComboBox.getSelectionModel().select(reservation.getPartyType() != null ? reservation.getPartyType() : "Khác...");
            break;
        }
        //set info to reservation
        tableInfoField.setText(tableInfoBuilder.toString());
        preOrderCuisineLabel.setText(cuisineQuantity + " món");
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
            customerIDField.setText("");
            nameField.setText("");
            emailField.setText("");
        } else {
            Customer customer = CustomerDAO.getInstance().getByOnePhoneNumber(phone);
            if(customer != null) {
                customerIDField.setText(customer.getCustomerId());
                nameField.setText(customer.getName());
                emailField.setText(customer.getEmail() == null ? "" : customer.getEmail());
                //Write JSON
                JsonArray jsonArray = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Customer ID", customer.getCustomerId());
                jsonArray.add(jsonObject);
                Utils.writeJsonToFile(jsonArray, Constants.CUSTOMER_PATH);
            } else {
                ToastsMessage.showToastsMessage("Thông báo", "(VN +84) Không tìm thấy khách hàng, nhập tên khách hàng để đăng ký mới");
            }
        }
    }

    @FXML
    void onSavePreOrderTableButtonAction(ActionEvent event) throws IOException {
        JsonArray jsonArrayReservation = Utils.readJsonFromFile(Constants.RESERVATION_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TABLE_PATH);
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.CUISINE_PATH);
        JsonArray jsonArrayEmployee = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
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
        if(customerIDField.getText().isEmpty()) {
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
                customerIDField.setText(customer.getCustomerId());
                ToastsMessage.showToastsMessage("Thông báo", "Đăng ký khách hàng mới thành công, nhấn LƯU để tạo đơn đặt mới");
                return;
            }
        }

        if(jsonArrayTable.isEmpty()) {
            ToastsMessage.showMessage("Vui lòng chọn ít nhất 1 bàn", "warning");
            return;
        }

        //TODO: Xử lý database ghi đơn đặt taị đây
        Reservation reservation = new Reservation();

        String reservationID = null;
        for (JsonElement element : jsonArrayReservation) {
            JsonObject jsonObject = element.getAsJsonObject();
            reservationID = jsonObject.has("Reservation ID") ? jsonObject.get("Reservation ID").getAsString() : null;
            break;
        }

        String employeeID = null;
        for(JsonElement element : jsonArrayEmployee) {
            JsonObject jsonObject = element.getAsJsonObject();
            employeeID = jsonObject.get("Employee ID").getAsString();
            break;
        }

        String customerID = customerIDField.getText();
        LocalDate receiveDate = receiveDatePicker.getValue();
        LocalTime receiveTime = LocalTime.of(Integer.parseInt(hourComboBox.getValue()), Integer.parseInt(minuteComboBox.getValue()));
        int partySize = Integer.parseInt(numOfAttendeesField.getText());
        String note = noteField.getText();
        String partyType = partyTypeComboBox.getValue();
        Customer customer = CustomerDAO.getInstance().getById(customerID);
        Employee employee = EmployeeDAO.getInstance().getOneById(employeeID);
//        double deposit = Double.parseDouble(totalAmoutLabel.getText().replaceAll("\\.", "").replaceAll(" VNĐ", ""));
        double deposit = Double.parseDouble(totalAmoutLabel.getText().replaceAll(",", "").replaceAll(" VNĐ", ""));

        reservation.setReservationId(reservationID);
        reservation.setPartyType(partyType);
        reservation.setPartySize(partySize);
        reservation.setReservationDate(LocalDate.now());
        reservation.setReservationTime(LocalTime.now());
        reservation.setReceiveDate(receiveDate);
        reservation.setReceiveTime(receiveTime);
        reservation.setStatus(Variable.statusReservation[0]);
        reservation.setDeposit(deposit);
        reservation.setNote(note);
        reservation.setEmployee(employee);
        reservation.setCustomer(customer);

        ArrayList<Table> tables = new ArrayList<>();
        for(JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String tableID = jsonObject.get("Table ID").getAsString();
            Table table = new Table();
            table.setId(tableID);
            tables.add(table);
        }

        ArrayList<FoodOrder> foodOrders = new ArrayList<>();
        for(JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();

            String cuisineID = jsonObject.get("Cuisine ID").getAsString();
            double cuisinePrice = jsonObject.get("Cuisine Price").getAsDouble();
            String cuisineNote = jsonObject.get("Cuisine Note").getAsString();
            int cuisineQuantity = jsonObject.get("Cuisine Quantity").getAsInt();

            FoodOrder foodOrder = new FoodOrder();
            foodOrder.setFoodOrderId(reservation.getReservationId());
            foodOrder.setQuantity(cuisineQuantity);
            foodOrder.setNote(cuisineNote);
            foodOrder.setSalePrice(cuisinePrice);

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(cuisineID);

            foodOrder.setCuisine(cuisine);
            foodOrders.add(foodOrder);
        }

        reservation.setTables(tables);
        reservation.setFoodOrders(foodOrders);

        ReservationBUS reservationBUS = new ReservationBUS();
        if(reservationID == null) {
            if (reservationBUS.addReservation(reservation)) {
                ToastsMessage.showMessage("Tạo đơn đặt trước thành công, trang quản lý đặt bàn sẽ mở sau 3 giây", "success");
            } else {
                ToastsMessage.showMessage("Tạo đơn đặt trước thất bại, vui lòng thử lại", "error");
            }
        } else {
            if (reservationBUS.updateReservation(reservation)) {
                ToastsMessage.showMessage("Cập nhật đơn đặt trước: "+reservationID+" thành công, trang quản lý đặt bàn sẽ mở sau 3 giây", "success");
            } else {
                ToastsMessage.showMessage("Cập nhật đơn đặt trước: "+reservationID+" thất bại, vui lòng thử lại", "error");
            }
        }

        //Delay để thông báo thao tác hợp lệ rồi mới chuyển trang
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
        emailField.setText("");
        customerIDField.setText("");
        nameField.setText("");
        noteField.setText("");
    }
}
