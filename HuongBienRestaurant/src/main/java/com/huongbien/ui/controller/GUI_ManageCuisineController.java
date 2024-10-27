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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private Button btn_cuisineClear;

    private void setCellValues() {
        try {
            DAO_Cuisine dao_Cuisine = new DAO_Cuisine(Database.getConnection());
            List<Cuisine> cuisineList = dao_Cuisine.get();

            ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
            tabCol_cuisineID.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
            tabCol_cuisineName.setCellValueFactory(new PropertyValueFactory<>("name"));

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            tabCol_cuisinePrice.setCellFactory(column -> {
                return new TextFieldTableCell<>(new StringConverter<Double>() {
                    @Override
                    public String toString(Double price) {
                        return price != null ? priceFormat.format(price) : "";
                    }
                    @Override
                    public Double fromString(String string) {
                        try {
                            return priceFormat.parse(string).doubleValue();
                        } catch (Exception e) {
                            return 0.0;
                        }
                    }
                });
            });
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
            DAO_Category categoryDAO = new DAO_Category(Database.getConnection());
            List<Category> categoryList = categoryDAO.get();
            ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
            comboBox_cuisineCategory.setItems(categories);
            comboBox_cuisineCategory.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
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

    public void clearTxt() {
        txt_cuisineName.setText("");
        txt_cuisinePrice.setText("");
        txtArea_cuisineDescription.setText("");
        comboBox_cuisineCategory.getSelectionModel().clearSelection();
        tabViewCuisine.getSelectionModel().clearSelection();
        btn_cuisineDelete.setVisible(false);
    }

    public void utilsButton_1() {
        btn_cuisineSub.setText("Thêm món");
        btn_cuisineMain.setText("Sửa món");
        btn_cuisineSub.setStyle("-fx-background-color:   #1D557E");
        btn_cuisineMain.setStyle("-fx-background-color:  #761D7E");
    }

    public void utilsButton_2() {
        btn_cuisineSub.setText("Sửa món");
        btn_cuisineMain.setText("Thêm món");
        btn_cuisineSub.setStyle("-fx-background-color:   #761D7E");
        btn_cuisineMain.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
        txt_cuisineName.setEditable(false);
        txt_cuisinePrice.setEditable(false);
        comboBox_cuisineCategory.setDisable(true);
        txtArea_cuisineDescription.setEditable(false);
    }

    void enableInput() {
        txt_cuisineName.setEditable(true);
        txt_cuisinePrice.setEditable(true);
        comboBox_cuisineCategory.setDisable(false);
        txtArea_cuisineDescription.setEditable(true);
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
                //--Format Price
                DecimalFormat priceFormat = new DecimalFormat("#,###");
                String formattedPrice = priceFormat.format(cuisine.getPrice());
                txt_cuisinePrice.setText(formattedPrice);
                //---------------
                comboBox_cuisineCategory.getSelectionModel().select(cuisine.getCategory());
                txtArea_cuisineDescription.setText(cuisine.getDescription());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
            btn_cuisineDelete.setVisible(true);
            btn_cuisineClear.setVisible(true);
            btn_cuisineSub.setVisible(true);
            btn_cuisineMain.setVisible(true);
            enableInput();
            utilsButton_1();
        }
    }

    @FXML
    void btn_cuisineClear(ActionEvent event) {
        btn_cuisineClear.setVisible(false);
        btn_cuisineDelete.setVisible(false);
        btn_cuisineSub.setVisible(true);
        btn_cuisineMain.setVisible(false);
        tabViewCuisine.getSelectionModel().clearSelection();
        utilsButton_1();
        disableInput();
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
            utilsButton_1();
        } else if (btn_cuisineSub.getText().equals("Thêm món")) {
            btn_cuisineClear.setVisible(true);
            btn_cuisineDelete.setVisible(false);
            btn_cuisineSub.setVisible(false);
            btn_cuisineMain.setVisible(true);
            enableInput();
            utilsButton_2();
            clearTxt();
        }
    }

    @FXML
    void btn_cuisineMain(ActionEvent event) {
        if (btn_cuisineMain.getText().equals("Sửa món")) {
            //Lay ID cua table thuc hien chinh sua
            Cuisine selectedItem = tabViewCuisine.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String idSelect = selectedItem.getCuisineId();
                String name = txt_cuisineName.getText();
                double price = Double.parseDouble(txt_cuisinePrice.getText().replace(".", ""));
                String description = txtArea_cuisineDescription.getText();
                String categoryId = comboBox_cuisineCategory.getValue().getCategoryId();
                String categoryName = comboBox_cuisineCategory.getValue().getName();
                String categoryDescription = comboBox_cuisineCategory.getValue().getDescription();
                try {
                    DAO_Cuisine dao_cuisine = new DAO_Cuisine(Database.getConnection());
                    Cuisine cuisine = new Cuisine(idSelect, name, price, description, null,
                            new Category(categoryId, categoryName, categoryDescription)
                    );
                    if (dao_cuisine.update(cuisine)) {
                        System.out.println("Đã sửa món thành công");
                    } else {
                        System.out.println("Sửa món không thành công");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    Database.closeConnection();
                }
            }
        } else if (btn_cuisineMain.getText().equals("Thêm món")) {
            String name = txt_cuisineName.getText();
            double price = Double.parseDouble(txt_cuisinePrice.getText().replace(".", ""));
            String description = txtArea_cuisineDescription.getText();
            String categoryId = comboBox_cuisineCategory.getValue().getCategoryId();
            String categoryName = comboBox_cuisineCategory.getValue().getName();
            String categoryDescription = comboBox_cuisineCategory.getValue().getDescription();
            try {
                DAO_Cuisine dao_cuisine = new DAO_Cuisine(Database.getConnection());
                Cuisine cuisine = new Cuisine(name, price, description, null,
                        new Category(categoryId, categoryName, categoryDescription)
                );
                if (dao_cuisine.add(cuisine)) {
                    System.out.println("Thêm món thành công");
                } else {
                    System.out.println("Thêm món không thành công");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
            clearTxt();
        }
        disableInput();
        tabViewCuisine.getItems().clear();
        setCellValues();
        btn_cuisineClear.setVisible(false);
        btn_cuisineDelete.setVisible(false);
        btn_cuisineSub.setVisible(true);
        btn_cuisineMain.setVisible(false);
        utilsButton_1();
    }

    @FXML
    void btn_cuisineDelete(ActionEvent event) {
        Cuisine selectedItem = tabViewCuisine.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getCuisineId();
            try {
                DAO_Cuisine dao_cuisine = new DAO_Cuisine(Database.getConnection());
                if (dao_cuisine.delete(idSelect)) {
                    System.out.println("xoá món thành công");
                } else {
                    System.out.println("xoá món không thành công");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection();
            }
        }
        tabViewCuisine.getItems().clear();
        setCellValues();
        btn_cuisineClear.fire();
        disableInput();
    }

    @FXML
    void txt_cuisinePrice_keyReleased(KeyEvent event) {
        String input = txt_cuisinePrice.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            txt_cuisinePrice.setText(formattedText);
            txt_cuisinePrice.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            txt_cuisinePrice.setText(validInput.toString());
            txt_cuisinePrice.positionCaret(validInput.length());
        }
    }
}
