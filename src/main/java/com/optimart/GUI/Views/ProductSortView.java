package com.optimart.GUI.Views;

import com.optimart.DataStructures.LinkedList;
import com.optimart.GUI.Components.ProductCard;
import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Product;
import com.optimart.Services.ProductServices;
import com.optimart.Utils.ProductSorter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class ProductSortView extends VBox {

    private OptimartApp app;
    private ProductServices productServices;
    private FlowPane productGrid;
    private Label titleLabel;
    private boolean isGuest;

    public ProductSortView(OptimartApp app, ProductServices productServices, boolean isGuest) {
        this.app = app;
        this.productServices = productServices;
        this.isGuest = isGuest;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        titleLabel = new Label("View Sorted Products");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Theme.AMAZON_NAVY);

        // Sort options
        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(
                "Sort by Name (A-Z)",
                "Sort by Price (Low to High)",
                "Sort by Price (High to Low)");
        sortCombo.setValue("Sort by Name (A-Z)");
        sortCombo.setStyle("-fx-font-size: 14px; -fx-pref-width: 250px;");

        sortCombo.setOnAction(e -> {
            String selected = sortCombo.getValue();
            refreshProducts(selected);
        });

        Button backBtn = new Button(isGuest ? "← Back to Main Menu" : "← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> {
            if (isGuest) {
                app.showMainMenuView();
            } else {
                app.showCustomerDashboard();
            }
        });

        // Product grid
        productGrid = new FlowPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setAlignment(Pos.CENTER);
        productGrid.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        this.getChildren().addAll(titleLabel, sortCombo, scrollPane, backBtn);

        // Load initial products
        refreshProducts("Sort by Name (A-Z)");
    }

    private void refreshProducts(String sortOption) {
        productGrid.getChildren().clear();

        List<Product> productList = productServices.getAllProducts();

        LinkedList<Product> linkedProducts = new LinkedList<>();
        for (Product p : productList) {
            linkedProducts.addLast(p);
        }

        LinkedList<Product> sortedProducts;


        switch (sortOption) {
            case "Sort by Name (A-Z)":
                sortedProducts = ProductSorter.sortByName(linkedProducts);
                break;

            case "Sort by Price (Low to High)":
                sortedProducts = ProductSorter.sortByPriceLowToHigh(linkedProducts);
                break;

            case "Sort by Price (High to Low)":
                sortedProducts = ProductSorter.sortByPriceHighToLow(linkedProducts);
                break;

            default:
                sortedProducts = linkedProducts;
        }

        // Display sorted products
        for (int i = 0; i < sortedProducts.size(); i++) {
            Product product = sortedProducts.get(i);

            ProductCard card = new ProductCard(product, null, p -> {
                if (isGuest) {
                    showGuestAlert();
                } else {
                    app.getCurrentUserCart().addItem(p, 1);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Added " + p.getName() + " to cart!");
                    alert.showAndWait();
                }
            });

            productGrid.getChildren().add(card);
        }

        titleLabel.setText("Products - " + sortOption + " (" + sortedProducts.size() + " items ");
    }

    private void showGuestAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText(null);
        alert.setContentText("Please login or signup to add items to cart!");
        alert.showAndWait();
    }
}