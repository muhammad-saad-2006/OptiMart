package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Order;
import com.optimart.Utils.FileHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class OrderTrackingView extends VBox {

    private OptimartApp app;
    private Label statusLabel;
    private VBox statusDisplay;

    public OrderTrackingView(OptimartApp app) {
        this.app = app;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("Track Your Order");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        Label infoLabel = new Label("Enter your order ID to track status:");
        infoLabel.setFont(Theme.FONT_REGULAR);

        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Order ID (e.g., ORD001)");
        orderIdField.setMaxWidth(400);
        orderIdField.setFont(Theme.FONT_REGULAR);

        Button trackBtn = new Button("Track Order");
        trackBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        trackBtn.setMaxWidth(400);

        statusDisplay = new VBox(10);
        statusDisplay.setAlignment(Pos.CENTER);
        statusDisplay.setMaxWidth(600);
        statusDisplay.setVisible(false);

        trackBtn.setOnAction(e -> {
            String orderId = orderIdField.getText().trim();
            if (orderId.isEmpty()) {
                showError("Please enter an order ID");
            } else {
                trackOrder(orderId);
            }
        });

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard());

        this.getChildren().addAll(title, infoLabel, orderIdField, trackBtn, statusDisplay, backBtn);
    }

    private void trackOrder(String orderId) {
        try {
            List<Order> orders = FileHandler.loadOrders();

            Order foundOrder = null;
            for (Order order : orders) {
                if (order.getOrderId().equalsIgnoreCase(orderId)) {
                    foundOrder = order;
                    break;
                }
            }

            if (foundOrder == null) {
                showError("Order not found: " + orderId);
                return;
            }

            // Check if order belongs to current user
            if (app.getUserService().getCurrentUser() != null) {
                String currentUser = app.getUserService().getCurrentUser().getUsername();
                if (!foundOrder.getUsername().equals(currentUser)) {
                    showError("This order does not belong to you");
                    return;
                }
            }

            displayOrderStatus(foundOrder);

        } catch (Exception e) {
            showError("Error tracking order: " + e.getMessage());
        }
    }

    private void displayOrderStatus(Order order) {
        statusDisplay.getChildren().clear();

        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        orderIdLabel.setTextFill(Theme.AMAZON_NAVY);

        Label dateLabel = new Label("Placed on: " + order.getTimestamp());
        dateLabel.setFont(Theme.FONT_REGULAR);

        Label itemsLabel = new Label("Items: " + order.getItems().size());
        itemsLabel.setFont(Theme.FONT_REGULAR);

        Label totalLabel = new Label(String.format("Total: Rs %.2f", order.getTotalAmount()));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalLabel.setTextFill(Theme.PRICE_RED);

        Separator sep = new Separator();

        Label statusTitleLabel = new Label("Order Status:");
        statusTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label statusLabel = new Label("📦 " + order.getStatus());
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        statusLabel.setTextFill(getStatusColor(order.getStatus()));

        Label statusDesc = new Label(getStatusDescription(order.getStatus()));
        statusDesc.setFont(Theme.FONT_REGULAR);
        statusDesc.setTextFill(Theme.TEXT_GRAY);
        statusDesc.setWrapText(true);
        statusDesc.setMaxWidth(500);

        statusDisplay.getChildren().addAll(
                orderIdLabel, dateLabel, itemsLabel, totalLabel, sep,
                statusTitleLabel, statusLabel, statusDesc);
        statusDisplay.setVisible(true);
    }

    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "delivered":
                return Color.GREEN;
            case "shipped":
                return Color.BLUE;
            case "processing":
                return Color.ORANGE;
            case "pending":
                return Color.GRAY;
            default:
                return Theme.AMAZON_NAVY;
        }
    }

    private String getStatusDescription(String status) {
        switch (status.toLowerCase()) {
            case "delivered":
                return "Your order has been delivered successfully!";
            case "shipped":
                return "Your order is on the way to your location.";
            case "processing":
                return "Your order is being prepared for shipment.";
            case "pending":
                return "Your order has been received and is pending confirmation.";
            default:
                return "Status information not available.";
        }
    }

    private void showError(String message) {
        statusDisplay.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setFont(Theme.FONT_REGULAR);
        errorLabel.setTextFill(Color.RED);
        statusDisplay.getChildren().add(errorLabel);
        statusDisplay.setVisible(true);
    }
}
