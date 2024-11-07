package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huongbien.dao.AccountDAO;
import com.huongbien.entity.Account;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GUI_LoginController implements Initializable {
    private final static String path_user = "src/main/resources/com/huongbien/temp/loginSession.json";

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
    private ImageView togglePwdBtn;

    @FXML
    private Label text_message;

    @FXML
    private Button handleLogin;

    public GUI_MainController gui_mainController;

    public void setGUI_MainController(GUI_MainController gui_mainController) {
        this.gui_mainController = gui_mainController;
    }

    private void loadCarousel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_LoginCarousel.fxml"));
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
            JsonArray jsonArray = new JsonArray();
            Utils.writeJsonToFile(jsonArray, path_user);
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
        if (!status) {
            Image imgShow = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/eye-16px.png"));
            togglePwdBtn.setImage(imgShow);
            compoent_show.setVisible(true);
            txt_pwdShow.setVisible(true);
            compoent_hide.setVisible(false);
            txt_pwdHide.setVisible(false);
            txt_pwdShow.setText(txt_pwdHide.getText());
            status = true;
        } else {
            Image imgHide = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/hiddenEye-16px.png"));
            togglePwdBtn.setImage(imgHide);
            compoent_show.setVisible(false);
            txt_pwdShow.setVisible(false);
            compoent_hide.setVisible(true);
            txt_pwdHide.setVisible(true);
            txt_pwdHide.setText(txt_pwdShow.getText());
            status = false;
        }
    }

    private String getCurrentPwd() {
        if (txt_pwdHide.isVisible()) {
            return txt_pwdHide.getText();
        }
        if (txt_pwdShow.isVisible()) {
            return txt_pwdShow.getText();
        }
        return "";
    }

    @FXML
    void handleLogin(ActionEvent event) {
        AccountDAO accountDAO = AccountDAO.getInstance();

        String username = txt_empID.getText();
        String password = getCurrentPwd();
        String passwordHash = Utils.hashPassword(password);

        Account account = accountDAO.getByUsername(username);

        if (account == null) {
            text_message.setText("Tài khoản không tồn tại.");
            text_message.setStyle("-fx-text-fill: red;");
            return;
        }
        if (username.equals(account.getUsername().trim()) && passwordHash.equals(account.getHashcode().trim())) {
            text_message.setText("Đăng nhập thành công");
            text_message.setStyle("-fx-text-fill: green;");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/GUI_Main.fxml"));
                Parent root = loader.load();
                //
                Scene mainScene = new Scene(root);
                //
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                //
                Stage mainStage = new Stage();
                mainStage.setScene(mainScene);
                mainStage.setMaximized(true);
                mainStage.setTitle("Dashboard - Huong Bien Restaurant");
                mainStage.initStyle(StageStyle.UNDECORATED);
                mainStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //write JSON user current
            JsonArray jsonArray;
            try {
                jsonArray = Utils.readJsonFromFile(path_user);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }
            if (!jsonArray.isEmpty()) {
                jsonArray.remove(0);
            }
            //WRITE JSON
            String id = txt_empID.getText();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Employee ID", id);
            jsonArray.add(jsonObject);
            Utils.writeJsonToFile(jsonArray, path_user);
        } else {
            text_message.setText("Sai mã nhân viên và mật khẩu đăng nhập");
            text_message.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void txt_empID(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            if (txt_pwdHide.isVisible()) {
                txt_pwdHide.requestFocus();
            }
            if (txt_pwdShow.isVisible()) {
                txt_pwdShow.requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin.fire();
        }
    }

    @FXML
    void txt_pwdHide(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            handleLogin.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin.fire();
        }
    }

    @FXML
    void txt_pwdShow(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            handleLogin.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin.fire();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCarousel();
    }
}
