package com.optimart.Models;

public class CartItem {
    private Product product;
    private int quantity;
    private double effectivePrice;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.effectivePrice = product.getPrice();
    }

    public CartItem(Product product, int quantity, double effectivePrice) {
        this.product = product;
        this.quantity = quantity;
        this.effectivePrice = effectivePrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(double effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public double getTotalPrice() {
        return effectivePrice * quantity;
    }

    @Override
    public String toString() {
        String priceDisplay;
        if (effectivePrice < product.getPrice()) {
            priceDisplay = String.format("Rs %-10.2f (DEAL!)", effectivePrice);
        } else {
            priceDisplay = String.format("Rs %-10.2f", effectivePrice);
        }

        return String.format("%-25s | %-4s | %-3d | %s | Rs %-10.2f",
                product.getName(), product.getProductId(), quantity, priceDisplay, getTotalPrice());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return product.getProductId().equals(cartItem.product.getProductId());
    }
}