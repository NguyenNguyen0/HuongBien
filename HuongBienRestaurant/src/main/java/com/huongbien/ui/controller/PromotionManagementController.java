package com.huongbien.ui.controller;

import com.huongbien.dao.PromotionDAO;
import com.huongbien.entity.Promotion;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PromotionManagementController implements Initializable {
    @FXML
    private ImageView deleteSearchButton;
    @FXML
    private TextField promotionSearchField;
    @FXML
    private TextField promotionNameField;
    @FXML
    private DatePicker startedDateDatePicker;
    @FXML
    private DatePicker endedDateDatePicker;
    @FXML
    private TextField orderDiscountField;
    @FXML
    private ComboBox<String> promotionStatusComboBox;
    @FXML
    private TextField minimumOrderField;
    @FXML
    private ComboBox<String> memberShipLevelComboBox;
    @FXML
    private TextArea promotionDescriptionTextArea;
    @FXML
    private Button handleActionPromotionButton;
    @FXML
    private Button clearPromotionFormButton;
    @FXML
    private Button swapModePromotionButton;
    @FXML
    private ComboBox<String> searchPromotionStatusComboBox;
    @FXML
    private TableView<Promotion> promotionTable;
    @FXML
    private TableColumn<Promotion, String> promotionIdColumn;
    @FXML
    private TableColumn<Promotion, Date> promotionStartedDateColumn;
    @FXML
    private TableColumn<Promotion, Date> promotionEndedDateColumn;
    @FXML
    private TableColumn<Promotion, Double> promotionDiscountColumn;
    @FXML
    private TableColumn<Promotion, String> promotionMembershipLevelColumn;
    private Utils utils;

    public void changeHandleButtonModeToEditPromotion() {
        swapModePromotionButton.setText("Thêm");
        handleActionPromotionButton.setText("Sửa");
        swapModePromotionButton.setStyle("-fx-background-color:   #1D557E");
        handleActionPromotionButton.setStyle("-fx-background-color:  #761D7E");
    }

    public void changeHandleButtonModeToAddPromotion() {
        swapModePromotionButton.setText("Sửa");
        handleActionPromotionButton.setText("Thêm");
        swapModePromotionButton.setStyle("-fx-background-color:   #761D7E");
        handleActionPromotionButton.setStyle("-fx-background-color:  #1D557E");
    }

    private void setComboBoxValue() {
        //Status
        ObservableList<String> memberShipLevelList = FXCollections.observableArrayList("Đồng", "Bạc", "Vàng", "Kim cương");
        memberShipLevelComboBox.setItems(memberShipLevelList);
        memberShipLevelComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return memberShipLevelComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        ObservableList<String> statusList = FXCollections.observableArrayList("Còn hiệu lực", "Hết hiệu lực");
        searchPromotionStatusComboBox.setItems(statusList);
        searchPromotionStatusComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return searchPromotionStatusComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        promotionStatusComboBox.setItems(statusList);
        promotionStatusComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String status) {
                return status != null ? status : "";
            }

            @Override
            public String fromString(String string) {
                return promotionStatusComboBox.getItems().stream()
                        .filter(item -> item.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public void setPromotionTableValue() {
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getAll();
        ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);

        promotionNameField.setEditable(true);
        promotionDescriptionTextArea.setEditable(true);
        minimumOrderField.setEditable(true);

        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("promotionId"));

        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        promotionDiscountColumn.setCellFactory(col -> new TableCell<Promotion, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Chuyển đổi và định dạng giá trị thành phần trăm
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });

        promotionMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        promotionEndedDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        promotionStartedDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        promotionTable.setItems(listPromotion);
    }

    public void setPromotionTableValueExpired() {
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getExpired();
        ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);

        promotionNameField.setEditable(true);
        promotionDescriptionTextArea.setEditable(true);
        minimumOrderField.setEditable(true);

        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("promotionId"));

        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        promotionDiscountColumn.setCellFactory(col -> new TableCell<Promotion, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Chuyển đổi và định dạng giá trị thành phần trăm
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });

        promotionMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        promotionEndedDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        promotionStartedDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        promotionTable.setItems(listPromotion);
    }

    public void enableInput() {
        promotionStatusComboBox.setDisable(false);
        minimumOrderField.setDisable(false);
        promotionNameField.setDisable(false);
        orderDiscountField.setDisable(false);
        memberShipLevelComboBox.setDisable(false);
        promotionDescriptionTextArea.setDisable(false);
        endedDateDatePicker.setDisable(false);
        startedDateDatePicker.setDisable(false);
    }

    public void disableInput() {
        promotionStatusComboBox.setDisable(true);
        minimumOrderField.setDisable(true);
        promotionNameField.setDisable(true);
        orderDiscountField.setDisable(true);
        memberShipLevelComboBox.setDisable(true);
        promotionDescriptionTextArea.setDisable(true);
        endedDateDatePicker.setDisable(true);
        startedDateDatePicker.setDisable(true);
    }

    public void clearPromotionForm() {
        promotionStatusComboBox.getSelectionModel().clearSelection();
        minimumOrderField.clear();
        promotionNameField.clear();
        orderDiscountField.clear();
        promotionDescriptionTextArea.clear();
        startedDateDatePicker.setValue(null);
        endedDateDatePicker.setValue(null);
        memberShipLevelComboBox.getSelectionModel().clearSelection();
        promotionTable.getSelectionModel().clearSelection();
    }

    public boolean validatePromotionData() {
        if (promotionNameField.getText().trim().isEmpty()) {
            return false;
        }
        if (orderDiscountField.getText().trim().isEmpty()) {
            return false;
        }
        if (endedDateDatePicker.getValue() == null) {
            return false;
        }
        if (startedDateDatePicker.getValue() == null) {
            return false;
        }
        return !endedDateDatePicker.getValue().isBefore(startedDateDatePicker.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableInput();
        setPromotionTableValue();
        setComboBoxValue();
        searchPromotionStatusComboBox.getSelectionModel().select(0);
    }

    @FXML
    void onPromotionTableClicked(MouseEvent mouseEvent) {
        changeHandleButtonModeToEditPromotion();
        Promotion selectedItem = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enableInput();
            promotionNameField.setText(selectedItem.getName());
            promotionStatusComboBox.getSelectionModel().select(selectedItem.getStatus());
            double dis = selectedItem.getDiscount();
            int discount = (int) (dis * 100);

            DecimalFormat priceFormat = new DecimalFormat("#,###");
            String formattedPrice = priceFormat.format(selectedItem.getMinimumOrderAmount());
            minimumOrderField.setText(formattedPrice);

            promotionDescriptionTextArea.setText(selectedItem.getDescription());
            orderDiscountField.setText(discount + "%");
            endedDateDatePicker.setValue(selectedItem.getEndDate());
            startedDateDatePicker.setValue(selectedItem.getStartDate());
            int memberShip = selectedItem.getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            memberShipLevelComboBox.getSelectionModel().select(memberShipLevel);
        }
    }

    @FXML
    void onDeleteSearchButtonClicked(MouseEvent mouseEvent) {
        promotionSearchField.setText("");
        setPromotionTableValue();
    }

    @FXML
    void onPromotionSearchFieldKeyReleased(KeyEvent keyEvent) {
        String id = promotionSearchField.getText();
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getAllById(id);
        ObservableList<Promotion> promotions = FXCollections.observableArrayList(promotionList);

        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        promotionMembershipLevelColumn.setCellValueFactory(cellData -> {
            int memberShip = cellData.getValue().getMembershipLevel();
            String memberShipLevel = Utils.toStringMembershipLevel(memberShip);
            return new SimpleStringProperty(memberShipLevel);
        });
        promotionEndedDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        promotionStartedDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        promotionTable.setItems(promotions);
    }

    @FXML
    void onSwapModePromotionButtonClicked(ActionEvent actionEvent) {
        if (swapModePromotionButton.getText().equals("Thêm")) {
            promotionNameField.requestFocus();
            changeHandleButtonModeToAddPromotion();
            clearPromotionForm();
            enableInput();
            promotionStatusComboBox.getSelectionModel().select(0);
            promotionStatusComboBox.setDisable(true);
        } else {
            disableInput();
            changeHandleButtonModeToEditPromotion();
        }
    }

    @FXML
    void onHandleActionPromotionButtonClicked(ActionEvent actionEvent) {
        if (handleActionPromotionButton.getText().equals("Sửa")) {
            enableInput();
            Promotion promotion = null;
            if (validatePromotionData()) {
                String name = promotionNameField.getText();
                String status = promotionStatusComboBox.getSelectionModel().getSelectedItem();

                String minimum = minimumOrderField.getText();
                double minimumOrder = Double.parseDouble(minimum.replace(",", ""));

                String dis = orderDiscountField.getText();
                dis = dis.replace("%", "");
                double discount = Double.parseDouble(dis) / 100;

                String memberShip = memberShipLevelComboBox.getSelectionModel().getSelectedItem();
                int memberShipLevel = Utils.toIntMembershipLevel(memberShip);

                LocalDate start = startedDateDatePicker.getValue();
                LocalDate end = endedDateDatePicker.getValue();
                String description = promotionDescriptionTextArea.getText();
                String id = promotionTable.getSelectionModel().getSelectedItem().getPromotionId();
                PromotionDAO promotionDAO = PromotionDAO.getInstance();
                promotion = promotionDAO.getById(id);
                promotion.setName(name);
                promotion.setDescription(description);
                promotion.setEndDate(end);
                promotion.setStartDate(start);
                promotion.setDiscount(discount);
                promotion.setStatus(status);
                promotion.setMinimumOrderAmount(minimumOrder);
                promotion.setMembershipLevel(memberShipLevel);
                System.out.println(promotion.getStatus());
                if (promotionDAO.updateInfo(promotion)) {
                    promotionTable.getItems().clear();
                    setPromotionTableValue();
                }
            }
            clearPromotionForm();
        } else if (handleActionPromotionButton.getText().equals("Thêm")) {
            Promotion promotion = null;
            if (validatePromotionData()) {
                String name = promotionNameField.getText();
                String status = promotionStatusComboBox.getSelectionModel().getSelectedItem();

                double minimumOrder = Double.parseDouble(minimumOrderField.getText());
                String dis = orderDiscountField.getText();
                int i = dis.indexOf("%");
                dis = dis.substring(0, i);
                Double discount = Double.parseDouble(dis) / 100;

                String memberShip = memberShipLevelComboBox.getSelectionModel().getSelectedItem();

                int memberShipLevel = Utils.toIntMembershipLevel(memberShip);

                LocalDate start = startedDateDatePicker.getValue();
                LocalDate end = endedDateDatePicker.getValue();
                String description = promotionDescriptionTextArea.getText();
                PromotionDAO promotionDAO = PromotionDAO.getInstance();
                promotion = new Promotion(name, start, end, discount, description, minimumOrder, memberShipLevel, "Còn hiệu lực");
                if (promotionDAO.add(promotion)) {
                    promotionTable.getItems().clear();
                    setPromotionTableValue();
                }
            }
            clearPromotionForm();
        }
    }

    @FXML
    void onClearPromotionFormButtonClicked(ActionEvent actionEvent) {
        clearPromotionForm();
    }

    @FXML
    public void onSearchPromotionStatusComboBoxSelected(ActionEvent actionEvent) {
        if (searchPromotionStatusComboBox.getSelectionModel().isSelected(0)) {
            setPromotionTableValue();
            swapModePromotionButton.setVisible(true);
        } else {
            changeHandleButtonModeToEditPromotion();
            swapModePromotionButton.setVisible(false);
            setPromotionTableValueExpired();
            clearPromotionForm();
        }
    }

    @FXML
    void onMinimumOrderFieldKeyReleased(KeyEvent keyEvent) {
        String input = minimumOrderField.getText().replace(".", "").replace(",", "");
        if (input.isEmpty()) {
            return;
        }
        if (input.matches("\\d*")) {
            NumberFormat format = DecimalFormat.getInstance();
            String formattedText = format.format(Long.parseLong(input));
            minimumOrderField.setText(formattedText);
            minimumOrderField.positionCaret(formattedText.length());
        } else {
            StringBuilder validInput = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (Character.isDigit(ch)) {
                    validInput.append(ch);
                }
            }
            minimumOrderField.setText(validInput.toString());
            minimumOrderField.positionCaret(validInput.length());
        }
    }
}
