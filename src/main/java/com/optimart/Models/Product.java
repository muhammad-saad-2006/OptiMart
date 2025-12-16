package com.optimart.Models;

public class Product implements Comparable<Product> {
    private String productId;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;

    public Product() {}

    public Product(String productId, String name, String category, double price, int stock, String description) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    public void increaseStock(int quantity) {
        stock += quantity;
    }

    @Override
    public int compareTo(Product other) {
        return this.productId.compareTo(other.productId);
    }

    public String toDisplayString() {
        return String.format("%-10s | %-25s | %-15s | Rs %-10.2f |  %-5d",
                productId, name, category, price, stock);
    }

    @Override
    public String toString() {
        return String.format("Product [ID: %s, Name: %s, Category: %s, Price: Rs.%.2f, Stock: %d]",
                productId, name, category, price, stock);
    }

    public String toDetailedString() {
        return String.format("""
                ========================
                Product ID: %s
                Name: %s
                Category: %s
                Price: Rs.%.2f
                Stock: %d units
                Description: %s
                ========================""",
                productId, name, category, price, stock, description);
    }
}