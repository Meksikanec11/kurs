package com.example.kurs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductController implements Initializable {


    public static int id;

    @FXML
    private Label amountProduct;

    @FXML
    private Label nameProduct;

    @FXML
    private Label priceProduct;

    @FXML
    private Label typeProduct;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Map<String, String> dataProduct = getDataProduct();
            typeProduct.setText(dataProduct.get("type"));
            amountProduct.setText(dataProduct.get("count"));
            nameProduct.setText(dataProduct.get("name"));
            priceProduct.setText(dataProduct.get("price"));
            dataProduct.clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public  Map<String,String> getDataProduct() throws SQLException {
        Map<String,String> map = new HashMap<>();

        String query;
        query = "select product.name,product.price,type_product.name,product.amount from product,type_product where product.type_product_idtype_product = type_product.idtype_product and product.idProduct = " + id;
        ResultSet resultSet = DBConnector.executeQuery(query);
        while(resultSet.next()){
            map.put("name",resultSet.getString(1));
            map.put("price",resultSet.getString(2));
            map.put("type",resultSet.getString(3));
            map.put("count",resultSet.getString(4));
        }

        resultSet.close();
        return map;
    }

    public void toProductCard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("product-view.fxml"));
        Stage stage = (Stage) nameProduct.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void toOrderProduct() throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("order-view.fxml"));
        Stage stage = (Stage) nameProduct.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }




}
