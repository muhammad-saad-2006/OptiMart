package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Product;
import com.optimart.Services.DealService;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class AdminDashboardView extends BorderPane {
    private OptimartApp app;
    private UserServices userService;
    private ProductServices productService;
    private DealService dealService;

    public AdminDashboardView(OptimartApp app, UserServices userService, ProductServices productService,
                              DealService dealService) {
        this.app = app;
        this.userService = userService;
        this.productService = productService;
        this.dealService = dealService;
        initialize();
    }

    private void initialize() {
        // Header
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + Theme.toHex(Theme.AMAZON_NAVY) + ";");

        Label title = new Label("🛡️ ADMIN DASHBOARD");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Theme.AMAZON_YELLOW);

        Label welcome = new Label("Welcome, " + userService.getCurrentUser().getUsername());
        welcome.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        welcome.setTextFill(javafx.scene.paint.Color.WHITE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        logoutBtn.setOnAction(e -> {
            userService.logout();
            app.showMainMenuView();
        });

        header.getChildren().addAll(title, welcome, logoutBtn);
        this.setTop(header);

        // Main content - Grid of admin functions
        FlowPane menuGrid = new FlowPane();
        menuGrid.setHgap(25);
        menuGrid.setVgap(25);
        menuGrid.setPadding(new Insets(30));
        menuGrid.setAlignment(Pos.CENTER);
        menuGrid.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        // Product Management
        VBox productMgmt = createAdminSection("📦 Product Management",
                createAdminButton("View All Products", () -> showProductList()),
                createAdminButton("Add New Product", () -> showAddProductDialog()),
                createAdminButton("Edit Product", () -> showEditProductDialog()),
                createAdminButton("Delete Product", () -> showDeleteProductDialog()));

        // Deal Management
        VBox dealMgmt = createAdminSection("🎉 Deal Management",
                createAdminButton("Manage Deals", () -> showDealsManagement()));

        // User Management
        VBox userMgmt = createAdminSection("👥 User Management",
                createAdminButton("View All Users", () -> showUserList()),
                createAdminButton("Promote to Admin", () -> showPromoteDialog()),
                createAdminButton("Demote to Customer", () -> showDemoteDialog()));

        // Order Management
        VBox orderMgmt = createAdminSection("📋 Order Management",
                createAdminButton("View All Orders", () -> app.showAdminOrderManagement()));

        menuGrid.getChildren().addAll(productMgmt, dealMgmt, userMgmt, orderMgmt);

        ScrollPane scroll = new ScrollPane(menuGrid);
        scroll.setFitToWidth(true);
        this.setCenter(scroll);
    }

    private VBox createAdminSection(String sectionTitle, Button... buttons) {
        VBox section = new VBox(12);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(20));
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        section.setPrefWidth(320);

        Label title = new Label(sectionTitle);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Theme.AMAZON_NAVY);

        section.getChildren().add(title);
        section.getChildren().addAll(buttons);
        return section;
    }

    private Button createAdminButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 13px; -fx-padding: 10 15;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());

        btn.setOnMouseEntered(e -> btn.setStyle(Theme.YELLOW_BUTTON_HOVER_STYLE +
                "-fx-font-size: 13px; -fx-padding: 10 15;"));
        btn.setOnMouseExited(e -> btn.setStyle(Theme.YELLOW_BUTTON_STYLE +
                "-fx-font-size: 13px; -fx-padding: 10 15;"));

        return btn;
    }

    // FIXED: Sort by Product ID
    private void showProductList() {
        Stage dialog = new Stage();
        dialog.setTitle("All Products");

        TableView<Product> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductId()));
        idCol.setPrefWidth(80);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setPrefWidth(250);

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        categoryCol.setPrefWidth(150);

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getPrice())));
        priceCol.setPrefWidth(100);

        TableColumn<Product, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStock())));
        stockCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, nameCol, categoryCol, priceCol, stockCol);

        // FIXED: Sort by Product ID
        List<Product> products = productService.getAllProducts();
        products.sort(Comparator.comparing(Product::getProductId));
        table.getItems().setAll(products);

        VBox layout = new VBox(10, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 750, 600);
        dialog.setScene(scene);
        dialog.show();
    }

    // FIXED: Unified deals management
    private void showDealsManagement() {
        AdminDealsManagement dealsView = new AdminDealsManagement(app, dealService, productService);

        // Wrap it with a back button at the top
        VBox wrapper = new VBox(20);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(40));
        wrapper.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        wrapper.getChildren().addAll(dealsView);

        // Switch main scene root
        app.getPrimaryStage().getScene().setRoot(wrapper);
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField categoryField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);

        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Stock:"), 0, 4);
        grid.add(stockField, 1, 4);
        grid.add(new Label("Description:"), 0, 5);
        grid.add(descField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    boolean success = productService.addProduct(
                            idField.getText(),
                            nameField.getText(),
                            categoryField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(stockField.getText()),
                            descField.getText());
                    if (success) {
                        showMessage("Success", "Product added successfully!");
                    } else {
                        showMessage("Error", "Failed to add product");
                    }
                } catch (Exception e) {
                    showMessage("Error", "Invalid input: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditProductDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Edit Product");
        idDialog.setHeaderText("Enter Product ID to edit:");
        String productId = idDialog.showAndWait().orElse("");

        if (!productId.isEmpty()) {
            Product product = productService.findProductById(productId);
            if (product != null) {
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Edit Product: " + productId);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20));

                TextField nameField = new TextField(product.getName());
                TextField categoryField = new TextField(product.getCategory());
                TextField priceField = new TextField(String.valueOf(product.getPrice()));
                TextField stockField = new TextField(String.valueOf(product.getStock()));
                TextArea descField = new TextArea(product.getDescription());

                grid.add(new Label("Name:"), 0, 0);
                grid.add(nameField, 1, 0);
                grid.add(new Label("Category:"), 0, 1);
                grid.add(categoryField, 1, 1);
                grid.add(new Label("Price:"), 0, 2);
                grid.add(priceField, 1, 2);
                grid.add(new Label("Stock:"), 0, 3);
                grid.add(stockField, 1, 3);
                grid.add(new Label("Description:"), 0, 4);
                grid.add(descField, 1, 4);

                dialog.getDialogPane().setContent(grid);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.setResultConverter(button -> {
                    if (button == ButtonType.OK) {
                        try {
                            boolean success = productService.updateProduct(
                                    productId,
                                    nameField.getText(),
                                    categoryField.getText(),
                                    Double.parseDouble(priceField.getText()),
                                    Integer.parseInt(stockField.getText()),
                                    descField.getText());
                            if (success) {
                                showMessage("Success", "Product updated successfully!");
                            }
                        } catch (Exception e) {
                            showMessage("Error", e.getMessage());
                        }
                    }
                    return null;
                });

                dialog.showAndWait();
            } else {
                showMessage("Error", "Product not found: " + productId);
            }
        }
    }

    private void showDeleteProductDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Product");
        dialog.setHeaderText("Enter Product ID to delete:");

        String productId = dialog.showAndWait().orElse("");
        if (!productId.isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete product: " + productId + "?");
            confirm.setContentText("This action cannot be undone.");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                boolean success = productService.deleteProduct(productId);
                if (success) {
                    showMessage("Success", "Product deleted successfully!");
                } else {
                    showMessage("Error", "Failed to delete product");
                }
            }
        }
    }

    private void showUserList() {
        Stage dialog = new Stage();
        dialog.setTitle("All Users");

        TableView<com.optimart.Models.User> table = new TableView<>();

        TableColumn<com.optimart.Models.User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<com.optimart.Models.User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<com.optimart.Models.User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        table.getColumns().addAll(usernameCol, emailCol, roleCol);
        table.getItems().setAll(userService.getAllUsers());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox layout = new VBox(10, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 450);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showPromoteDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Promote User");
        dialog.setHeaderText("Enter username to promote to Admin:");

        String username = dialog.showAndWait().orElse("");
        if (!username.isEmpty()) {
            boolean success = userService.promoteToAdmin(username);
            if (success) {
                showMessage("Success", username + " promoted to Admin!");
            } else {
                showMessage("Error", "Failed to promote user");
            }
        }
    }

    private void showDemoteDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Demote User");
        dialog.setHeaderText("Enter username to demote to Customer:");

        String username = dialog.showAndWait().orElse("");
        if (!username.isEmpty()) {
            boolean success = userService.demoteToCustomer(username);
            if (success) {
                showMessage("Success", username + " demoted to Customer!");
            } else {
                showMessage("Error", "Failed to demote user");
            }
        }
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}