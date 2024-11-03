package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.huongbien.dao.DAO_Customer;
import com.huongbien.dao.DAO_Promotion;
import com.huongbien.database.Database;
import com.huongbien.entity.Cuisine;
import com.huongbien.entity.Customer;
import com.huongbien.entity.OrderDetail;
import com.huongbien.entity.Promotion;
import com.huongbien.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
import java.util.List;
import java.util.ResourceBundle;

public class GUI_OrderPaymentController implements Initializable {
    private final static String path_bill = "src/main/resources/com/huongbien/temp/bill.json";
    private final static String path_table = "src/main/resources/com/huongbien/temp/table.json";
    public Button btn_qrCustomer;
    public ImageView qrCodeView;
    @FXML
    private TableView<Promotion> tabView_promotion;

    @FXML
    private TableColumn<Promotion, Double> col_proDiscount;

    @FXML
    private TableColumn<Promotion, String> col_proID;

    @FXML
    private TableColumn<Promotion, String> col_proName;

    @FXML
    private Label lbl_payEmp;

    @FXML
    private Label lbl_payCuisineQuantity;

    @FXML
    private Label lbl_payTotalAmount;

    @FXML
    private Label lbl_payVAT;

    @FXML
    private Label lbl_payDiscount;

    @FXML
    private Label lbl_payFinalAmount;

    @FXML
    private Label lbl_payTabFloor;

    @FXML
    private Label lbl_payTabName;

    @FXML
    private Label lbl_payTabTypeName;

    @FXML
    private ScrollPane compoent_scrollBill;

    @FXML
    public GridPane compoent_gridBill;

    @FXML
    private TextField txt_searchCustomer;

    @FXML
    private TextField txt_idCustomer;

    @FXML
    private TextField txt_nameCustomer;

    @FXML
    private TextField txt_rankCustomer;

    @FXML
    private Button btn_addCustomer;


    private VideoCapture capture;
    private Timer timer;

    public GUI_MainController gui_mainController;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setGUI_MainController(GUI_MainController gui_mainController) {
        this.gui_mainController = gui_mainController;
    }

    public void loadingBill() throws FileNotFoundException {
        List<OrderDetail> orderDetails = new ArrayList<>(dataBill());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < orderDetails.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/huongbien/fxml/GUI_OrderPaymentBillItem.fxml"));
                HBox paymentBillBox = fxmlLoader.load();
                GUI_OrderPaymentBillItemController gui_OrderPaymentBillItemController = fxmlLoader.getController();
                gui_OrderPaymentBillItemController.setDataBill(orderDetails.get(i));
                gui_OrderPaymentBillItemController.setOrderPaymnetBillController(this);
                if (columns == 1) {
                    columns = 0;
                    ++rows;
                }
                compoent_gridBill.add(paymentBillBox, columns++, rows);
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

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineId(id);
            cuisine.setName(name);
            cuisine.setPrice(price);
            OrderDetail orderDetail = new OrderDetail(null, quantity, note, money, cuisine);
            orderDetailsList.add(orderDetail);
        }
        return orderDetailsList;
    }

    private List<OrderDetail> dataBill() throws FileNotFoundException {
        List<OrderDetail> ls = readFromJSON_bill();
        return ls;
    }

    public void setInfoPayment() throws FileNotFoundException, SQLException {
        JsonArray jsonArrayBill = Utils.readJsonFromFile(path_bill);
        JsonArray jsonArrayTab = Utils.readJsonFromFile(path_table);

        //get Employee

        for (JsonElement element : jsonArrayTab) {
            JsonObject jsonObject = element.getAsJsonObject();

            String name = jsonObject.get("Table Name").getAsString();
            int floor = jsonObject.get("Table Floor").getAsInt();
            JsonObject tableTypeObject = jsonObject.getAsJsonObject("Table Type");
            String typeName = tableTypeObject.get("Table Type Name").getAsString();

            String floorStr = (floor == 0 ? "Tầng trệt" : "Tầng "+floor);
            lbl_payTabFloor.setText(floorStr);
            lbl_payTabName.setText(name);
            lbl_payTabTypeName.setText(typeName);
        }

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
        lbl_payDiscount.setText(Utils.formatPrice(discountMoney)+" VNĐ");
        //set VAT
        double vat = totalAmount * 0.1;
        lbl_payCuisineQuantity.setText(totalQuantityCuisine+ " món");
        lbl_payTotalAmount.setText(Utils.formatPrice(totalAmount)+ " VNĐ");
        lbl_payVAT.setText("- "+ Utils.formatPrice(vat)+ " VNĐ");
        //FinalAmount
        double finalAmount = totalAmount - discountMoney - vat;
        lbl_payFinalAmount.setText(Utils.formatPrice(finalAmount)+ " VNĐ");
    }

    public void setCellValues() {
        try {
            DAO_Promotion dao_promotion = new DAO_Promotion(Database.getConnection());
            List<Promotion> promotionList = dao_promotion.get();
            ObservableList<Promotion> listPromotion = FXCollections.observableArrayList(promotionList);

            col_proID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
            col_proName.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_proDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            col_proDiscount.setCellFactory(col -> new TableCell<Promotion, Double>() {
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

            tabView_promotion.setItems(listPromotion);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadingBill();
            setInfoPayment();
            setCellValues();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btn_back(ActionEvent event) throws IOException {
        gui_mainController.openCuisine();
    }

    @FXML
    void btn_addCustomer(ActionEvent event) throws IOException {
        gui_mainController.openManageCustomer();
    }

    @FXML
    void btn_searchCustomer(MouseEvent event) throws SQLException {
        String inputPhone = txt_searchCustomer.getText().trim();
        DAO_Customer dao_customer = new DAO_Customer(Database.getConnection());
        Customer customer = dao_customer.getByPhone(inputPhone);
        if(customer != null) {
            txt_idCustomer.setText(customer.getCustomerId());
            txt_nameCustomer.setText(customer.getName());
            txt_rankCustomer.setText(Utils.toStringMembershipLevel(customer.getMembershipLevel()));
        } else {
            System.out.println("Không tìm thấy khách hàng, vui lòng đăng ký thành viên");
            txt_idCustomer.setText("");
            txt_nameCustomer.setText("");
            txt_rankCustomer.setText("");
        }
    }

    @FXML
    void btn_qrCustomer(ActionEvent event) {
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
                        updateCustomerFields(qrCodeContent);
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

    private void updateCustomerFields(String qrCodeContent) {
        String[] parts = qrCodeContent.split(",");
        if (parts.length >= 4) {
            Platform.runLater(() -> {
                txt_idCustomer.setText(parts[0]);
                txt_nameCustomer.setText(parts[1]);
                txt_rankCustomer.setText(parts[2]);
                txt_searchCustomer.setText(parts[3]);
            });
        }
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
