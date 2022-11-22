package com.example.kurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductCardController implements Initializable {

    @FXML
    private TextField findField;

    @FXML
    private Label nullProducts;

    private ObservableList<ProductCard> products = FXCollections.observableArrayList();


    public static int id = 0;

    @FXML
    private Label productLabel;

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setCardProduct();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void setCardProduct() throws IOException, SQLException {
        if (findField.getText().isEmpty()) {
            addDataToProducts();
            nullProducts.setVisible(false);
        } else addDataToProductsFind();
        if (!products.isEmpty()) {
            nullProducts.setVisible(false);
            Scene scene = HelloApplication.stage.getScene();

            double x = scene.getWindow().getX() / 4;
            double y = scene.getWindow().getY() / 3;
            int col = 0;
            int rows = 2;
            int rowSpace = 80;
            for (int i = 0; i < products.size(); i++) {
                if (i == 1) x += 10;
                if (rows > 0) y = scene.getWindow().getY() / 3+80 - rowSpace;
                if ((i % 4 == 0)) {
                    col = 0;
                } else if (i != 0) {
                    ++col;
                }
                if ((i % 4 == 0) && (i > 0)) rows++;
                root.getChildren().add(products.get(i).createCardProduct(x * col + 20, y * rows - 40));
            }
        } else nullProducts.setVisible(true);
        products.clear();
    }


    private void addDataToProducts() throws SQLException, IOException {
        ArrayList<String> data = null;
        ArrayList<Integer> idData = null;
        data = getProductInformation();
        idData = getIdProduct();

        int id = 0;
        for (int i = 0; i < data.size(); i += 3) {
            products.add(new ProductCard(data.get(i), data.get(i + 1), data.get(i + 2), idData.get(id), "Card" + id));
            id++;
        }
    }
    private void addDataToProductsFind() throws SQLException, IOException {
        ArrayList<String> data = null;
        ArrayList<Integer> idData = null;
        idData = getIdProductFind();
        data = getProductInformationFind();
        int id = 0;
        for (int i = 0; i < data.size(); i += 3) {
            products.add(new ProductCard(data.get(i), data.get(i + 1), data.get(i + 2), idData.get(id), "Card" + id));
            id++;
        }
    }


    private ArrayList<String> getProductInformation() throws SQLException {
        ArrayList<String> arrayList = new ArrayList<>();
        String query = "select product.name,product.price,type_product.name from product,type_product where product.type_product_idtype_product = type_product.idtype_product";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(1));
            arrayList.add(resultSet.getString(2));
            arrayList.add(resultSet.getString(3));
        }
        DBConnector.closeConnection();
        return arrayList;
    }

    private ArrayList<Integer> getIdProduct() throws SQLException {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String query = "select product.idProduct from product,type_product where product.type_product_idtype_product = type_product.idtype_product";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            arrayList.add(resultSet.getInt(1));
        }
        DBConnector.closeConnection();
        return arrayList;
    }

    private ArrayList<Integer> getIdProductFind() throws SQLException {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String query = "select product.idProduct from product,type_product where product.type_product_idtype_product = type_product.idtype_product and product.name like '%" + findField.getText() + "%'";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            arrayList.add(resultSet.getInt(1));
        }
        DBConnector.closeConnection();
        return arrayList;
    }


    private ArrayList<String> getProductInformationFind() throws SQLException {
        ArrayList<String> arrayList = new ArrayList<>();
        String query = "select product.name,product.price,type_product.name from product,type_product where product.type_product_idtype_product = type_product.idtype_product and product.name like '%" + findField.getText() + "%'";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(1));
            arrayList.add(resultSet.getString(2));
            arrayList.add(resultSet.getString(3));
        }
        DBConnector.closeConnection();
        return arrayList;
    }

    public void onFindKeyTyped() throws SQLException, IOException {
        while (root.getChildren().size() != 4) {
            root.getChildren().remove(4);
        }
        setCardProduct();
    }


    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
