package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.config.Constants;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InvoicePrinterDialogController implements Initializable {
    @FXML
    private TextArea content;

    public OrderPaymentFinalController orderPaymentFinalController;
    public void setOrderPaymentFinalController(OrderPaymentFinalController orderPaymentFinalController) {
        this.orderPaymentFinalController = orderPaymentFinalController;
    }

    //Initialize area
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            printer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void newLine() {
        content.setText(content.getText() + "\n");
    }

    public void separator() {
        content.setText(content.getText() + "----------------------------------------------------");
    }

    public void printer() throws FileNotFoundException {
        JsonArray jsonArrayTable = Utils.readJsonFromFile(Constants.TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayCustomer = Utils.readJsonFromFile(Constants.TEMPORARY_CUSTOMER_PATH);
        JsonArray jsonArraySession = Utils.readJsonFromFile(Constants.LOGIN_SESSION_PATH);
        content.setText("\n\t\tHuong Bien Restaurant");
        newLine();
        content.setText(content.getText() + "  12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP.HCM");
        newLine();
        content.setText(content.getText() + "\t\tHotline: 0353.999.798");
        newLine();
        newLine();
        content.setText(content.getText() + "\t\t   HOÁ ĐƠN BÁN HÀNG");
        newLine();
        newLine();
        newLine();
        //info bill
        content.setText(content.getText() + String.format("%-13s %37s", "Mã hoá đơn:", ""));
        newLine();
        //cashier
        String currentLoginSession = "";
        for (JsonElement element : jsonArraySession) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO dao_employee = EmployeeDAO.getInstance();
            Employee employee = dao_employee.getById(id).get(0);
            currentLoginSession = (employee.getName() != null ? employee.getName() : "Không xác định");
        }
        content.setText(content.getText() + String.format("%-13s %37s", "Thu ngân:", currentLoginSession));
        newLine();
        //customer
        String currentCustomer = "Khách vãng lai";
        double discount = 0;
        String promotionId = "Không áp dụng";
        for (JsonElement element : jsonArrayCustomer) {
            JsonObject jsonObject = element.getAsJsonObject();
            //get customer
            String customerID = jsonObject.get("Customer ID").getAsString();
            CustomerDAO customerDAO = CustomerDAO.getInstance();
            Customer customer = customerDAO.getById(customerID);
            currentCustomer = (customer.getName() != null ? customer.getName() : "Khách vãng lai");
            //get promotion
            String promotionID = jsonObject.get("Promotion ID").getAsString();
            PromotionDAO promotionDAO = PromotionDAO.getInstance();
            Promotion promotion = promotionDAO.getById(promotionID);
            discount = promotion.getDiscount();
            promotionId = (promotion.getPromotionId() != null ? promotion.getPromotionId() : "Không áp dụng");
        }
        content.setText(content.getText() + String.format("%-13s %37s", "Khách hàng:", currentCustomer));
        newLine();
        //timer
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(dateTimeFormatter);
        content.setText(content.getText() + String.format("%-13s %37s", "Ngày lập:", currentDateTime));
        newLine();
        //table
        double tableAmount = 0;
        for (int i = 0; i < jsonArrayTable.size(); i++) {
            JsonObject jsonObject = jsonArrayTable.get(i).getAsJsonObject();
            String id = jsonObject.get("Table ID").getAsString();
            TableDAO tableDAO = TableDAO.getInstance();
            Table table = tableDAO.getById(id);
            if (table != null) {
                if (i == 0) {
                    content.setText(content.getText() + String.format("%-13s %37s", "Khu vực bàn:", table.getName() + " (" + (table.getFloor() == 0 ? "Tầng trệt" : "Tầng " + table.getFloor()) + " - " + table.getTableType().getName() + ")"));
                } else {
                    content.setText(content.getText() + String.format("%-13s %37s", "", table.getName() + " " + (table.getFloor() == 0 ? "Tầng trệt" : "Tầng " + table.getFloor()) + " - " + table.getTableType().getName() + ")"));
                }
                newLine();
                tableAmount += (table.getTableType().getTableId().equals("LB002") ? Constants.TABLE_PRICE : 0);
            }
        }
        content.setText(content.getText() + String.format("%-20s %30s", "Mã khuyến mãi:", promotionId));
        newLine();
        newLine();
        // Table cuisine
        content.setText(content.getText() + String.format("%-20s %-10s %8s %10s", "Tên món", "Đơn giá", "Số lượng", "Tổng tiền"));
        newLine();
        separator();
        newLine();
        int totalQuantityCuisine = 0;
        double cuisineAmount = 0;
        JsonArray jsonArrayCuisine = Utils.readJsonFromFile(Constants.TEMPORARY_CUISINE_PATH);
        for (JsonElement element : jsonArrayCuisine) {
            JsonObject jsonObject = element.getAsJsonObject();
            String name = jsonObject.get("Cuisine Name").getAsString();
            double price = jsonObject.get("Cuisine Price").getAsDouble();
            String note = jsonObject.get("Cuisine Note").getAsString();
            int quantity = jsonObject.get("Cuisine Quantity").getAsInt();
            double money = jsonObject.get("Cuisine Money").getAsDouble();
            totalQuantityCuisine += quantity;
            cuisineAmount += money;
            String formattedLine = String.format("%-20s %-10s %8s %10s", name, String.format("%,.0f", price), "x" + quantity, String.format("%,.0f", money));
            content.setText(content.getText() + formattedLine);
            newLine();
            content.setText(content.getText() + (!note.isEmpty() ? "Ghi chú: " + note : ""));
            newLine();
            separator();
            newLine();
        }
        newLine();
        content.setText(content.getText() + String.format("%-20s %30s", "Tổng số lượng món:", totalQuantityCuisine+" Món"));
        newLine();
        content.setText(content.getText() + String.format("%-20s %30s", "Tổng tiền món:", String.format("%,.0f VNĐ", cuisineAmount)));
        newLine();
        content.setText(content.getText() + String.format("%-20s %30s", "Phí chọn bàn:", String.format("%,.0f VNĐ", tableAmount)));
        newLine();
        double vatPercent = Constants.VAT * 100;
        double vatAmount = cuisineAmount * Constants.VAT;
        content.setText(content.getText() + String.format("%-20s %30s", "Thuế VAT"+String.format("(+%.0f%%):", vatPercent), String.format("%,.0f VNĐ", vatAmount)));
        newLine();
        double discountPercent = discount * 100;
        double discountAmount = cuisineAmount * discount;
        content.setText(content.getText() + String.format("%-20s %30s", "Khuyến mãi"+String.format("(-%.0f%%):", discountPercent), String.format("-%,.0f VNĐ", discountAmount)));
        newLine();
        double finalAmount = cuisineAmount + tableAmount + vatAmount - discountAmount;
        content.setText(content.getText() + String.format("%-20s %30s", "Thành tiền:", String.format("%,.0f VNĐ", finalAmount)));
        newLine();
        newLine();
        double moneyFromCustomer = 0;
        content.setText(content.getText() + String.format("%-20s %30s", "Tiền khách đưa:", String.format("%,.0f VNĐ", moneyFromCustomer)));
        newLine();
        double refund = 0;
        content.setText(content.getText() + String.format("%-20s %30s", "Hoàn trả dư:", String.format("%,.0f VNĐ", refund)));
        newLine();
        boolean status = false; // TODO: change to true if paid
        content.setText(content.getText() + String.format("%-20s %30s", "Hoàn trả dư:", "Chưa thanh toán"));
        newLine();
        //Other
        newLine();
        separator();
        newLine();
        content.setText(content.getText() + "\n\t\tPASSWORD WIFI: 12345678");
        newLine();
        content.setText(content.getText() + "  Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!");
        newLine();
        content.setText(content.getText() + "\t   Hân hạnh được phục vụ quý khách!");
        newLine();
    }

    @FXML
    void onOverlayClicked(MouseEvent event) {
//        Node source = (Node) event.getSource();
//        Stage stage = (Stage) source.getScene().getWindow();
//        stage.close();
    }

    @FXML
    void preventEventClicked(MouseEvent event) {
        event.consume();
    }
}
