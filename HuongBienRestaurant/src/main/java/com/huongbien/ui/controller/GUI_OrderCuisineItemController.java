package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huongbien.dao.CuisineDAO;
import com.huongbien.entity.Cuisine;
import com.huongbien.utils.Converter;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GUI_OrderCuisineItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/temporaryBill.json";
    @FXML
    private Circle circle_imgCuisine;

    @FXML
    private Label lbl_cuisineID;

    @FXML
    private Label lbl_cuisineName;

    @FXML
    private Label lbl_cuisinePrice;

    private GUI_OrderCuisineController gui_orderCuisineController;

    public void setOrderCuisineController(GUI_OrderCuisineController gui_orderCuisineController) {
        this.gui_orderCuisineController = gui_orderCuisineController;
    }

    public void setDataCuisine(Cuisine cuisine) {
        lbl_cuisineID.setText(cuisine.getCuisineId());
        lbl_cuisineName.setText(cuisine.getName());
        lbl_cuisinePrice.setText(Utils.formatPrice(cuisine.getPrice()));

        byte[] imageBytes = cuisine.getImage();
        Image image;

        if (imageBytes != null) {
            image = Converter.bytesToImage(imageBytes);
        } else {
            image = new Image("/com/huongbien/icon/mg_cuisine/empty-256px.png");
        }

        circle_imgCuisine.setFill(new ImagePattern(image));
        circle_imgCuisine.setStroke(Color.BLUE);
        circle_imgCuisine.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void writeDataJSONtoFile(String cuisineID, String cuisineName, double cuisinePrice, String cuisineNote, int cuisineQuantity) {
        boolean idExists = false;
        JsonArray jsonArray;

        try {
            jsonArray = Utils.readJsonFromFile(path);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String existingCuisineID = jsonObject.get("Cuisine ID").getAsString();
            if (existingCuisineID.equals(cuisineID)) {
                int currentQuantity = jsonObject.get("Cuisine Quantity").getAsInt();
                jsonObject.addProperty("Cuisine Quantity", currentQuantity + 1);
                double updatedMoney = jsonObject.get("Cuisine Price").getAsDouble() * (currentQuantity + 1);
                jsonObject.addProperty("Cuisine Money", updatedMoney);
                idExists = true;
                break;
            }
        }

        if (!idExists) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Cuisine ID", cuisineID);
            jsonObject.addProperty("Cuisine Name", cuisineName);
            jsonObject.addProperty("Cuisine Price", cuisinePrice);
            jsonObject.addProperty("Cuisine Note", cuisineNote);
            jsonObject.addProperty("Cuisine Quantity", cuisineQuantity);
            double cuisineMoney = cuisinePrice * cuisineQuantity;
            jsonObject.addProperty("Cuisine Money", cuisineMoney);
            jsonArray.add(jsonObject);
        }
        Utils.writeJsonToFile(jsonArray, path);
    }

    @FXML
    void ml_getInfoCuisine(MouseEvent event) throws FileNotFoundException, SQLException {
        String id = lbl_cuisineID.getText();
        CuisineDAO cuisineDAO = CuisineDAO.getInstance();
        Cuisine cuisine = cuisineDAO.getById(id);
        writeDataJSONtoFile(cuisine.getCuisineId(), cuisine.getName(), cuisine.getPrice(), "", 1);
        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
        //update lbl
        gui_orderCuisineController.readFromJSON_setInfo();
    }
}
