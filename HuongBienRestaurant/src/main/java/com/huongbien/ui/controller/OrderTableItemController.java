package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.TableDAO;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderTableItemController implements Initializable {
    private final static String TEMPORARY_TABLE_PATH = "src/main/resources/com/huongbien/temp/temporaryTable.json";

    @FXML
    private ImageView tableImageView;

    @FXML
    private ImageView checkedImageView;

    @FXML
    private Label tableIdLabel;

    @FXML
    private Label tableNameLabel;

    @FXML
    private Label tableSeatLabel;

    @FXML
    private Label tableTypeLabel;

    private boolean isCheck = false;

    public OrderTableController orderTableController;

    public void setOrderTableController(OrderTableController orderTableController) {
        this.orderTableController = orderTableController;
    }

    public void setTableItemData(Table table) {
        tableIdLabel.setText(table.getId());
        tableNameLabel.setText(table.getName());
        tableTypeLabel.setText(table.getTableType().getName());
        tableSeatLabel.setText("Số chỗ: " + table.getSeats());

        setTableImage(table.getStatus());
        setCheckedTableFromJSON();
    }

    private void setTableImage(String tableStatus) {
        switch (tableStatus) {
            case "Bàn trống" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-empty-512px.png"));
            case "Đặt trước" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-preOrder-512px.png"));
            case "Phục vụ" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-ordered-512px.png"));
            case "Bàn đóng" -> tableImageView.setImage(new Image("/com/huongbien/icon/order/tab-stop-512px.png"));
        }
    }

    private void setCheckedTableFromJSON() {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableIdLabel.getText())) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    public void updateCheckedIcon(String tableId) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(TEMPORARY_TABLE_PATH);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            checkedImageView.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        } else {
            checkedImageView.setImage(null);
            isCheck = false;
        }
    }

    private void writeDataToJSONFile(String tableId) throws SQLException {
        TableDAO dao_table = TableDAO.getInstance();
        Table table = dao_table.getById(tableId);

        if (table != null) {
            JsonArray jsonArray;
            try {
                jsonArray = Utils.readJsonFromFile(TEMPORARY_TABLE_PATH);
            } catch (FileNotFoundException e) {
                jsonArray = new JsonArray();
            }

            boolean tableExists = false;
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject existingTable = jsonArray.get(i).getAsJsonObject();
                if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tableId)) {
                    jsonArray.remove(i);
                    tableExists = true;
                    break;
                }
            }

            if (!tableExists) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Table ID", table.getId());
                jsonArray.add(jsonObject);
            }

            Utils.writeJsonToFile(jsonArray, TEMPORARY_TABLE_PATH);

            updateCheckedIcon(tableId);
        } else {
            System.out.println("Table with ID " + tableId + " not found in the database.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onTableItemComponentClicked(MouseEvent event) throws FileNotFoundException, SQLException {
        String tableId = tableIdLabel.getText();
        writeDataToJSONFile(tableId);
        orderTableController.tableInfoTabPane.getTabs().clear();
        orderTableController.readTableDataFromJSON();
    }
}
