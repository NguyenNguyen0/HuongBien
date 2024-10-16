package com.huongbien.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GUI_LoginController implements Initializable {
    @FXML
    private TextField txt_empID;

    @FXML
    private PasswordField txt_pwdHide;

    @FXML
    private TextField txt_pwdShow;

    @FXML
    private BorderPane borderPaneCarousel;

    @FXML
    private AnchorPane compoent_hide;

    @FXML
    private AnchorPane compoent_show;

    @FXML
    private ImageView exit;

    @FXML
    private ImageView togglePwdBtn;

    private void loadCarousel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Login_Carousel.fxml"));
            Parent carousel = loader.load();
            borderPaneCarousel.setCenter(carousel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btn_exit(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn có muốn thoát khỏi?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType btn_cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, btn_cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            System.exit(0);
        } else if (result.isPresent() && result.get() == btn_cancel) {
            txt_empID.requestFocus();
        }
    }

    @FXML
    void checkBox_remember(ActionEvent event) {

    }

    @FXML
    private boolean status = false;

    @FXML
    void togglePwdBtn(MouseEvent event) {
        if(!status) {
            Image imgShow = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/eye-16px.png"));
            togglePwdBtn.setImage(imgShow);
            compoent_show.setVisible(true);
            compoent_hide.setVisible(false);
            txt_pwdShow.setText(txt_pwdHide.getText()+"");
            status = true;
        } else {
            Image imgHide = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/hiddenEye-16px.png"));
            togglePwdBtn.setImage(imgHide);
            compoent_show.setVisible(false);
            compoent_hide.setVisible(true);
            txt_pwdHide.setText(txt_pwdShow.getText()+"");
            status = false;
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Main.fxml"));
            Parent root = loader.load();

            Scene mainScene = new Scene(root);

            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setMaximized(true);
            mainStage.setTitle("Dashboard - Huong Bien Restaurant");
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCarousel();
    }
}
