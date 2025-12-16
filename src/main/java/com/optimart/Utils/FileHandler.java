package com.optimart.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.optimart.Models.Deal;
import com.optimart.Models.Order;
import com.optimart.Models.Product;
import com.optimart.Models.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.json";
    private static final String PRODUCTS_FILE = DATA_DIR + "products.json";
    private static final String ORDERS_FILE = DATA_DIR + "orders.json";
    private static final String DEALS_FILE = DATA_DIR + "deals.json";


    static {
        // Create data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** ======================== USERS ======================== */

    public static void saveUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
            System.out.println("✓ Users saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        File file = new File(USERS_FILE);

        if (!file.exists() || file.length() == 0) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                System.err.println("Error initializing users file: " + e.getMessage());
            }
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** ======================== PRODUCTS ======================== */

    public static void saveProducts(List<Product> products) {
        try (FileWriter writer = new FileWriter(PRODUCTS_FILE)) {
            gson.toJson(products, writer);
            System.out.println("✓ Products saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public static List<Product> loadProducts() {
        File file = new File(PRODUCTS_FILE);

        // If file doesn't exist or is empty, initialize with empty array
        if (!file.exists() || file.length() == 0) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                System.err.println("Error initializing products file: " + e.getMessage());
            }
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Product>>() {}.getType();
            List<Product> products = gson.fromJson(reader, listType);
            return products != null ? products : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** ======================== ORDERS ======================== **/

    public static void saveOrders(List<Order> orders) {
        try (FileWriter writer = new FileWriter(ORDERS_FILE)) {
            gson.toJson(orders, writer);
            System.out.println("✓ Orders saved to " + ORDERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    public static List<Order> loadOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            System.out.println("No existing orders file found. Starting fresh.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(ORDERS_FILE)) {
            Type listType = new TypeToken<ArrayList<Order>>(){}.getType();
            List<Order> orders = gson.fromJson(reader, listType);

            if (orders != null) {
                for (Order order : orders) {
                    Order.updateOrderCounter(order.getOrderId());
                }

                System.out.println("✓ Loaded " + orders.size() + " orders");
                return orders;
            }
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** ======================== Deals ======================== **/
    public static void saveDeals(List<Deal> deals) {
        try (FileWriter writer = new FileWriter(DEALS_FILE)) {
            gson.toJson(deals, writer);
            System.out.println("✓ Deals saved to " + DEALS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving deals: " + e.getMessage());
        }
    }

    public static List<Deal> loadDeals() {
        File file = new File(DEALS_FILE);
        if (!file.exists()) {
            System.out.println("No existing deals file. Starting fresh.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(DEALS_FILE)) {
            Type listType = new TypeToken<ArrayList<Deal>>(){}.getType();
            List<Deal> deals = gson.fromJson(reader, listType);

            if (deals != null) {
                System.out.println("✓ Loaded " + deals.size() + " deals from file");
                return deals;
            }
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading deals: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    /** ======================== BACKUP ======================== */

    public static void createBackup () {
        try {
            copyFile(USERS_FILE, USERS_FILE + ".backup");
            copyFile(PRODUCTS_FILE, PRODUCTS_FILE + ".backup");
            copyFile(ORDERS_FILE, ORDERS_FILE + ".backup");
            copyFile(DEALS_FILE, DEALS_FILE + ".backup");
            System.out.println("✓ Backup created successfully");
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }

    private static void copyFile(String source, String dest) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) return;

        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
