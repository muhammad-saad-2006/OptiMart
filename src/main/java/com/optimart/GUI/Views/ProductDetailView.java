package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Product;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProductDetailView extends VBox {
    private OptimartApp app;
    private ProductServices productServices;
    private UserServices userServices;
    private VBox detailsContainer;

    public ProductDetailView(OptimartApp app, ProductServices productServices, UserServices userServices) {
        this.app = app;
        this.productServices = productServices;
        this.userServices = userServices;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("View Product Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Search section
        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setMaxWidth(600);
        searchBox.setPadding(new Insets(20));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label instructionLabel = new Label("Enter Product ID to view details:");
        instructionLabel.setFont(Theme.FONT_REGULAR);

        TextField productIdField = new TextField();
        productIdField.setPromptText("e.g., P001");
        productIdField.setFont(Theme.FONT_REGULAR);
        productIdField.setMaxWidth(400);

        Button searchButton = new Button("View Details");
        searchButton.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 14px;");
        searchButton.setMaxWidth(400);

        searchBox.getChildren().addAll(instructionLabel, productIdField, searchButton);

        // Details container
        detailsContainer = new VBox(15);
        detailsContainer.setAlignment(Pos.CENTER_LEFT);
        detailsContainer.setMaxWidth(800);
        detailsContainer.setPadding(new Insets(20));
        detailsContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        detailsContainer.setVisible(false);

        searchButton.setOnAction(e -> {
            String productId = productIdField.getText().trim().toUpperCase();
            if (!productId.isEmpty()) {
                showProductDetails(productId);
            } else {
                showError("Please enter a Product ID");
            }
        });

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard());

        ScrollPane scrollPane = new ScrollPane(detailsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().addAll(title, searchBox, scrollPane, backBtn);
    }

    private void showProductDetails(String productId) {
        Product product = productServices.findProductById(productId);

        detailsContainer.getChildren().clear();

        if (product == null) {
            showError("Product not found with ID: " + productId);
            return;
        }

        detailsContainer.setVisible(true);

        Label productTitle = new Label(product.getName());
        productTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        productTitle.setTextFill(Theme.AMAZON_NAVY);
        productTitle.setWrapText(true);

        Separator sep1 = new Separator();

        Label idLabel = createDetailLabel("Product ID: " + product.getProductId());
        Label categoryLabel = createDetailLabel("Category: " + product.getCategory());

        Label priceLabel = new Label(String.format("Price: Rs %.2f", product.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        priceLabel.setTextFill(Theme.PRICE_RED);

        Label stockLabel = createDetailLabel("Stock Available: " + product.getStock() + " units");
        if (product.getStock() > 0) {
            stockLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        } else {
            stockLabel.setTextFill(javafx.scene.paint.Color.RED);
        }

        Separator sep2 = new Separator();

        Label descTitle = new Label("Description:");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextArea descArea = new TextArea(product.getDescription());
        descArea.setWrapText(true);
        descArea.setEditable(false);
        descArea.setPrefRowCount(5);
        descArea.setStyle("-fx-font-size: 14px;");

        Separator sep3 = new Separator();

        // Add to cart button
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Spinner<Integer> qtySpinner = new Spinner<>(1, product.getStock(), 1);
        qtySpinner.setPrefWidth(80);
        qtySpinner.setEditable(true);

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 14px; -fx-padding: 10 20;");

        addToCartBtn.setOnAction(e -> {
            int quantity = qtySpinner.getValue();
            app.getCurrentUserCart().addItem(product, quantity);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(quantity + " x " + product.getName() + " added to cart!");
            alert.showAndWait();
        });

        buttonBox.getChildren().addAll(new Label("Quantity:"), qtySpinner, addToCartBtn);

        detailsContainer.getChildren().addAll(
                productTitle, sep1,
                idLabel, categoryLabel, priceLabel, stockLabel,
                sep2, descTitle, descArea, sep3, buttonBox
        );
    }

    private Label createDetailLabel(String text) {
        Label label = new Label(text);
        label.setFont(Theme.FONT_REGULAR);
        label.setWrapText(true);
        return label;
    }

    private void showError(String message) {
        detailsContainer.getChildren().clear();
        detailsContainer.setVisible(true);

        Label errorLabel = new Label(message);
        errorLabel.setFont(Theme.FONT_REGULAR);
        errorLabel.setTextFill(javafx.scene.paint.Color.RED);

        detailsContainer.getChildren().add(errorLabel);
    }
}