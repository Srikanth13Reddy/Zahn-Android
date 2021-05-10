package com.kenzahn.zahn.model;

public class ListProductsModel
{
    String ProductId;
    String ProductName;
    String Price;
    String Description;
    String ProductTypeID;
    String ProductType;

    public ListProductsModel(String productId, String productName, String price, String description, String productTypeID, String productType) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Description = description;
        ProductTypeID = productTypeID;
        ProductType = productType;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProductTypeID() {
        return ProductTypeID;
    }

    public void setProductTypeID(String productTypeID) {
        ProductTypeID = productTypeID;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }
}
