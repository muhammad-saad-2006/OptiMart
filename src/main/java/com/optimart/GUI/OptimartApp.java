package com.optimart.GUI;

import com.optimart.DataStructures.HashMap;
import com.optimart.GUI.Views.*;
import com.optimart.Models.Cart;
import com.optimart.Models.Order;
import com.optimart.Models.Product;
import com.optimart.Services.DealService;
import com.optimart.Services.OrderStatusService;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import com.optimart.Utils.FileHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class OptimartApp extends Application {

    // ================== Services & Data ==================
    private UserServices userService;
    private ProductServices productService;
    private DealService dealService;
    private OrderStatusService orderStatusService;

    private HashMap<String, Product> productMap;
    private HashMap<String, Cart> userCarts;
    private List<Order> orders;

    // ================== GUI ==================
    private Stage primaryStage;
    private Scene mainScene;   // ✅ SINGLE SCENE

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        initializeSystem();

        primaryStage.setTitle("Optimart: Online Shopping");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);

        try {
            // Load logo from resources folder
            Image icon = new Image(getClass().getResourceAsStream("/images/logo.png"));
            primaryStage.getIcons().add(icon);
            System.out.println("✓ Logo loaded successfully!");
        } catch (Exception e) {
            System.err.println("⚠ Could not load logo: " + e.getMessage());
            System.err.println("  Make sure optimart-logo.png is in src/main/resources/images/");
        }

        // ✅ Create scene ONCE
        mainScene = new Scene(new MainMenuView(this));

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // ================== VIEW SWITCHING ==================

    public void showMainMenuView() {
        mainScene.setRoot(new MainMenuView(this));
    }

    public void showLoginView() {
        mainScene.setRoot(new LoginView(this, userService));
    }

    public void showSignupView() {
        mainScene.setRoot(new SignupView(this, userService));
    }

    public void showCustomerDashboard() {
        mainScene.setRoot(new CustomerDashboardView(this, userService));
    }

    public void showAdminDashboard() {
        mainScene.setRoot(
                new AdminDashboardView(this, userService, productService, dealService)
        );
    }

    public void showHomeView(boolean isGuest) {
        mainScene.setRoot(
                new HomeView(
                        this,
                        userService,
                        productService,
                        isGuest ? new HashMap<>() : userCarts,
                        isGuest
                )
        );
    }

    public void showSearch(boolean isGuest) {
        mainScene.setRoot(new SearchView(this, productService, userService, isGuest));
    }

    public void showDealsView(boolean isGuest) {
        mainScene.setRoot(new DealsView(this, isGuest));
    }

    public void showCartView() {
        mainScene.setRoot(new CartView(this, getCurrentUserCart(), productMap));
    }

    public void showProductDetail() {
        mainScene.setRoot(new ProductDetailView(this, productService, userService));
    }

    public void showProductSort(boolean isGuest) {
        mainScene.setRoot(new ProductSortView(this, productService, isGuest));
    }

    public void showSimpleMessage(String title, String message) {
        mainScene.setRoot(new SimpleMessageView(this, title, message));
    }

    public DealService getDealService() {
        return dealService;
    }

    public UserServices getUserService() {
        return userService;
    }

    public ProductServices getProductService() {
        return productService;
    }

    public HashMap<String, Product> getProductMap() {
        return productMap;
    }

    public void showPasswordChange() {
        mainScene.setRoot(new PasswordChangeView(this, userService));
    }

    public void showOrderHistory() {
        mainScene.setRoot(new OrderHistoryView(this));
    }

    public void showOrderTracking() {
        mainScene.setRoot(new OrderTrackingView(this));
    }

    public void showAdminOrderManagement() {
        mainScene.setRoot(new AdminOrderManagementView(this));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // ================== DATA HELPERS ==================

    public Cart getCurrentUserCart() {
        if (userService.getCurrentUser() != null) {
            String username = userService.getCurrentUser().getUsername();
            userCarts.putIfAbsent(username, new Cart());
            return userCarts.get(username);
        }
        return new Cart();
    }

    private void initializeSystem() {
        userService = new UserServices();
        productService = new ProductServices();
        dealService = new DealService();
        orderStatusService = new OrderStatusService();

        productMap = new HashMap<>();
        userCarts = new HashMap<>();

        List<Product> products = FileHandler.loadProducts();
        for (Product p : products) {
            productMap.put(p.getProductId().toUpperCase(), p);
        }

        orders = FileHandler.loadOrders();

        System.out.println("✓ System initialized successfully");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
