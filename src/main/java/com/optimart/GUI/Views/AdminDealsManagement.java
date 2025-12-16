package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.Deal;
import com.optimart.Models.Product;
import com.optimart.Services.DealService;
import com.optimart.Services.ProductServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDealsManagement extends VBox {
    private OptimartApp app;
    private DealService dealService;
    private ProductServices productService;
    private TableView<Deal> dealsTable;

    public AdminDealsManagement(OptimartApp app, DealService dealService, ProductServices productService) {
        this.app = app;
        this.dealService = dealService;
        this.productService = productService;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("🎉 Deals Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button addDealBtn = new Button("➕ Add New Deal");
        addDealBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        addDealBtn.setOnAction(e -> showAddDealDialog());

        Button removeExpiredBtn = new Button("🗑️ Remove Expired Deals");
        removeExpiredBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        removeExpiredBtn.setOnAction(e -> removeExpiredDeals());

        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        refreshBtn.setOnAction(e -> loadDeals());



        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE + "-fx-font-size: 15px; -fx-padding: 12 25;");
        backBtn.setOnAction(e -> app.showAdminDashboard());

        buttonBox.getChildren().addAll(addDealBtn, removeExpiredBtn, refreshBtn, backBtn);

        dealsTable = new TableView<>();
        dealsTable.setPrefHeight(500);
        dealsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Deal, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductId()));
        idCol.setMinWidth(100);

        TableColumn<Deal, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        nameCol.setMinWidth(200);

        TableColumn<Deal, String> discountCol = new TableColumn<>("Discount %");
        discountCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDiscountPercentage() + "%"));
        discountCol.setMinWidth(100);

        TableColumn<Deal, String> originalCol = new TableColumn<>("Original Price");
        originalCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getOriginalPrice())));
        originalCol.setMinWidth(120);

        TableColumn<Deal, String> discountedCol = new TableColumn<>("Deal Price");
        discountedCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("Rs %.2f", cellData.getValue().getDiscountedPrice())));
        discountedCol.setMinWidth(120);

        TableColumn<Deal, String> expiryCol = new TableColumn<>("Expiry");
        expiryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpiryDate()));
        expiryCol.setMinWidth(160);

        TableColumn<Deal, String> timeLeftCol = new TableColumn<>("Time Left");
        timeLeftCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimeRemaining()));
        timeLeftCol.setMinWidth(120);

        TableColumn<Deal, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isExpired() ? "EXPIRED" : "ACTIVE"));
        statusCol.setMinWidth(100);

        dealsTable.getColumns().addAll(idCol, nameCol, discountCol, originalCol,
                discountedCol, expiryCol, timeLeftCol, statusCol);

        loadDeals();

        this.getChildren().addAll(title, buttonBox, dealsTable);
    }

    private void loadDeals() {
        List<Deal> deals = dealService.getTopDeals(100);
        dealsTable.getItems().setAll(deals);

        long active = deals.stream().filter(d -> !d.isExpired()).count();
        long expired = deals.stream().filter(Deal::isExpired).count();
        System.out.println("✓ Loaded " + deals.size() + " deals (" + active + " active, " + expired + " expired)");
    }

    private void showAddDealDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add New Deal");
        dialog.setHeaderText("Create a new deal for a product\n💡 Tip: Set time for live demo!");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25));

        Label productIdLabel = new Label("Product ID:");
        TextField productIdField = new TextField();
        productIdField.setPromptText("e.g., P001");

        Button searchProductBtn = new Button("Search");
        searchProductBtn.setStyle("-fx-background-color: #FF9900; -fx-text-fill: white;");

        Label productNameDisplay = new Label("");
        productNameDisplay.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        productNameDisplay.setTextFill(javafx.scene.paint.Color.GREEN);

        searchProductBtn.setOnAction(e -> {
            String productId = productIdField.getText().trim();
            Product product = productService.findProductById(productId);
            if (product != null) {
                productNameDisplay.setText("✓ Found: " + product.getName() + " (Rs " +
                        String.format("%.2f", product.getPrice()) + ")");
            } else {
                productNameDisplay.setText("✗ Product not found!");
                productNameDisplay.setTextFill(javafx.scene.paint.Color.RED);
            }
        });

        Label discountLabel = new Label("Discount %:");
        Spinner<Integer> discountSpinner = new Spinner<>(5, 90, 20, 5);
        discountSpinner.setEditable(true);
        discountSpinner.setPrefWidth(150);

        Label expiryLabel = new Label("Expiry Date:");
        DatePicker expiryPicker = new DatePicker(LocalDate.now().plusDays(1));
        expiryPicker.setPromptText("yyyy-MM-dd");

        Label timeLabel = new Label("Expiry Time:");
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, LocalDateTime.now().getHour(), 1);
        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(80);

        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, LocalDateTime.now().getMinute(), 5);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(80);

        HBox timeBox = new HBox(10);
        timeBox.getChildren().addAll(
                new Label("Hour:"), hourSpinner,
                new Label("Minute:"), minuteSpinner
        );

        HBox presetBox = new HBox(10);
        presetBox.setAlignment(Pos.CENTER_LEFT);

        Button in5MinBtn = new Button("+5 min");
        in5MinBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 11px;");
        in5MinBtn.setOnAction(e -> {
            LocalDateTime future = LocalDateTime.now().plusMinutes(5);
            expiryPicker.setValue(future.toLocalDate());
            hourSpinner.getValueFactory().setValue(future.getHour());
            minuteSpinner.getValueFactory().setValue(future.getMinute());
        });

        Button in10MinBtn = new Button("+10 min");
        in10MinBtn.setStyle("-fx-background-color: #ff8800; -fx-text-fill: white; -fx-font-size: 11px;");
        in10MinBtn.setOnAction(e -> {
            LocalDateTime future = LocalDateTime.now().plusMinutes(10);
            expiryPicker.setValue(future.toLocalDate());
            hourSpinner.getValueFactory().setValue(future.getHour());
            minuteSpinner.getValueFactory().setValue(future.getMinute());
        });

        Button in30MinBtn = new Button("+30 min");
        in30MinBtn.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-font-size: 11px;");
        in30MinBtn.setOnAction(e -> {
            LocalDateTime future = LocalDateTime.now().plusMinutes(30);
            expiryPicker.setValue(future.toLocalDate());
            hourSpinner.getValueFactory().setValue(future.getHour());
            minuteSpinner.getValueFactory().setValue(future.getMinute());
        });

        Label presetLabel = new Label("Quick Set:");
        presetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        presetBox.getChildren().addAll(presetLabel, in5MinBtn, in10MinBtn, in30MinBtn);

        Label stockLimitLabel = new Label("Stock Limit:");
        Spinner<Integer> stockSpinner = new Spinner<>(1, 1000, 10, 1);
        stockSpinner.setEditable(true);
        stockSpinner.setPrefWidth(150);

        grid.add(productIdLabel, 0, 0);
        grid.add(productIdField, 1, 0);
        grid.add(searchProductBtn, 2, 0);
        grid.add(productNameDisplay, 1, 1, 2, 1);
        grid.add(discountLabel, 0, 2);
        grid.add(discountSpinner, 1, 2);
        grid.add(expiryLabel, 0, 3);
        grid.add(expiryPicker, 1, 3);
        grid.add(timeLabel, 0, 4);
        grid.add(timeBox, 1, 4, 2, 1);
        grid.add(presetBox, 1, 5, 2, 1);
        grid.add(stockLimitLabel, 0, 6);
        grid.add(stockSpinner, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String productId = productIdField.getText().trim();
                    Product product = productService.findProductById(productId);

                    if (product == null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Product not found!");
                        return null;
                    }

                    int discount = discountSpinner.getValue();
                    int stockLimit = stockSpinner.getValue();

                    String expiryDateTime = String.format("%s %02d:%02d",
                            expiryPicker.getValue().toString(),
                            hourSpinner.getValue(),
                            minuteSpinner.getValue()
                    );

                    dealService.addDeal(product, discount, expiryDateTime, stockLimit);
                    loadDeals();

                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Deal created successfully!\n" +
                                    discount + "% off on " + product.getName() + "\n" +
                                    "Expires: " + expiryDateTime);

                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to create deal: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void removeExpiredDeals() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Expired Deals");
        confirm.setHeaderText("This will permanently delete expired deals from the database.");
        confirm.setContentText("Are you sure?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            int sizeBefore = dealsTable.getItems().size();
            dealService.removeExpiredDeals();
            loadDeals();
            int sizeAfter = dealsTable.getItems().size();
            int removed = sizeBefore - sizeAfter;

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Removed " + removed + " expired deal(s)!\n" + sizeAfter + " active deals remaining.");
        }
    }

//    private void createDemoDeals() {
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
//        confirm.setTitle("Create Demo Deals");
//        confirm.setHeaderText("Create 3 sample deals for live demonstration?");
//        confirm.setContentText(
//                "This will create deals on first 3 products:\n" +
//                        "• Deal 1: 50% OFF - Expires in 5 minutes\n" +
//                        "• Deal 2: 30% OFF - Expires in 10 minutes\n" +
//                        "• Deal 3: 20% OFF - Expires in 15 minutes"
//        );
//
//        if (confirm.showAndWait().get() == ButtonType.OK) {
//            try {
//                List<Product> products = productService.getAllProducts();
//
//                if (products.size() < 3) {
//                    showAlert(Alert.AlertType.ERROR, "Error",
//                            "Need at least 3 products in the system!");
//                    return;
//                }
//
//                LocalDateTime now = LocalDateTime.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//                // Deal 1: +5 minutes
//                dealService.addDeal(products.get(0), 50,
//                        now.plusMinutes(5).format(formatter), 10);
//
//                // Deal 2: +10 minutes
//                dealService.addDeal(products.get(1), 30,
//                        now.plusMinutes(10).format(formatter), 10);
//
//                // Deal 3: +15 minutes
//                dealService.addDeal(products.get(2), 20,
//                        now.plusMinutes(15).format(formatter), 10);
//
//                loadDeals();
//
//                showAlert(Alert.AlertType.INFORMATION, "Demo Deals Created",
//                        "✓ Created 3 demo deals!\n\n" +
//                                "Watch them expire in real-time:\n" +
//                                "• First deal expires in 5 minutes\n" +
//                                "• Second deal expires in 10 minutes\n" +
//                                "• Third deal expires in 15 minutes\n\n" +
//                                "Click 'Refresh' to see updated status!");
//
//            } catch (Exception e) {
//                showAlert(Alert.AlertType.ERROR, "Error",
//                        "Failed to create demo deals: " + e.getMessage());
//            }
//        }
//    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}