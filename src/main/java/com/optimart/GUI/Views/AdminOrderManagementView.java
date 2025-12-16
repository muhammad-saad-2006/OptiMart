package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.CartItem;
import com.optimart.Models.Order;
import com.optimart.Utils.FileHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class AdminOrderManagementView extends VBox {

    private OptimartApp app;
    private TableView<Order> orderTable;

    public AdminOrderManagementView(OptimartApp app) {
        this.app = app;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("📦 Order Management - All Orders");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Order table
        orderTable = new TableView<>();
        orderTable.setPrefHeight(450);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Order, String> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderId()));

        TableColumn<Order, String> usernameCol = new TableColumn<>("Customer");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

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

        // Actions column
        TableColumn<Order, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View Details");
            private final Button updateBtn = new Button("Update Status");

            {
                viewBtn.setStyle("-fx-background-color: #FF9900; -fx-text-fill: white; -fx-font-size: 11px;");
                updateBtn.setStyle("-fx-background-color: #232F3E; -fx-text-fill: white; -fx-font-size: 11px;");

                viewBtn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    showOrderDetails(order);
                });

                updateBtn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    updateOrderStatus(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5, viewBtn, updateBtn);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });

        orderTable.getColumns().addAll(orderIdCol, usernameCol, dateCol, itemsCol, totalCol, statusCol, actionsCol);

        Button refreshBtn = new Button("🔄 Refresh Orders");
        refreshBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        refreshBtn.setOnAction(e -> loadOrders());

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        backBtn.setOnAction(e -> app.showAdminDashboard());

        loadOrders();

        this.getChildren().addAll(title, orderTable, refreshBtn, backBtn);
    }

    private void loadOrders() {
        try {
            List<Order> orders = FileHandler.loadOrders();
            orderTable.getItems().setAll(orders);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load orders: " + e.getMessage());
        }
    }

    private void showOrderDetails(Order order) {
        Stage dialog = new Stage();
        dialog.setTitle("Order Details - " + order.getOrderId());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        orderIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label customerLabel = new Label("Customer: " + order.getUsername());
        Label dateLabel = new Label("Date: " + order.getTimestamp());
        Label statusLabel = new Label("Status: " + order.getStatus());
        Label totalLabel = new Label(String.format("Total: Rs %.2f", order.getTotalAmount()));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label itemsTitle = new Label("\nOrder Items:");
        itemsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea itemsArea = new TextArea();
        itemsArea.setPrefRowCount(10);
        itemsArea.setEditable(false);

        StringBuilder itemsText = new StringBuilder();
        for (CartItem item : order.getItems()) {
            double unitPrice = item.getEffectivePrice();
            double itemTotal = item.getTotalPrice();

            if (unitPrice == 0.0 && item.getProduct() != null) {
                unitPrice = item.getProduct().getPrice();
                itemTotal = unitPrice * item.getQuantity();
            }

            itemsText.append(String.format("%s x %d of Rs %.2f each = Rs %.2f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    unitPrice,
                    itemTotal));
        }
        itemsArea.setText(itemsText.toString());

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(orderIdLabel, customerLabel, dateLabel, statusLabel,
                totalLabel, itemsTitle, itemsArea, closeBtn);

        Scene scene = new Scene(content, 500, 450);
        dialog.setScene(scene);
        dialog.show();
    }

    private void updateOrderStatus(Order order) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Current Status: " + order.getStatus());

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Pending", "Confirmed", "Packed", "Out for Delivery", "Delivered", "Cancelled");
        statusCombo.setValue(order.getStatus());

        VBox content = new VBox(10, new Label("Select new status:"), statusCombo);
        content.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String newStatus = statusCombo.getValue();

                try {
                    // Load all orders
                    List<Order> orders = FileHandler.loadOrders();

                    // Find and update the specific order
                    boolean found = false;
                    for (Order o : orders) {
                        if (o.getOrderId().equals(order.getOrderId())) {
                            o.setStatus(newStatus);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        // Save updated orders back to file
                        FileHandler.saveOrders(orders);

                        // Update local order object
                        order.setStatus(newStatus);

                        // Refresh the table
                        loadOrders();

                        showAlert(Alert.AlertType.INFORMATION, "Success",
                                "Order status updated to: " + newStatus);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Order not found in system");
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to update order: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
