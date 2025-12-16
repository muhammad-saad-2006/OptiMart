package com.optimart.Services;

import com.optimart.DataStructures.HashMap;
import com.optimart.DataStructures.Trie;
import com.optimart.Models.Product;
import com.optimart.Utils.FileHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductServices {
    private HashMap<String, Product> productMap;
    private Trie productTrie;

    public ProductServices() {
        productMap = new HashMap<>();
        productTrie = new Trie();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> loadedProducts = FileHandler.loadProducts();

        for (Product product : loadedProducts) {
            productMap.put(product.getProductId(), product);
            productTrie.insert(product.getName());
        }

        System.out.println("✓ Loaded " + loadedProducts.size() + " products");
    }

    private void saveProducts() {
        List<Product> products = new ArrayList<>();
        for (String productId : productMap.keySet()) {
            products.add(productMap.get(productId));
        }
        FileHandler.saveProducts(products);
    }

    public boolean addProduct(String productId, String name, String category, double price, int stock, String description) {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("✗ Product ID cannot be empty");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("✗ Product name cannot be empty");
            return false;
        }

        if (price < 0 || stock < 0) {
            System.out.println("✗ Price and stock must be non-negative");
            return false;
        }

        if (productMap.containsKey(productId)) {
            System.out.println("✗ Product ID already exists");
            return false;
        }

        Product product = new Product(productId, name, category, price, stock, description);

        productMap.put(productId, product);

        productTrie.insert(name);

        saveProducts();
        System.out.println("✓ Product added successfully with ID: " + productId);
        return true;
    }

    public Product findProductById(String productId) {
        return productMap.get(productId);
    }

    public boolean updateProduct(String productId, String name, String category, double price, int stock, String description) {
        Product product = productMap.get(productId);

        if (product == null) {
            System.out.println("✗ Product not found");
            return false;
        }

        if (!product.getName().equals(name)) {
            productTrie.delete(product.getName());
            productTrie.insert(name);
        }

        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setDescription(description);

        productMap.put(productId, product);

        saveProducts();
        System.out.println("✓ Product updated successfully");
        return true;
    }

    public boolean deleteProduct(String productId) {
        Product product = productMap.get(productId);

        if (product == null) {
            System.out.println("✗ Product not found");
            return false;
        }

        productTrie.delete(product.getName());

        productMap.remove(productId);

        saveProducts();
        System.out.println("✓ Product deleted successfully");
        return true;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        for (String productId : productMap.keySet()) {
            products.add(productMap.get(productId));
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (String productId : productMap.keySet()) {
            Product product = productMap.get(productId);
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<String> autocomplete(String prefix) {
        return productTrie.autocomplete(prefix);
    }

    public List<String> getTopSuggestions(String prefix, int limit) {
        return productTrie.getTopSuggestions(prefix, limit);
    }

    public List<Product> searchProducts(String query) {
        List<String> names = productTrie.autocomplete(query);
        List<Product> results = new ArrayList<>();

        for (String name : names) {
            for (String productId : productMap.keySet()) {
                Product product = productMap.get(productId);
                if (product.getName().equalsIgnoreCase(name)) {
                    results.add(product);
                    break;
                }
            }
        }

        return results;
    }

    public void saveProductsToFile() {
        List<Product> products = getAllProducts();
        FileHandler.saveProducts(products);
        System.out.println("✓ Product inventory updated");
    }

    public void displayStats() {
        System.out.println("\n=== Product Service Statistics ===");
        System.out.println("Total products: " + productMap.size());
        productMap.displayStats();
        productTrie.displayStats();
    }

    public void displayAllProducts() {
        List<Product> products = getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products available");
            return;
        }

        products.sort(Comparator.comparing(Product::getProductId));

        System.out.println("\n=== ALL PRODUCTS ===");
        System.out.println("ID         | Name                      | Category        | Price          | Stock");
        System.out.println("----------------------------------------------------------------------------------------");

        for (Product product : products) {
            System.out.println(product.toDisplayString());
        }
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Total Products: " + products.size());
    }
}