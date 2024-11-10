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

public class RestaurantLoginController implements Initializable {
    private final static String TEMPORARY_USER_PATH = "src/main/resources/com/huongbien/temp/loginSession.json";

    @FXML
    private TextField employeeIdField;

    @FXML
    private PasswordField hidedPasswordField;

    @FXML
    private TextField showedPasswordField;

    @FXML
    private BorderPane borderPaneCarousel;

    @FXML
    private AnchorPane compoent_hide;

    @FXML
    private AnchorPane compoent_show;

    @FXML
    private ImageView toggleShowingPasswordButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button loginButton;

    public RestaurantMainController restaurantMainController;

    public void setRestaurantMainController(RestaurantMainController _Restaurant_mainController) {
        this.restaurantMainController = _Restaurant_mainController;
    }

    private void loadCarousel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantLoginCarousel.fxml"));
            Parent carousel = loader.load();
            borderPaneCarousel.setCenter(carousel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onExitButtonClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Exit");
        alert.setHeaderText("Bạn có muốn thoát khỏi?");
        ButtonType btn_ok = new ButtonType("Ok");
        ButtonType onCancelButtonClicked = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btn_ok, onCancelButtonClicked);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btn_ok) {
            JsonArray jsonArray = new JsonArray();
            Utils.writeJsonToFile(jsonArray, TEMPORARY_USER_PATH);
            System.exit(0);
        } else if (result.isPresent() && result.get() == onCancelButtonClicked) {
            employeeIdField.requestFocus();
        }
    }

    @FXML
    void onRememberCheckBoxChecked(ActionEvent event) {

    }

    @FXML
    private boolean status = false;

    @FXML
    void onToggleShowingPasswordButtonClicked(MouseEvent event) {
        if (!status) {
            Image imgShow = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/eye-16px.png"));
            toggleShowingPasswordButton.setImage(imgShow);
            compoent_show.setVisible(true);
            showedPasswordField.setVisible(true);
            compoent_hide.setVisible(false);
            hidedPasswordField.setVisible(false);
            showedPasswordField.setText(hidedPasswordField.getText());
            status = true;
        } else {
            Image imgHide = new Image(getClass().getResourceAsStream("/com/huongbien/icon/mg_login/hiddenEye-16px.png"));
            toggleShowingPasswordButton.setImage(imgHide);
            compoent_show.setVisible(false);
            showedPasswordField.setVisible(false);
            compoent_hide.setVisible(true);
            hidedPasswordField.setVisible(true);
            hidedPasswordField.setText(showedPasswordField.getText());
            status = false;
        }
    }

    private String getCurrentPassword() {
        if (hidedPasswordField.isVisible()) {
            return hidedPasswordField.getText();
        }
        if (showedPasswordField.isVisible()) {
            return showedPasswordField.getText();
        }
        return "";
    }

    @FXML
    void onLoginButtonClicked(ActionEvent event) {
        AccountDAO accountDAO = AccountDAO.getInstance();

        String username = employeeIdField.getText();
        String password = getCurrentPassword();
        String passwordHash = Utils.hashPassword(password);

        Account account = accountDAO.getByUsername(username);

        if (account == null) {
            loginMessageLabel.setText("Tài khoản không tồn tại.");
            loginMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        if (username.equals(account.getUsername().trim()) && passwordHash.equals(account.getHashcode().trim())) {
            loginMessageLabel.setText("Đăng nhập thành công");
            loginMessageLabel.setStyle("-fx-text-fill: green;");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/RestaurantMain.fxml"));
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
                jsonArray = Utils.readJsonFromFile(TEMPORARY_USER_PATH);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }
            if (!jsonArray.isEmpty()) {
                jsonArray.remove(0);
            }
            //WRITE JSON
            String id = employeeIdField.getText();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Employee ID", id);
            jsonArray.add(jsonObject);
            Utils.writeJsonToFile(jsonArray, TEMPORARY_USER_PATH);
        } else {
            loginMessageLabel.setText("Sai mã nhân viên và mật khẩu đăng nhập");
            loginMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void onEmployeeIdFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            if (hidedPasswordField.isVisible()) {
                hidedPasswordField.requestFocus();
            }
            if (showedPasswordField.isVisible()) {
                showedPasswordField.requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML
    void onHidedPasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            loginButton.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @FXML
    void onShowedPasswordFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            loginButton.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCarousel();
    }
}
