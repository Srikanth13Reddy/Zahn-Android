package com.kenzahn.zahn.model;

public class CartModel
{
    String ProductID;
    String ProductCode;
    String ProductName;
    String Price;
    String OrderItemID;
    String OrderID;
    String Quantity;
    String IdentityKeyID;
    String ProductTypeId;

    public CartModel(String productID, String productCode, String productName, String price, String orderItemID, String orderID, String quantity, String identityKeyID, String productTypeId) {
        ProductID = productID;
        ProductCode = productCode;
        ProductName = productName;
        Price = price;
        OrderItemID = orderItemID;
        OrderID = orderID;
        Quantity = quantity;
        IdentityKeyID = identityKeyID;
        ProductTypeId = productTypeId;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
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

    public String getOrderItemID() {
        return OrderItemID;
    }

    public void setOrderItemID(String orderItemID) {
        OrderItemID = orderItemID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getIdentityKeyID() {
        return IdentityKeyID;
    }

    public void setIdentityKeyID(String identityKeyID) {
        IdentityKeyID = identityKeyID;
    }

    public String getProductTypeId() {
        return ProductTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        ProductTypeId = productTypeId;
    }
}
