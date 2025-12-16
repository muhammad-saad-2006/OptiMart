package com.optimart.GUI.Views;

import com.optimart.GUI.Components.ProductCard;
import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Product;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class SearchView extends BorderPane {

    private OptimartApp app;
    private ProductServices productServices;
    private UserServices userServices;
    private FlowPane resultsGrid;
    private TextField searchField;
    private Label resultLabel;
    private boolean isGuest;

    public SearchView(OptimartApp app, ProductServices productServices, UserServices userServices, boolean isGuest) {
        this.app = app;
        this.productServices = productServices;
        this.userServices = userServices;
        this.isGuest = isGuest;
        initialize();
    }

    private void initialize() {
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        // Header
        VBox header = new VBox(15);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: white;");

        Label title = new Label("🔍 Search Products");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Search Field
        searchField = new TextField();
        searchField.setPromptText("Start typing to search...");
        searchField.setPrefWidth(600);
        searchField.setFont(Theme.FONT_TITLE);
        searchField.setStyle("-fx-padding: 10; -fx-font-size: 16px;");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String text = newVal == null ? "" : newVal.trim();

            if (text.isEmpty()) {
                List<Product> allProducts = productServices.getAllProducts();
                resultsGrid.getChildren().clear();

                resultLabel.setText("Browsing All Products (" + allProducts.size() + " items)");
                resultLabel.setTextFill(Theme.AMAZON_NAVY);

                for (Product product : allProducts) {
                    ProductCard card = new ProductCard(product, userServices, p -> {
                        if (isGuest || userServices.getCurrentUser() == null) {
                            showGuestAlert();
                        } else {
                            app.getCurrentUserCart().addItem(p, 1);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Added!");
                            alert.setHeaderText(null);
                            alert.setContentText(p.getName() + " added to cart!");
                            alert.showAndWait();
                        }
                    });
                    resultsGrid.getChildren().add(card);
                }
            } else {
                performSearch(text);
            }
        });

        // Back button
        Button backBtn = new Button(isGuest ? "← Back to Main Menu" : "← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> {
            if (isGuest) {
                app.showMainMenuView();
            } else {
                app.showCustomerDashboard();
            }
        });

        resultLabel = new Label("");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        resultLabel.setTextFill(Theme.AMAZON_NAVY);

        header.getChildren().addAll(title, searchField, resultLabel, backBtn);
        this.setTop(header);

        // Results Grid
        resultsGrid = new FlowPane();
        resultsGrid.setHgap(20);
        resultsGrid.setVgap(20);
        resultsGrid.setPadding(new Insets(20));
        resultsGrid.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane(resultsGrid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        this.setCenter(scroll);
    }

    private void performSearch(String query) {
        List<Product> results = productServices.searchProducts(query);
        resultsGrid.getChildren().clear();

        if (results.isEmpty()) {
            resultLabel.setText("No products found for \"" + query + "\"");
            resultLabel.setTextFill(Theme.TEXT_GRAY);
        } else {
            String countText = results.size() == 1 ? "1 product found" : results.size() + " products found";
            resultLabel.setText("Search Results for \"" + query + "\" · " + countText);
            resultLabel.setTextFill(Theme.AMAZON_NAVY);

            for (Product product : results) {
                ProductCard card = new ProductCard(product, userServices, p -> {
                    if (isGuest || userServices.getCurrentUser() == null) {
                        showGuestAlert();
                    } else {
                        app.getCurrentUserCart().addItem(p, 1);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Added to Cart");
                        alert.setHeaderText(null);
                        alert.setContentText(p.getName() + " added to your cart!");
                        alert.showAndWait();
                    }
                });
                resultsGrid.getChildren().add(card);
            }
        }
    }

    // FIXED: Show alert for guest users
    private void showGuestAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText(null);
        alert.setContentText("Please login or signup to add items to cart!");
        alert.showAndWait();
    }
}