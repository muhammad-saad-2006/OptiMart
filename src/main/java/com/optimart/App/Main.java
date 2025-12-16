package com.optimart.App;

import com.optimart.DataStructures.HashMap;
import com.optimart.DataStructures.LinkedList;
import com.optimart.Models.*;
import com.optimart.Services.DealService;
import com.optimart.Services.OrderStatusService;
import com.optimart.Services.ProductServices;
import com.optimart.Services.UserServices;
import com.optimart.Utils.FileHandler;
import com.optimart.Utils.ProductSorter;

import java.util.List;
import java.util.Scanner;

public class Main {
    //Part 1
    private static UserServices userService;
    private static ProductServices productService;
    private static Scanner scanner;

    // Part 2
    private static HashMap<String, Product> productMap;
    private static HashMap<String, Cart> userCarts;
    private static List<Order> orders;

    //Part 3
    private static DealService dealService;
    private static OrderStatusService orderStatusService;

    public static void main(String[] args) {
        showSplashScreen();

        // Initialize services
        initializeSystem();
        scanner = new Scanner(System.in);

        // Main menu loop
        boolean running = true;
        while (running) {
            if (!userService.isLoggedIn()) {
                running = showMainMenu();
            } else if (userService.isAdmin()) {
                running = showAdminMenu();
            } else {
                running = showCustomerMenu();
            }
        }

        scanner.close();
        System.out.println("\n✓ Thank you for using OptiMart!!!");
    }

    private static void initializeSystem() {
        userService = new UserServices();
        productService = new ProductServices();

        userCarts = new HashMap<>();
        productMap = new HashMap<>();

        List<Product> products = FileHandler.loadProducts();
        for (Product p : products) {
            productMap.put(p.getProductId().toUpperCase(), p);
        }

        orders = FileHandler.loadOrders();

        dealService = new DealService();
        orderStatusService = new OrderStatusService();

        System.out.println("✓ System initialized successfully!");
        System.out.println("✓ Loaded " + orders.size() + " orders from history");
    }

    private static void showSplashScreen() {
        // Display large OptiMart logo
        System.out.println("\n\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                       ║");
        System.out.println("║     ██████╗ ██████╗ ████████╗██╗███╗   ███╗ █████╗ ██████╗ ████████╗  ║");
        System.out.println("║    ██╔═══██╗██╔══██╗╚══██╔══╝██║████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝  ║");
        System.out.println("║    ██║   ██║██████╔╝   ██║   ██║██╔████╔██║███████║██████╔╝   ██║     ║");
        System.out.println("║    ██║   ██║██╔═══╝    ██║   ██║██║╚██╔╝██║██╔══██║██╔══██╗   ██║     ║");
        System.out.println("║    ╚██████╔╝██║        ██║   ██║██║ ╚═╝ ██║██║  ██║██║  ██║   ██║     ║");
        System.out.println("║     ╚═════╝ ╚═╝        ╚═╝   ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝     ║");
        System.out.println("║                                                                       ║");
        System.out.println("║               YOUR SMART SHOPPING MANAGEMENT SOLUTION                 ║");
        System.out.println("║                                                                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n                           Loading...");

        // Animated loading bar
        try {
            System.out.print("                           [");
            for (int i = 0; i < 30; i++) {
                System.out.print("█");
                Thread.sleep(50);
            }
            System.out.println("]");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          Welcome To OptiMart           ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static boolean showMainMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         MAIN MENU                  ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Browse Products (Guest)");
        System.out.println("4. Search Products");
        System.out.println("5. View Products (Sorted)");
        System.out.println("6. View Deals of the Day");
        System.out.println("7. Exit");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> handleLogin();
            case 2 -> handleSignup();
            case 3 -> browseProducts();
            case 4 -> handleSearch();
            case 5 -> sortProductsMenu();
            case 6 -> viewDeals();
            case 7 -> {
                return false;
            }
            default -> System.out.println("Invalid choice!");
        }

        return true;
    }

    private static boolean showAdminMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ADMIN DASHBOARD             ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. View All Products");
        System.out.println("5. Search Products");
        System.out.println("6. View Products (Sorted)");
        System.out.println("7. Manage Users (Promote/Demote)");
        System.out.println("8. View All Users");
        System.out.println("9. View All Orders");
        System.out.println("10. Update Order Status");
        System.out.println("11. Manage Deals");
        System.out.println("12. View Statistics");
        System.out.println("13. Change Password");
        System.out.println("14. Logout");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> addProduct();
            case 2 -> updateProduct();
            case 3 -> deleteProduct();
            case 4 -> productService.displayAllProducts();
            case 5 -> handleSearch();
            case 6 -> sortProductsMenu();
            case 7 -> manageUsers();
            case 8 -> viewAllUsers();
            case 9 -> viewAllOrders();
            case 10 -> updateOrderStatus();
            case 11 -> manageDeals();
            case 12 -> displayStatistics();
            case 13 -> changePassword();
            case 14 -> {
                userService.logout();
            }
            default -> System.out.println("Invalid choice!");
        }

        return true;
    }

    private static boolean showCustomerMenu() {
        Cart userCart = getCurrentUserCart();

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║       CUSTOMER DASHBOARD           ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Browse Products");
        System.out.println("2. Search Products");
        System.out.println("3. View Products (Sorted)");
        System.out.println("4. View Product Details");
        System.out.println("5. View Deals of the Day");
        System.out.println("6. Add to Cart");
        System.out.println("7. View Cart");
        System.out.println("8. Remove from Cart");
        System.out.println("9. Update Cart Quantity");
        System.out.println("10. Undo Cart Action");
        System.out.println("11. Redo Cart Action");
        System.out.println("12. Place Order");
        System.out.println("13. View My Orders");
        System.out.println("14. Track My Order");
        System.out.println("15. Clear Cart");
        System.out.println("16. Change Password");
        System.out.println("17. Logout");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> browseProducts();
            case 2 -> handleSearch();
            case 3 -> sortProductsMenu();
            case 4 -> viewProductDetails();
            case 5 -> viewDeals();
            case 6 -> addToCart(userCart);
            case 7 -> userCart.viewCart();
            case 8 -> removeFromCart(userCart);
            case 9 -> updateCartQuantity(userCart);
            case 10 -> userCart.undo(productMap);
            case 11 -> userCart.redo(productMap);
            case 12 -> placeOrder(userCart);
            case 13 -> viewMyOrders();
            case 14 -> trackMyOrder();
            case 15 -> userCart.clearCart();
            case 16 -> changePassword();
            case 17 -> {
                userService.logout();
            }
            default -> System.out.println("Invalid choice!");
        }

        return true;
    }

    private static void viewDeals() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        DEALS OF THE DAY            ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. View Top 5 Deals");
        System.out.println("2. View All Active Deals");
        System.out.println("3. Back");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> dealService.displayTopDeals(5);
            case 2 -> dealService.displayTopDeals(Integer.MAX_VALUE);
            case 3 -> {
                return;
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void trackMyOrder() {
        String currentUsername = userService.getCurrentUser().getUsername();

        if (orders.isEmpty()) {
            System.out.println("✗ No orders to track.");
            return;
        }

        // Show user's orders first
        System.out.println("\n=== YOUR ORDERS ===");
        boolean hasOrders = false;

        for (Order order : orders) {
            if (order.getUsername() != null && order.getUsername().equals(currentUsername)) {
                System.out.println(order.getOrderId() + " - " + order.getTimestamp() + " - " + order.getStatus());
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            System.out.println("You haven't placed any orders yet.");
            return;
        }

        System.out.print("\nEnter Order ID to track: ");
        String orderId = scanner.nextLine().trim().toUpperCase();

        // Verify it's the user's order
        Order order = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                if (o.getUsername().equals(currentUsername)) {
                    order = o;
                } else {
                    System.out.println("✗ This order doesn't belong to you.");
                    return;
                }
                break;
            }
        }

        if (order == null) {
            System.out.println("✗ Order not found.");
            return;
        }

        orderStatusService.trackOrder(orderId, orders);
    }

    private static void manageDeals() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║          MANAGE DEALS              ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Add New Deal");
        System.out.println("2. View All Deals");
        System.out.println("3. Remove Expired Deals");
        System.out.println("4. Back");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> addNewDeal();
            case 2 -> dealService.displayTopDeals(Integer.MAX_VALUE);
            case 3 -> dealService.removeExpiredDeals();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void addNewDeal() {
        System.out.println("\n=== ADD NEW DEAL ===");

        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim().toUpperCase();

        Product product = productService.findProductById(productId);
        if (product == null) {
            System.out.println("✗ Product not found");
            return;
        }

        System.out.println("Selected: " + product.getName() + " (Rs " + product.getPrice() + ")");

        System.out.print("Enter discount percentage (1-90): ");
        int discount = getIntInput();

        if (discount < 1 || discount > 90) {
            System.out.println("✗ Invalid discount percentage");
            return;
        }

        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine().trim();

        System.out.print("Enter stock limit for deal: ");
        int stockLimit = getIntInput();

        if (stockLimit > product.getStock()) {
            System.out.println("⚠ Warning: Stock limit (" + stockLimit + ") exceeds available stock (" + product.getStock() + ")");
            System.out.print("Continue anyway? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("✗ Deal creation cancelled");
                return;
            }
        }

        dealService.addDeal(product, discount, expiryDate, stockLimit);

        double discountedPrice = product.getPrice() - (product.getPrice() * discount / 100.0);
        System.out.println("\n✓ Deal created successfully!");
        System.out.println("   Original Price: Rs " + String.format("%.2f", product.getPrice()));
        System.out.println("   Discounted Price: Rs " + String.format("%.2f", discountedPrice));
        System.out.println("   You Save: Rs " + String.format("%.2f", product.getPrice() - discountedPrice));
    }

    private static void updateOrderStatus() {
        if (orders.isEmpty()) {
            System.out.println("✗ No orders to update");
            return;
        }

        System.out.println("\n=== UPDATE ORDER STATUS ===");

        // Show all orders
        System.out.println("\nRecent Orders:");
        System.out.println("─".repeat(70));
        int count = 0;
        for (int i = orders.size() - 1; i >= 0 && count < 10; i--, count++) {
            Order order = orders.get(i);
            System.out.printf("%-10s | %-15s | %-20s | %s%n",
                    order.getOrderId(),
                    order.getUsername(),
                    order.getTimestamp(),
                    order.getStatus());
        }
        System.out.println("─".repeat(70));

        System.out.print("\nEnter Order ID: ");
        String orderId = scanner.nextLine().trim().toUpperCase();

        // Find order
        Order order = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                order = o;
                break;
            }
        }

        if (order == null) {
            System.out.println("✗ Order not found");
            return;
        }

        // Show current status
        System.out.println("\nCurrent Status: " + order.getStatus());

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      SELECT NEW STATUS             ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Pending");
        System.out.println("2. Confirmed");
        System.out.println("3. Packed");
        System.out.println("4. Out for Delivery");
        System.out.println("5. Delivered");
        System.out.println("6. Cancelled");
        System.out.println("7. Auto-advance to next status");
        System.out.println("8. View order details");
        System.out.println("9. Cancel");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        String[] statuses = {"Pending", "Confirmed", "Packed", "Out for Delivery", "Delivered", "Cancelled"};

        if (choice >= 1 && choice <= 6) {
            boolean success = orderStatusService.updateOrderStatus(orderId, statuses[choice - 1], orders);
            if (success) {
                FileHandler.saveOrders(orders);
                System.out.println("\n✓ Order status updated successfully!");
            }
        } else if (choice == 7) {
            orderStatusService.advanceOrderStatus(orderId, orders);
            FileHandler.saveOrders(orders);
        } else if (choice == 8) {
            orderStatusService.trackOrder(orderId, orders);
        } else if (choice == 9) {
            System.out.println("✗ Update cancelled");
        } else {
            System.out.println("Invalid choice!");
        }
    }

    // ==================== PART 2====================

    private static Cart getCurrentUserCart() {
        String username = userService.getCurrentUser().getUsername();

        if (!userCarts.containsKey(username)) {
            Cart newCart = new Cart(dealService);
            userCarts.put(username, newCart);
        } else {
            Cart existingCart = userCarts.get(username);
            existingCart.setDealService(dealService);
        }

        return userCarts.get(username);
    }

    private static void sortProductsMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        SORT PRODUCTS               ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("1. Sort by Name (A-Z)");
        System.out.println("2. Sort by Price (Low to High)");
        System.out.println("3. Sort by Price (High to Low)");
        System.out.println("4. Back");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        LinkedList<Product> productsList = convertToLinkedList(FileHandler.loadProducts());

        switch (choice) {
            case 1 -> {
                LinkedList<Product> sorted = ProductSorter.sortByName(productsList);
                ProductSorter.displayProducts(sorted, "Name A-Z");
            }
            case 2 -> {
                LinkedList<Product> sorted = ProductSorter.sortByPriceLowToHigh(productsList);
                ProductSorter.displayProducts(sorted, "Price Low to High");
            }
            case 3 -> {
                LinkedList<Product> sorted = ProductSorter.sortByPriceHighToLow(productsList);
                ProductSorter.displayProducts(sorted, "Price High to Low");
            }
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void addToCart(Cart cart) {
        System.out.println("\n=== ADD TO CART ===");
        System.out.print("Enter Product ID (e.g., P001): ");
        String productId = scanner.nextLine().trim().toUpperCase();

        Product product = productMap.get(productId);
        if (product == null) {
            System.out.println("✗ Product not found");
            return;
        }

        System.out.println("\n✓ Selected: " + product.getName() + " - Rs " + product.getPrice());
        System.out.println("Available Stock: " + product.getStock());
        System.out.print("Enter quantity: ");
        int quantity = getIntInput();

        cart.addItem(product, quantity);
    }

    private static void removeFromCart(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("✗ Cart is empty!");
            return;
        }

        cart.viewCart();
        System.out.print("\nEnter Product ID to remove: ");
        String productId = scanner.nextLine().trim().toUpperCase();

        cart.removeItem(productId);
    }

    private static void updateCartQuantity(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("✗ Cart is empty!");
            return;
        }

        cart.viewCart();
        System.out.print("\nEnter Product ID: ");
        String productId = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter new quantity: ");
        int quantity = getIntInput();

        cart.updateQuantity(productId, quantity);
    }

    private static void placeOrder(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("✗ Your cart is empty!");
            return;
        }

        // Show cart
        cart.viewCart();

        // Confirm
        System.out.print("\n✓ Confirm order? (y/n): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("✗ Order cancelled.");
            return;
        }

        String username = userService.getCurrentUser().getUsername();
        LinkedList<CartItem> items = cart.getItems();

        System.out.println("\nValidating stock...");
        boolean allAvailable = true;

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);

            Product freshProduct = productService.findProductById(item.getProduct().getProductId());

            if (freshProduct == null) {
                System.out.println("✗ Product not found: " + item.getProduct().getName());
                allAvailable = false;
                break;
            }

            if (freshProduct.getStock() < item.getQuantity()) {
                System.out.println("✗ Insufficient stock for " + freshProduct.getName());
                System.out.println("   Requested: " + item.getQuantity() +
                        ", Available: " + freshProduct.getStock());
                allAvailable = false;
            }
        }

        if (!allAvailable) {
            System.out.println("\n✗ Order cannot be placed due to stock issues");
            System.out.println("Tip: Please update your cart or try again later");
            return;
        }

        System.out.println("✓ Stock validation passed");

        System.out.println("\nProcessing order...");

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = productService.findProductById(item.getProduct().getProductId());

            if (product != null) {
                int oldStock = product.getStock();
                boolean reduced = product.reduceStock(item.getQuantity());

                if (reduced) {
                    System.out.println("  ✓ " + product.getName() +
                            ": " + oldStock + " → " + product.getStock());

                    productMap.put(product.getProductId().toUpperCase(), product);
                }
            }
        }

        productService.saveProductsToFile();

        List<CartItem> orderItems = cart.getItemsAsList();
        Order order = new Order(username, orderItems);
        orders.add(order);

        FileHandler.saveOrders(orders);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✓ ORDER PLACED SUCCESSFULLY!");
        System.out.println("=".repeat(70));
        System.out.println(order.toString());

        cart.clearCart();

        System.out.println("✓ Cart has been cleared");
        System.out.println("\nYou can view your order in 'View My Orders' menu");
    }

    private static void viewMyOrders() {
        if (orders.isEmpty()) {
            System.out.println("✗ No orders yet.");
            return;
        }

        String currentUsername = userService.getCurrentUser().getUsername();

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         MY ORDERS                  ║");
        System.out.println("╚════════════════════════════════════╝");

        boolean hasOrders = false;
        int userOrderCount = 0;
        double totalSpent = 0;

        for (Order order : orders) {
            if (order.getUsername() != null && order.getUsername().equals(currentUsername)) {
                System.out.println(order.toString());
                hasOrders = true;
                userOrderCount++;
                totalSpent += order.getTotalAmount();
            }
        }

        if (!hasOrders) {
            System.out.println("No orders found.");
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("Your Order Summary:");
            System.out.println("Total Orders: " + userOrderCount);
            System.out.printf("Total Spent: Rs %.2f\n", totalSpent);
            System.out.println("=".repeat(70));
        }
    }

    private static void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("✗ No orders yet.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ALL ORDERS (ADMIN)          ║");
        System.out.println("╚════════════════════════════════════╝");

        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-12s | %-20s | %-25s | %-15s | %-12s%n",
                "ORDER ID", "CUSTOMER", "DATE", "STATUS", "AMOUNT");
        System.out.println("=".repeat(90));

        double totalRevenue = 0;

        for (Order order : orders) {
            System.out.printf("%-12s | %-20s | %-25s | %-15s | Rs %-10.2f%n",
                    order.getOrderId(),
                    order.getUsername() != null ? order.getUsername() : "Unknown",
                    order.getTimestamp(),
                    order.getStatus(),
                    order.getTotalAmount());
            totalRevenue += order.getTotalAmount();
        }

        System.out.println("=".repeat(90));
        System.out.println("\nTotal Orders: " + orders.size());
        System.out.printf("Total Revenue: Rs %.2f\n", totalRevenue);
        System.out.println("=".repeat(90));

        System.out.print("\nView order details? Enter Order ID (or press Enter to skip): ");
        String orderId = scanner.nextLine().trim().toUpperCase();

        if (!orderId.isEmpty()) {
            boolean found = false;
            for (Order order : orders) {
                if (order.getOrderId().equalsIgnoreCase(orderId)) {
                    System.out.println(order.toString());
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("✗ Order not found");
            }
        }
    }

    private static void saveAllData() {
        List<Product> productsList = FileHandler.loadProducts();
        FileHandler.saveProducts(productsList);

        FileHandler.saveOrders(orders);
    }

    private static LinkedList<Product> convertToLinkedList(List<Product> list) {
        LinkedList<Product> linkedList = new LinkedList<>();
        for (Product p : list) {
            linkedList.addLast(p);
        }
        return linkedList;
    }

    // ==================== PART 1 ====================

    private static void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        userService.login(username, password);
    }

    private static void handleSignup() {
        System.out.println("\n=== SIGNUP ===");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password (min 6 chars, letters + numbers): ");
        String password = scanner.nextLine();

        User newUser = userService.signup(username, password, email);

        if (newUser != null) {
            System.out.println("\n✓ Welcome to OptiMart!");
        }
    }

    private static void addProduct() {
        System.out.println("\n=== ADD PRODUCT ===");

        System.out.print("Product ID (e.g., P051): ");
        String productId = scanner.nextLine().trim();

        System.out.print("Product Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Price: ");
        double price = getDoubleInput();

        System.out.print("Stock: ");
        int stock = getIntInput();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        boolean success = productService.addProduct(productId, name, category, price, stock, description);

        // Update productMap if added successfully
        if (success) {
            Product newProduct = productService.findProductById(productId);
            if (newProduct != null) {
                productMap.put(productId.toUpperCase(), newProduct);
            }
        }
    }

    private static void updateProduct() {
        System.out.println("\n=== UPDATE PRODUCT ===");
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim();

        Product existing = productService.findProductById(productId);
        if (existing == null) {
            System.out.println("✗ Product not found");
            return;
        }

        System.out.println("Current: " + existing);
        System.out.println("\nEnter new details (press Enter to keep current):");

        System.out.print("Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existing.getName();

        System.out.print("Category [" + existing.getCategory() + "]: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) category = existing.getCategory();

        System.out.print("Price [" + existing.getPrice() + "]: ");
        String priceStr = scanner.nextLine().trim();
        double price = priceStr.isEmpty() ? existing.getPrice() : Double.parseDouble(priceStr);

        System.out.print("Stock [" + existing.getStock() + "]: ");
        String stockStr = scanner.nextLine().trim();
        int stock = stockStr.isEmpty() ? existing.getStock() : Integer.parseInt(stockStr);

        System.out.print("Description [" + existing.getDescription() + "]: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = existing.getDescription();

        productService.updateProduct(productId, name, category, price, stock, description);

        // Update productMap
        Product updated = productService.findProductById(productId);
        if (updated != null) {
            productMap.put(productId.toUpperCase(), updated);
        }
    }

    private static void deleteProduct() {
        System.out.println("\n=== DELETE PRODUCT ===");
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim();

        Product product = productService.findProductById(productId);
        if (product != null) {
            System.out.println("Product to delete: " + product);
            System.out.print("Are you sure? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("yes")) {
                productService.deleteProduct(productId);
                productMap.remove(productId.toUpperCase());
            } else {
                System.out.println("✗ Deletion cancelled");
            }
        }
    }

    private static void browseProducts() {
        productService.displayAllProducts();
    }

    private static void viewProductDetails() {
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim();

        Product product = productService.findProductById(productId);
        if (product != null) {
            System.out.println(product.toDetailedString());
        } else {
            System.out.println("✗ Product not found");
        }
    }

    private static void handleSearch() {
        System.out.println("\n=== SMART SEARCH (Powered by Trie) ===");
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {
            System.out.println("✗ Please enter a search term");
            return;
        }

        List<String> suggestions = productService.getTopSuggestions(query, 10);

        if (suggestions.isEmpty()) {
            System.out.println("✗ No products found matching '" + query + "'");
            return;
        }

        System.out.println("\n✓ Found " + suggestions.size() + " suggestions:");
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println((i + 1) + ". " + suggestions.get(i));
        }

        List<Product> products = productService.searchProducts(query);
        if (!products.isEmpty()) {
            System.out.println("\n=== Matching Products ===");
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }

    private static void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");
        System.out.print("Current Password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm New Password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("✗ Passwords don't match");
            return;
        }

        userService.changePassword(oldPassword, newPassword);
    }

    private static void manageUsers() {
        System.out.println("\n=== MANAGE USERS ===");
        System.out.println("1. Promote User to Admin");
        System.out.println("2. Demote Admin to Customer");
        System.out.println("3. Back");
        System.out.print("\nEnter choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter username to promote: ");
                String username = scanner.nextLine().trim();
                userService.promoteToAdmin(username);
            }
            case 2 -> {
                System.out.print("Enter username to demote: ");
                String username = scanner.nextLine().trim();
                userService.demoteToCustomer(username);
            }
            case 3 -> {
                return;
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("%-20s %-30s %-15s%n", "USERNAME", "EMAIL", "ROLE");
        System.out.println("=".repeat(70));

        for (User user : users) {
            System.out.printf("%-20s %-30s %-15s%n",
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().toUpperCase());
        }

        System.out.println("=".repeat(70));
        System.out.println("Total users: " + users.size());
    }

    private static void displayStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       SYSTEM STATISTICS                ║");
        System.out.println("╚════════════════════════════════════════╝");

        userService.displayStats();
        productService.displayStats();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       ORDER STATISTICS                 ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Total Orders: " + orders.size());

        double totalRevenue = 0;

        for (Order order : orders) {
            totalRevenue += order.getTotalAmount();
        }

        System.out.printf("Total Revenue: Rs %.2f\n", totalRevenue);
    }

    private static int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}