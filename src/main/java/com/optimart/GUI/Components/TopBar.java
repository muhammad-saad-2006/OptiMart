package com.optimart.GUI.Components;

import com.optimart.GUI.Theme;
import com.optimart.Services.ProductServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class TopBar extends HBox {

    private AutocompleteSearchField searchField;
    private Button searchButton;
    private Label cartCountLabel;
    private Runnable onCartClick;
    private Runnable onBackClick;

    public TopBar(Runnable onCartClick, Runnable onBackClick, ProductServices productServices,
                  Consumer<String> onSearch) {
        this.onCartClick = onCartClick;
        this.onBackClick = onBackClick;

        this.setStyle(Theme.NAV_BAR_STYLE);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setPrefHeight(60);

        // Logo Area / Back Button
        Button backBtn = new Button("← Dashboard");
        backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            if (onBackClick != null)
                onBackClick.run();
        });

        Region spacer1 = new Region();
        spacer1.setPrefWidth(20);

        // Search Bar Container with TRIE AUTOCOMPLETE
        HBox searchContainer = new HBox();
        HBox.setHgrow(searchContainer, Priority.ALWAYS);
        searchContainer.setAlignment(Pos.CENTER);

        // Use autocomplete search field
        searchField = new AutocompleteSearchField(productServices, query -> {
            if (onSearch != null) {
                onSearch.accept(query);
            }
        });
        searchField.setPromptText("Search in Optimart (Type for suggestions - Press TAB to complete)");
        searchField.setPrefHeight(40);
        searchField.setStyle("-fx-background-radius: 4 0 0 4; -fx-background-color: white; -fx-font-size: 14px;");
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchButton = new Button("🔍");
        searchButton.setPrefHeight(40);
        searchButton.setPrefWidth(45);
        searchButton.setStyle("-fx-background-color: #febd69; -fx-background-radius: 0 4 4 0; -fx-cursor: hand;");

        searchButton.setOnAction(e -> {
            if (onSearch != null && !searchField.getText().trim().isEmpty()) {
                searchField.hideSuggestions();
                onSearch.accept(searchField.getText().trim());
            }
        });

        searchContainer.getChildren().addAll(searchField, searchButton);

        // Right Side Actions
        Label signInLabel = createNavLabel("Hello, sign in\nAccount & Lists");
        Label returnsLabel = createNavLabel("Returns\n& Orders");

        // Cart
        HBox cartBox = new HBox();
        cartBox.setAlignment(Pos.CENTER);
        cartBox.setSpacing(5);

        Label cartIcon = new Label("🛒");
        cartIcon.setTextFill(Color.WHITE);
        cartIcon.setFont(Font.font("Arial", 24));

        cartCountLabel = new Label("0");
        cartCountLabel.setTextFill(Theme.AMAZON_YELLOW);
        cartCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label cartText = new Label("Cart");
        cartText.setTextFill(Color.WHITE);
        cartText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cartText.setPadding(new Insets(10, 0, 0, 0));

        cartBox.getChildren().addAll(cartIcon, cartCountLabel, cartText);
        cartBox.setStyle("-fx-cursor: hand;");
        cartBox.setOnMouseClicked(e -> {
            if (onCartClick != null)
                onCartClick.run();
        });

        this.getChildren().addAll(backBtn, spacer1, searchContainer, signInLabel, returnsLabel, cartBox);
    }

    private Label createNavLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Theme.FONT_SMALL);
        label.setPadding(new Insets(0, 10, 0, 10));
        label.setStyle("-fx-cursor: hand;");
        return label;
    }

    public AutocompleteSearchField getSearchField() {
        return searchField;
    }

    public void updateCartCount(int count) {
        cartCountLabel.setText(String.valueOf(count));
    }

    public Button getSearchButton() {
        return searchButton;
    }
}