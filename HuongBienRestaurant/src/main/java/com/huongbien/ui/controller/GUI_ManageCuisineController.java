package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Category;
import com.huongbien.dao.DAO_Cuisine;
import com.huongbien.database.Database;
import com.huongbien.entity.Category;
import com.huongbien.entity.Cuisine;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_ManageCuisineController implements Initializable {
    @FXML
    private TableColumn<Cuisine, String> tabCol_cuisineCategory;

    @FXML
    private TableColumn<Cuisine, String> tabCol_cuisineID;

    @FXML
    private TableColumn<Cuisine, String> tabCol_cuisineName;

    @FXML
    private TableColumn<Cuisine, Double> tabCol_cuisinePrice;

    @FXML
    private TableView<Cuisine> tabViewCuisine;

    @FXML
    private TextField txt_cuisineName;

    @FXML
    private TextField txt_cuisinePrice;

    @FXML
    private ComboBox<Category> comboBox_cuisineCategory;

    @FXML
    private TextArea txtArea_cuisineDescription;

    @FXML
    private TextField txt_cuisineSearch;

    @FXML
    private Button btn_cuisineSub;

    @FXML
    private Button btn_cuisineMain;

    @FXML
    private Button btn_cuisineDelete;

    @FXML
    private Button btn_clearTextField;

    private void setCellValues() {
        try {
            Connection connection = Database.getConnection();
            DAO_Cuisine dao_Cuisine = new DAO_Cuisine(connection);
            List<Cuisine> cuisineList = dao_Cuisine.get();

            ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);

            tabCol_cuisineID.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
            tabCol_cuisineName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tabCol_cuisinePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            tabCol_cuisineCategory.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCategory().getName())
            );
            tabViewCuisine.setItems(listCuisine);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    private void setValueCombobox() {
        try {
            Connection connection = Database.getConnection();
            DAO_Category categoryDAO = new DAO_Category(connection);
            List<Category> categoryList = categoryDAO.get();
            ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
            comboBox_cuisineCategory.setItems(categories);
            comboBox_cuisineCategory.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
//                    return category.getName();
                    return category != null ? category.getName() : "";
                }

                @Override
                public Category fromString(String string) {
                    return comboBox_cuisineCategory.getItems().stream()
                            .filter(item -> item.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        setValueCombobox();
    }

    @FXML
    void getCuisineInfo(MouseEvent event) {
        Cuisine selectedItem = tabViewCuisine.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getCuisineId();
            try {
                Connection connection = Database.getConnection();
                DAO_Cuisine dao_Cuisine = new DAO_Cuisine(connection);
                Cuisine cuisine = dao_Cuisine.get(idSelect);
                txt_cuisineName.setText(cuisine.getName());
                txt_cuisinePrice.setText(cuisine.getPrice() + "");
                comboBox_cuisineCategory.getSelectionModel().select(cuisine.getCategory());
                txtArea_cuisineDescription.setText(cuisine.getDescription());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
            btn_cuisineDelete.setVisible(true);
            btn_clearTextField.setVisible(true);
            btn_cuisineSub.setVisible(true);
            btn_cuisineMain.setVisible(true);
            btn_cuisineSub.setText("Thêm món");
            btn_cuisineMain.setText("Sửa món");
            btn_cuisineSub.setStyle("-fx-background-color:   #1D557E");
            btn_cuisineMain.setStyle("-fx-background-color:  #761D7E");
            //
            txt_cuisineName.setEditable(true);
            txt_cuisinePrice.setEditable(true);
            comboBox_cuisineCategory.setDisable(false);
        }
    }

    public void clearTxt() {
        txt_cuisineName.setText("");
        txt_cuisinePrice.setText("");
        txtArea_cuisineDescription.setText("");
        comboBox_cuisineCategory.getSelectionModel().clearSelection();
        btn_cuisineDelete.setVisible(false);
    }

    @FXML
    void btn_clearTextField(ActionEvent event) {
        btn_cuisineSub.setText("Thêm món");
        btn_cuisineMain.setText("Sửa món");
        btn_cuisineSub.setStyle("-fx-background-color:   #1D557E");
        btn_cuisineMain.setStyle("-fx-background-color:  #761D7E");
        btn_cuisineSub.setVisible(true);
        btn_cuisineMain.setVisible(false);
        tabViewCuisine.getSelectionModel().clearSelection();
        clearTxt();
    }

    @FXML
    void btn_clearSearch(MouseEvent event) {
        txt_cuisineSearch.setText("");
        txt_cuisineSearch.requestFocus();
    }

    @FXML
    void btn_cuisineSub(ActionEvent event) {
        if (btn_cuisineSub.getText().equals("Sửa món")) {
            btn_cuisineSub.setText("Thêm món");
            btn_cuisineMain.setText("Sửa món");
            btn_cuisineSub.setStyle("-fx-background-color:   #1D557E");
            btn_cuisineMain.setStyle("-fx-background-color:  #761D7E");
        } else if (btn_cuisineSub.getText().equals("Thêm món")) {
            btn_cuisineSub.setText("Sửa món");
            btn_cuisineMain.setText("Thêm món");
            btn_cuisineSub.setStyle("-fx-background-color:   #761D7E");
            btn_cuisineMain.setStyle("-fx-background-color:  #1D557E");
            btn_cuisineSub.setVisible(false);
            btn_cuisineMain.setVisible(true);
            btn_clearTextField.setVisible(true);
            txt_cuisineName.setEditable(true);
            txt_cuisinePrice.setEditable(true);
            comboBox_cuisineCategory.setDisable(false);
            clearTxt();
        }
    }

    @FXML
    void btn_cuisineMain(ActionEvent event) {
        if (btn_cuisineMain.getText().equals("Sửa món")) {
            btn_cuisineMain.setText("Sửa món");
            btn_cuisineSub.setText("Thêm món");
            btn_cuisineMain.setStyle("-fx-background-color: #761D7E;");
            btn_cuisineSub.setStyle("-fx-background-color: #1D557E;");
            btn_cuisineSub.setVisible(true);
            btn_cuisineMain.setVisible(false);
            txt_cuisineName.setEditable(false);
            txt_cuisinePrice.setEditable(false);
            comboBox_cuisineCategory.setDisable(true);
        } else if (btn_cuisineMain.getText().equals("Thêm món")) {
            btn_cuisineMain.setText("Sửa món");
            btn_cuisineSub.setText("Thêm món");
            btn_cuisineMain.setStyle("-fx-background-color: #761D7E;");
            btn_cuisineSub.setStyle("-fx-background-color: #1D557E;");
            btn_cuisineSub.setVisible(true);
            btn_cuisineMain.setVisible(false);
            clearTxt();
            tabViewCuisine.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void btn_cuisineDelete(ActionEvent event) {

        clearTxt();
    }

    @FXML
    void btn_cuisineEditCategory(ActionEvent event) {

        clearTxt();
    }
}
