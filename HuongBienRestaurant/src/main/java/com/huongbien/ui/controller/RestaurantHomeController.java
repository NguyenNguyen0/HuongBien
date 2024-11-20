package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.StatisticsDAO;
import com.huongbien.entity.Employee;
import com.huongbien.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RestaurantHomeController implements Initializable {
    @FXML private Label employeeNameField;
    @FXML private Label employeePositionField;
    @FXML private MediaView mediaView;
    @FXML private Label startTimeField;
    @FXML private Label timeWorksField;
    @FXML private Label totalCustomersField;
    @FXML private Label totalReservationField;
    @FXML private Label totalOrderField;
    @FXML private Label totalRevenuesField;

    private LocalDateTime startTime;
    private ScheduledExecutorService scheduler;

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL resource = getClass().getClassLoader().getResource("com/huongbien/img/banner/banner.mp4");
        String mediaPath = null;
        Media media = null;
        if (resource != null) {
            mediaPath = resource.toString();
            media = new Media(mediaPath);
        } else {
            System.out.println("Tệp video không tìm thấy.");
        }
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaView.setFitWidth(1680.0);
        mediaView.setFitHeight(513.0);

        ((AnchorPane) mediaView.getParent()).setPadding(new Insets(0, 0, 0, 50));
        mediaView.setPreserveRatio(false);

        startTime = LocalDateTime.now();
        displayStartTime();
        updateTimeWorks();
        setStatistics();

        Platform.runLater(() -> {
            Stage stage = (Stage) mediaView.getScene().getWindow();
            stage.setOnCloseRequest(event -> stopScheduler());
        });

        //set emp
        try {
            loadEmployeeInfoFromJSON();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        startTimeField.setText(startTime.format(formatter));
    }

    private void updateTimeWorks() {
        LocalDateTime now = LocalDateTime.now();
        long secondsWorked = java.time.Duration.between(startTime, now).getSeconds();
        long hours = secondsWorked / 3600;
        long minutes = (secondsWorked % 3600) / 60;
        long seconds = secondsWorked % 60;

        Platform.runLater(() -> {
            timeWorksField.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        });

        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(this::updateTimeWorks, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void setStatistics() {
        try {
            totalCustomersField.setText(String.valueOf(StatisticsDAO.getTotalCustomers()));
            totalRevenuesField.setText(Utils.formatMoney(StatisticsDAO.getTotalRevenues()));
            totalReservationField.setText(String.valueOf(StatisticsDAO.getTotalReservations()));
            totalOrderField.setText(String.valueOf(StatisticsDAO.getTotalInvoices()));
        } catch (Exception e) {
            System.err.println("Error fetching statistics: " + e.getMessage());
        }
    }

    private void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Scheduler stopped.");
        }
    }

    private void loadEmployeeInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            Employee employee = employeeDAO.getById(id).getFirst();
            employeeNameField.setText(employee.getName());
            employeePositionField.setText(employee.getPosition());
        }
    }

}
