package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Deal;
import com.optimart.Models.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DealsView extends BorderPane {
    private OptimartApp app;
    private FlowPane dealsGrid;
    private boolean isGuest;

    public DealsView(OptimartApp app, boolean isGuest) {
        this.app = app;
        this.isGuest = isGuest;
        initialize();
    }

    private void initialize() {
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        // Header
        VBox header = new VBox(15);
        header.setPadding(new Insets(25));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 0, 5, 0, 0);");

        Label title = new Label("🔥 DEALS OF THE DAY 🔥");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Theme.PRICE_RED);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button(isGuest ? "← Back to Main Menu" : "← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        backBtn.setOnAction(e -> {
            if (isGuest) {
                app.showMainMenuView();
            } else {
                app.showCustomerDashboard();
            }
        });

        Button refreshBtn = new Button("🔄 Refresh Deals");
        refreshBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        refreshBtn.setOnAction(e -> {
            app.getDealService().removeExpiredDeals();
            loadDeals();
        });

        buttonBox.getChildren().addAll(backBtn, refreshBtn);
        header.getChildren().addAll(title, buttonBox);
        this.setTop(header);

        // Content
        dealsGrid = new FlowPane();
        dealsGrid.setHgap(25);
        dealsGrid.setVgap(25);
        dealsGrid.setPadding(new Insets(30));
        dealsGrid.setAlignment(Pos.CENTER);

        app.getDealService().removeExpiredDeals();
        loadDeals();

        ScrollPane scroll = new ScrollPane(dealsGrid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.setCenter(scroll);
    }

    private void loadDeals() {
        dealsGrid.getChildren().clear();

        List<Deal> deals = app.getDealService().getTopDeals(20);

        if (deals.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);

            Label noDealsLabel = new Label("No active deals at the moment. Check back later!");
            noDealsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            noDealsLabel.setTextFill(Theme.TEXT_GRAY);

            emptyBox.getChildren().add(noDealsLabel);
            dealsGrid.getChildren().add(emptyBox);
            return;
        }

        for (Deal deal : deals) {
            if (!deal.isExpired()) {
                Product product = findProductById(deal.getProductId());
                dealsGrid.getChildren().add(createDealCard(deal, product));
            }
        }
    }

    private VBox createDealCard(Deal deal, Product product) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 3); " +
                "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 12;");
        card.setPrefWidth(300);
        card.setPrefHeight(500);
        card.setAlignment(Pos.TOP_CENTER);

        // Discount Badge
        Label discountBadge = new Label(deal.getDiscountPercentage() + "% OFF");
        discountBadge.setStyle("-fx-background-color: #CC0C39; -fx-text-fill: white; " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-font-weight: bold;");
        discountBadge.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Image placeholder
        VBox imageContainer = new VBox();
        imageContainer.setPrefHeight(160);
        imageContainer.setPrefWidth(260);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle("-fx-background-color: #F7F7F7; -fx-background-radius: 8;");

        // Try to load product image
        String imagePath = "/images/" + getImageNameForProduct(product);
        try {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                    new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath)));
            imageView.setFitHeight(150);
            imageView.setFitWidth(250);
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } catch (Exception e) {
            // Fallback to product name
            Label placeholderLabel = new Label(product != null ? product.getName() : deal.getProductName());
            placeholderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            placeholderLabel.setWrapText(true);
            placeholderLabel.setMaxWidth(240);
            placeholderLabel.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(placeholderLabel);
        }

        Separator sep1 = new Separator();

        // Product Name
        Label productLabel = new Label(deal.getProductName());
        productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        productLabel.setWrapText(true);
        productLabel.setMaxHeight(60);
        productLabel.setTextFill(Theme.AMAZON_NAVY);
        productLabel.setAlignment(Pos.CENTER);

        // Product ID
        Label idLabel = new Label("ID: " + deal.getProductId());
        idLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        idLabel.setTextFill(Theme.TEXT_GRAY);

        // Discounted Price
        Label priceLabel = new Label("Rs " + String.format("%.2f", deal.getDiscountedPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        priceLabel.setTextFill(Theme.PRICE_RED);

        // Original Price
        Label originalPrice = new Label("M.R.P: Rs " + String.format("%.2f", deal.getOriginalPrice()));
        originalPrice.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        originalPrice.setTextFill(Theme.TEXT_GRAY);
        originalPrice.setStyle("-fx-strikethrough: true;");

        // Savings
        Label savingsLabel = new Label("You Save: Rs " + String.format("%.2f", deal.getSavings()));
        savingsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        savingsLabel.setTextFill(Color.web("#007600"));

        // Stock Limit
        Label stockLabel = new Label("Deal Stock: " + deal.getStockLimit() + " units");
        stockLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        stockLabel.setTextFill(Theme.TEXT_GRAY);

        // Expiry
        Label expiry = new Label("⏰ Expires: " + deal.getExpiryDate());
        expiry.setTextFill(Color.web("#B12704"));
        expiry.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Separator sep2 = new Separator();

        // Add to Cart Button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 20;");
        addToCartBtn.setMaxWidth(Double.MAX_VALUE);

        addToCartBtn.setOnAction(e -> {
            if (isGuest) {
                showGuestAlert();
            } else if (product != null) {
                Product dealProduct = new Product(
                        product.getProductId(),
                        product.getName(),
                        product.getCategory(),
                        deal.getDiscountedPrice(),
                        Math.min(product.getStock(), deal.getStockLimit()),
                        product.getDescription() + " [DEAL: " + deal.getDiscountPercentage() + "% OFF]"
                );

                app.getCurrentUserCart().addItem(dealProduct, 1);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deal Added!");
                alert.setHeaderText(null);
                alert.setContentText("Added " + deal.getProductName() + " at Rs " +
                        String.format("%.2f", deal.getDiscountedPrice()) + " to cart!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Product not found!");
                alert.showAndWait();
            }
        });

        card.getChildren().addAll(
                discountBadge, imageContainer, sep1,
                productLabel, idLabel,
                priceLabel, originalPrice, savingsLabel,
                stockLabel, expiry, sep2,
                addToCartBtn
        );

        return card;
    }

    private String getImageNameForProduct(Product product) {
        if (product == null) return "laptop.png";

        String name = product.getName().toLowerCase();
        String category = product.getCategory().toLowerCase();

        if (name.contains("macbook") || name.contains("laptop") || category.contains("laptop")) {
            return "laptop.png";
        } else if (name.contains("iphone") || name.contains("phone") || category.contains("phone")) {
            return "smartphone.png";
        } else if (name.contains("headphone") || category.contains("audio")) {
            return "headphones.png";
        } else if (name.contains("book") || category.contains("book")) {
            return "books.png";
        } else if (name.contains("shirt") || name.contains("fashion") || category.contains("fashion")) {
            return "fashion.png";
        } else if (name.contains("appliance") || category.contains("appliance")) {
            return "appliances.png";
        }

        return "laptop.png";
    }

    private Product findProductById(String productId) {
        return app.getProductService().findProductById(productId);
    }

    private void showGuestAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText(null);
        alert.setContentText("Please login or signup to add deal items to cart!");
        alert.showAndWait();
    }
}