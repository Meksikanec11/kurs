package com.example.kurs;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;

public class ProductCard{



    private final String nameProduct;
    private final int id;
    private final String priceProduct;
    private final String typeProduct;

    private final String idCard;

    public ProductCard(String nameProduct,String priceProduct,String typeProduct,int id,String  idCard){
        this.nameProduct = nameProduct;
        this.typeProduct = typeProduct;
        this.priceProduct = priceProduct;
        this.id = id;
        this.idCard = idCard;
    }


    public GridPane createCardProduct(double x, double y){
        GridPane gridPane = new GridPane();
        gridPane.setId(String.valueOf(idCard));
        Label name = new Label(nameProduct);
        name.setAlignment(Pos.CENTER);
        Label price = new Label(priceProduct);
        price.setAlignment(Pos.CENTER);
        Label type = new Label(typeProduct);
        type.setAlignment(Pos.CENTER);
        gridPane.add(name,0,0);
        gridPane.add(price,0,1);
        gridPane.add(type,0,2);

        gridPane.setBorder(Border.stroke(Color.color(0,0,0)));
        gridPane.setPrefSize(140,55);
        gridPane.setLayoutX(x);
        gridPane.setLayoutY(y);

        gridPane.setOnMouseClicked(mouseEvent -> {
            try {
                toProductTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return gridPane;
    }


    public void toProductTable() throws IOException, SQLException {
        ProductController.id = id;
        Parent root = FXMLLoader.load(ProductCard.class.getResource("productTable-view.fxml"));
        HelloApplication.stage.setScene(new Scene(root));
        HelloApplication.stage.show();
    }

    public int getId() {
        return id;
    }

    public String getIdCard() {
        return idCard;
    }
}