package com.optimart.Actions;

public class CartAction {
    public enum ActionType {
        ADD, REMOVE, UPDATE_QUANTITY
    }

    private ActionType actionType;
    private String productId;
    private int quantity;
    private int previousQuantity;

    public CartAction(ActionType actionType, String productId, int quantity) {
        this.actionType = actionType;
        this.productId = productId;
        this.quantity = quantity;
        this.previousQuantity = 0;
    }

    public CartAction(ActionType actionType, String productId, int quantity, int previousQuantity) {
        this.actionType = actionType;
        this.productId = productId;
        this.quantity = quantity;
        this.previousQuantity = previousQuantity;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPreviousQuantity() {
        return previousQuantity;
    }
}

