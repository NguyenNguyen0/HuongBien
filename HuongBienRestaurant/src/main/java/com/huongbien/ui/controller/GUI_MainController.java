package com.huongbien.ui.controller;

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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class GUI_MainController implements Initializable {
    @FXML
    private Button btn_menu_hide;

    @FXML
    private Button btn_menu_show;

    @FXML
    private BorderPane compoent_menu;

    @FXML
    private HBox menubar_hide;

    @FXML
    private HBox main_overlay;

    @FXML
    private VBox menubar_overlay;

    @FXML
    private Pane panel_line;

    @FXML
    private Label label_date;

    @FXML
    private Label label_day;

    @FXML
    private Label label_time;

    @FXML
    private Label label_title;

    @FXML
    private BorderPane compoent_main;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String time = timeFormat.format(Calendar.getInstance().getTime());
            label_time.setText(time);
            String day = dayFormat.format(Calendar.getInstance().getTime());
            label_day.setText(day);
            String date = dateFormat.format(Calendar.getInstance().getTime());
            label_date.setText(date);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //eventNav
    private void openHome() {
        label_title.setText("Trang chủ");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Home.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLookup() {
        label_title.setText("Tra cứu");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Lookup.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openPreOrder() {
        label_title.setText("Đặt trước");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_PreOrder.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openOrder() {
        label_title.setText("Đặt bàn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Order.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openListOrder() {
        label_title.setText("Đơn đặt");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ListOrder.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStatistics() {
        label_title.setText("Thống kê");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Statistics.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManageBill() {
        label_title.setText("Quản lý hoá đơn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageBill.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManageCuisine() {
        label_title.setText("Quản lý món ăn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageCuisine.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManageTable() {
        label_title.setText("Quản lý bàn ăn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageTable.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManageCustomer() {
        label_title.setText("Quản lý khách hàng");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageCustomer.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManageEmployee() {
        label_title.setText("Quản lý nhân viên");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManageEmployee.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openManagePromotion() {
        label_title.setText("Quản lý khuyến mãi");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_ManagePromotion.fxml"));
            Parent home_page = loader.load();
            compoent_main.setCenter(home_page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideMenu() {
        disableButtons();
        compoent_menu.setVisible(false);
        translateAnimation(0.3, compoent_menu, -250);
        menubar_hide.setVisible(true);
        //design
        panel_line.setOpacity(1);
    }

    private void openMenu() {
        disableButtons();
        compoent_menu.setVisible(true);
        translateAnimation(0.3, compoent_menu, 250);
        //main_overlay
        FadeTransition fadeInOverlay_main_overlay = new FadeTransition(Duration.seconds(0.5), main_overlay);
        fadeInOverlay_main_overlay.setFromValue(0);
        fadeInOverlay_main_overlay.setToValue(0.6);
        fadeInOverlay_main_overlay.play();
        //menubar_overlay
        FadeTransition fadeInOverlay_menubar_overlay = new FadeTransition(Duration.seconds(0.5), menubar_overlay);
        fadeInOverlay_menubar_overlay.setFromValue(0);
        fadeInOverlay_menubar_overlay.setToValue(0.6);
        fadeInOverlay_menubar_overlay.play();
        //
        menubar_hide.setVisible(false);
        panel_line.setOpacity(0);
    }

    //navbar-hide
    @FXML
    void btn_menu_hide(ActionEvent event) {
        openMenu();
    }

    @FXML
    void btn_lookup_hide(ActionEvent event) {
        openLookup();
    }

    @FXML
    void btn_home_hide(ActionEvent event) {
        openHome();
    }

    @FXML
    void btn_preOrder_hide(ActionEvent event) {
        openPreOrder();
    }

    @FXML
    void btn_order_hide(ActionEvent event) {
        openOrder();
    }

    @FXML
    void btn_listOrder_hide(ActionEvent event) {
        openListOrder();
    }

    @FXML
    void btn_statistic_hide(ActionEvent event) {
        openStatistics();
    }

    @FXML
    void btn_bill_hide(ActionEvent event) {
        openManageBill();
    }

    @FXML
    void btn_cuisine_hide(ActionEvent event) {
        openManageCuisine();
    }

    @FXML
    void btn_table_hide(ActionEvent event) {
        openManageTable();
    }

    @FXML
    void btn_customer_hide(ActionEvent event) {
        openManageCustomer();
    }

    @FXML
    void btn_employee_hide(ActionEvent event) {
        openManageCustomer();
    }

    @FXML
    void btn_promotion_hide(ActionEvent event) {
        openManagePromotion();
    }

    //navbar-show
    @FXML
    void btn_menu_show(ActionEvent event) {
        hideMenu();
    }

    @FXML
    void btn_home_show(MouseEvent event) {
        hideMenu();
        openHome();
    }

    @FXML
    void btn_lookup_show(MouseEvent event) {
        hideMenu();
        openLookup();
    }

    @FXML
    void btn_preOrder_show(MouseEvent event) {
        hideMenu();
        openPreOrder();
    }

    @FXML
    void btn_order_show(MouseEvent event) {
        hideMenu();
        openOrder();
    }

    @FXML
    void btn_listOrder_show(MouseEvent event) {
        hideMenu();
        openListOrder();
    }

    @FXML
    void btn_statistic_show(MouseEvent event) {
        hideMenu();
        openStatistics();
    }

    @FXML
    void btn_bill_show(MouseEvent event) {
        hideMenu();
        openManageBill();
    }

    @FXML
    void btn_cuisine_show(MouseEvent event) {
        hideMenu();
        openManageCuisine();
    }

    @FXML
    void btn_table_show(MouseEvent event) {
        hideMenu();
        openManageTable();
    }

    @FXML
    void btn_customer_show(MouseEvent event) {
        hideMenu();
        openManageCustomer();
    }

    @FXML
    void btn_employee_show(MouseEvent event) {
        hideMenu();
        openManageEmployee();
    }

    @FXML
    void btn_promotion_show(MouseEvent event) {
        hideMenu();
        openManagePromotion();
    }

    @FXML
    void menu_overlay(MouseEvent event) {
        disableButtons();
        compoent_menu.setVisible(false);
        translateAnimation(0.5, compoent_menu, -250);
        menubar_hide.setVisible(true);
        //design
        panel_line.setOpacity(1);
    }

    @FXML
    void btn_exit(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn có muốn kết thúc phiên làm việc?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType btn_cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Login.fxml"));
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
        btn_menu_hide.setDisable(true);
        btn_menu_show.setDisable(true);
    }

    private void enableButtons() {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            btn_menu_hide.setDisable(false);
            btn_menu_show.setDisable(false);
        });
        pause.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compoent_menu.setTranslateX(-250);
        setTime();
    }

}
