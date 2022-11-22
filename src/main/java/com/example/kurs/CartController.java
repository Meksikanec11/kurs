package com.example.kurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    public static  ObservableList<CartData> data = FXCollections.observableArrayList();

    @FXML
    private TableView<CartData> tableView;
    @FXML
    private TableColumn<CartData, Integer> countProductColumn;

    @FXML
    private Label finalPrice;

    @FXML
    private TableColumn<CartData, String> nameProductColumn;

    @FXML
    private TableColumn<CartData, Double> priceProductColumn;

    @FXML
    private TableColumn<CartData, String> typeProductColumn;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteCopies();
        setFinalFrice();
        nameProductColumn.setCellValueFactory(new PropertyValueFactory<CartData,String>("nameProduct"));
        typeProductColumn.setCellValueFactory(new PropertyValueFactory<CartData,String>("typeProduct"));
        countProductColumn.setCellValueFactory(new PropertyValueFactory<CartData,Integer>("countProduct"));
        priceProductColumn.setCellValueFactory(new PropertyValueFactory<CartData,Double>("priceProduct"));
        tableView.setItems(data);
    }


    private void setFinalFrice(){
        int price = 0;
        for (int i = 0; i < data.size(); i++) {
            price+=data.get(i).getPriceProduct();
        }
        finalPrice.setText(String.valueOf(price));
    }

    @FXML
    void toProdcutCards() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("product-view.fxml"));
        Stage stage = (Stage) finalPrice.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void orderProduct() throws SQLException, IOException {

        String orderQuery = "insert into `orderr` values("
                +(getLastOrderId()+1)+","
                +(getLastNumberOrder()+1)+","
                +(getAmountProduct())+","
                + finalPrice.getText()+","
                + getClientId() +
                ")";
        DBConnector.insertQuery(orderQuery);
        insertOrderHasProduct();
        data.clear();
        toClient();
        DBConnector.closeConnection();
    }

    private void insertOrderHasProduct() throws SQLException {
        ArrayList<Integer> dataProduct = getProductId();
        for (int i = 0; i < dataProduct.size(); i++) {
            String query = "insert into order_has_prod values("+getLastOrderId()+","+dataProduct.get(i)+")";
            DBConnector.insertQuery(query);
            DBConnector.closeConnection();
        }

    }

    private ArrayList<Integer> getProductId() throws SQLException {
        ArrayList<Integer> dataProduct = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String query = "select idProduct from product where name = '"+data.get(i).getNameProduct()+"'";
            ResultSet resultSet = DBConnector.executeQuery(query);
            while(resultSet.next()){
                dataProduct.add(resultSet.getInt(1));
            }
            DBConnector.closeConnection();
        }
        return dataProduct;
    }


    private int getLastOrderId() throws SQLException {
        String query = "select max(idOrder) from `orderr`";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while(resultSet.next()){
            return resultSet.getInt(1);
        }
        DBConnector.closeConnection();
        return 0;
    }
    private int getLastNumberOrder() throws SQLException {
        String query = "select max(order_num) from `orderr`";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while(resultSet.next()){
            return resultSet.getInt(1);
        }
        DBConnector.closeConnection();
        return 0;
    }

    private int getAmountProduct(){
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            count+=data.get(i).getCountProduct();
        }
        return count;
    }

    private int getClientId() throws SQLException {
        String query = "select idClient from client where login = '"+LoginController.login+"'";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()){
            return resultSet.getInt(1);
        }
        DBConnector.closeConnection();
        return 0;
    }


    private void deleteCopies(){
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if ((data.get(i).getNameProduct().equals(data.get(j).getNameProduct()))&&(i!=j)){
                    data.get(i).setCountProduct(data.get(i).getCountProduct()+data.get(j).getCountProduct());
                    data.get(i).updatePrice();
                    data.remove(j);
                }
            }
        }
    }

    private void toClient() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) finalPrice.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
