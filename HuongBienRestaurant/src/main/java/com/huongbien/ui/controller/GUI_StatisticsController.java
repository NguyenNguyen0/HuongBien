package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Statistics;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.text.NumberFormat;
import java.time.Year;
import java.util.Currency;
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

        if (selectedYear == null || criteria == null) {
            System.out.println("Vui lòng chọn năm và loại thống kê.");
            return;
        }

        switch (criteria) {
            case "Tháng":
                int month = comboBox_Years.getSelectionModel().getSelectedItem();
                loadMonthlyStatistics(selectedYear, month);
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

    private void loadMonthlyStatistics(int year, int month) {
        double totalRevenue = DAO_Statistics.getTotalRevenue("Tháng", month, year);
        int totalInvoices = DAO_Statistics.getTotalInvoices("Tháng", month, year);
        int totalItems = DAO_Statistics.getTotalItemsOrdered("Tháng", month, year);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setCurrency(Currency.getInstance("VND"));

        txt_TotalRevenues.setText(currencyFormat.format(totalRevenue));
        txt_TotalInvoices.setText(String.valueOf(totalInvoices));
        txt_TotalOrders.setText(String.valueOf(totalItems));

        double averageRevenue = totalInvoices > 0 ? totalRevenue / totalInvoices : 0;
        txt_AverageRevenue.setText(currencyFormat.format(averageRevenue));

        txt_TotalCustomers.setText(String.valueOf(DAO_Statistics.getTotalCustomers()));
        txt_Reservations.setText(String.valueOf(DAO_Statistics.getTotalReservations()));
        txt_TotalOfInvoice.setText(String.valueOf(DAO_Statistics.getTotalInvoices()));
        txt_TotalOfRevenues.setText(String.valueOf(DAO_Statistics.getTotalRevenues()));

        updateBarChart(12, year, "Tháng");
        updateLineChart(12, year, "Tháng");
    }


    private void loadQuarterlyStatistics(int year) {
        double totalRevenue = 0;
        int totalInvoices = 0;
        int totalItems = 0;
        for (int quarter = 1; quarter <= 4; quarter++) {
            totalRevenue += DAO_Statistics.getTotalRevenue("Quý", quarter, year);
            totalInvoices += DAO_Statistics.getTotalInvoices("Quý", quarter, year);
            totalItems += DAO_Statistics.getTotalItemsOrdered("Quý", quarter, year);
        }
        txt_TotalRevenues.setText(String.format("%.0f VNĐ", totalRevenue));
        txt_TotalInvoices.setText(String.valueOf(totalInvoices));
        txt_TotalOrders.setText(String.valueOf(totalItems));

        double averageRevenue = totalInvoices > 0 ? totalRevenue / totalInvoices : 0;
        txt_AverageRevenue.setText(String.format("%.0f VNĐ", averageRevenue));

        txt_TotalCustomers.setText(String.valueOf(DAO_Statistics.getTotalCustomers()));
        txt_Reservations.setText(String.valueOf(DAO_Statistics.getTotalReservations()));
        txt_TotalOfInvoice.setText(String.valueOf(DAO_Statistics.getTotalInvoices()));
        txt_TotalOfRevenues.setText(String.valueOf(DAO_Statistics.getTotalRevenues()));

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
        txt_TotalRevenues.setText(String.format("%.0f VNĐ", totalRevenue));
        txt_TotalInvoices.setText(String.valueOf(totalInvoices));
        txt_TotalOrders.setText(String.valueOf(totalItems));

        double averageRevenue = totalInvoices > 0 ? totalRevenue / totalInvoices : 0;
        txt_AverageRevenue.setText(String.format("%.0f VNĐ", averageRevenue));
        txt_TotalCustomers.setText(String.valueOf(DAO_Statistics.getTotalCustomers()));
        txt_Reservations.setText(String.valueOf(DAO_Statistics.getTotalReservations()));
        txt_TotalOfInvoice.setText(String.valueOf(DAO_Statistics.getTotalInvoices()));
        txt_TotalOfRevenues.setText(String.valueOf(DAO_Statistics.getTotalRevenues()));


        updateBarChart(comboBox_Years.getItems().size(), 0, "Năm");
        updateLineChart(comboBox_Years.getItems().size(), 0, "Năm");
    }

    private void updateBarChart(int periods, int year, String criteria) {
        barChartOfRevenue.getData().clear();
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
}
