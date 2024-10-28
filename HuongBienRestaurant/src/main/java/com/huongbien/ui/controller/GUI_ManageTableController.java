package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Category;
import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.database.Database;
import com.huongbien.entity.Category;
import com.huongbien.entity.Cuisine;
import com.huongbien.utils.Converter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_ManageTableController implements Initializable {
    @FXML
    private Button btn_tabClear;

    @FXML
    private Button btn_tabMain;

    @FXML
    private Button btn_tabSub;

    @FXML
    private ComboBox<?> comboBox_tabFloor;

    @FXML
    private ComboBox<?> comboBox_tabStatus;

    @FXML
    private ComboBox<?> comboBox_tabType;

    @FXML
    private ImageView imgView_tab;

    @FXML
    private TableColumn<?, ?> tabCol_tabFloor;

    @FXML
    private TableColumn<?, ?> tabCol_tabID;

    @FXML
    private TableColumn<?, ?> tabCol_tabName;

    @FXML
    private TableColumn<?, ?> tabCol_tabSeats;

    @FXML
    private TableColumn<?, ?> tabCol_tabStatus;

    @FXML
    private TableColumn<?, ?> tabCol_tabType;

    @FXML
    private TableView<?> tabViewTab;

    @FXML
    private TextField txt_tabName;

    @FXML
    private TextField txt_tabSearch;

    @FXML
    private TextField txt_tabSeats;

    private void setCellValues() {
//        try {
//            DAO_Cuisine dao_Cuisine = new DAO_Cuisine(Database.getConnection());
//            List<Cuisine> cuisineList = dao_Cuisine.get();
//
//            ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
//            tabCol_cuisineID.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
//            tabCol_cuisineName.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//            DecimalFormat priceFormat = new DecimalFormat("#,###");
//            tabCol_cuisinePrice.setCellFactory(column -> {
//                return new TextFieldTableCell<>(new StringConverter<Double>() {
//                    @Override
//                    public String toString(Double price) {
//                        return price != null ? priceFormat.format(price) : "";
//                    }
//
//                    @Override
//                    public Double fromString(String string) {
//                        try {
//                            return priceFormat.parse(string).doubleValue();
//                        } catch (Exception e) {
//                            return 0.0;
//                        }
//                    }
//                });
//            });
//            tabCol_cuisinePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//
//            tabCol_cuisineCategory.setCellValueFactory(cellData ->
//                    new SimpleStringProperty(cellData.getValue().getCategory().getName())
//            );
//
//            tabViewCuisine.setItems(listCuisine);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            Database.closeConnection();
//        }
    }

    private void setValueCombobox() {
//        try {
//            DAO_Category categoryDAO = new DAO_Category(Database.getConnection());
//            List<Category> categoryList = categoryDAO.get();
//            ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
//            comboBox_cuisineCategory.setItems(categories);
//            comboBox_cuisineCategory.setConverter(new StringConverter<Category>() {
//                @Override
//                public String toString(Category category) {
//                    return category != null ? category.getName() : "";
//                }
//
//                @Override
//                public Category fromString(String string) {
//                    return comboBox_cuisineCategory.getItems().stream()
//                            .filter(item -> item.getName().equals(string))
//                            .findFirst()
//                            .orElse(null);
//                }
//            });
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            Database.closeConnection();
//        }
    }

    public void clear() {
//        txt_cuisineName.setText("");
//        txt_cuisinePrice.setText("");
//        txtArea_cuisineDescription.setText("");
//        comboBox_cuisineCategory.getSelectionModel().clearSelection();
//        tabViewCuisine.getSelectionModel().clearSelection();
//        btn_cuisineDelete.setVisible(false);
    }

    public void utilsButton_1() {
//        btn_cuisineSub.setText("Thêm bàn");
//        btn_cuisineMain.setText("Sửa bàn");
//        btn_cuisineSub.setStyle("-fx-background-color:   #1D557E");
//        btn_cuisineMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
//        btn_cuisineSub.setText("Sửa bàn");
//        btn_cuisineMain.setText("Thêm bàn");
//        btn_cuisineSub.setStyle("-fx-background-color:   #761D7E");
//        btn_cuisineMain.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
//        txt_cuisineName.setDisable(true);
//        txt_cuisinePrice.setDisable(true);
//        comboBox_cuisineCategory.setDisable(true);
//        txtArea_cuisineDescription.setDisable(true);
//        btn_imgChooser.setDisable(true);
    }

    void enableInput() {
//        txt_cuisineName.setDisable(false);
//        txt_cuisinePrice.setDisable(false);
//        comboBox_cuisineCategory.setDisable(false);
//        txtArea_cuisineDescription.setDisable(false);
//        btn_imgChooser.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        setValueCombobox();
    }

    //dựa vào cuisine viết theo <không hiểu xử lý UI thì copy qua thay phù hợp vơ tên biến bên trên tao cài>
    @FXML
    void btn_clearSearch(MouseEvent event) {

    }

    @FXML
    void btn_tabClear(ActionEvent event) {

    }

    @FXML
    void btn_tabMain(ActionEvent event) {

    }

    @FXML
    void btn_tabSub(ActionEvent event) {

    }

    @FXML
    void getTabInfo(MouseEvent event) {

    }
}