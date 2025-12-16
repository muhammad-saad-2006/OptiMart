package com.optimart.GUI.Views;

import com.optimart.DataStructures.HashMap;
import com.optimart.GUI.Components.ProductCard;
import com.optimart.GUI.Components.TopBar;
import com.optimart.GUI.Theme;
import com.optimart.Models.Cart;
import com.optimart.Models.Product;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class HomeView extends BorderPane {

    private UserServices userService;
    private ProductServices productServices;
    private HashMap<String, Cart> userCarts;
    private FlowPane productsGrid;
    private com.optimart.GUI.OptimartApp app;
    private boolean isGuest;
    private Label infoLabel;

    public HomeView(com.optimart.GUI.OptimartApp app, UserServices userService, ProductServices productServices,
                    HashMap<String, Cart> userCarts, boolean isGuest) {
        this.app = app;
        this.userService = userService;
        this.productServices = productServices;
        this.userCarts = userCarts;
        this.isGuest = isGuest;

        TopBar topBar = new TopBar(
                () -> {
                    if (isGuest) {
                        showGuestAlert();
                    } else {
                        app.showCartView();
                    }
                },
                () -> {
                    if (isGuest) {
                        app.showMainMenuView();
                    } else {
                        app.showCustomerDashboard();
                    }
                },
                productServices,
                query -> performSearch(query)
        );
        this.setTop(topBar);


        VBox mainContent = new VBox(10);
        mainContent.setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND_WHITE) + ";");
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);


        infoLabel = new Label("All Products");
        infoLabel.setFont(Theme.FONT_TITLE);
        infoLabel.setTextFill(Theme.AMAZON_NAVY);


        productsGrid = new FlowPane();
        productsGrid.setHgap(20);
        productsGrid.setVgap(20);
        productsGrid.setPadding(new Insets(20));
        productsGrid.setAlignment(Pos.CENTER);
        productsGrid.setStyle("-fx-background-color: transparent;");

        // Load Products
        loadAllProducts();

        mainContent.getChildren().addAll(infoLabel, productsGrid);

        // ScrollPane for scrolling content
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + toHex(Theme.BACKGROUND_WHITE) + "; -fx-background-color: transparent;");

        this.setCenter(scrollPane);
    }

    private void performSearch(String query) {
        productsGrid.getChildren().clear();

        // Use TRIE to search products
        List<Product> results = productServices.searchProducts(query);

        if (results.isEmpty()) {
            infoLabel.setText("No products found for: \"" + query + "\"");
            Label noResults = new Label("No products match your search.");
            noResults.setFont(Theme.FONT_REGULAR);
            noResults.setTextFill(Theme.TEXT_GRAY);
            productsGrid.getChildren().add(noResults);
            return;
        }

        infoLabel.setText("Search Results for \"" + query + "\" (" + results.size() + " products found)");

        for (Product product : results) {
            ProductCard card = createProductCard(product);
            productsGrid.getChildren().add(card);
        }

        updateCartCount();
    }

    private void loadAllProducts() {
        List<Product> products = productServices.getAllProducts();
        productsGrid.getChildren().clear();
        infoLabel.setText("All Products (" + products.size() + " items)");

        for (Product product : products) {
            ProductCard card = createProductCard(product);
            productsGrid.getChildren().add(card);
        }

        updateCartCount();
    }

    private ProductCard createProductCard(Product product) {
        return new ProductCard(product, userService, p -> {
            if (isGuest) {
                showGuestAlert();
            } else if (userService.getCurrentUser() != null) {
                String username = userService.getCurrentUser().getUsername();
                if (!userCarts.containsKey(username)) {
                    userCarts.put(username, new Cart());
                }
                userCarts.get(username).addItem(p, 1);

                updateCartCount();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Added " + p.getName() + " to cart!");
                alert.showAndWait();
            } else {
                showGuestAlert();
            }
        });
    }

    private void showGuestAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText(null);
        alert.setContentText("Please login or signup to add items to cart!");
        alert.showAndWait();
    }

    public void updateCartCount() {
        TopBar topBar = (TopBar) this.getTop();
        if (userService.getCurrentUser() != null) {
            String username = userService.getCurrentUser().getUsername();
            if (userCarts.containsKey(username)) {
                Cart c = userCarts.get(username);
                int count = c.getItems().size();
                topBar.updateCartCount(count);
            }
        } else {
            topBar.updateCartCount(0);
        }
    }

    private String toHex(javafx.scene.paint.Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}