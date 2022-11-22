package com.example.kurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable {




    private ObservableList<OrderData> orderData = FXCollections.observableArrayList();

    public static String login;

    @FXML
    private Label FIO;
    @FXML
    private Label Email;
    @FXML
    private Label phoneNumber;


    @FXML
    private TableColumn<OrderData, String> nameProductColomn;

    @FXML
    private Label nullOrder;

    @FXML
    private TableColumn<OrderData, String> numberOrderColomn;

    @FXML
    private TableView<OrderData> tableView;

    @FXML
    private TableColumn<OrderData,String> typeProductColomn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addDataToOrderClient();
            setDataNormal();
            Map<String, String> information = getInformationClient(login);
            FIO.setText(information.get("FIO"));
            Email.setText(information.get("e-mail"));
            phoneNumber.setText(information.get("phone"));

            if(!orderData.isEmpty()) {
                numberOrderColomn.setCellValueFactory(new PropertyValueFactory<OrderData, String>("numberOrder"));
                typeProductColomn.setCellValueFactory(new PropertyValueFactory<OrderData, String>("typeProduct"));
                nameProductColomn.setCellValueFactory(new PropertyValueFactory<OrderData, String>("nameProduct"));
                tableView.setItems(orderData);
            } else {
                tableView.setVisible(false);
                nullOrder.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void onProductButtonClick() throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("product-view.fxml"));
        Stage stage = (Stage) FIO.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void setDataNormal(){
        if(!orderData.isEmpty()){
            for (int i = 0; i < orderData.size(); i++) {
                for (int j = 0; j < orderData.size(); j++) {
                    if((orderData.get(i).getNumberOrder().equals(orderData.get(j).getNumberOrder()))&&(i!=j)){
                        orderData.get(i).setNameProduct(orderData.get(i).getNameProduct()+",\n"+orderData.get(j).getNameProduct());
                        orderData.get(i).setTypeProduct(orderData.get(i).getTypeProduct()+",\n"+orderData.get(j).getTypeProduct());
                        orderData.remove(j);
                    }
                }
            }
        }
    }

    private Map<String, String> getInformationClient(String login) throws SQLException {

        String query = "Select fio,`email`,phone from client where login = '" + login + "'";
        Map<String, String> returnState = new HashMap<>();
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            returnState.put("FIO", resultSet.getString(1));
            returnState.put("e-mail", resultSet.getString(2));
            returnState.put("phone", resultSet.getString(3));
        }
        DBConnector.closeConnection();
        return returnState;
    }



    private void addDataToOrderClient() throws SQLException {
        ArrayList<String> data = getDataOrderClient();
        if(!data.isEmpty()){
            for (int i = 0; i < data.size(); i+=3) {
                orderData.add(new OrderData(data.get(i+2),data.get(i),data.get(i+1)));
            }
        }
    }


    private ArrayList<String> getDataOrderClient() throws SQLException {
        ArrayList<String> data = new ArrayList<>();
        String query = "select product.name,type_product.name,`orderr`.`order_num` from type_product,product,`orderr`, client, order_has_prod where client.idClient = `orderr`.`client_idClient` and `orderr`.idOrder = order_has_prod.orderr_idOrder and product.idProduct = order_has_prod.prod_idProd and client.login = '"+LoginController.login+"' and product.type_product_idtype_product = type_product.idtype_product order by `orderr`.`order_num` ASC" ;
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()){
            if(!resultSet.getString(1).isEmpty()) {
                data.add(resultSet.getString(1));
                data.add(resultSet.getString(2));
                data.add(resultSet.getString(3));
            }
        }
        DBConnector.closeConnection();
        return data;
    }


}
