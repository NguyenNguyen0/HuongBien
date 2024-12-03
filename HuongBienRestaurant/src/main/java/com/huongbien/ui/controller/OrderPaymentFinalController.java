package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.config.Variable;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Promotion;
import com.huongbien.entity.Table;
import com.huongbien.utils.Converter;
import com.huongbien.utils.ToastsMessage;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentFinalController implements Initializable {
    @FXML
    public GridPane suggestMoneyButtonContainer;
    @FXML
    private VBox screenCashMethodVBox;
    @FXML
    private VBox screenBankingMethodVBox;
    @FXML
    private VBox screenEWalletMethodVBox;
    @FXML
    private ScrollPane cuisineScrollPane;
    @FXML
    public GridPane cuisineGridPane;
    @FXML
    private TextField resultField;
    @FXML
    private Label resultLabel;
    @FXML
    private Label finalAmountLabel;
    @FXML
    private Label refundLabel;
    @FXML
    private Label statusLabel;

    //Controller area
    public RestaurantMainController restaurantMainController;

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    //initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultInfo();
        try {
            setFinalAmountInfoFromJSON();
            loadCuisine();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultInfo() {
        resultField.setText("0");
        resultLabel.setText("0 VNĐ");
        finalAmountLabel.setText("0 VNĐ");
        refundLabel.setText("0 VNĐ");
        statusLabel.setText("Khách chưa đưa đủ tiền");
        statusLabel.setStyle("-fx-text-fill: red");
    }

    public void loadCuisine() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getCuisineData());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                OrderPaymentBillItemController orderPaymentBillItemController = fxmlLoader.getController();
                orderPaymentBillItemController.setDataBill(orderDetails.get(i));
                orderPaymentBillItemController.setOrderPaymentFinalController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                cuisineGridPane.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cuisineScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cuisineGridPane.prefWidthProperty().bind(cuisineScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromCuisineJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<OrderDetail> getCuisineData() throws FileNotFoundException {
        return readFromCuisineJSON();
    }

    public void setFinalAmountInfoFromJSON() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        //Set screen method default
        screenCashMethodVBox.setVisible(true);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
        //calc table amount
        double tableAmount = 0.0;
        for (JsonElement element : jsonArrayTable) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            tableAmount += (table.getTableType().getTableId().equals("LB002")) ? Variable.tablePrice : 0;
        }
        //calc cuisine amount
        double cuisineAmount = 0.0;
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            cuisineAmount += cuisineMoney;
        }
        //calc discount
        double discount = 0.0;
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Promotion ID").getAsString();
            PromotionDAO promotionDAO = PromotionDAO.getInstance();
            Promotion promotion = promotionDAO.getById(id);
            if (promotion != null) {
                discount = promotion.getDiscount();
            }
        }
        double discountMoney = cuisineAmount * discount;
        double vat = cuisineAmount * 0.1;
        double finalAmount = tableAmount + cuisineAmount + vat - discountMoney;
        finalAmountLabel.setText(Converter.formatMoney((int) finalAmount) + " VNĐ");

        renderSuggestMoneyButtons(finalAmount);
    }

    public Button createSuggestMoneyButton(String label) {
        Button button = new Button(label);
        button.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        button.setFont(javafx.scene.text.Font.font("System", FontWeight.BOLD, 30));
        button.setStyle("-fx-background-color: #fff; -fx-text-fill: #000;");
        button.setMinSize(200, 60);
        return button;
    }

    public void renderSuggestMoneyButtons(double finalAmount) {
        suggestMoneyButtonContainer.getChildren().clear();
        int buttonsPerRow = 3;
        int[] suggestedMoneys = Utils.suggestMoneyReceived((int) finalAmount);
        int columns = 0;
        int rows = 0;
        for (int label : suggestedMoneys) {
            Button button = createSuggestMoneyButton(Converter.formatMoney(label));
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, _ -> {
                resultField.setText(Converter.formatMoney(label));
                resultLabel.setText(Converter.formatMoney(label) + " VNĐ");
                setFinalAmountInfo();
            });
            suggestMoneyButtonContainer.add(button, columns, rows);
            columns++;
            if (columns == buttonsPerRow) {
                columns = 0;
                rows++;
            }
        }
    }

    public void setFinalAmountInfo() {
        double moneyFromCustomer = Converter.parseMoney(resultField.getText());
        double finalAmount = Converter.parseMoney(finalAmountLabel.getText());
        if (moneyFromCustomer > finalAmount) {
            statusLabel.setText("Khách đưa đủ tiền");
            statusLabel.setStyle("-fx-text-fill: green");
            double refund = moneyFromCustomer - finalAmount;
            refundLabel.setText(Converter.formatMoney((int) refund) + " VNĐ");
        } else {
            statusLabel.setText("Khách chưa đưa đủ tiền");
            statusLabel.setStyle("-fx-text-fill: red");
            refundLabel.setText("0 VNĐ");
        }
    }

    public void openCashMethod() {
        screenCashMethodVBox.setVisible(true);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openBankingMethod() {
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(true);
        screenEWalletMethodVBox.setVisible(false);
    }

    public void openEWalletMethod() {
        screenCashMethodVBox.setVisible(false);
        screenBankingMethodVBox.setVisible(false);
        screenEWalletMethodVBox.setVisible(true);
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openOrderPayment();
    }

    @FXML
    void onCashButtonAction(ActionEvent event) {
        openCashMethod();
    }

    @FXML
    void onBankingButtonAction(ActionEvent event) {
        openBankingMethod();
    }

    @FXML
    void onEWalletButtonAction(ActionEvent event) {
        openEWalletMethod();
    }

    //mini calculator
    @FXML
    void onNumberClicked(MouseEvent event) {
        int value = Integer.parseInt(((Pane) event.getSource()).getId().replace("keyFlowPane", ""));
        double currentResult = Converter.parseMoney(resultField.getText());
        double newResult = currentResult == 0 ? (double) value : currentResult * 10 + value;
        String result = Converter.formatMoney((int) newResult);
        resultField.setText(result);
        resultLabel.setText(result + " VNĐ");
        setFinalAmountInfo();
    }

    @FXML
    void onSymbolClicked(MouseEvent event) {
        String symbol = ((Pane) event.getSource()).getId().replace("keyFlowPane", "");
        switch (symbol) {
            case "Clear":
                resultField.setText("0");
                resultLabel.setText("0" + " VNĐ");
                break;
            case "Delete":
                String currentText = resultField.getText().replace(".", "");
                if (!currentText.isEmpty()) {
                    currentText = currentText.substring(0, currentText.length() - 1);
                    String result = currentText.isEmpty() ? "0" : Converter.formatMoney(Converter.parseMoney(currentText));
                    resultField.setText(result);
                    resultLabel.setText(result + " VNĐ");
                }
                break;
        }
        setFinalAmountInfo();
    }

    @FXML
    void showInvoiceButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/huongbien/fxml/InvoicePrinterDialog.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        InvoicePrinterDialogController invoicePrinterDialogController = loader.getController();
        invoicePrinterDialogController.setOrderPaymentFinalController(this);
        primaryStage.show();
    }

    @FXML
    void onCompleteOrderPaymentFinalAction(ActionEvent event) {
        ToastsMessage.showToastsMessage("Thông báo gián đoạn", "Chức năng đang phát triển");
    }
}