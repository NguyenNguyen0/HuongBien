package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.CuisineDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderCuisineController implements Initializable {
    private final static String path_bill = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    private final static String path_table = "src/main/resources/com/huongbien/temp/temporaryTable.json";
    //cuisine
    @FXML
    private BorderPane compoent_borderCuisine;
    @FXML
    private ScrollPane compoent_scrollCuisine;
    @FXML
    private GridPane compoent_gridCuisine;

    private List<Cuisine> cuisines;

    private List<OrderDetail> orderDetails;
    @FXML
    private ScrollPane compoent_scrollBill;
    @FXML
    public GridPane compoent_gridBill;
    @FXML
    private Label lbl_tabInfo;
    @FXML
    private Label lbl_billAmountTotal;
    @FXML
    private Label lbl_billQuantityCuisine;

    public GUI_MainController gui_mainController;

    public void setGUI_MainController(GUI_MainController gui_mainController) {
        this.gui_mainController = gui_mainController;
    }

    private void loadingCuisine() {
        cuisines = new ArrayList<>(dataCuisine());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < cuisines.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderCuisineItem.fxml"));
                VBox cuisineBox = fxmlLoader.load();
                GUI_OrderCuisineItemController gui_orderCuisineItemController = fxmlLoader.getController();
                gui_orderCuisineItemController.setDataCuisine(cuisines.get(i));
                gui_orderCuisineItemController.setOrderCuisineController(this);
                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridCuisine.add(cuisineBox, columns++, rows);
                GridPane.setMargin(cuisineBox, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compoent_scrollCuisine.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridCuisine.prefWidthProperty().bind(compoent_scrollCuisine.widthProperty());
    }

    public void loadingBill() throws FileNotFoundException {
        orderDetails = new ArrayList<>(dataBill());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderBillItem.fxml"));
                HBox billBox = fxmlLoader.load();
                GUI_OrderBillItemController gui_orderBillItemController = fxmlLoader.getController();
                gui_orderBillItemController.setDataBill(orderDetails.get(i));
                gui_orderBillItemController.setOrderBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridBill.add(billBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compoent_scrollBill.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        compoent_gridBill.prefWidthProperty().bind(compoent_scrollBill.widthProperty());
    }

    public List<OrderDetail> readFromJSON_bill() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(path_bill);

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String id = jsonObject.get("Cuisine ID").getAsString();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            //set item cuisine bill
            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<Cuisine> dataCuisine() {
        CuisineDAO cuisineDAO = CuisineDAO.getInstance();
        List<Cuisine> ls = cuisineDAO.getAll();
        return ls;
    }

    private List<OrderDetail> dataBill() throws FileNotFoundException {
        List<OrderDetail> ls = readFromJSON_bill();
        return ls;
    }

    public void readFromJSON_setInfo() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayBill = Utils.readJsonFromFile(path_bill);
        JsonArray jsonArrayTab = Utils.readJsonFromFile(path_table);

        int totalQuantityCuisine = 0;
        double totalAmount = 0.0;
        for (JsonElement element : jsonArrayBill) {
            JsonObject jsonObject = element.getAsJsonObject();
            totalQuantityCuisine++;
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        lbl_billQuantityCuisine.setText(totalQuantityCuisine + " món");
        lbl_billAmountTotal.setText(Utils.formatPrice(totalAmount) + " VNĐ");

        //table
        StringBuilder tabInfoBuilder = new StringBuilder();
        for (JsonElement element : jsonArrayTab) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO dao_table = TableDAO.getInstance();
            Table table = dao_table.getById(id);
            String floorStr = (table.getFloor() == 0 ? "Tầng trệt" : "Tầng " + table.getFloor());

            tabInfoBuilder.append(floorStr).append(" - ").append(table.getName()).append(", ");
        }
        if (!tabInfoBuilder.isEmpty()) {
            tabInfoBuilder.setLength(tabInfoBuilder.length() - 2);
        }
        lbl_tabInfo.setText(tabInfoBuilder.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingCuisine();
        try {
            //bill
            loadingBill();
            //lbl
            readFromJSON_setInfo();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btn_payment(ActionEvent event) throws IOException {
        JsonArray jsonArray = Utils.readJsonFromFile(path_bill);
        if (!jsonArray.isEmpty()) {
            gui_mainController.openPayment();
        } else {
            System.out.println("Vui lòng chọn món ăn");
        }
    }

    @FXML
    void btn_back(ActionEvent event) throws IOException {
        gui_mainController.openOrder();
    }

    @FXML
    void btn_cancel(ActionEvent event) throws FileNotFoundException, SQLException {
        Utils.writeJsonToFile(new JsonArray(), path_bill);
        compoent_gridBill.getChildren().clear();
        loadingBill();
        readFromJSON_setInfo();
    }
}
