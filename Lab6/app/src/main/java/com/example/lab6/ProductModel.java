package com.example.lab6;

public class ProductModel {
    private int productId;
    private String productName;
    private int productSKU;
    public static final int UNASSIGNED_ID = -1;

    public ProductModel(int productId, String productName, int productSKU) {
        this.productId = productId;
        this.productName = productName;
        this.productSKU = productSKU;
    }

    public ProductModel(String productName, int productSKU) {
        this.productName = productName;
        this.productSKU = productSKU;
        this.productId = UNASSIGNED_ID;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(int productSKU) {
        this.productSKU = productSKU;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        // 1: iPhone (30)
        return productId + ": " + productName + " (" + productSKU + ")";
    }
}