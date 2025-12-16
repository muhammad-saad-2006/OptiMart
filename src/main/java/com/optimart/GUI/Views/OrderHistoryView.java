package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Order;
import com.optimart.Utils.FileHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.stream.Collectors;

public class OrderHistoryView extends VBox {

    private OptimartApp app;
    private TableView<Order> orderTable;

    public OrderHistoryView(OptimartApp app) {
        this.app = app;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(40));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("My Orders");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        Label infoLabel = new Label("Your order history:");
        infoLabel.setFont(Theme.FONT_REGULAR);
        infoLabel.setTextFill(Theme.TEXT_GRAY);

        // Order table
        orderTable = new TableView<>();
        orderTable.setPlaceholder(new Label("No orders yet. Start shopping!"));
        orderTable.setPrefHeight(400);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Order, String> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderId()));

        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp()));

        TableColumn<Order, String> itemsCol = new TableColumn<>("Items");
        itemsCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getItems().size())));

        TableColumn<Order, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getTotalAmount())));

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        orderTable.getColumns().addAll(orderIdCol, dateCol, itemsCol, totalCol, statusCol);

        // Load and display orders
        loadOrders();

        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        refreshBtn.setOnAction(e -> loadOrders());

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard());

        this.getChildren().addAll(title, infoLabel, orderTable, refreshBtn, backBtn);
    }

    private void loadOrders() {
        try {
            List<Order> allOrders = FileHandler.loadOrders();

            if (app.getUserService().getCurrentUser() != null) {
                String currentUsername = app.getUserService().getCurrentUser().getUsername();

                // Filter orders for current user
                List<Order> userOrders = allOrders.stream()
                        .filter(order -> order.getUsername().equals(currentUsername))
                        .collect(Collectors.toList());

                orderTable.getItems().setAll(userOrders);

                System.out.println("Loaded " + userOrders.size() + " orders for user: " + currentUsername);
            }
        } catch (Exception e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }
}
