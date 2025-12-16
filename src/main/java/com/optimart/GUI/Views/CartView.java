package com.optimart.GUI.Views;

import com.optimart.DataStructures.HashMap;
import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Cart;
import com.optimart.Models.CartItem;
import com.optimart.Models.Order;
import com.optimart.Models.Product;
import com.optimart.Utils.FileHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class CartView extends VBox {
    private OptimartApp app;
    private Cart cart;
    private HashMap<String, Product> productMap;
    private TableView<CartItem> table;
    private Label totalLabel;

    public CartView(OptimartApp app, Cart cart, HashMap<String, Product> productMap) {
        this.app = app;
        this.cart = cart;
        this.productMap = productMap;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        // Header with Undo/Redo
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Shopping Cart");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        Button undoBtn = new Button("↶ Undo");
        undoBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        undoBtn.setOnAction(e -> {
            cart.undo(productMap);
            refresh();
        });

        Button redoBtn = new Button("↷ Redo");
        redoBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        redoBtn.setOnAction(e -> {
            cart.redo(productMap);
            refresh();
        });

        header.getChildren().addAll(title, undoBtn, redoBtn);

        // Table
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        TableColumn<CartItem, String> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.format("Rs %.2f", cellData.getValue().getProduct().getPrice())));

        TableColumn<CartItem, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));

        TableColumn<CartItem, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getTotalPrice())));

        // Actions column with buttons
        TableColumn<CartItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button removeBtn = new Button("Remove");
            private final Spinner<Integer> qtySpinner = new Spinner<>(1, 100, 1);
            private final HBox hbox = new HBox(5, qtySpinner, updateBtn, removeBtn);

            {
                qtySpinner.setPrefWidth(70);
                updateBtn.setStyle("-fx-background-color: #FF9900; -fx-text-fill: white; -fx-font-size: 11px;");
                removeBtn.setStyle("-fx-background-color: #B12704; -fx-text-fill: white; -fx-font-size: 11px;");
                hbox.setAlignment(Pos.CENTER);

                updateBtn.setOnAction(e -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    int newQty = qtySpinner.getValue();
                    cart.updateQuantity(item.getProduct().getProductId(), newQty);
                    refresh();
                });

                removeBtn.setOnAction(e -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    cart.removeItem(item.getProduct().getProductId());
                    refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    qtySpinner.getValueFactory().setValue(cartItem.getQuantity());
                    setGraphic(hbox);
                }
            }
        });

        table.getColumns().addAll(nameCol, priceCol, qtyCol, totalCol, actionsCol);

        // Total and Actions Bar
        HBox bottomBar = new HBox(20);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));

        totalLabel = new Label("Total: Rs 0.00");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        totalLabel.setTextFill(Theme.PRICE_RED);

        Button clearBtn = new Button("Clear Cart");
        clearBtn.setStyle(
                "-fx-background-color: #B12704; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14px;");
        clearBtn.setOnAction(e -> {
            cart.clearCart();
            refresh();
        });

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-padding: 10 30; -fx-font-size: 16px;");
        placeOrderBtn.setOnAction(e -> placeOrder());


        Button backBtn = new Button("← Continue Shopping");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard());

        bottomBar.getChildren().addAll(clearBtn, totalLabel, placeOrderBtn);

        refresh();

        this.getChildren().addAll(header, table, bottomBar, backBtn);
    }

    private void placeOrder() {
        if (cart.getItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Cart", "Please add items to cart before placing order.");
            return;
        }

        if (app.getUserService().getCurrentUser() == null) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please login to place an order.");
            return;
        }

        try {
            // Create order from cart
            Order order = new Order(
                    app.getUserService().getCurrentUser().getUsername(),
                    cart.getItemsAsList() // Pass List<CartItem> not Cart
            ); // Load existing orders
            List<Order> orders = FileHandler.loadOrders();

            // Add new order
            orders.add(order);

            // Save orders
            FileHandler.saveOrders(orders);

            double total = cart.getTotalPrice();

            // Clear cart after successful order
            cart.clearCart();
            refresh();

            showAlert(Alert.AlertType.INFORMATION,
                    "Order Placed Successfully!",
                    String.format(
                            "Order ID: %s\\nTotal: Rs %.2f\\n\\nThank you for shopping with OptiMart!\\nYou can track your order in 'My Orders'.",
                            order.getOrderId(), total));

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Order Failed", "Failed to place order: " + e.getMessage());
        }
    }

    private void refresh() {
        if (cart != null && cart.getItems() != null) {
            table.getItems().setAll(cart.getItemsAsList());
            totalLabel.setText(String.format("Total: Rs %.2f", cart.getTotalPrice()));
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
