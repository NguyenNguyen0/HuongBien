package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Statistics;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.stream.IntStream;

public class GUI_StatisticsController {
    @FXML
    private BarChart<String, Number> barChartOfRevenue;
    @FXML
    private LineChart<String, Number> lineChartOfOrders;
    @FXML
    private ComboBox<String> comboBox_Statistical;
    @FXML
    private ComboBox<Integer> comboBox_Years;
    @FXML
    private Label txt_TotalRevenues;
    @FXML
    private Label txt_TotalInvoices;
    @FXML
    private Label txt_TotalOrders;
    @FXML
    private Label txt_AverageRevenue;
    @FXML
    private Label txt_TotalCustomers;
    @FXML
    private Label txt_Reservations;
    @FXML
    private Label txt_TotalOfInvoice;
    @FXML
    private Label txt_TotalOfRevenues;
    @FXML
    private Label labelSelectYear;
    @FXML
    private TableView<Order> table_NewInvoices;
    @FXML
    private TableColumn<?, ?> tabCol_InvoiceID;
    @FXML
    private TableColumn<?, ?> tabCol_Note;
    @FXML
    private TableColumn<Order, String> tabCol_Tables;
    @FXML
    private TableColumn<?, ?> tabCol_TotalAmount;
    @FXML
    private TableView<Customer> table_NewCustomers;
    @FXML
    private TableColumn<?, ?> tabCol_customerAccumulatedPoint;
    @FXML
    private TableColumn<?, ?> tabCol_customerPhoneNumber;
    @FXML
    private TableColumn<?, ?> tabCol_customerID;
    @FXML
    private TableColumn<?, ?> tabCol_customerMemLevel;
    @FXML
    private TableColumn<?, ?> tabCol_customerName;

    @FXML
    public void initialize() {
        comboBox_Statistical.getItems().addAll("Tháng", "Quý", "Năm");
        comboBox_Statistical.getSelectionModel().select("Tháng");

        int currentYear = Year.now().getValue();
        comboBox_Years.getItems().addAll(IntStream.rangeClosed(currentYear - 3, currentYear).boxed().toList());
        comboBox_Years.getSelectionModel().select(currentYear);

        comboBox_Statistical.setOnAction(event -> {
            updateVisibilityBasedOnStatisticalType();
            loadStatistics();
        });
        comboBox_Years.setOnAction(event -> loadStatistics());

        updateVisibilityBasedOnStatisticalType();
        loadStatistics();
        setBusinessSummary();
        setUpTableView();
    }

    private void updateVisibilityBasedOnStatisticalType() {
        String criteria = comboBox_Statistical.getSelectionModel().getSelectedItem();
        boolean isYearSelected = "Năm".equals(criteria);
        comboBox_Years.setVisible(!isYearSelected);
        labelSelectYear.setVisible(!isYearSelected);
    }

    private void loadStatistics() {
        String criteria = comboBox_Statistical.getSelectionModel().getSelectedItem();
        Integer selectedYear = comboBox_Years.getSelectionModel().getSelectedItem();

        if (selectedYear == null && "Năm".equals(criteria)) {
            loadYearlyStatistics();
        } else {
            setBusinessSituation(0, 0, 0);
            barChartOfRevenue.getData().clear();
            lineChartOfOrders.getData().clear();
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
        txt_TotalCustomers.setText(String.valueOf(DAO_Statistics.getTotalCustomers()));
        txt_Reservations.setText(String.valueOf(DAO_Statistics.getTotalReservations()));
        txt_TotalOfInvoice.setText(String.valueOf(DAO_Statistics.getTotalInvoices()));
        txt_TotalOfRevenues.setText(Utils.formatMoney(DAO_Statistics.getTotalRevenues()));
    }

    private void setBusinessSituation(double totalRevenue, int totalInvoices, int totalItems) {
        txt_TotalRevenues.setText(Utils.formatMoney(totalRevenue));
        txt_TotalInvoices.setText(totalInvoices + " Đơn");
        txt_TotalOrders.setText(totalItems + " Đơn");

        double averageRevenue = totalInvoices > 0 ? totalRevenue / totalInvoices : 0;
        txt_AverageRevenue.setText(Utils.formatMoney(averageRevenue));
    }

    private void loadMonthlyStatistics(int year) {
        double totalRevenue = DAO_Statistics.getTotalRevenue("Năm", 0, year);
        int totalInvoices = DAO_Statistics.getTotalInvoices("Năm", 0, year);
        int totalItems = DAO_Statistics.getTotalItemsOrdered("Năm", 0, year);

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(12, year, "Tháng");
        updateLineChart(12, year, "Tháng");
    }

    private void loadQuarterlyStatistics(int year) {
        double totalRevenue = DAO_Statistics.getTotalRevenue("Năm", 0, year);
        int totalInvoices = DAO_Statistics.getTotalInvoices("Năm", 0, year);
        int totalItems = DAO_Statistics.getTotalItemsOrdered("Năm", 0, year);

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(4, year, "Quý");
        updateLineChart(4, year, "Quý");
    }

    private void loadYearlyStatistics() {
        double totalRevenue = 0;
        int totalInvoices = 0;
        int totalItems = 0;
        for (int year : comboBox_Years.getItems()) {
            totalRevenue += DAO_Statistics.getTotalRevenue("Năm", 0, year);
            totalInvoices += DAO_Statistics.getTotalInvoices("Năm", 0, year);
            totalItems += DAO_Statistics.getTotalItemsOrdered("Năm", 0, year);
        }

        setBusinessSituation(totalRevenue, totalInvoices, totalItems);

        updateBarChart(comboBox_Years.getItems().size(), 0, "Năm");
        updateLineChart(comboBox_Years.getItems().size(), 0, "Năm");
    }

    private void updateBarChart(int periods, int year, String criteria) {
        barChartOfRevenue.getData().clear();
        barChartOfRevenue.getYAxis().setLabel("Đồng");
        barChartOfRevenue.getXAxis().setLabel(criteria);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo " + criteria);

        if ("Tháng".equals(criteria)) {
            for (int month = 1; month <= periods; month++) {
                double revenue = DAO_Statistics.getTotalRevenue("Tháng", month, year);
                series.getData().add(new XYChart.Data<>(String.valueOf(month), revenue));
            }
        } else if ("Quý".equals(criteria)) {
            for (int quarter = 1; quarter <= periods; quarter++) {
                double revenue = DAO_Statistics.getTotalRevenue("Quý", quarter, year);
                series.getData().add(new XYChart.Data<>("Quý " + quarter, revenue));
            }
        } else if ("Năm".equals(criteria)) {
            for (int i = 0; i < periods; i++) {
                int yearToShow = comboBox_Years.getItems().get(i);
                double revenue = DAO_Statistics.getTotalRevenue("Năm", 0, yearToShow);
                series.getData().add(new XYChart.Data<>("Năm " + yearToShow, revenue));
            }
        }
        barChartOfRevenue.getData().add(series);
    }

    private void updateLineChart(int periods, int year, String criteria) {
        lineChartOfOrders.getData().clear();
        lineChartOfOrders.getXAxis().setLabel(criteria);
        lineChartOfOrders.getYAxis().setLabel("Đồng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng hóa đơn theo " + criteria);

        if ("Tháng".equals(criteria)) {
            for (int month = 1; month <= periods; month++) {
                int count = DAO_Statistics.getTotalInvoices("Tháng", month, year);
                series.getData().add(new XYChart.Data<>(String.valueOf(month), count));
            }
        } else if ("Quý".equals(criteria)) {
            for (int quarter = 1; quarter <= periods; quarter++) {
                int count = DAO_Statistics.getTotalInvoices("Quý", quarter, year);
                series.getData().add(new XYChart.Data<>("Quý " + quarter, count));
            }
        } else if ("Năm".equals(criteria)) {
            for (int i = 0; i < periods; i++) {
                int yearToShow = comboBox_Years.getItems().get(i);
                int count = DAO_Statistics.getTotalInvoices("Năm", 0, yearToShow);
                series.getData().add(new XYChart.Data<>("Năm " + yearToShow, count));
            }
        }
        lineChartOfOrders.getData().add(series);
    }

    public void setUpTableView() {
        Label customersTablePlaceholder = new Label("Không có khách hàng mới nào trong hôm nay");
        Label invoicesTablePlaceholder = new Label("Không có hóa đơn nào mới trong hôm này ");

        customersTablePlaceholder.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: lightgray;");
        invoicesTablePlaceholder.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: lightgray;");

        table_NewCustomers.setPlaceholder(customersTablePlaceholder);
        table_NewInvoices.setPlaceholder(invoicesTablePlaceholder);
//      TODO: css lại bảng cho các ô nhỏ lại
//        table_NewCustomers.getStylesheets().add("com/huongbien/css/statistic-table.css");
//        table_NewInvoices.getStylesheets().add("com/huongbien/css/statistic-table.css");

        fillDataToCustomerTable();
        fillDataToInvoiceTable();
    }

    public void fillDataToCustomerTable() {
        tabCol_customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tabCol_customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabCol_customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tabCol_customerAccumulatedPoint.setCellValueFactory(new PropertyValueFactory<>("accumulatedPoints"));
        tabCol_customerMemLevel.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));

        ObservableList<Customer> customers = FXCollections.observableArrayList(DAO_Statistics.getNewCusomterInDay());

        table_NewCustomers.setItems(customers);
    }

    public void fillDataToInvoiceTable() {
        tabCol_InvoiceID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tabCol_TotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        tabCol_Note.setCellValueFactory(new PropertyValueFactory<>("notes"));
        tabCol_Tables.setCellValueFactory(cellDataFeatures ->
                new SimpleStringProperty(Utils.toStringTables(cellDataFeatures.getValue().getTables()))
        );

        ObservableList<Order> orders = FXCollections.observableArrayList(DAO_Statistics.getNewOrderInDay());

        table_NewInvoices.setItems(orders);
    }
}
