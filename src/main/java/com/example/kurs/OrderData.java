package com.example.kurs;

public class OrderData {

    private final String numberOrder;
    private String nameProduct;
    private String typeProduct;


    public OrderData(String numberOrder, String nameProduct, String typeProduct){
        this.nameProduct = nameProduct;
        this.numberOrder = numberOrder;
        this.typeProduct = typeProduct;
    }


    public String getNumberOrder() {
        return numberOrder;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }
}
