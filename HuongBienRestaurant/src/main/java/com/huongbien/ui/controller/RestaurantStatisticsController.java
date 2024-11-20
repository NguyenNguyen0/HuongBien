package com.huongbien.ui.controller;

import com.huongbien.dao.StatisticsDAO;
import com.huongbien.entity.Customer;
import com.huongbien.entity.Order;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.stream.IntStream;

public class RestaurantStatisticsController {
    @FXML private BarChart<String, Number> revenueBarChart;
    @FXML private LineChart<String, Number> orderLineChart;
    @FXML private ComboBox<String> statisticalComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalInvoiceLabel;
    @FXML private Label totalOrderLabel;
    @FXML private Label averageRevenueLabel;
    @FXML private Label totalCustomersField;
    @FXML private Label totalReservationField;
    @FXML private Label totalOrderField;
    @FXML private Label totalRevenuesField;
    @FXML private Label selectedYearLabel;
    @FXML private TableView<Order> newOrderTable;
    @FXML private TableColumn<?, ?> orderIdColumn;
    @FXML private TableColumn<?, ?> orderNoteColumn;
    @FXML private TableColumn<Order, String> orderTablesColumn;
    @FXML private TableColumn<?, ?> orderTotalAmountColumn;
    @FXML private TableView<Customer> newCustomerTable;
    @FXML private TableColumn<?, ?> customerAccumulatedPointColumn;
    @FXML private TableColumn<?, ?> customerPhoneNumberColumn;
    @FXML private TableColumn<?, ?> customerIdColumn;
    @FXML private TableColumn<?, ?> customerMembershipLevelColumn;
    @FXML private TableColumn<?, ?> customerNameColumn;

    @FXML
    public void initialize() {
        statisticalComboBox.getItems().addAll("Tháng", "Quý", "Năm");
        statisticalComboBox.getSelectionModel().select("Tháng");

        int currentYear = Year.now().getValue();
        yearComboBox.getItems().addAll(IntStream.rangeClosed(currentYear - 3, currentYear).boxed().toList());
        yearComboBox.getSelectionModel().select(currentYear);

        statisticalComboBox.setOnAction(event -> {
            updateVisibilityBasedOnStatisticalType();
            loadStatistics();
        });
        yearComboBox.setOnAction(event -> loadStatistics());

        updateVisibilityBasedOnStatisticalType();
        loadStatistics();
        setBusinessSummary();
        setUpTableView();
    }

    private void updateVisibilityBasedOnStatisticalType() {
        String criteria = statisticalComboBox.getSelectionModel().getSelectedItem();
        boolean isYearSelected = "Năm".equals(criteria);
        yearComboBox.setVisible(!isYearSelected);
        selectedYearLabel.setVisible(!isYearSelected);
    }

    private void loadStatistics() {
        String criteria = statisticalComboBox.getSelectionModel().getSelectedItem();
        Integer selectedYear = yearComboBox.getSelectionModel().getSelectedItem();

        if (selectedYear == null && "Năm".equals(criteria)) {
            loadYearlyStatistics();
        } else {
            setBusinessSituation(0, 0, 0);
            revenueBarChart.getData().clear();
            orderLineChart.getData().clear();
        }

        if (selectedYear == null || criteria == null) {
            System.out.println("Vui lòng chọn năm và loại thống kê.");
            return;
        }

        switch (criteria) {
            case "Tháng":
                loadMonthlyStatistics(selectedYear);
                break;
            case "Quý":
                loadQuarterlyStatistics(selectedYear);
                break;
            case "Năm":
                loadYearlyStatistics();
                break;
            default:
                System.out.println("Loại thống kê không hợp lệ.");
                break;
        }
    }

    private void setBusinessSummary() {
        totalCustomersField.setText(String.valueOf(StatisticsDAO.getTotalCustomers()));
        totalReservationField.setText(String.valueOf(StatisticsDAO.getTotalReservations()));
        totalOrderField.setText(String.valueOf(StatisticsDAO.getTotalInvoices()));
        totalRevenuesField.setText(Utils.formatMoney(StatisticsDAO.getTotalRevenues()));
    }

    private void setBusinessSituation(double totalRevenue, int totalInvoices, int totalItems) {
        totalRevenueLabel.setText(Utils.formatMoney(totalRevenue));
        totalInvoiceLabel.setText(totalInvoices + " Đơn");
        totalOrderLabel.setText(totalItems + " Đơn");

        double averageRevenue = totalInvoices > 0 ? totalRevenue / totalInvoices : 0;
        averageRevenueLabel.setText(Utils.formatMoney(averageRevenue));
    }

    private void loadMonthlyStatistics(int year) {
        double totalRevenue = StatisticsDAO.getTotalRevenue("Năm", 0, year);
        int totalInvoices = StatisticsDAO.getTotalInvoices("Năm", 0, year);
        int totalItems = StatisticsDAO.getTotalItemsOrdered("Năm", 0, year);

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(12, year, "Tháng");
        updateLineChart(12, year, "Tháng");
    }

    private void loadQuarterlyStatistics(int year) {
        double totalRevenue = StatisticsDAO.getTotalRevenue("Năm", 0, year);
        int totalInvoices = StatisticsDAO.getTotalInvoices("Năm", 0, year);
        int totalItems = StatisticsDAO.getTotalItemsOrdered("Năm", 0, year);

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(4, year, "Quý");
        updateLineChart(4, year, "Quý");
    }

    private void loadYearlyStatistics() {
        double totalRevenue = 0;
        int totalInvoices = 0;
        int totalItems = 0;
        for (int year : yearComboBox.getItems()) {
            totalRevenue += StatisticsDAO.getTotalRevenue("Năm", 0, year);
            totalInvoices += StatisticsDAO.getTotalInvoices("Năm", 0, year);
            totalItems += StatisticsDAO.getTotalItemsOrdered("Năm", 0, year);
        }

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(yearComboBox.getItems().size(), 0, "Năm");
        updateLineChart(yearComboBox.getItems().size(), 0, "Năm");
    }

    private void updateBarChart(int periods, int year, String criteria) {
        revenueBarChart.getData().clear();
        revenueBarChart.getYAxis().setLabel("Đồng");
        revenueBarChart.getXAxis().setLabel(criteria);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo " + criteria);

        if ("Tháng".equals(criteria)) {
            for (int month = 1; month <= periods; month++) {
                double revenue = StatisticsDAO.getTotalRevenue("Tháng", month, year);
                series.getData().add(new XYChart.Data<>(String.valueOf(month), revenue));
            }
        } else if ("Quý".equals(criteria)) {
            for (int quarter = 1; quarter <= periods; quarter++) {
                double revenue = StatisticsDAO.getTotalRevenue("Quý", quarter, year);
                series.getData().add(new XYChart.Data<>("Quý " + quarter, revenue));
            }
        } else if ("Năm".equals(criteria)) {
            for (int i = 0; i < periods; i++) {
                int yearToShow = yearComboBox.getItems().get(i);
                double revenue = StatisticsDAO.getTotalRevenue("Năm", 0, yearToShow);
                series.getData().add(new XYChart.Data<>("Năm " + yearToShow, revenue));
            }
        }
        revenueBarChart.getData().add(series);
    }

    private void updateLineChart(int periods, int year, String criteria) {
        orderLineChart.getData().clear();
        orderLineChart.getXAxis().setLabel(criteria);
        orderLineChart.getYAxis().setLabel("Đồng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng hóa đơn theo " + criteria);

        if ("Tháng".equals(criteria)) {
            for (int month = 1; month <= periods; month++) {
                int count = StatisticsDAO.getTotalInvoices("Tháng", month, year);
                series.getData().add(new XYChart.Data<>(String.valueOf(month), count));
            }
        } else if ("Quý".equals(criteria)) {
            for (int quarter = 1; quarter <= periods; quarter++) {
                int count = StatisticsDAO.getTotalInvoices("Quý", quarter, year);
                series.getData().add(new XYChart.Data<>("Quý " + quarter, count));
            }
        } else if ("Năm".equals(criteria)) {
            for (int i = 0; i < periods; i++) {
                int yearToShow = yearComboBox.getItems().get(i);
                int count = StatisticsDAO.getTotalInvoices("Năm", 0, yearToShow);
                series.getData().add(new XYChart.Data<>("Năm " + yearToShow, count));
            }
        }
        orderLineChart.getData().add(series);
    }

    public void setUpTableView() {
        Label customersTablePlaceholder = new Label("Không có khách hàng mới nào trong hôm nay");
        Label invoicesTablePlaceholder = new Label("Không có hóa đơn nào mới trong hôm này ");

        customersTablePlaceholder.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: lightgray;");
        invoicesTablePlaceholder.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: lightgray;");

        newCustomerTable.setPlaceholder(customersTablePlaceholder);
        newOrderTable.setPlaceholder(invoicesTablePlaceholder);
//      TODO: css lại bảng cho các ô nhỏ lại
//        newCustomerTable.getStylesheets().add("com/huongbien/css/statistic-table.css");
//        newOrderTable.getStylesheets().add("com/huongbien/css/statistic-table.css");

        fillDataToCustomerTable();
        fillDataToInvoiceTable();
    }

    public void fillDataToCustomerTable() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerAccumulatedPointColumn.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        customerMembershipLevelColumn.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));

        ObservableList<Customer> customers = FXCollections.observableArrayList(StatisticsDAO.getNewCusomterInDay());

        newCustomerTable.setItems(customers);
    }

    public void fillDataToInvoiceTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderTotalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderNoteColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        orderTablesColumn.setCellValueFactory(cellDataFeatures ->
                new SimpleStringProperty(Utils.toStringTables(cellDataFeatures.getValue().getTables()))
        );

        ObservableList<Order> orders = FXCollections.observableArrayList(StatisticsDAO.getNewOrderInDay());

        newOrderTable.setItems(orders);
    }
}
