package com.huongbien.ui.controller;

import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Promotion;
import com.huongbien.entity.Reservation;
import com.huongbien.entity.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;

public class RestaurantLookupController implements Initializable {
    //Vbox1
    @FXML private ComboBox<String> tableFloorComboBox;
    @FXML private TextField tabkeNameTextField;
    @FXML private ImageView deleteNameButton;
    @FXML private ComboBox<Integer> tableSeatsComboBox;
    @FXML private ComboBox<String> tableTypesComboBox;
    @FXML private ComboBox<String> tableStatusComboBox;
    @FXML private TableView<Table> tablesTableView;
    @FXML private TableColumn<Table, String> tableFloorColumn;
    @FXML private TableColumn<Table, String> tableIdColumn;
    @FXML private TableColumn<Table, Integer> tableSeatColumn;
    @FXML private TableColumn<Table, String> tableTypeColumn;
    @FXML private TableColumn<Table, String> tableStatusColumn;
    //Vbox2
    @FXML private ComboBox<String> cuisineTypeComboBox;
    @FXML private TextField cuisineNameTextField;
    @FXML private ImageView deleteCuisineNameButton;
    @FXML private TableView<Cuisine> cuisinesTableView;
    @FXML private TableColumn<Cuisine, String> cuisineTypeColumn;
    @FXML private TableColumn<Cuisine, String> cuisineNameColumn;
    @FXML private TableColumn<Cuisine, Double> cuisinePriceColumn;
    @FXML private TableColumn<Cuisine, Integer> cuisineCountSaleColumn;
    //Vbox3
    @FXML private TextField promotionNameTextField;
    @FXML private ImageView deletePromotionNameButton;
    @FXML private DatePicker promotionStartDate;
    @FXML private DatePicker promotionEndDate;
    @FXML private ComboBox<Double> promotionDiscountComboBox;
    @FXML private ComboBox<Double> promotionMinimumOrderAmount;
    @FXML private ComboBox<String> promotionStatusComboBox;
    @FXML private TableView<Promotion> promotionsTableView;
    @FXML private TableColumn<Promotion, String> promotionNameColumn;
    @FXML private TableColumn<Promotion, Date> promotionStartDateColumn;
    @FXML private TableColumn<Promotion, Date> promotionEndDateColumn;
    @FXML private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML private TableColumn<Promotion, Double> promotionMinimumOrderAmountColumn;
    @FXML private TableColumn<Promotion, String> promotionStatusColumn;
    //Vbox4
    @FXML private TextField reservationIdTextField;
    @FXML private ImageView deleteReservationIdButton;
    @FXML private TextField reservationCustomerNameTextField;
    @FXML private ImageView deleteReservationCustomerName;
    @FXML private DatePicker reservationDate;
    @FXML private DatePicker reservationReceiveDate;
    @FXML private TableView<Reservation> reservationsTableView;
    @FXML private TableColumn<Reservation, String> reservationIdColumn;
    @FXML private TableColumn<Reservation, String> reservationCustomerNameColumn;
    @FXML private TableColumn<Reservation, Date> reservationDateColumn;
    @FXML private TableColumn<Reservation, Timer> reservationTimeColumn;
    @FXML private TableColumn<Reservation, Date> reservationReceiveDateColumn;
    @FXML private TableColumn<Reservation, Timer> reservationReceiveTimeColumn;
    @FXML private TableColumn<Reservation, Double> reservationDepositColumn;
    //All
    @FXML private ComboBox<String> restaurantLookupComboBox;
    @FXML private VBox restaurantLookupCuisineVBox;
    @FXML private VBox restaurantLookupPreOrderTableVBox;
    @FXML private VBox restaurantLookupPromotionVBox;
    @FXML private VBox restaurantLookupTableVBox;

    //Controller area
    public RestaurantMainController restaurantMainController;

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultRestaurantLookupComboBox();
        setDefaultTableView(); //set default table view display table first

    }

    private void setTableViewColumn(){
        String selectedItem = restaurantLookupComboBox.getValue();
        switch (selectedItem) {
            case "Bàn":

                break;
            case "Món ăn":
                restaurantMainController.featureTitleLabel.setText("Tra cứu món ăn");
                restaurantLookupCuisineVBox.setVisible(true);
                break;
            case "Khuyến mãi":
                restaurantMainController.featureTitleLabel.setText("Tra cứu khuyến mãi");
                restaurantLookupPromotionVBox.setVisible(true);
                break;
            case "Đơn đặt trước":
                restaurantMainController.featureTitleLabel.setText("Tra cứu đơn đặt trước");
                restaurantLookupPreOrderTableVBox.setVisible(true);
                break;
        }
    }

    //function area
    private void setDefaultTableView() {
        restaurantLookupTableVBox.setVisible(true);
        restaurantLookupCuisineVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupPreOrderTableVBox.setVisible(false);
    }

    private void setDefaultRestaurantLookupComboBox() {
        ObservableList<String> restaurantLookupList = FXCollections.observableArrayList();
        restaurantLookupList.add("Bàn");
        restaurantLookupList.add("Món ăn");
        restaurantLookupList.add("Khuyến mãi");
        restaurantLookupList.add("Đơn đặt trước");
        restaurantLookupComboBox.setItems(restaurantLookupList);
        restaurantLookupComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }
            @Override
            public String fromString(String string) {
                return string;
            }
        });
        restaurantLookupComboBox.getSelectionModel().selectFirst();
    }
    //---TODO: add more function

    //event area--------------------------------------------------------------------------
    //---
    @FXML
    void onRestaurantLookupComboBoxAction(ActionEvent event) {
        String selectedItem = restaurantLookupComboBox.getValue();
        restaurantLookupCuisineVBox.setVisible(false);
        restaurantLookupPreOrderTableVBox.setVisible(false);
        restaurantLookupPromotionVBox.setVisible(false);
        restaurantLookupTableVBox.setVisible(false);
        switch (selectedItem) {
            case "Bàn":
                restaurantMainController.featureTitleLabel.setText("Tra cứu bàn");
                restaurantLookupTableVBox.setVisible(true);
                setTableViewColumn();
                break;
            case "Món ăn":
                restaurantMainController.featureTitleLabel.setText("Tra cứu món ăn");
                restaurantLookupCuisineVBox.setVisible(true);
                setTableViewColumn();
                break;
            case "Khuyến mãi":
                restaurantMainController.featureTitleLabel.setText("Tra cứu khuyến mãi");
                restaurantLookupPromotionVBox.setVisible(true);
                setTableViewColumn();
                break;
            case "Đơn đặt trước":
                restaurantMainController.featureTitleLabel.setText("Tra cứu đơn đặt trước");
                restaurantLookupPreOrderTableVBox.setVisible(true);
                setTableViewColumn();
                break;
        }
    }

    public void onTableFloorComboBoxAction(ActionEvent actionEvent) {
    }

    public void onTableNameTextFieldKeyReleased(KeyEvent keyEvent) {
    }

    public void onDeleteNameButtonMouseClicked(MouseEvent mouseEvent) {
    }

    public void onTableSeatsComboBoxAction(ActionEvent actionEvent) {
    }

    public void onTableTypesComboBoxAction(ActionEvent actionEvent) {
    }

    public void onTableStatusComboBoxAction(ActionEvent actionEvent) {
    }

    public void onCuisineTypeComboBoxAction(ActionEvent actionEvent) {
    }

    public void onCuisineNameTextFieldKeyReleased(KeyEvent keyEvent) {
    }

    public void onDeleteCuisineNameButtonMouseClicked(MouseEvent mouseEvent) {
    }

    public void onPromotionNameTextFieldKeyReleased(KeyEvent keyEvent) {
    }

    public void deletePromotionNameButtonClicked(MouseEvent mouseEvent) {
    }

    public void onPromotionStartDateAction(ActionEvent actionEvent) {
    }

    public void onPromotionEndDateAction(ActionEvent actionEvent) {
    }

    public void onPromotionDiscountComboBoxAction(ActionEvent actionEvent) {
    }

    public void onPromotionMinimumOrderAmountComboBoxAction(ActionEvent actionEvent) {
    }

    public void onPromotionStatusComboBoxAction(ActionEvent actionEvent) {
    }

    public void onReservationIdTextFieldKeyReleased(KeyEvent keyEvent) {
    }

    public void onDeleteReservationIdButtonMouseClicked(MouseEvent mouseEvent) {
    }

    public void onReservationCustomerNameTextFieldKeyReleased(KeyEvent keyEvent) {
    }

    public void onDeleteReservationCustomerNameMouseClicked(MouseEvent mouseEvent) {
    }

    public void onReservationDateAction(ActionEvent actionEvent) {
    }

    public void onReservationReceiveDateAction(ActionEvent actionEvent) {
    }

    //---TODO: add more event


}
