package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomerDashboardView extends StackPane {  // Changed to StackPane for perfect centering

    private OptimartApp app;
    private UserServices userService;

    public CustomerDashboardView(OptimartApp app, UserServices userService) {
        this.app = app;
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        // Full-screen background
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        // Main centered container (card-like dashboard)
        VBox dashboardCard = new VBox(30);
        dashboardCard.setAlignment(Pos.TOP_CENTER);
        dashboardCard.setMaxWidth(600);
        dashboardCard.setPadding(new Insets(50));
        dashboardCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 30, 0.2, 0, 10);"
        );

        // Header
        Label title = new Label("🛍️ Customer Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setTextFill(Theme.AMAZON_NAVY);

        Label welcomeLabel = new Label("Welcome, " + userService.getCurrentUser().getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
        welcomeLabel.setTextFill(Theme.TEXT_GRAY);

        // Menu buttons container
        VBox menuBox = new VBox(16);
        menuBox.setAlignment(Pos.CENTER);

        // Add all interactive buttons
        menuBox.getChildren().addAll(
                createInteractiveButton("🏪 Browse Products", () -> app.showHomeView(false)),
                createInteractiveButton("📋 View Products (Sorted)", () -> app.showProductSort(false)),
                createInteractiveButton("🔍 View Product Details", () -> app.showProductDetail()),
                createInteractiveButton("🔥 View Deals of the Day", () -> app.showDealsView(false)),
                createInteractiveButton("🛒 My Cart & Checkout", () -> app.showCartView()),
                createInteractiveButton("📦 My Orders", () -> app.showOrderHistory()),
                createInteractiveButton("📍 Track Order", () -> app.showOrderTracking()),
                createInteractiveButton("🔑 Change Password", () -> app.showPasswordChange()),
                createInteractiveButton("🚪 Logout", () -> {
                    userService.logout();
                    app.showMainMenuView();
                })
        );

        // Scrollable area for future expansion
        ScrollPane scrollPane = new ScrollPane(menuBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);  // Allows growth

        // Assemble dashboard card
        dashboardCard.getChildren().addAll(title, welcomeLabel, scrollPane);

        // Add spacer to push content up slightly (better visual balance)
        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        // Final layout: vertical centering with spacer
        VBox outerBox = new VBox(topSpacer, dashboardCard);
        outerBox.setAlignment(Pos.CENTER);

        this.getChildren().add(outerBox);
        StackPane.setAlignment(outerBox, Pos.CENTER);
        this.setPadding(new Insets(20)); // Safe edge on small screens
    }

    private Button createInteractiveButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
        btn.setTextFill(Theme.TEXT_BLACK);
        btn.setMaxWidth(Double.MAX_VALUE);  // Important: allows stretching
        btn.setPrefHeight(60);
        btn.setAlignment(Pos.CENTER_LEFT);

        // Base style - clean card look
        String baseStyle = "-fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1.5; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-padding: 15 30; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 3);";

        // Hover: Amazon yellow/orange accent
        String hoverStyle = "-fx-background-color: #fff8e1; " +
                "-fx-border-color: #FF9900; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-padding: 15 30; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(255,153,0,0.3), 12, 0.3, 0, 5);";

        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        // Slight press effect
        btn.setOnMousePressed(e -> btn.setStyle(hoverStyle.replace("#fff8e1", "#ffe082")));
        btn.setOnMouseReleased(e -> btn.setStyle(hoverStyle));

        btn.setOnAction(e -> action.run());

        return btn;
    }
}