package com.huongbien.ui.controller;

import com.huongbien.dao.DAO_Reservation;
import com.huongbien.database.Database;
import com.huongbien.entity.Customer;
import com.huongbien.entity.FoodOrder;
import com.huongbien.entity.Reservation;
import com.huongbien.entity.Table;
import com.huongbien.utils.Paginator;
import com.huongbien.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class GUI_ManageListOrderController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
