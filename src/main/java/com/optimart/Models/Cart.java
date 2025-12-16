package com.optimart.Models;

import com.optimart.Actions.CartAction;
import com.optimart.DataStructures.HashMap;
import com.optimart.DataStructures.LinkedList;
import com.optimart.DataStructures.Stack;
import com.optimart.Services.DealService;

import java.util.ArrayList;
import java.util.List;

public class Cart {
     LinkedList<CartItem> items;
     Stack<CartAction> undoStack;
     Stack<CartAction> redoStack;
     DealService dealService;

    public Cart() {
        this.items = new LinkedList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.dealService = null;
    }

    public Cart(DealService dealService) {
        this.items = new LinkedList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.dealService = dealService;
    }

    public void setDealService(DealService dealService) {
        this.dealService = dealService;
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive");
            return;
        }

        if (product.getStock() < quantity) {
            System.out.println("Insufficient stock! Available: " + product.getStock());
            return;
        }

        Deal activeDeal = null;
        double effectivePrice = product.getPrice();
        if (dealService != null) {
            activeDeal = dealService.getDealByProductId(product.getProductId());
            if (activeDeal != null) {
                effectivePrice = activeDeal.getDiscountedPrice();
                System.out.println("Deal Applied! " + activeDeal.getDiscountPercentage() + "% OFF");
                System.out.println("Original Price: Rs " + String.format("%.2f", product.getPrice()));
                System.out.println("Discounted Price: Rs " + String.format("%.2f", effectivePrice));
            }
        }

        CartItem existingItem = findItemByProductId(product.getProductId());

        if (existingItem != null) {
            int oldQuantity = existingItem.getQuantity();
            int newQuantity = oldQuantity + quantity;

            if (product.getStock() < newQuantity) {
                System.out.println("Cannot add more Only " + product.getStock() + " units available.");
                return;
            }

            existingItem.setQuantity(newQuantity);
            if (activeDeal != null) {
                existingItem.setEffectivePrice(effectivePrice);
            }
            undoStack.push(new CartAction(CartAction.ActionType.UPDATE_QUANTITY,
                    product.getProductId(), newQuantity, oldQuantity));
            redoStack.clear();
            System.out.println("Updated quantity of " + product.getName() + " in cart.");
        } else {
            CartItem newItem = new CartItem(product, quantity, effectivePrice);
            items.addLast(newItem);
            undoStack.push(new CartAction(CartAction.ActionType.ADD,
                    product.getProductId(), quantity));
            redoStack.clear();
            System.out.println("Added " + product.getName() + " to cart.");
        }
    }

    public void removeItem(String productId) {
        CartItem item = findItemByProductId(productId);

        if (item == null) {
            System.out.println("Product not found in cart");
            return;
        }

        items.removeFrom(item);
        undoStack.push(new CartAction(CartAction.ActionType.REMOVE,
                productId, item.getQuantity()));
        redoStack.clear();
        System.out.println(" Removed " + item.getProduct().getName() + " from cart.");
    }

    public void updateQuantity(String productId, int newQuantity) {
        if (newQuantity <= 0) {
            System.out.println(" Quantity must be positive");
            return;
        }

        CartItem item = findItemByProductId(productId);

        if (item == null) {
            System.out.println(" Product not found in cart");
            return;
        }

        if (item.getProduct().getStock() < newQuantity) {
            System.out.println("Insufficient stock! Available: " + item.getProduct().getStock());
            return;
        }

        int oldQuantity = item.getQuantity();
        item.setQuantity(newQuantity);
        undoStack.push(new CartAction(CartAction.ActionType.UPDATE_QUANTITY,
                productId, newQuantity, oldQuantity));
        redoStack.clear();
        System.out.println("Updated quantity to " + newQuantity);
    }

    public void undo(HashMap<String, Product> productMap) {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo");
            return;
        }

        CartAction action = undoStack.pop();
        redoStack.push(action);

        switch (action.getActionType()) {
            case ADD:
                CartItem itemToRemove = findItemByProductId(action.getProductId());
                if (itemToRemove != null) {
                    items.removeFrom(itemToRemove);
                    System.out.println("Undo: Removed " + itemToRemove.getProduct().getName());
                }
                break;

            case REMOVE:
                Product product = findProductById(productMap, action.getProductId());
                if (product != null) {
                    double effectivePrice = product.getPrice();
                    if (dealService != null) {
                        Deal deal = dealService.getDealByProductId(product.getProductId());
                        if (deal != null) {
                            effectivePrice = deal.getDiscountedPrice();
                        }
                    }
                    CartItem newItem = new CartItem(product, action.getQuantity(), effectivePrice);
                    items.addLast(newItem);
                    System.out.println("✓ Undo: Added back " + product.getName());
                }
                break;

            case UPDATE_QUANTITY:
                CartItem itemToUpdate = findItemByProductId(action.getProductId());
                if (itemToUpdate != null) {
                    itemToUpdate.setQuantity(action.getPreviousQuantity());
                    System.out.println("Undo: Restored quantity to " + action.getPreviousQuantity());
                }
                break;
        }
    }

    public void redo(HashMap<String, Product> productMap) {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }

        CartAction action = redoStack.pop();
        undoStack.push(action);

        switch (action.getActionType()) {
            case ADD:
                Product product = findProductById(productMap, action.getProductId());
                if (product != null) {
                    double effectivePrice = product.getPrice();
                    if (dealService != null) {
                        Deal deal = dealService.getDealByProductId(product.getProductId());
                        if (deal != null) {
                            effectivePrice = deal.getDiscountedPrice();
                        }
                    }
                    CartItem newItem = new CartItem(product, action.getQuantity(), effectivePrice);
                    items.addLast(newItem);
                    System.out.println("✓ Redo: Added " + product.getName());
                }
                break;

            case REMOVE:
                CartItem itemToRemove = findItemByProductId(action.getProductId());
                if (itemToRemove != null) {
                    items.removeFrom(itemToRemove);
                    System.out.println("Redo: Removed " + itemToRemove.getProduct().getName());
                }
                break;

            case UPDATE_QUANTITY:
                CartItem itemToUpdate = findItemByProductId(action.getProductId());
                if (itemToUpdate != null) {
                    itemToUpdate.setQuantity(action.getQuantity());
                    System.out.println("Redo: Updated quantity to " + action.getQuantity());
                }
                break;
        }
    }

    public List<CartItem> getItemsAsList() {
        List<CartItem> list = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            list.add(items.get(i));
        }
        return list;
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              SHOPPING CART                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Product Name              |  Id  | Qty | Unit Price     | Total");
        System.out.println("--------------------------------------------------------------------------");

        for (int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).toString());
        }


        System.out.println("==========================================================================");
        System.out.printf("CART TOTAL: Rs %.2f\n", getTotalPrice());
        System.out.println("==========================================================================\n");

    }

    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared");
    }

    public LinkedList<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private CartItem findItemByProductId(String productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getProductId().equals(productId)) {
                return items.get(i);
            }
        }
        return null;
    }

    private Product findProductById(HashMap<String, Product> productMap, String productId) {

        return productMap.get(productId.toUpperCase());
    }
}