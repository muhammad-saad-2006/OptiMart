package com.optimart.Services;

import com.optimart.Models.Order;

import java.util.List;

public class OrderStatusService {
    public void trackOrder(String orderId, List<Order> allOrders) {
        Order order = findOrderById(orderId, allOrders);

        if (order == null) {
            System.out.println("✗ Order not found");
            return;
        }

        displayOrderStatus(order);
    }

    public void displayOrderStatus(Order order) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ORDER STATUS TRACKING                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("Order ID       : " + order.getOrderId());
        System.out.println("Customer       : " + order.getUsername());
        System.out.println("Order Date     : " + order.getTimestamp());
        System.out.println("Current Status : " + order.getStatus());
        System.out.println();

        displayStatusProgress(order.getStatus());

        System.out.println("\nTotal Amount   : Rs " + String.format("%.2f", order.getTotalAmount()));
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    private void displayStatusProgress(String currentStatus) {
        String[] stages = {"Pending", "Confirmed", "Packed", "Out for Delivery", "Delivered"};

        System.out.println("Progress:");
        for (String stage : stages) {
            if (stage.equals(currentStatus)) {
                System.out.println("  ► " + stage + " ✓ [CURRENT]");
            } else if (isPastStage(currentStatus, stage)) {
                System.out.println("  ✓ " + stage + " [COMPLETED]");
            } else {
                System.out.println("  ○ " + stage + " [PENDING]");
            }
        }
    }

    private boolean isPastStage(String currentStatus, String checkStage) {
        String[] stages = {"Pending", "Confirmed", "Packed", "Out for Delivery", "Delivered"};

        int currentIndex = -1;
        int checkIndex = -1;

        for (int i = 0; i < stages.length; i++) {
            if (stages[i].equals(currentStatus)) currentIndex = i;
            if (stages[i].equals(checkStage)) checkIndex = i;
        }

        return currentIndex > checkIndex;
    }

    public boolean updateOrderStatus(String orderId, String newStatus, List<Order> allOrders) {
        Order order = findOrderById(orderId, allOrders);

        if (order == null) {
            System.out.println("✗ Order not found");
            return false;
        }

        if (!order.canTransitionTo(newStatus)) {
            System.out.println("✗ Cannot change status from " + order.getStatus() + " to " + newStatus);
            return false;
        }

        order.updateStatus(newStatus);
        System.out.println("✓ Order " + orderId + " status updated to: " + newStatus);
        return true;
    }

    public void advanceOrderStatus(String orderId, List<Order> allOrders) {
        Order order = findOrderById(orderId, allOrders);

        if (order == null) {
            System.out.println("✗ Order not found");
            return;
        }

        String nextStatus = order.getNextStatus();

        if (nextStatus.equals(order.getStatus())) {
            System.out.println("✗ Order is already at final status: " + order.getStatus());
            return;
        }

        order.updateStatus(nextStatus);
        System.out.println("✓ Order " + orderId + " advanced to: " + nextStatus);
    }

    private Order findOrderById(String orderId, List<Order> orders) {
        for (Order order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                return order;
            }
        }
        return null;
    }
}