package com.optimart.GUI.Components;

import com.optimart.GUI.Theme;
import com.optimart.Models.Product;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;

public class ProductCard extends VBox {

    private UserServices userServices;
    private Product product;
    private Button addToCartButton;
    private Consumer<Product> onAddToCart;

    public ProductCard(Product product, UserServices userServices, Consumer<Product> onAddToCart) {
        this.product = product;
        this.userServices = userServices;
        this.onAddToCart = onAddToCart;

        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 4;");
        this.setEffect(Theme.getCardShadow());
        this.setPrefWidth(250);
        this.setPrefHeight(350);
        this.setMaxWidth(250);

        // Image Handling
        VBox imageContainer = new VBox();
        imageContainer.setPrefHeight(150);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle("-fx-background-color: #F7F7F7;");

        String imagePath = "/images/" + getImageNameForCategory(product);
        try {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                    new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath)));
            imageView.setFitHeight(140);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } catch (Exception e) {
            // Fallback: Generate Dynamic Placeholder
            javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(200, 140);
            javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

            // Generate color from product name
            int hash = product.getName().hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = (hash & 0x0000FF);
            Color baseColor = Color.rgb(Math.abs(r % 255), Math.abs(g % 255), Math.abs(b % 255), 0.3);

            gc.setFill(baseColor);
            gc.fillRect(0, 0, 200, 140);

            gc.setFill(Theme.TEXT_GRAY);
            gc.setFont(Theme.FONT_TITLE);
            gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
            gc.setTextBaseline(javafx.geometry.VPos.CENTER);
            gc.fillText(product.getName(), 100, 70, 180);

            imageContainer.getChildren().add(canvas);
        }

        // Title
        Label title = new Label(product.getName());
        title.setFont(Theme.FONT_TITLE);
        title.setWrapText(true);
        title.setMaxHeight(60);
        title.setTextFill(Theme.TEXT_BLACK);

        // Rating
        Label rating = new Label("⭐⭐⭐⭐☆ 42");
        rating.setTextFill(Color.web("#007185"));
        rating.setFont(Theme.FONT_SMALL);

        // Price
        Text currency = new Text("Rs ");
        currency.setFont(Theme.FONT_PRICE_SYMBOL);
        currency.setFill(Theme.TEXT_BLACK);

        Text amount = new Text(String.format("%.0f", product.getPrice()));
        amount.setFont(Theme.FONT_PRICE);
        amount.setFill(Theme.TEXT_BLACK);

        Text fraction = new Text("00");
        fraction.setFont(Theme.FONT_PRICE_SYMBOL);
        fraction.setFill(Theme.TEXT_BLACK);

        TextFlow priceFlow = new TextFlow(currency, amount, fraction);

        // Delivery Info
        Label delivery = new Label("FREE Delivery by Optimart");
        delivery.setFont(Theme.FONT_SMALL);
        delivery.setTextFill(Theme.TEXT_GRAY);

        // Stock Status
        Label stockLabel;
        if (product.getStock() > 0) {
            stockLabel = new Label("In Stock");
            stockLabel.setTextFill(Color.web("#007600"));
        } else {
            stockLabel = new Label("Currently Unavailable");
            stockLabel.setTextFill(Color.web("#B12704"));
        }
        stockLabel.setFont(Theme.FONT_SMALL);

        // Add to Cart Button
        addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle(Theme.YELLOW_BUTTON_STYLE);
        addToCartButton.setMaxWidth(Double.MAX_VALUE);

        // Hover effects
        addToCartButton.setOnMouseEntered(e -> addToCartButton.setStyle(Theme.YELLOW_BUTTON_HOVER_STYLE));
        addToCartButton.setOnMouseExited(e -> addToCartButton.setStyle(Theme.YELLOW_BUTTON_STYLE));

        // Cart Action
        addToCartButton.setOnAction(e -> {
            if (onAddToCart != null) {
                onAddToCart.accept(product);
            }
        });

        // Assemble
        this.getChildren().addAll(
                imageContainer,
                title,
                rating,
                priceFlow,
                delivery,
                stockLabel,
                addToCartButton);
    }

    private String getImageNameForCategory(Product product) {
        String name = product.getName().toLowerCase();
        String category = product.getCategory().toLowerCase();

        if (name.contains("macbook") || name.contains("laptop") || name.contains("dell") ||
                name.contains("hp") || name.contains("thinkpad") || name.contains("lenovo") ||
                name.contains("asus") || name.contains("acer") || category.contains("laptop") ||
                category.contains("computer")) {
            return "laptop.png";
        }

        // Smartphones
        if (name.contains("iphone") || name.contains("samsung") || name.contains("pixel") ||
                name.contains("galaxy") || name.contains("phone") || name.contains("mobile") ||
                name.contains("oneplus") || name.contains("xiaomi") || category.contains("phone") ||
                category.contains("mobile") || category.contains("smartphone")) {
            return "smartphone.png";
        }

        // Headphones
        if (name.contains("headphone") || name.contains("earphone") || name.contains("airpod") ||
                name.contains("sony") || name.contains("bose") || name.contains("beats") ||
                name.contains("audio") || category.contains("audio") || category.contains("headphone")) {
            return "headphones.png";
        }

        // Books
        if (name.contains("book") || name.contains("guide") || name.contains("edition") ||
                name.contains("manual") || name.contains("novel") || name.contains("textbook") ||
                category.contains("book") || category.contains("literature")) {
            return "books.png";
        }

        // Fashion
        if (name.contains("shirt") || name.contains("pant") || name.contains("jacket") ||
                name.contains("dress") || name.contains("jean") || name.contains("shoe") ||
                name.contains("bag") || name.contains("watch") || category.contains("fashion") ||
                category.contains("clothing") || category.contains("apparel") || category.contains("wear")) {
            return "fashion.png";
        }

        // Appliances
        if (name.contains("oven") || name.contains("mixer") || name.contains("blender") ||
                name.contains("refrigerator") || name.contains("microwave") || name.contains("toaster") ||
                name.contains("kettle") || name.contains("cooker") || category.contains("appliance") ||
                category.contains("kitchen") || category.contains("home")) {
            return "appliances.png";
        }

        if (category.contains("electronic"))
            return "laptop.png";
        if (category.contains("tech"))
            return "smartphone.png";

        return "laptop.png";
    }

    public Product getProduct() {
        return product;
    }
}
