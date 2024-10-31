package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Customer;
import com.huongbien.dao.DAO_Employee;
import com.huongbien.dao.DAO_Promotion;
import com.huongbien.database.Database;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Employee;
import com.huongbien.entity.Promotion;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GUI_ManagePromotionController implements Initializable {

    @FXML
    private ImageView btnDeleteSearch;
    @FXML
    private TextField txt_promotionSearch;
    @FXML
    private TextField txt_promotionName;
    @FXML
    private DatePicker date_started;
    @FXML
    private DatePicker date_ended;
    @FXML
    private TextField txt_discount;
    @FXML
    private TextField txt_status;
    @FXML
    private TextField txt_minimumOrder;
    @FXML
    private ComboBox<String> comboBox_memberShipLevel;
    @FXML
    private TextArea txtArea_promotionDescription;
    @FXML
    private Button btn_promotionMain;
    @FXML
    private Button btn_promotionClear;
    @FXML
    private Button btn_promotionSub;
    @FXML
    private ComboBox<String> comboBox_status;
    @FXML
    private TableView<Promotion> tabViewPromotion;
    @FXML
    private TableColumn<Promotion, String> tabCol_promotionID;
    @FXML
    private TableColumn<Promotion, Date> tabCol_startedDate;
    @FXML
    private TableColumn<Promotion, Date> tabCol_endedDate;
    @FXML
    private TableColumn<Promotion, Double> tabCol_discount;
    @FXML
    private  TableColumn<Promotion, String> tabCol_memberShipLevel;
    private Utils utils;

    public void utilsButton_1() {
        btn_promotionSub.setText("Thêm");
        btn_promotionMain.setText("Sửa");
        btn_promotionSub.setStyle("-fx-background-color:   #1D557E");
        btn_promotionMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        btn_promotionSub.setText("Sửa");
        btn_promotionMain.setText("Thêm");
        btn_promotionSub.setStyle("-fx-background-color:   #761D7E");
        btn_promotionMain.setStyle("-fx-background-color:  #1D557E");
    }

    private void setValueCombobox() {
        //Status
        ObservableList<String> memberShipLevelList = FXCollections.observableArrayList("Đồng", "Bạc", "Vàng", "Kim cương");
        comboBox_memberShipLevel.setItems(memberShipLevelList);
        comboBox_memberShipLevel.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return comboBox_memberShipLevel.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        ObservableList<String> statusList = FXCollections.observableArrayList("Còn hiệu lực", "Hết hiệu lực");
        comboBox_status.setItems(statusList);
        comboBox_status.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return comboBox_memberShipLevel.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public void setCellValues(){
        try {
            Connection connection = Database.getConnection();
            DAO_Promotion dao_promotion = new DAO_Promotion(connection);
            List<Promotion> promotionList = dao_promotion.get();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);

            txt_promotionName.setEditable(true);
            txtArea_promotionDescription.setEditable(true);
            txt_status.setEditable(true);
            txt_minimumOrder.setEditable(true);

            tabCol_promotionID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
            tabCol_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            tabCol_memberShipLevel.setCellValueFactory(cellData -> {
                int memberShip = cellData.getValue().getMembershipLevel();
                String memberShipLevel = utils.toStringMembershipLevel(memberShip);
                return new SimpleStringProperty(memberShipLevel);
            });
            tabCol_endedDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tabCol_startedDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tabViewPromotion.setItems(listPromotion);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    public void setCellValuesExpired(){
        try {
            Connection connection = Database.getConnection();
            DAO_Promotion dao_promotion = new DAO_Promotion(connection);
            List<Promotion> promotionList = dao_promotion.getExpired();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);

            txt_promotionName.setEditable(true);
            txtArea_promotionDescription.setEditable(true);
            txt_minimumOrder.setEditable(true);

            tabCol_promotionID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
            tabCol_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            tabCol_memberShipLevel.setCellValueFactory(cellData -> {
                int memberShip = cellData.getValue().getMembershipLevel();
                String memberShipLevel = utils.toStringMembershipLevel(memberShip);
                return new SimpleStringProperty(memberShipLevel);
            });
            tabCol_endedDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tabCol_startedDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tabViewPromotion.setItems(listPromotion);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }
    public void enableInput() {
        txt_status.setDisable(true);
        txt_minimumOrder.setDisable(false);
        txt_promotionName.setDisable(false);
        txt_discount.setDisable(false);
        comboBox_memberShipLevel.setDisable(false);
        txtArea_promotionDescription.setDisable(false);
        date_ended.setDisable(false);
        date_started.setDisable(false);
    }

    public void disableInput() {
        txt_status.setDisable(true);
        txt_minimumOrder.setDisable(true);
        txt_promotionName.setDisable(true);
        txt_discount.setDisable(true);
        comboBox_memberShipLevel.setDisable(true);
        txtArea_promotionDescription.setDisable(true);
        date_ended.setDisable(true);
        date_started.setDisable(true);
    }

    public void clear(){
        txt_status.clear();
        txt_minimumOrder.clear();
        txt_promotionName.clear();
        txt_discount.clear();
        txtArea_promotionDescription.clear();
        date_started.setValue(null);
        date_ended.setValue(null);
        comboBox_memberShipLevel.getSelectionModel().clearSelection();
        tabViewPromotion.getSelectionModel().clearSelection();
    }
    public boolean checkData(){
        if (txt_promotionName.getText().trim().equals("")){
            return false;
        }
        if (txt_discount.getText().trim().equals("")){
            return false;
        }
        if (date_ended.getValue() == null){
            return false;
        }
        if (date_started.getValue() == null){
            return false;
        }
        if(date_ended.getValue().isBefore(date_started.getValue())){
            return false;
        }
        if(txt_status.getText().equals("Còn hi?u l?c")){

        }
        return true;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableInput();
        setCellValues();
        setValueCombobox();
        comboBox_status.getSelectionModel().select(0);
    }

    @FXML
    void getPromotionInfo(MouseEvent mouseEvent) {
        utilsButton_1();
        Promotion selectedItem = tabViewPromotion.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            enableInput();
            txt_promotionName.setText(selectedItem.getName());
            txt_status.setText(selectedItem.getStatus());
            double dis = selectedItem.getDiscount();
            int discount = (int) (dis * 100);

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedPrice = priceFormat.format(selectedItem.getMinimumOrderAmount());
            txt_minimumOrder.setText(formattedPrice);

            txtArea_promotionDescription.setText(selectedItem.getDescription());
            txt_discount.setText(discount+"%");
            date_ended.setValue(selectedItem.getEndDate());
            date_started.setValue(selectedItem.getStartDate());
            int memberShip = selectedItem.getMembershipLevel();
            String memberShipLevel = utils.toStringMembershipLevel(memberShip);
            comboBox_memberShipLevel.getSelectionModel().select(memberShipLevel);
        }
    }

    @FXML
    void btnDeleteSearch(MouseEvent mouseEvent) {
        txt_promotionSearch.setText("");
        setCellValues();
    }

    @FXML
    void txt_promotionSearch(KeyEvent keyEvent) {
        String id = txt_promotionSearch.getText();
        try {
            Connection connection = Database.getConnection();
            DAO_Promotion dao_promotion = new DAO_Promotion(connection);
            List<Promotion> promotionList = dao_promotion.search(id);
            ObservableList<Promotion> promotions = FXCollections.observableArrayList(promotionList);

            tabCol_promotionID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
            tabCol_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            tabCol_memberShipLevel.setCellValueFactory(cellData -> {
                int memberShip = cellData.getValue().getMembershipLevel();
                String memberShipLevel = utils.toStringMembershipLevel(memberShip);
                return new SimpleStringProperty(memberShipLevel);
            });
            tabCol_endedDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tabCol_startedDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tabViewPromotion.setItems(promotions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @FXML
    void btn_promotionSub(ActionEvent actionEvent) {
        if(btn_promotionSub.getText().equals("Thêm")){
            txt_promotionName.requestFocus();
            utilsButton_2();
            clear();
            enableInput();
        }
        else{
            disableInput();
            utilsButton_1();
        }
    }

    @FXML
    void btn_promotionMain(ActionEvent actionEvent) {
        if (btn_promotionMain.getText().equals("Sửa")){
            enableInput();
            Promotion promotion = null;
            if (checkData()) {
                String name = txt_promotionName.getText();
                String status = txt_status.getText();

                String minimum = txt_minimumOrder.getText();
                double minimumOrder = Double.parseDouble(minimum.replace(",",""));

                String dis = txt_discount.getText();
                dis = dis.replace("%","");
                Double discount = Double.parseDouble(dis) / 100;

                String memberShip = comboBox_memberShipLevel.getSelectionModel().getSelectedItem();
                System.out.println(memberShip);
                int memberShipLevel = utils.toIntMembershipLevel(memberShip);

                LocalDate start = date_started.getValue();
                LocalDate end = date_ended.getValue();
                String description = txtArea_promotionDescription.getText();
                String id = tabViewPromotion.getSelectionModel().getSelectedItem().getPromotionId();
                try {
                    Connection connection = Database.getConnection();
                    DAO_Promotion dao_promotion = new DAO_Promotion(connection);
                    promotion = dao_promotion.get(id);
                    promotion.setName(name);
                    promotion.setDescription(description);
                    promotion.setEndDate(end);
                    promotion.setStartDate(start);
                    promotion.setDiscount(discount);
                    promotion.setStatus(status);
                    promotion.setMinimumOrderAmount(minimumOrder);
                    promotion.setMembershipLevel(memberShipLevel);
                    if (dao_promotion.update(promotion)) {
                        tabViewPromotion.getItems().clear();
                        setCellValues();
                    }
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            clear();
        }
        else if (btn_promotionMain.getText().equals("Thêm")){
            Promotion promotion = null;
            if (checkData()) {
                String name = txt_promotionName.getText();
                String status = txt_status.getText();

                double minimumOrder = Double.parseDouble(txt_minimumOrder.getText());
                String dis = txt_discount.getText();
                int i = dis.indexOf("%");
                dis = dis.substring(0,i);
                Double discount = Double.parseDouble(dis) / 100;

                String memberShip = comboBox_memberShipLevel.getSelectionModel().getSelectedItem();

                int memberShipLevel = utils.toIntMembershipLevel(memberShip);

                LocalDate start = date_started.getValue();
                LocalDate end = date_ended.getValue();
                String description = txtArea_promotionDescription.getText();
                try {
                    Connection connection = Database.getConnection();
                    DAO_Promotion dao_promotion = new DAO_Promotion(connection);
                    promotion = new Promotion(name, start, end, discount, description, minimumOrder, memberShipLevel, "Còn hi?u l?c");
                    if (dao_promotion.add(promotion)) {
                        tabViewPromotion.getItems().clear();
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
    void btn_promotionClear(ActionEvent actionEvent) {
        clear();
    }

    public void comboBox_status(ActionEvent actionEvent) {
        if(comboBox_status.getSelectionModel().isSelected(0)){
            setCellValues();
            btn_promotionSub.setVisible(true);
        }
        else{
            utilsButton_1();
            btn_promotionSub.setVisible(false);
            setCellValuesExpired();
            clear();
        }
    }
}
