package com.example.kurs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TextField countProduct;

    @FXML
    private Label idProduct;

    @FXML
    private Label nameProduct;

    @FXML
    private Label typeProduct;

    @FXML
    void toProductCard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("product-view.fxml"));
        Stage stage = (Stage) nameProduct.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void addProductToCart() throws SQLException, IOException {
        addDataProductToCart();
        Parent root = FXMLLoader.load(getClass().getResource("cart-view.fxml"));
        Stage stage = (Stage) nameProduct.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void addDataProductToCart() throws SQLException {
        ArrayList<Integer> intData = getPriceAmountProduct();
        ArrayList<String> stringData = getNameTypeProduct();
        CartController.data.add(new CartData(stringData.get(0),stringData.get(1),Integer.parseInt(countProduct.getText()),intData.get(0)));
    }

    private ArrayList<String> getNameTypeProduct() throws SQLException {
        ArrayList<String> stringDataProdcut = new ArrayList<>();

        String query = "select product.name, type_product.name from product,type_product where type_product.idtype_product = product.type_product_idtype_product and product.idProduct = "+ProductController.id;
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()){
            stringDataProdcut.add(resultSet.getString(1));
            stringDataProdcut.add(resultSet.getString(2));
        }
        DBConnector.closeConnection();
        return stringDataProdcut;
    }
    private ArrayList<Integer> getPriceAmountProduct() throws SQLException {
        ArrayList<Integer> intDataProduct = new ArrayList<>();

        String query = "select price from product  where idProduct = "+ProductController.id;
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()){
            intDataProduct.add(resultSet.getInt(1));
        }
        DBConnector.closeConnection();
        return intDataProduct;
    }

    private Map<Integer,String> getDataProduct() throws SQLException {
        Map<Integer,String> dataProduct = new HashMap<>();
        String query = "select product.name,product.idProduct,type_product.name from product,type_product where product.type_product_idtype_product = type_product.idtype_product and product.idProduct = " + ProductController.id;
        ResultSet resultSet = DBConnector.executeQuery(query);
        while(resultSet.next()){
            dataProduct.put(1,resultSet.getString(1));
            dataProduct.put(2,resultSet.getString(2));
            dataProduct.put(3,resultSet.getString(3));
        }
        DBConnector.closeConnection();
        return dataProduct;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Map<Integer,String> data = getDataProduct();
            nameProduct.setText(data.get(1));
            idProduct.setText(data.get(2));
            typeProduct.setText(data.get(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
