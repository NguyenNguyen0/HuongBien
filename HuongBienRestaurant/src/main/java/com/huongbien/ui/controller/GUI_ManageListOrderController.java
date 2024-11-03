package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Reservation;
import com.huongbien.database.Database;
import com.huongbien.entity.Customer;
import com.huongbien.entity.FoodOrder;
import com.huongbien.entity.Reservation;
import com.huongbien.entity.Table;
import com.huongbien.utils.Paginator;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_ManageListOrderController implements Initializable {
    @FXML
    private TextField reservationSearchField;
    @FXML
    private Button searchReservationButton;
    @FXML
    private ImageView clearSearchButton;
    @FXML
    private Button firstPageButton;
    @FXML
    private Button lastPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Label pageIndexLabel;
    @FXML
    private Button prevPageButton;
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField depositTextField;
    @FXML
    private TextField employeeIdTextField;
    @FXML
    private TextField partySizeTextField;
    @FXML
    private TextField partyTypeTextField;
    @FXML
    private DatePicker receiveDateDatePicker;
    @FXML
    private DatePicker reservationDateDatePicker;
    @FXML
    private TextField reservationIdTextField;
    @FXML
    private TextField statusTextField;
    @FXML
    private TextField tablesTextField;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, String> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> receiveDateColumn;
    @FXML
    private TableColumn<Reservation, String> tablesColumn;
    @FXML
    private TableColumn<Reservation, String> customerPhoneNumberColumn;
    @FXML
    private TableView<FoodOrder> foodOrderTable;
    @FXML
    private TableColumn<FoodOrder, String> unitPriceColumn;
    @FXML
    private TableColumn<FoodOrder, String> quantityColumn;
    @FXML
    private TableColumn<FoodOrder, String> noteColumn;
    @FXML
    private TableColumn<FoodOrder, String> foodNameColumn;

    //    TODO: viết lại logic cho code đỡ bẩn
    private static final DAO_Reservation daoReservation;

    static {
        try {
            daoReservation = new DAO_Reservation(Database.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Paginator<Reservation> reservationPaginator = new Paginator<>((offset, limit) -> {
        DAO_Reservation daoReservation = null;
        try {
            daoReservation = new DAO_Reservation(Database.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return daoReservation.getWithPaginator(offset, limit);
    }, daoReservation.getTotalReservationCount(), 10, false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpReservationTable();
        setUpFoodOrderTable();
        setPageIndexLabel();
        fillDataReservationTable(reservationPaginator.getCurrentPage());
    }

    public void setUpReservationTable() {
        reservationTable.setPlaceholder(new Label("Không có đơn đặt bàn"));
        reservationTable.getItems().clear();

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        receiveDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiveDate"));
        tablesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                    cellData.getValue().getTables() != null
                        ? Utils.toStringTables(cellData.getValue().getTables())
                        : "")
        );
        customerPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null
                        ? cellData.getValue().getCustomer().getPhoneNumber()
                        : "")
        );
    }

    public void setUpFoodOrderTable() {
        foodOrderTable.setPlaceholder(new Label("Không có món đặt trước"));
        foodOrderTable.getItems().clear();

        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        foodNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCuisine() != null
                        ? cellData.getValue().getCuisine().getName()
                        : "")
        );
    }

    public void fillDataFoodOrderTable(List<FoodOrder> foodOrders) {
        foodOrderTable.getItems().clear();
        if (foodOrders == null) return;
        foodOrderTable.setItems(FXCollections.observableArrayList(foodOrders));
    }

    public void fillDataReservationTable(List<Reservation> reservations) {
        reservationTable.getItems().clear();
        if (reservations == null) return;
        reservationTable.setItems(FXCollections.observableArrayList(reservations));
    }

    public void fillFormData(Reservation reservation) {
        reservationIdTextField.setText(reservation.getReservationId());
        partySizeTextField.setText(String.valueOf(reservation.getPartySize()));
        partyTypeTextField.setText(reservation.getPartyType());
        reservationDateDatePicker.setValue(reservation.getReservationDate());
        receiveDateDatePicker.setValue(reservation.getReceiveDate());
        employeeIdTextField.setText(reservation.getEmployee() != null ? reservation.getEmployee().getEmployeeId() : "");
        customerIdTextField.setText(reservation.getCustomer() != null ? reservation.getCustomer().getCustomerId() : "");
        depositTextField.setText(String.valueOf(reservation.getDeposit()));
        statusTextField.setText(reservation.getStatus());
        tablesTextField.setText(Utils.toStringTables(reservation.getTables()));

        fillDataFoodOrderTable(reservation.getFoodOrders());
    }

    public void setPageIndexLabel() {
        pageIndexLabel.setText(reservationPaginator.getCurrentPageIndex() + "/" + reservationPaginator.getTotalPages());
    }

    @FXML
    void loadReservationInfo(MouseEvent event) {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) return;
        fillFormData(selectedReservation);
    }

    @FXML
    void clearSearchBarOnClick(MouseEvent event) {
        reservationSearchField.setText(null);
    }

    @FXML
    void blankFormOnClick(MouseEvent event) {
        fillFormData(new Reservation());
    }

    @FXML
    void updateReservationButtonOnClick(MouseEvent event) {
//        TODO:
    }

    @FXML
    void firstPageButtonOnClick(MouseEvent event) {
        reservationPaginator.goToFirstPage();
        fillDataReservationTable(reservationPaginator.getCurrentPage());
        setPageIndexLabel();
    }

    @FXML
    void lastPageButtonOnClick(MouseEvent event) {
        reservationPaginator.goToLastPage();
        fillDataReservationTable(reservationPaginator.getCurrentPage());
        setPageIndexLabel();
    }

    @FXML
    void nextPageButtonOnClick(MouseEvent event) {
        reservationPaginator.goToNextPage();
        fillDataReservationTable(reservationPaginator.getCurrentPage());
        setPageIndexLabel();
    }

    @FXML
    void prevPageButtonOnClick(MouseEvent event) {
        reservationPaginator.goToPreviousPage();
        fillDataReservationTable(reservationPaginator.getCurrentPage());
        setPageIndexLabel();
    }

    @FXML
    void searchButtonOnClick(MouseEvent event) {

    }
}
