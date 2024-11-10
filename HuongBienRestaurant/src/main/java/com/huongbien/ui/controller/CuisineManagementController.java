package com.huongbien.ui.controller;

import com.huongbien.dao.CategoryDAO;
import com.huongbien.dao.CuisineDAO;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

public class CuisineManagementController implements Initializable {
    @FXML
    private TableColumn<Cuisine, String> cuisineCategoryColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineIdColumn;
    @FXML
    private TableColumn<Cuisine, String> cuisineNameColumn;
    @FXML
    private TableColumn<Cuisine, Double> cuisinePriceColumn;
    @FXML
    private TableView<Cuisine> cuisineTable;
    @FXML
    private TextField cuisineNameField;
    @FXML
    private TextField cuisinePriceField;
    @FXML
    private ComboBox<Category> cuisineCategoryComboBox;
    @FXML
    private TextArea cuisineDescriptionTextArea;
    @FXML
    private TextField cuisineSearchField;
    @FXML
    private Button swapModeCuisineButton;
    @FXML
    private Button handleActionCuisineButton;
    @FXML
    private Button deleteCuisineButton;
    @FXML
    private Button clearCuisineButton;
    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView cuisineImageView;

    public byte[] imageCuisineByte = null;

    private void setCuisineTableValues() {
        CuisineDAO cuisineDao = CuisineDAO.getInstance();
        List<Cuisine> cuisineList = cuisineDao.getAll();

        ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
        cuisineIdColumn.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
        cuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        cuisinePriceColumn.setCellFactory(column -> {
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
        cuisinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        cuisineCategoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName())
        );

        cuisineTable.setItems(listCuisine);
    }

    private void setValueComboBox() {
        CategoryDAO categoryDAO = CategoryDAO.getInstance();
        List<Category> categoryList = categoryDAO.getAll();
        ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
        cuisineCategoryComboBox.setItems(categories);
        cuisineCategoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                return cuisineCategoryComboBox.getItems().stream()
                        .filter(item -> item.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public void clearChooserImage() {
        imageCuisineByte = null;
        Image image = new Image(getClass().getResourceAsStream("/com/huongbien/icon/all/gallery-512px.png"));
        cuisineImageView.setImage(image);
    }

    public void clearCuisineForm() {
        cuisineNameField.setText("");
        cuisinePriceField.setText("");
        cuisineDescriptionTextArea.setText("");
        cuisineCategoryComboBox.getSelectionModel().clearSelection();
        cuisineTable.getSelectionModel().clearSelection();
        deleteCuisineButton.setVisible(false);
        clearChooserImage();
    }

    private void searchCuisine() {
        cuisineTable.getItems().clear();
        String input = cuisineSearchField.getText();
        CuisineDAO cuisineDao = CuisineDAO.getInstance();
        List<Cuisine> cuisineList = cuisineDao.getByName(input);

        ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
        cuisineIdColumn.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
        cuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        DecimalFormat priceFormat = new DecimalFormat("#,###");
        cuisinePriceColumn.setCellFactory(column -> {
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
        cuisinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        cuisineCategoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName())
        );
        cuisineTable.setItems(listCuisine);
    }
    
    public void setHandleActionButtonToEditCuisine() {
        swapModeCuisineButton.setText("Thêm món");
        handleActionCuisineButton.setText("Sửa món");
        swapModeCuisineButton.setStyle("-fx-background-color:   #1D557E");
        handleActionCuisineButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void setHandleActionButtonToAddCuisine() {
        swapModeCuisineButton.setText("Sửa món");
        handleActionCuisineButton.setText("Thêm món");
        swapModeCuisineButton.setStyle("-fx-background-color:   #761D7E");
        handleActionCuisineButton.setStyle("-fx-background-color:  #1D557E");
    }

    void disableInput() {
        cuisineNameField.setDisable(true);
        cuisinePriceField.setDisable(true);
        cuisineCategoryComboBox.setDisable(true);
        cuisineDescriptionTextArea.setDisable(true);
        chooseImageButton.setDisable(true);
    }

    void enableInput() {
        cuisineNameField.setDisable(false);
        cuisinePriceField.setDisable(false);
        cuisineCategoryComboBox.setDisable(false);
        cuisineDescriptionTextArea.setDisable(false);
        chooseImageButton.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCuisineTableValues();
        setValueComboBox();
    }

    @FXML
    void onCuisineTableViewClicked(MouseEvent event) {
        Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getCuisineId();
            CuisineDAO cuisineDao = CuisineDAO.getInstance();
            Cuisine cuisine = cuisineDao.getById(idSelect);
            //load img------
            byte[] imageBytes = cuisine.getImage();
            imageCuisineByte = imageBytes;
            Image image;
            if (imageBytes != null) {
                image = Converter.bytesToImage(imageBytes);
            } else {
                image = new Image(getClass().getResourceAsStream("/com/huongbien/icon/all/gallery-512px.png"));
            }
            cuisineImageView.setImage(image);
            //---------------
            cuisineNameField.setText(cuisine.getName());
            //--Format Price
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedPrice = priceFormat.format(cuisine.getPrice());
            cuisinePriceField.setText(formattedPrice);
            //---------------
            cuisineCategoryComboBox.getSelectionModel().select(cuisine.getCategory());
            cuisineDescriptionTextArea.setText(cuisine.getDescription());
            deleteCuisineButton.setVisible(true);
            clearCuisineButton.setVisible(true);
            swapModeCuisineButton.setVisible(true);
            handleActionCuisineButton.setVisible(true);
            enableInput();
            setHandleActionButtonToEditCuisine();
        }
    }

    @FXML
    void onClearCuisineClicked(ActionEvent event) {
        clearCuisineButton.setVisible(false);
        deleteCuisineButton.setVisible(false);
        swapModeCuisineButton.setVisible(true);
        handleActionCuisineButton.setVisible(false);
        cuisineTable.getSelectionModel().clearSelection();
        setHandleActionButtonToEditCuisine();
        disableInput();
        clearCuisineForm();
    }

    @FXML
    void onClearSearchClicked(MouseEvent event) {
        cuisineSearchField.setText("");
        cuisineSearchField.requestFocus();
        cuisineTable.getItems().clear();
        setCuisineTableValues();
    }

    @FXML
    void onSwapModeButtonClicked(ActionEvent event) {
        if (swapModeCuisineButton.getText().equals("Sửa món")) {
            clearChooserImage();
            setHandleActionButtonToEditCuisine();
        } else if (swapModeCuisineButton.getText().equals("Thêm món")) {
            clearCuisineButton.setVisible(true);
            deleteCuisineButton.setVisible(false);
            swapModeCuisineButton.setVisible(false);
            handleActionCuisineButton.setVisible(true);
            enableInput();
            clearCuisineForm();
            setHandleActionButtonToAddCuisine();
        }
    }

    @FXML
    void onHandleActionButtonClicked(ActionEvent event) {
        if (handleActionCuisineButton.getText().equals("Sửa món")) {
            //Lay ID cua table thuc hien chinh sua
            Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String idSelect = selectedItem.getCuisineId();
                String name = cuisineNameField.getText();
                double price = cuisinePriceField.getText().isEmpty() ? 0.0 : Double.parseDouble(cuisinePriceField.getText().replace(".", ""));
                String description = cuisineDescriptionTextArea.getText();
                String categoryId = cuisineCategoryComboBox.getValue().getCategoryId();
                String categoryName = cuisineCategoryComboBox.getValue().getName();
                String categoryDescription = cuisineCategoryComboBox.getValue().getDescription();
                CuisineDAO cuisineDao = CuisineDAO.getInstance();
                Cuisine cuisine = new Cuisine(idSelect, name, price, description, imageCuisineByte,
                        new Category(categoryId, categoryName, categoryDescription)
                );
                if (cuisineDao.updateCuisineInfo(cuisine)) {
                    System.out.println("Đã sửa món thành công");
                } else {
                    System.out.println("Sửa món không thành công");
                }
            }
        } else if (handleActionCuisineButton.getText().equals("Thêm món")) {
            String name = cuisineNameField.getText();
            double price = cuisinePriceField.getText().isEmpty() ? 0.0 : Double.parseDouble(cuisinePriceField.getText().replace(".", ""));
            String description = cuisineDescriptionTextArea.getText();
            String categoryId = cuisineCategoryComboBox.getValue().getCategoryId();
            String categoryName = cuisineCategoryComboBox.getValue().getName();
            String categoryDescription = cuisineCategoryComboBox.getValue().getDescription();

            CuisineDAO cuisineDao = CuisineDAO.getInstance();
            Cuisine cuisine = new Cuisine(name, price, description, imageCuisineByte,
                    new Category(categoryId, categoryName, categoryDescription)
            );
            if (cuisineDao.add(cuisine)) {
                System.out.println("Thêm món thành công");
            } else {
                System.out.println("Thêm món không thành công");
            }
            clearCuisineForm();
        }
        disableInput();
        cuisineTable.getItems().clear();
        setCuisineTableValues();
        clearCuisineButton.setVisible(false);
        deleteCuisineButton.setVisible(false);
        swapModeCuisineButton.setVisible(true);
        handleActionCuisineButton.setVisible(false);
        setHandleActionButtonToEditCuisine();
    }

    @FXML
    void onDeleteCuisineButtonClicked(ActionEvent event) throws SQLException {
        Cuisine selectedItem = cuisineTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getCuisineId();
            CuisineDAO cuisineDao = CuisineDAO.getInstance();
            if (cuisineDao.delete(idSelect)) {
                System.out.println("Xoá món thành công");
            } else {
                System.out.println("Xoá món không thành công");
            }
        }
        cuisineTable.getItems().clear();
        setCuisineTableValues();
        clearCuisineButton.fire();
        disableInput();
    }

    @FXML
    void onCuisinePriceTextFieldKeyReleased(KeyEvent event) {
        String input = cuisinePriceField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            cuisinePriceField.setText(formattedText);
            cuisinePriceField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            cuisinePriceField.setText(validInput.toString());
            cuisinePriceField.positionCaret(validInput.length());
        }
    }

    //search cuisine area
    @FXML
    void onCuisineSearchTextFieldClicked(MouseEvent event) {
        cuisineTable.getItems().clear();
        String input = cuisineSearchField.getText();
        CuisineDAO cuisineDao = CuisineDAO.getInstance();
        List<Cuisine> cuisineList = cuisineDao.getByName(input);
        //
        ObservableList<Cuisine> listCuisine = FXCollections.observableArrayList(cuisineList);
        cuisineIdColumn.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
        cuisineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //
        DecimalFormat priceFormat = new DecimalFormat("#,###");
        cuisinePriceColumn.setCellFactory(column -> {
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
        cuisinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        cuisineCategoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName())
        );
        cuisineTable.setItems(listCuisine);
    }

    private Timeline searchDelay;

    @FXML
    void onCuisineSearchTextFieldKeyReleased(KeyEvent event) {
        if (searchDelay != null) {
            searchDelay.stop();
        }
        searchDelay = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
            searchCuisine();
        }));
        searchDelay.setCycleCount(1);
        searchDelay.play();
    }

    @FXML
    void onImageChooserButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            cuisineImageView.setImage(image);
            //convert
            try {
                imageCuisineByte = Converter.fileToBytes(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}