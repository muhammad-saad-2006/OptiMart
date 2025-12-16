package com.optimart.Services;

import com.optimart.Models.Deal;
import com.optimart.Models.Product;
import com.optimart.Utils.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class DealService {

    private List<Deal> allDeals;

    public DealService() {
        allDeals = new ArrayList<>();
        loadDeals();
    }

    private void loadDeals() {
        allDeals = FileHandler.loadDeals();

        int activeCount = 0;
        for (Deal deal : allDeals) {
            if (!deal.isExpired()) {
                activeCount++;
            }
        }

        System.out.println("✓ Loaded " + allDeals.size() + " deals (" + activeCount + " active)");
    }

    public void addDeal(Product product, int discountPercentage, String expiryDate, int stockLimit) {
        Deal deal = new Deal(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                discountPercentage,
                expiryDate,
                stockLimit
        );

        allDeals.add(deal);
        saveDeals();

        System.out.println("✓ Deal added: " + discountPercentage + "% off on " + product.getName());
    }

    public List<Deal> getTopDeals(int limit) {
        List<Deal> topDeals = new ArrayList<>();

        for (Deal deal : allDeals) {
            if (!deal.isExpired()) {
                topDeals.add(deal);
            }
        }

        // Sort by highest discount
        topDeals.sort((d1, d2) -> d2.getDiscountPercentage() - d1.getDiscountPercentage());

        int count = Math.min(limit, topDeals.size());
        return topDeals.subList(0, count);
    }

    public Deal getDealByProductId(String productId) {
        for (Deal deal : allDeals) {
            if (deal.getProductId().equalsIgnoreCase(productId) && !deal.isExpired()) {
                return deal;
            }
        }
        return null;
    }

    public void displayTopDeals(int limit) {
        List<Deal> topDeals = getTopDeals(limit);

        if (topDeals.isEmpty()) {
            System.out.println("No active deals available.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             DEALS OF THE DAY                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Product ID | Product Name              | Original → Discounted       | Discount | Expires");
        System.out.println("─".repeat(95));

        for (Deal deal : topDeals) {
            System.out.println(deal.toString());
        }

        System.out.println("─".repeat(95));
    }

    public void removeExpiredDeals() {
        List<Deal> activeDeals = new ArrayList<>();

        for (Deal deal : allDeals) {
            if (!deal.isExpired()) {
                activeDeals.add(deal);
            }
        }

        int removed = allDeals.size() - activeDeals.size();
        allDeals = activeDeals;

        saveDeals();

        System.out.println("✓ Removed " + removed + " expired deals");
    }

    private void saveDeals() {
        FileHandler.saveDeals(allDeals);
    }
}