package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Employee;
import com.huongbien.entity.OrderDetail;
import com.huongbien.utils.Converter;
import com.huongbien.utils.RefreshJSON;
import com.huongbien.utils.ToastsMessage;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RestaurantMainController implements Initializable {
    @FXML
    private Label userNameDetailUserLabel;
    @FXML
    private Circle avatarDetailUserCircle;
    @FXML
    private Circle avatarMainCircle;
    @FXML
    public Label employeeNameLabel;
    @FXML
    private Button hideMenuButton;
    @FXML
    private Button showMenuButton;
    @FXML
    private HBox hidedMenuBarHBox;
    @FXML
    private HBox hideDetailUserHBox;
    @FXML
    private HBox menuOverlayHBox;
    @FXML
    private HBox detailUserOverlayHBox;
    @FXML
    private VBox menuBarOverlayHBox;
    @FXML
    private VBox detailUserBarOverlayHBox;
    @FXML
    private Pane linePane;
    @FXML
    private Label currentDateLabel;
    @FXML
    private Label currentDayLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    public Label featureTitleLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private BorderPane menuBorderPane;
    @FXML
    private BorderPane detailUserBorderPane;
    @FXML
    private Label nameDetailUserLabel;
    @FXML
    private TextField idDetailUserField;
    @FXML
    private TextField nameDetailUserField;
    @FXML
    private DatePicker birthDateDetailUserDatePicker;
    @FXML
    private TextField phoneDetailUserField;
    @FXML
    private TextField emailDetailUserField;
    @FXML
    private PasswordField currentPwdField;
    @FXML
    private PasswordField newPwdField;
    @FXML
    private PasswordField new2PwdField;
    @FXML
    private Label roleDetailUserLabel;
    @FXML
    private Label hourPayDetailUserLabel;
    @FXML
    private Label salaryDetailUserLabel;
    @FXML
    private Label countHourWorkDetailUserLabel;
    @FXML
    private Label worksHourDetailUserLabel;


    //timer
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private byte[] employeeImageBytes = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefault();
        try {
            loadUserInfoFromJSON();
            openHome();
            setDetailUserInfo();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        setTime();
        menuBorderPane.setTranslateX(-250);
        detailUserBorderPane.setTranslateX(250);

    }

    public void setDefault() {
        mainBorderPane.setVisible(true);
        menuBorderPane.setVisible(false);
        detailUserBorderPane.setVisible(false);
    }

    public void setDetailUserInfo() throws FileNotFoundException {
        employeeImageBytes = null; //reset byte image
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            Employee employee = employeeDAO.getOneById(id);
            nameDetailUserLabel.setText(employee.getName());
            userNameDetailUserLabel.setText("@"+employee.getEmployeeId());
            setAvatar(employee.getProfileImage());
            //table Pane
            idDetailUserField.setText(employee.getEmployeeId());
            nameDetailUserField.setText(employee.getName());
            birthDateDetailUserDatePicker.setValue(employee.getBirthday());
            phoneDetailUserField.setText(employee.getPhoneNumber());
            emailDetailUserField.setText(employee.getEmail());
            roleDetailUserLabel.setText(employee.getPosition());
            hourPayDetailUserLabel.setText(Converter.formatMoney(employee.getHourlyPay())+" VNĐ/h");
            salaryDetailUserLabel.setText(Converter.formatMoney(employee.getSalary())+" VNĐ");
            worksHourDetailUserLabel.setText((int)(employee.getWorkHours())+" Giờ");
        }
    }

    //TODO: chưa xử lý độ phân giải của ảnh
    public void setAvatar(byte[] profileImage) {
        try {
            if (profileImage == null || profileImage.length == 0) {
                throw new IllegalArgumentException("Profile image is null or empty");
            }
            Image image = new Image(Converter.bytesToInputStream(profileImage)); //fix here
            avatarMainCircle.setFill(new ImagePattern(image));
            avatarDetailUserCircle.setFill(new ImagePattern(image));
        } catch (Exception e) {
            avatarMainCircle.setFill(new ImagePattern(new Image("/com/huongbien/img/avatar/avatar-default-128px.png")));
            avatarDetailUserCircle.setFill(new ImagePattern(new Image("/com/huongbien/img/avatar/avatar-default-128px.png")));
        }
    }

    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String time = timeFormat.format(Calendar.getInstance().getTime());
            currentTimeLabel.setText(time);
            String day = dayFormat.format(Calendar.getInstance().getTime());
            currentDayLabel.setText(day);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            currentDateLabel.setText(date);
            //count hour work
            try {
                String[] timeParts = countHourWorkDetailUserLabel.getText().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }
                countHourWorkDetailUserLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            } catch (Exception e) {
                // Khởi tạo lại khi nhãn không hợp lệ
                countHourWorkDetailUserLabel.setText("00:00:00");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void openHome() throws IOException {
        featureTitleLabel.setText("Trang chủ");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantHome.fxml"));
        BorderPane home = loader.load();
        mainBorderPane.setCenter(home);
        home.prefWidthProperty().bind(mainBorderPane.widthProperty());
        home.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openLookup() throws IOException {
        featureTitleLabel.setText("Tra cứu bàn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantLookup.fxml"));
        BorderPane lookup = loader.load();
        mainBorderPane.setCenter(lookup);
        lookup.prefWidthProperty().bind(mainBorderPane.widthProperty());
        lookup.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        RestaurantLookupController restaurantLookupController = loader.getController();
        restaurantLookupController.setRestaurantMainController(this);
    }

    public void openOrderTable() throws IOException {
        featureTitleLabel.setText("Chọn bàn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderTable.fxml"));
        BorderPane orderTable = loader.load();
        mainBorderPane.setCenter(orderTable);
        orderTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderTableController orderTableController = loader.getController();
        orderTableController.setRestaurantMainController(this);
    }

    public void openPreOrder() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Thông tin đặt trước");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/PreOrder.fxml"));
        BorderPane preOrderTable = loader.load();
        mainBorderPane.setCenter(preOrderTable);
        preOrderTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        preOrderTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        PreOrderController preOrderController = loader.getController();
        preOrderController.setRestaurantMainController(this);
    }

    public void openOrderCuisine() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderCuisine.fxml"));
        BorderPane orderCuisine = loader.load();
        mainBorderPane.setCenter(orderCuisine);
        orderCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderCuisineController orderCuisineController = loader.getController();
        orderCuisineController.setRestaurantMainController(this);
    }

    public void openPreOrderCuisine() throws IOException {
        featureTitleLabel.setText("Chọn bàn  - Thông tin đặt trước -  Đặt món");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/PreOrderCuisine.fxml"));
        BorderPane orderCuisine = loader.load();
        mainBorderPane.setCenter(orderCuisine);
        orderCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        PreOrderCuisineController preOrderCuisineController = loader.getController();
        preOrderCuisineController.setRestaurantMainController(this);
    }

    public void openOrderPayment() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món  -  Tính tiền");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderPayment.fxml"));
        BorderPane orderPayment = loader.load();
        mainBorderPane.setCenter(orderPayment);
        orderPayment.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderPayment.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderPaymentController orderPaymentController = loader.getController();
        orderPaymentController.setRestaurantMainController(this);
    }

    public void openOrderPaymentFinal() throws IOException {
        featureTitleLabel.setText("Chọn bàn  -  Đặt món  -  Tính tiền  -  Thanh toán");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderPaymentFinal.fxml"));
        BorderPane orderPaymentFinal = loader.load();
        mainBorderPane.setCenter(orderPaymentFinal);
        orderPaymentFinal.prefWidthProperty().bind(mainBorderPane.widthProperty());
        orderPaymentFinal.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        OrderPaymentFinalController orderPaymentFinalController = loader.getController();
        orderPaymentFinalController.setRestaurantMainController(this);
    }

    public void openStatistics() throws IOException {
        featureTitleLabel.setText("Thống kê");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantStatistics.fxml"));
        BorderPane statistics = loader.load();
        mainBorderPane.setCenter(statistics);
        statistics.prefWidthProperty().bind(mainBorderPane.widthProperty());
        statistics.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openReservationManagement() throws IOException {
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

    public void openOrderManagement() throws IOException {
        featureTitleLabel.setText("Quản lý hoá đơn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/OrderManagement.fxml"));
        BorderPane manageBill = loader.load();
        mainBorderPane.setCenter(manageBill);
        manageBill.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageBill.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openCuisineManagement() throws IOException {
        featureTitleLabel.setText("Quản lý món ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/CuisineManagement.fxml"));
        BorderPane manageCuisine = loader.load();
        mainBorderPane.setCenter(manageCuisine);
        manageCuisine.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCuisine.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openTableManagement() throws IOException {
        featureTitleLabel.setText("Quản lý bàn ăn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/TableManagement.fxml"));
        BorderPane manageTable = loader.load();
        mainBorderPane.setCenter(manageTable);
        manageTable.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageTable.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openCustomerManagement() throws IOException {
        featureTitleLabel.setText("Quản lý khách hàng");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/CustomerManagement.fxml"));
        BorderPane manageCustomer = loader.load();
        mainBorderPane.setCenter(manageCustomer);
        manageCustomer.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageCustomer.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openEmployeeManagement() throws IOException {
        featureTitleLabel.setText("Quản lý nhân viên");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/EmployeeManagement.fxml"));
        BorderPane manageEmployee = loader.load();
        mainBorderPane.setCenter(manageEmployee);
        manageEmployee.prefWidthProperty().bind(mainBorderPane.widthProperty());
        manageEmployee.prefHeightProperty().bind(mainBorderPane.heightProperty());
        //setController
        EmployeeManagementController employeeManagementController = loader.getController();
        employeeManagementController.setRestaurantMainController(this);
    }

    public void openPromotionManagement() throws IOException {
        featureTitleLabel.setText("Quản lý khuyến mãi");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/PromotionManagement.fxml"));
        BorderPane managePromotion = loader.load();
        mainBorderPane.setCenter(managePromotion);
        managePromotion.prefWidthProperty().bind(mainBorderPane.widthProperty());
        managePromotion.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    public void openRestaurantHelp() throws IOException {
        featureTitleLabel.setText("Help");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantHelp.fxml"));
        BorderPane help = loader.load();
        mainBorderPane.setCenter(help);
        help.prefWidthProperty().bind(mainBorderPane.widthProperty());
        help.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }
    public void openRestaurantAbout() throws IOException {
        featureTitleLabel.setText("About");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantAbout.fxml"));
        BorderPane help = loader.load();
        mainBorderPane.setCenter(help);
        help.prefWidthProperty().bind(mainBorderPane.widthProperty());
        help.prefHeightProperty().bind(mainBorderPane.heightProperty());
    }

    //navbar-hide
    @FXML
    void onHideMenuButtonClicked(ActionEvent event) {
        openMenu();
    }

    @FXML
    void onOpenDetailUserHBoxClicked(MouseEvent event) {
        openDetailUser();
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
        openReservationManagement();
    }

    @FXML
    void onHideStatisticButtonClicked(ActionEvent event) throws IOException {
        openStatistics();
    }

    @FXML
    void onHideBillButtonClicked(ActionEvent event) throws IOException {
        openOrderManagement();
    }

    @FXML
    void onHideCuisineButtonClicked(ActionEvent event) throws IOException {
        openCuisineManagement();
    }

    @FXML
    void onHideTableButtonClicked(ActionEvent event) throws IOException {
        openTableManagement();
    }

    @FXML
    void onHideCustomerButtonClicked(ActionEvent event) throws IOException {
        openCustomerManagement();
    }

    @FXML
    void onHideEmployeeButtonClicked(ActionEvent event) throws IOException {
        openEmployeeManagement();
    }

    @FXML
    void onHidePromotionButtonClicked(ActionEvent event) throws IOException {
        openPromotionManagement();
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
        openReservationManagement();
    }

    @FXML
    void onShowStatisticButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openStatistics();
    }

    @FXML
    void onShowBillButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openOrderManagement();
    }

    @FXML
    void onShowCuisineButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openCuisineManagement();
    }

    @FXML
    void onShowTableButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openTableManagement();
    }

    @FXML
    void onShowCustomerButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openCustomerManagement();
    }

    @FXML
    void onShowEmployeeButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openEmployeeManagement();
    }

    @FXML
    void onShowPromotionButtonClicked(MouseEvent event) throws IOException {
        hideMenu();
        openPromotionManagement();
    }

    //service button
    @FXML
    void onShowRestaurantHelpButtonAction(ActionEvent event) throws IOException {
        hideMenu();
        openRestaurantHelp();
    }
    @FXML
    void onShowRestaurantAboutButtonAction(ActionEvent event) throws IOException {
        hideMenu();
        openRestaurantAbout();
    }

    public void translateAnimationMenu(double duration, Node node, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(toX);
        translateTransition.setOnFinished(event -> enableMenuButtons());
        translateTransition.play();
    }

    public void translateAnimationDetailUser(double duration, Node node, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
        translateTransition.setByX(toX);
        translateTransition.setOnFinished(event -> enableDetailUserButtons());
        translateTransition.play();
    }

    //fix bug khi translate khong dung vi tri =)))
    private void disableMenuButtons() {
        hideMenuButton.setDisable(true);
        showMenuButton.setDisable(true);
    }

    private void disableDetailUserButtons() {
        hideDetailUserHBox.setDisable(true);
    }

    private void enableMenuButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            hideMenuButton.setDisable(false);
            showMenuButton.setDisable(false);
        });
        pause.play();
    }

    private void enableDetailUserButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            hideDetailUserHBox.setDisable(false);
        });
        pause.play();
    }

    public void loadUserInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            List<Employee> employees = employeeDAO.getManyById(id);
            Employee employee = (employees.isEmpty() ? null : employees.get(0));
            assert employee != null;
            employeeNameLabel.setText(employee.getName());
            setAvatar(employee.getProfileImage());
        }
    }

    private void openMenu() {
        disableMenuButtons();
        menuBorderPane.setVisible(true);
        translateAnimationMenu(0.3, menuBorderPane, 250);
        //menuOverlayHBox
        FadeTransition fadeInOverlay_menuOverlayHBox = new FadeTransition(Duration.seconds(0.5), menuOverlayHBox);
        fadeInOverlay_menuOverlayHBox.setFromValue(0);
        fadeInOverlay_menuOverlayHBox.setToValue(0.6);
        fadeInOverlay_menuOverlayHBox.play();
        //menuBarOverlayHBox
        FadeTransition fadeInOverlay_menuBarOverlayHBox = new FadeTransition(Duration.seconds(0.5), menuBarOverlayHBox);
        fadeInOverlay_menuBarOverlayHBox.setFromValue(0);
        fadeInOverlay_menuBarOverlayHBox.setToValue(0.6);
        fadeInOverlay_menuBarOverlayHBox.play();
        //
        hidedMenuBarHBox.setVisible(false);
        linePane.setOpacity(0);
    }

    private void openDetailUser() {
        detailUserBorderPane.setVisible(true);
        translateAnimationDetailUser(0.2, detailUserBorderPane, -250);
        //detailUserOverlayHBox
        FadeTransition fadeInOverlay_detailUserOverlayHBox = new FadeTransition(Duration.seconds(0.5), detailUserOverlayHBox);
        fadeInOverlay_detailUserOverlayHBox.setFromValue(0);
        fadeInOverlay_detailUserOverlayHBox.setToValue(0.6);
        fadeInOverlay_detailUserOverlayHBox.play();
        //detailUserBarOverlayHBox
        FadeTransition fadeInOverlay_detailUserBarOverlayHBox = new FadeTransition(Duration.seconds(0.5), detailUserBarOverlayHBox);
        fadeInOverlay_detailUserBarOverlayHBox.setFromValue(0);
        fadeInOverlay_detailUserBarOverlayHBox.setToValue(0.6);
        fadeInOverlay_detailUserBarOverlayHBox.play();
    }

    @FXML
    void onMenuOverlayHBoxClicked(MouseEvent event) {
        disableMenuButtons();
        menuBorderPane.setVisible(false);
        translateAnimationMenu(0.5, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    private void hideMenu() {
        disableMenuButtons();
        menuBorderPane.setVisible(false);
        translateAnimationMenu(0.3, menuBorderPane, -250);
        hidedMenuBarHBox.setVisible(true);
        //design
        linePane.setOpacity(1);
    }

    @FXML
    void onDetailUserOverlayHBoxClicked(MouseEvent event) throws FileNotFoundException {
        disableDetailUserButtons();
        detailUserBorderPane.setVisible(false);
        translateAnimationDetailUser(0.5, detailUserBorderPane, 250);
        //reset detail User Info
        setDetailUserInfo();
    }

    @FXML
    public void onImageChooserButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                employeeImageBytes = Converter.fileToBytes(selectedFile);
                Image image = new Image(Converter.bytesToInputStream(employeeImageBytes));
                avatarMainCircle.setFill(new ImagePattern(image));
                avatarDetailUserCircle.setFill(new ImagePattern(image));
            } catch (IOException e) {
                ToastsMessage.showToastsMessage("Lỗi", "Không thể mở ảnh");
            }
        }
    }

    @FXML
    void saveInfoDetailUserButtonAction(ActionEvent event) throws FileNotFoundException {
        byte[] profileImage = employeeImageBytes;
        String name = nameDetailUserField.getText();
        LocalDate birthDate = birthDateDetailUserDatePicker.getValue();
        String phone = phoneDetailUserField.getText();
        String email = emailDetailUserField.getText();
        //TODO: update info to database
        System.out.println(profileImage + " " + name + " " + birthDate + " " + phone + " " + email);
        ToastsMessage.showMessage("Chức năng đang phát triển (Chưa update vào database)", "warning");
        //---write here
//

        //update info
        setDetailUserInfo();
    }

    @FXML
    void updatePwdDetailUserButtonAction(ActionEvent event) {
        //TODO: regex valid form change password
        String currentPwd = currentPwdField.getText();
        String newPwd = newPwdField.getText();
        String new2Pwd = new2PwdField.getText();
        System.out.println(currentPwd + " " + newPwd + " " + new2Pwd);
        //TODO: update password to database

        ToastsMessage.showMessage("Chức năng đang phát triển (Chưa update vào database)", "warning");
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        //TODO: lấy ra số giờ đã làm trong phiên làm việc => Chưa cộng vào tổng số giờ làm việc xuống database!
        String currentEmpID = idDetailUserField.getText();
        int hours = Integer.parseInt(countHourWorkDetailUserLabel.getText().split(":")[0]);
        System.out.println("Employee ID: " + currentEmpID + " - Hours: " + hours);
        //---Write here...........

        alert.setHeaderText("Bạn có muốn kết thúc phiên làm việc, với số giờ đã ghi nhận là " + hours + " giờ?");
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
            RefreshJSON.clearAllJSON();
        }
    }
}
