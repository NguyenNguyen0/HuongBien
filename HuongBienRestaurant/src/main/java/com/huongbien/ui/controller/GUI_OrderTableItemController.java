package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.dao.DAO_Table;
import com.huongbien.database.Database;
import com.huongbien.entity.Table;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GUI_OrderTableItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/table.json";
    @FXML
    private ImageView imgView_table;

    @FXML
    private ImageView imgView_check;

    @FXML
    private Label lbl_tableID;

    @FXML
    private Label lbl_tableName;

    @FXML
    private Label lbl_tableSeats;

    @FXML
    private Label lbl_tableType;

    public void setData(Table table) {
        lbl_tableID.setText(table.getId());
        lbl_tableName.setText(table.getName());
        lbl_tableType.setText(table.getTableType().getName());
        lbl_tableSeats.setText("Số chỗ: "+ table.getSeats());

        String tableStatus = table.getStatus();
        switch (tableStatus) {
            case "Bàn trống" -> imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-empty-512px.png"));
            case "Đặt trước" -> imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-preOrder-512px.png"));
            case "Phục vụ" -> imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-ordered-512px.png"));
            case "Bàn đóng" -> imgView_table.setImage(new Image("/com/huongbien/icon/order/tab-stop-512px.png"));
        }
        setCheckTableFromJSON();
    }

    public void setCheckTableFromJSON() {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }
        boolean tableExists = false;
        for (JsonElement element : jsonArray) {
            JsonObject existingTable = element.getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(lbl_tableID.getText())) {
                tableExists = true;
                break;
            }
        }
        if (tableExists) {
            imgView_check.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        }
    }



    public GUI_OrderTableController gui_orderTableController;
    public void setGui_orderTableController(GUI_OrderTableController gui_orderTableController) {
        this.gui_orderTableController = gui_orderTableController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    boolean isCheck = false;
    public void handleCheck() {
        if (!isCheck) {
            imgView_check.setImage(new Image("/com/huongbien/icon/order/check-mark-128px.png"));
            isCheck = true;
        } else {
            imgView_check.setImage(null);
            isCheck = false;
        }
    }

    private void writeDataJSONtoFile(String tabID, String tabName, int tabFloor, int tabSeats, String tabStatus, String tabTypeID, String tabTypeName, String tabTypeDescription) {
        JsonArray jsonArray;
        try {
            jsonArray = Utils.readJsonFromFile(path);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }

        boolean tableExists = false;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject existingTable = jsonArray.get(i).getAsJsonObject();
            if (existingTable.has("Table ID") && existingTable.get("Table ID").getAsString().equals(tabID)) {
                jsonArray.remove(i);
                tableExists = true;
                break;
            }
        }

        if (!tableExists) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Table ID", tabID);
            jsonObject.addProperty("Table Name", tabName);
            jsonObject.addProperty("Table Floor", tabFloor);
            jsonObject.addProperty("Table Seats", tabSeats);
            jsonObject.addProperty("Table Status", tabStatus);

            // Thêm sub-document cho Table Type
            JsonObject tableTypeObject = new JsonObject();
            tableTypeObject.addProperty("Table Type ID", tabTypeID);
            tableTypeObject.addProperty("Table Type Name", tabTypeName);
            tableTypeObject.addProperty("Table Type Description", tabTypeDescription);

            jsonObject.add("Table Type", tableTypeObject);

            jsonArray.add(jsonObject);
        }
        Utils.writeJsonToFile(jsonArray, path);
    }


    private void handleWriteJS() {
        String tabID = lbl_tableID.getText();
        try {
            DAO_Table dao_table = new DAO_Table(Database.getConnection());
            Table table = dao_table.get(tabID);
            writeDataJSONtoFile(
                    table.getId(),table.getName(), table.getFloor(), table.getSeats(), table.getStatus(),
                    table.getTableType().getTableId(), table.getTableType().getName(), table.getTableType().getDescription()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection();
        }
    }

    @FXML
    void getInfoTable(MouseEvent event) throws FileNotFoundException {
        handleCheck();
        handleWriteJS();
        gui_orderTableController.tabPane_infoTable.getTabs().clear();
        gui_orderTableController.readFromJSON();
    }
}
