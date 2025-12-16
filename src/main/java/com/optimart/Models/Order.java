package com.optimart.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int orderCounter = 1;
    private String orderId;
    private String username;
    private List<CartItem> items;
    private double totalAmount;
    private String status;
    private String timestamp;


    public Order(String username, List<CartItem> items) {
        this.orderId = generateOrderId();
        this.username = username;
        this.items = new ArrayList<>(items);
        this.status = "Pending";
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.totalAmount = calculateTotal();
    }

    public Order() {}

    private static synchronized String generateOrderId() {
        return String.format("ORD%03d", orderCounter++);
    }

    public static void updateOrderCounter(String orderId) {
        if (orderId != null && orderId.startsWith("ORD")) {
            try {
                int id = Integer.parseInt(orderId.substring(3));
                if (id >= orderCounter) {
                    orderCounter = id + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing order ID: " + orderId);
            }
        }
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public boolean canTransitionTo(String newStatus) {
        String[] validStatuses = {"Pending", "Confirmed", "Packed", "Out for Delivery", "Delivered", "Cancelled"};

        boolean isValid = false;
        for (String s : validStatuses) {
            if (s.equals(newStatus)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) return false;

        if (status.equals("Delivered") || status.equals("Cancelled")) {
            return false;
        }

        return true;
    }

    public String getNextStatus() {
        switch (status) {
            case "Pending": return "Confirmed";
            case "Confirmed": return "Packed";
            case "Packed": return "Out for Delivery";
            case "Out for Delivery": return "Delivered";
            default: return status;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                        ORDER DETAILS                               ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════════╝\n");
        sb.append("Order ID       : ").append(orderId).append("\n");
        sb.append("Order Date     : ").append(timestamp).append("\n");
        sb.append("Status         : ").append(status).append("\n");
        sb.append("\nITEMS:\n");
        sb.append("─────────────────────────────────────────────────────────────────────\n");

        for (int i = 0; i < items.size(); i++) {
            sb.append("  ").append(items.get(i).toString()).append("\n");
        }

        sb.append("─────────────────────────────────────────────────────────────────────\n");
        sb.append(String.format("TOTAL AMOUNT   : Rs %.2f\n", totalAmount));
        sb.append("═════════════════════════════════════════════════════════════════════\n");

        return sb.toString();
    }
}
