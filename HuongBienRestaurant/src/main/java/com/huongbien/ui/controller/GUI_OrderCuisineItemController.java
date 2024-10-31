package com.huongbien.ui.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huongbien.entity.Cuisine;
import com.huongbien.utils.Converter;
import com.huongbien.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GUI_OrderCuisineItemController implements Initializable {
    private final static String path = "src/main/resources/com/huongbien/temp/bill.json";
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
        lbl_cuisinePrice.setText(cuisine.getPrice()+"");
        //image
        byte[] imageBytes = cuisine.getImage();
        Image image = Converter.bytesToImage(imageBytes);
        circle_imgCuisine.setFill(new ImagePattern(image));
        circle_imgCuisine.setStroke(Color.BLUE);
        circle_imgCuisine.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    private void writeDataJSONtoFile(String cuisineID, String cuisineName, double cuisinePrice, String cuisineNote, int cuisineQuantity) {
        JsonArray jsonArray;

        try {
            jsonArray = Utils.readJsonFromFile(path);
        } catch (FileNotFoundException e) {
            jsonArray = new JsonArray();
        }

        boolean idExists = false;

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
    void ml_getInfoCuisine(MouseEvent event) throws FileNotFoundException {
        String id = lbl_cuisineID.getText();
        String name = lbl_cuisineName.getText();
        double price = Double.parseDouble(lbl_cuisinePrice.getText());

        System.out.println(id);
        System.out.println(name);
        System.out.println(price);

        writeDataJSONtoFile(id, name, price, "", 1);

        gui_orderCuisineController.compoent_gridBill.getChildren().clear();
        gui_orderCuisineController.loadingBill();
    }
}
