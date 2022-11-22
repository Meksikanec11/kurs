package com.example.kurs;

public class CartData {

    private final String nameProduct;
    private final String typeProduct;
    private int countProduct;
    private double priceProduct;


    public CartData(String nameProduct, String typeProduct, Integer countProduct, Integer priceProduct){
        this.nameProduct = nameProduct;
        this.typeProduct = typeProduct;
        this.countProduct = countProduct;
        this.priceProduct = priceProduct*countProduct;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }
    public void updatePrice(){
        this.priceProduct = priceProduct*countProduct;
    }
}
