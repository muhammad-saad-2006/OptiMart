package com.optimart.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deal implements Comparable<Deal> {
    private String productId;
    private String productName;
    private double originalPrice;
    private double discountedPrice;
    private int discountPercentage;
    private String expiryDateTime;
    private int stockLimit;

    public Deal() {}

    public Deal(String productId, String productName, double originalPrice,
                int discountPercentage, String expiryDateTime, int stockLimit) {
        this.productId = productId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.discountPercentage = discountPercentage;
        this.discountedPrice = originalPrice - (originalPrice * discountPercentage / 100.0);
        this.expiryDateTime = expiryDateTime;
        this.stockLimit = stockLimit;
    }

    @Override
    public int compareTo(Deal other) {
        return other.discountPercentage - this.discountPercentage;
    }

    public boolean isExpired() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiry;

            if (expiryDateTime.contains(" ")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                expiry = LocalDateTime.parse(expiryDateTime, formatter);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiry = java.time.LocalDate.parse(expiryDateTime, formatter).atTime(23, 59, 59);
            }

            return now.isAfter(expiry);
        } catch (DateTimeParseException e) {
            System.err.println("⚠ Invalid date format for deal: " + expiryDateTime);
            return false;
        }
    }

    public String getTimeRemaining() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiry;

            if (expiryDateTime.contains(" ")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                expiry = LocalDateTime.parse(expiryDateTime, formatter);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiry = java.time.LocalDate.parse(expiryDateTime, formatter).atTime(23, 59, 59);
            }

            if (now.isAfter(expiry)) {
                return "EXPIRED";
            }

            long seconds = java.time.Duration.between(now, expiry).getSeconds();

            if (seconds < 60) {
                return seconds + " seconds";
            } else if (seconds < 3600) {
                return (seconds / 60) + " minutes";
            } else if (seconds < 86400) {
                return (seconds / 3600) + " hours";
            } else {
                return (seconds / 86400) + " days";
            }

        } catch (Exception e) {
            return "Unknown";
        }
    }

    public double getSavings() {
        return originalPrice - discountedPrice;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(double discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }

    public String getExpiryDate() { return expiryDateTime; }
    public void setExpiryDate(String expiryDateTime) { this.expiryDateTime = expiryDateTime; }

    public int getStockLimit() { return stockLimit; }
    public void setStockLimit(int stockLimit) { this.stockLimit = stockLimit; }

    @Override
    public String toString() {
        String status = isExpired() ? "[EXPIRED]" : "[" + getTimeRemaining() + " left]";
        return String.format("%-10s | %-25s | Rs %-10.2f → Rs %-10.2f | %d%% OFF | Expires: %s %s",
                productId, productName, originalPrice, discountedPrice,
                discountPercentage, expiryDateTime, status);
    }
}