package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.huongbien.dao.CustomerDAO;
import com.huongbien.dao.EmployeeDAO;
import com.huongbien.dao.PromotionDAO;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.*;
import com.huongbien.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentController implements Initializable {
    private final static String TEMPORARY_USER_PATH = "src/main/resources/com/huongbien/temp/loginSession.json";
    private final static String TEMPORARY_BILL_PATH = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    private final static String TEMPORARY_TABLE_PATH = "src/main/resources/com/huongbien/temp/temporaryTable.json";
    @FXML
    private TableView<Promotion> promotionTable;

    @FXML
    private TableColumn<Promotion, Double> promotionDiscountColumn;

    @FXML
    private TableColumn<Promotion, String> promotionIdColumn;

    @FXML
    private TableColumn<Promotion, String> promotionNameColumn;

    @FXML
    private Label paymentEmployeeLabel;

    @FXML
    private Label paymentCuisineQuantityLabel;

    @FXML
    private Label paymentTotalAmountLabel;

    @FXML
    private Label paymentVATLabel;

    @FXML
    private Label paymentDiscountLabel;

    @FXML
    private Label paymentFinalAmountLabel;

    @FXML
    private Label tableInfoLabel;

    @FXML
    private ScrollPane billScrollPane;

    @FXML
    public GridPane billGridPane;

    @FXML
    private TextField searchCustomerField;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerRankField;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button createCustomerQRButton;

    private VideoCapture capture;
    private Timer timer;

    public RestaurantMainController restaurantMainController;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setRestaurantMainController(RestaurantMainController restaurantMainController) {
        this.restaurantMainController = restaurantMainController;
    }

    public void loadBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(getBillData());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                OrderPaymentBillItemController _OrderPaymentBillItemController = fxmlLoader.getController();
                _OrderPaymentBillItemController.setDataBill(orderDetails.get(i));
                _OrderPaymentBillItemController.setOrderPaymentBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                billGridPane.add(paymentBillBox, columns++, rows);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        billScrollPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        billGridPane.prefWidthProperty().bind(billScrollPane.widthProperty());
    }

    public List<OrderDetail> readFromBillJSON() throws FileNotFoundException {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        JsonArray jsonArray = Utils.readJsonFromFile(TEMPORARY_BILL_PATH);

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

    private List<OrderDetail> getBillData() throws FileNotFoundException {
        List<OrderDetail> ls = readFromBillJSON();
        return ls;
    }

    public void setPaymentInfo() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayBill = Utils.readJsonFromFile(TEMPORARY_BILL_PATH);
        JsonArray jsonArrayTab = Utils.readJsonFromFile(TEMPORARY_TABLE_PATH);
        JsonArray jsonArrayEmp = Utils.readJsonFromFile(TEMPORARY_USER_PATH);
        //Emp Session
        for (JsonElement element : jsonArrayEmp) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("Employee ID").getAsString();
            EmployeeDAO dao_employee = EmployeeDAO.getInstance();
            Employee employee = dao_employee.getById(id).getFirst();
            paymentEmployeeLabel.setText(employee.getName());
        }
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
        tableInfoLabel.setText(tabInfoBuilder.toString());
        //
        int totalQuantityCuisine = 0;
        double totalAmount = 0.0;
        for (JsonElement element : jsonArrayBill) {
            totalQuantityCuisine++;
            JsonObject jsonObject = element.getAsJsonObject();
            double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
            totalAmount += cuisineMoney;
        }
        //set discount
        double discount = 0.0;
        double discountMoney = totalAmount * discount;
        paymentDiscountLabel.setText(Utils.formatPrice(discountMoney) + " VNĐ");
        //set VAT
        double vat = totalAmount * 0.1;
        paymentCuisineQuantityLabel.setText(totalQuantityCuisine + " món");
        paymentTotalAmountLabel.setText(Utils.formatPrice(totalAmount) + " VNĐ");
        paymentVATLabel.setText("- " + Utils.formatPrice(vat) + " VNĐ");
        //FinalAmount
        double finalAmount = totalAmount - discountMoney - vat;
        paymentFinalAmountLabel.setText(Utils.formatPrice(finalAmount) + " VNĐ");
    }

    public void setPromotionTableValue() {
        PromotionDAO promotionDAO = PromotionDAO.getInstance();
        List<Promotion> promotionList = promotionDAO.getAll();
        promotionList.sort(Comparator.comparing(Promotion::getDiscount).reversed());

        ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
        promotionIdColumn.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        promotionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        promotionDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        promotionDiscountColumn.setCellFactory(col -> new TableCell<Promotion, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item * 100));
                }
            }
        });

        promotionTable.setItems(listPromotion);
        promotionTable.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadBill();
            setPaymentInfo();
            setPromotionTableValue();
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        restaurantMainController.openCuisine();
    }

    @FXML
    void addCustomerButton(ActionEvent event) throws IOException {
        restaurantMainController.openManageCustomer();
    }

    @FXML
    void onSearchCustomerButtonClicked(MouseEvent event) throws SQLException, FileNotFoundException {
        String inputPhone = searchCustomerField.getText().trim();
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        List<Customer> customers = customerDAO.getByPhoneNumber(inputPhone);
        Customer customer = customers.isEmpty() ? null : customers.get(0);
        if (customer != null) {
            customerIdField.setText(customer.getCustomerId());
            customerNameField.setText(customer.getName());
            customerRankField.setText(Utils.toStringMembershipLevel(customer.getMembershipLevel()));
            promotionTable.setDisable(false);
            // Set discount
            double totalAmount = 0;
            JsonArray jsonArrayBill = Utils.readJsonFromFile(TEMPORARY_BILL_PATH);
            for (JsonElement element : jsonArrayBill) {
                JsonObject jsonObject = element.getAsJsonObject();
                double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
                totalAmount += cuisineMoney;
            }
            double discount = 0.0;
            PromotionDAO promotionDAO = PromotionDAO.getInstance();
            List<Promotion> promotionList = promotionDAO.getAll();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            if (!listPromotion.isEmpty()) {
                Promotion maxDiscountPromotion = listPromotion.stream()
                        .max(Comparator.comparingDouble(Promotion::getDiscount))
                        .orElse(null);

                promotionTable.getSelectionModel().select(maxDiscountPromotion);
                discount = maxDiscountPromotion.getDiscount();
            } else {
                discount = 0.0;
                System.out.println("No promotions available.");
            }
            double discountMoney = totalAmount * discount;
            paymentDiscountLabel.setText(" - " + Utils.formatPrice(discountMoney) + " VNĐ");
            //set VAT
            double vat = totalAmount * 0.1;
            paymentTotalAmountLabel.setText(Utils.formatPrice(totalAmount) + " VNĐ");
            paymentVATLabel.setText("- " + Utils.formatPrice(vat) + " VNĐ");
            //FinalAmount
            double finalAmount = totalAmount - discountMoney - vat;
            paymentFinalAmountLabel.setText(Utils.formatPrice(finalAmount) + " VNĐ");
        } else {
            System.out.println("Không tìm thấy khách hàng, vui lòng đăng ký thành viên");
            customerIdField.setText("");
            customerNameField.setText("");
            customerRankField.setText("");
            promotionTable.setDisable(true);
            promotionTable.getSelectionModel().clearSelection();
            setPaymentInfo();
        }
    }

    @FXML
    void onPromotionTableClicked(MouseEvent event) throws FileNotFoundException, SQLException {
        Promotion selectedItem = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String idSelect = selectedItem.getPromotionId();
            PromotionDAO promotionDAO = PromotionDAO.getInstance();
            Promotion promotion = promotionDAO.getById(idSelect);

            double totalAmount = 0;
            JsonArray jsonArrayBill = Utils.readJsonFromFile(TEMPORARY_BILL_PATH);
            for (JsonElement element : jsonArrayBill) {
                JsonObject jsonObject = element.getAsJsonObject();
                double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
                totalAmount += cuisineMoney;
            }

            double discount = promotion.getDiscount();
            double discountMoney = totalAmount * discount;
            paymentDiscountLabel.setText(" - " + Utils.formatPrice(discountMoney) + " VNĐ");
            //set VAT
            double vat = totalAmount * 0.1;
            paymentTotalAmountLabel.setText(Utils.formatPrice(totalAmount) + " VNĐ");
            paymentVATLabel.setText("- " + Utils.formatPrice(vat) + " VNĐ");
            //FinalAmount
            double finalAmount = totalAmount - discountMoney - vat;
            paymentFinalAmountLabel.setText(Utils.formatPrice(finalAmount) + " VNĐ");
        }
    }

    @FXML
    void onCreateCustomerQRButtonClicked(ActionEvent event) {
        openSwingWindow();
    }

    private void openSwingWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cửa sổ quét mã QR");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLayout(new BorderLayout());

            JLabel cameraLabel = new JLabel();
            frame.add(cameraLabel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Đóng");
            closeButton.addActionListener(e -> {
                if (capture != null) {
                    capture.release();
                }
                frame.dispose();
            });
            frame.add(closeButton, BorderLayout.SOUTH);

            frame.setVisible(true);
            readQRCode(cameraLabel, frame);
        });
    }

    private void readQRCode(JLabel cameraLabel, JFrame frame) {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            showAlert("Không thể mở camera!", "Lỗi");
            return;
        }

        timer = new Timer(100, e -> {
            Mat frameMat = new Mat();
            if (capture != null && capture.read(frameMat)) {
                if (!frameMat.empty()) {
                    BufferedImage bufferedImage = matToBufferedImage(frameMat);
                    String qrCodeContent = decodeQRCode(bufferedImage);
                    if (qrCodeContent != null) {
                        try {
                            updateCustomerFields(qrCodeContent);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        timer.stop();
                        capture.release();
                        cameraLabel.setIcon(null);
                        frame.dispose();
                    } else {
                        ImageIcon icon = new ImageIcon(bufferedImage);
                        cameraLabel.setIcon(icon);
                    }
                }
            } else {
                System.err.println("Không thể đọc frame từ camera.");
            }
        });

        timer.start();
    }


    private BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int channels = matrix.channels();
        byte[] data = new byte[cols * rows * channels];
        matrix.get(0, 0, data);
        BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    private String decodeQRCode(BufferedImage bufferedImage) {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            QRCodeReader reader = new QRCodeReader();
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            return null;
        }
    }

    private void updateCustomerFields(String qrCodeContent) throws SQLException, FileNotFoundException {
        String[] parts = qrCodeContent.split(",");
        if (parts.length >= 4) {
            Platform.runLater(() -> {
                customerIdField.setText(parts[0]);
                customerNameField.setText(parts[1]);
                customerRankField.setText(Utils.toStringMembershipLevel(Integer.parseInt(parts[2])));
                searchCustomerField.setText(parts[3]);
            });
        }
        Platform.runLater(() -> {
            promotionTable.setDisable(false);
            // Set discount
            double totalAmount = 0;
            JsonArray jsonArrayBill = null;
            try {
                jsonArrayBill = Utils.readJsonFromFile(TEMPORARY_BILL_PATH);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            for (JsonElement element : jsonArrayBill) {
                JsonObject jsonObject = element.getAsJsonObject();
                double cuisineMoney = jsonObject.get("Cuisine Money").getAsDouble();
                totalAmount += cuisineMoney;
            }
            double discount = 0.0;
            PromotionDAO promotionDAO = null;
            promotionDAO = PromotionDAO.getInstance();
            List<Promotion> promotionList = promotionDAO.getAll();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);
            if (!listPromotion.isEmpty()) {
                Promotion maxDiscountPromotion = listPromotion.stream()
                        .max(Comparator.comparingDouble(Promotion::getDiscount))
                        .orElse(null);

                promotionTable.getSelectionModel().select(maxDiscountPromotion);
                discount = maxDiscountPromotion.getDiscount();
            } else {
                discount = 0.0;
                System.out.println("No promotions available.");
            }
            double discountMoney = totalAmount * discount;
            paymentDiscountLabel.setText(" - " + Utils.formatPrice(discountMoney) + " VNĐ");
            //set VAT
            double vat = totalAmount * 0.1;
            paymentTotalAmountLabel.setText(Utils.formatPrice(totalAmount) + " VNĐ");
            paymentVATLabel.setText("- " + Utils.formatPrice(vat) + " VNĐ");
            //FinalAmount
            double finalAmount = totalAmount - discountMoney - vat;
            paymentFinalAmountLabel.setText(Utils.formatPrice(finalAmount) + " VNĐ");
        });
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}