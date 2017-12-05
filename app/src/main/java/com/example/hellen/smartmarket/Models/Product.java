package com.example.hellen.smartmarket.Models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "Product")
public class Product {

    private String productID;
    private String productDesc;
    private int productQuantity;
    private int productPrice;

    public Product(String productID, int productQuantity, int productPrice) {
        this.productID = productID;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public Product() {
    }

    @DynamoDBHashKey(attributeName = "ProductID")
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @DynamoDBIndexRangeKey(attributeName = "Description")
    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @DynamoDBIndexRangeKey(attributeName = "Quantity")
    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @DynamoDBIndexRangeKey(attributeName = "Price")
    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
