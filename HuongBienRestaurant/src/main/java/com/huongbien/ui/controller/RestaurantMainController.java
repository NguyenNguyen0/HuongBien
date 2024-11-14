package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.entity.Employee;
import com.huongbien.utils.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RestaurantMainController implements Initializable {
    private final static String TEMPORARY_USER_PATH = "src/main/resources/com/huongbien/temp/loginSession.json";

    @FXML
    public Label employeeNameLabel;

    @FXML
    private Button hideMenuButton;

    @FXML
    private Button showMenuButton;

    @FXML
    private BorderPane menuBorderPane;

    @FXML
    private HBox hidedMenuBarHBox;

    @FXML
    private HBox mainOverlayHBox;

    @FXML
    private VBox overlayMenubarVBox;

    @FXML
    private Pane linePane;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Label currentDayLabel;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label featureTitleLabel;

    @FXML
    private BorderPane mainBorderPane;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String time = timeFormat.format(Calendar.getInstance().getTime());
            currentTimeLabel.setText(time);
            String day = dayFormat.format(Calendar.getInstance().getTime());
            currentDayLabel.setText(day);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            currentDateLabel.setText(date);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //eventNav
    public void openHome() throws IOException {
        featureTitleLabel.setText("Trang chủ");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantHome.fxml"));
        BorderPane home = loader.load();
        mainBorderPane.setCenter(home);
        home.prefWidthProperty().bind(mainBorderPane.widthProperty());
        home.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openLookup() throws IOException {
        featureTitleLabel.setText("Tra cứu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantLookup.fxml"));
        BorderPane lookup = loader.load();
        mainBorderPane.setCenter(lookup);
        lookup.prefWidthProperty().bind(mainBorderPane.widthProperty());
        lookup.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openOrderTable() throws IOException {
        featureTitleLabel.setText("Đặt bàn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderTable.fxml"));
        BorderPane orderTable = loader.load();
        mainBorderPane.setCenter(orderTable);
        orderTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderTableController orderTableController = loader.getController();
        orderTableController.setRestaurantMainController(this);
    }

    public void openOrderCuisine() throws IOException {
        featureTitleLabel.setText("Đặt bàn  -  Đặt món");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderCuisine.fxml"));
        BorderPane orderCuisine = loader.load();
        mainBorderPane.setCenter(orderCuisine);
        orderCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderCuisineController orderCuisineController = loader.getController();
        orderCuisineController.setRestaurantMainController(this);
    }

    public void openOrderPayment() throws IOException {
        featureTitleLabel.setText("Đặt bàn  -  Đặt món  -  Thanh toán");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderPayment.fxml"));
        BorderPane orderPayment = loader.load();
        mainBorderPane.setCenter(orderPayment);
        orderPayment.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderPayment.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderPaymentController orderPaymentController = loader.getController();
        orderPaymentController.setRestaurantMainController(this);
    }

    public void openStatistics() throws IOException {
        featureTitleLabel.setText("Thống kê");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantStatistics.fxml"));
        BorderPane statistics = loader.load();
        mainBorderPane.setCenter(statistics);
        statistics.prefWidthProperty().bind(mainBorderPane.widthProperty());
        statistics.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void ReservationManagement() throws IOException {
        featureTitleLabel.setText("Quản lý đơn đặt");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/ReservationManagement.fxml"));
        BorderPane listOrder = loader.load();
        mainBorderPane.setCenter(listOrder);
        listOrder.prefWidthProperty().bind(mainBorderPane.widthProperty());
        listOrder.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        ReservationManagementController reservationManagementController = loader.getController();
        reservationManagementController.setRestaurantMainController(this);
    }

    public void openManageBill() throws IOException {
        featureTitleLabel.setText("Quản lý hoá đơn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderManagement.fxml"));
        BorderPane manageBill = loader.load();
        mainBorderPane.setCenter(manageBill);
        manageBill.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageBill.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openManageCuisine() throws IOException {
        featureTitleLabel.setText("Quản lý món ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/CuisineManagement.fxml"));
        BorderPane manageCuisine = loader.load();
        mainBorderPane.setCenter(manageCuisine);
        manageCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openManageTable() throws IOException {
        featureTitleLabel.setText("Quản lý bàn ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/TableManagement.fxml"));
        BorderPane manageTable = loader.load();
        mainBorderPane.setCenter(manageTable);
        manageTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openManageCustomer() throws IOException {
        featureTitleLabel.setText("Quản lý khách hàng");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/CustomerManagement.fxml"));
        BorderPane manageCustomer = loader.load();
        mainBorderPane.setCenter(manageCustomer);
        manageCustomer.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCustomer.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openManageEmployee() throws IOException {
        featureTitleLabel.setText("Quản lý nhân viên");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/EmployeeManagement.fxml"));
        BorderPane manageEmployee = loader.load();
        mainBorderPane.setCenter(manageEmployee);
        manageEmployee.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageEmployee.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openManagePromotion() throws IOException {
        featureTitleLabel.setText("Quản lý khuyến mãi");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/PromotionManagement.fxml"));
        BorderPane managePromotion = loader.load();
        mainBorderPane.setCenter(managePromotion);
        managePromotion.prefWidthProperty().bind(mainBorderPane.widthProperty());
        managePromotion.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    private void hideMenu() {
        disableButtons();
        menuBorderPane.setVisible(false);
        translateAnimation(0.3, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    private void openMenu() {
        disableButtons();
        menuBorderPane.setVisible(true);
        translateAnimation(0.3, menuBorderPane, 250);
        //mainOverlayHBox
        FadeTransition fadeInOverlay_mainOverlayHBox = new FadeTransition(Duration.seconds(0.5), mainOverlayHBox);
        fadeInOverlay_mainOverlayHBox.setFromValue(0);
        fadeInOverlay_mainOverlayHBox.setToValue(0.6);
        fadeInOverlay_mainOverlayHBox.play();
        //overlayMenubarVBox
        FadeTransition fadeInOverlay_overlayMenubarVBox = new FadeTransition(Duration.seconds(0.5), overlayMenubarVBox);
        fadeInOverlay_overlayMenubarVBox.setFromValue(0);
        fadeInOverlay_overlayMenubarVBox.setToValue(0.6);
        fadeInOverlay_overlayMenubarVBox.play();
        //
        hidedMenuBarHBox.setVisible(false);
        linePane.setOpacity(0);
    }

    //navbar-hide
    @FXML
    void onHideMenuButtonClicked(ActionEvent event) {
        openMenu();
    }

    @FXML
    void onHideHomeButtonClicked(ActionEvent event) throws IOException {
        openHome();
    }

    @FXML
    void onHideLookupButtonClicked(ActionEvent event) throws IOException {
        openLookup();
    }

    @FXML
    void onHideOrderButtonClicked(ActionEvent event) throws IOException {
        openOrderTable();
    }

    @FXML
    void onHideReservationButtonClicked(ActionEvent event) throws IOException {
        ReservationManagement();
    }

    @FXML
    void onHideStatisticButtonClicked(ActionEvent event) throws IOException {
        openStatistics();
    }

    @FXML
    void onHideBillButtonClicked(ActionEvent event) throws IOException {
        openManageBill();
    }

    @FXML
    void onHideCuisineButtonClicked(ActionEvent event) throws IOException {
        openManageCuisine();
    }

    @FXML
    void onHideTableButtonClicked(ActionEvent event) throws IOException {
        openManageTable();
    }

    @FXML
    void onHideCustomerButtonClicked(ActionEvent event) throws IOException {
        openManageCustomer();
    }

    @FXML
    void onHideEmployeeButtonClicked(ActionEvent event) throws IOException {
        openManageEmployee();
    }

    @FXML
    void onHidePromotionButtonClicked(ActionEvent event) throws IOException {
        openManagePromotion();
    }

    //navbar-show
    @FXML
    void onShowMenuButtonClicked(ActionEvent event) {
        hideMenu();
    }

    @FXML
    void onShowHomeButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openHome();
    }

    @FXML
    void onShowLookupButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openLookup();
    }

    @FXML
    void onShowOrderButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openOrderTable();
    }

    @FXML
    void onShowReservationButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        ReservationManagement();
    }

    @FXML
    void onShowStatisticButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openStatistics();
    }

    @FXML
    void onShowBillButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManageBill();
    }

    @FXML
    void onShowCuisineButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManageCuisine();
    }

    @FXML
    void onShowTableButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManageTable();
    }

    @FXML
    void onShowCustomerButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManageCustomer();
    }

    @FXML
    void onShowEmployeeButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManageEmployee();
    }

    @FXML
    void onShowPromotionButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openManagePromotion();
    }

    @FXML
    void onMainOverlayHBoxClicked(MouseEvent event) {
        disableButtons();
        menuBorderPane.setVisible(false);
        translateAnimation(0.5, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn có muốn kết thúc phiên làm việc?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType onCancelButtonClicked = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, onCancelButtonClicked);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantLogin.fxml"));
                Parent root = loader.load();
                Scene mainScene = new Scene(root);
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                Stage mainStage = new Stage();
                mainStage.setScene(mainScene);
                mainStage.setMaximized(true);
                mainStage.setTitle("Login - Huong Bien Restaurant");
                mainStage.initStyle(StageStyle.UNDECORATED);
                mainStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonArray jsonArray = new JsonArray();
            Utils.writeJsonToFile(jsonArray, TEMPORARY_USER_PATH);
        }
    }

    public void translateAnimation(double duration, Node node, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(toX);
        translateTransition.setOnFinished(event -> enableButtons());
        translateTransition.play();
    }

    //fix bug khi translate khong dung vi tri =)))
    private void disableButtons() {
        hideMenuButton.setDisable(true);
        showMenuButton.setDisable(true);
    }

    private void enableButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            hideMenuButton.setDisable(false);
            showMenuButton.setDisable(false);
        });
        pause.play();
    }

    private void loadEmployeeInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(TEMPORARY_USER_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            List<Employee> employees = employeeDAO.getById(id);
            Employee employee = (employees.isEmpty() ? null : employees.get(0));
            assert employee != null;
            employeeNameLabel.setText(employee.getName());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuBorderPane.setTranslateX(-250);
        setTime();
        try {
            openHome();
            loadEmployeeInfoFromJSON();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
