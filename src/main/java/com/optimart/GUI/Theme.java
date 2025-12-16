package com.optimart.GUI;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Theme {
    // Amazon Brand Colors
    public static final Color AMAZON_NAVY = Color.web("#131921"); // Top bar background
    public static final Color AMAZON_LIGHT_NAVY = Color.web("#232F3E");
    public static final Color AMAZON_YELLOW = Color.web("#FF9900"); // Actions / Highlights
    public static final Color AMAZON_ORANGE = Color.web("#FA8900");
    public static final Color BACKGROUND_WHITE = Color.web("#EAEDED"); // App background
    public static final Color CARD_WHITE = Color.web("#FFFFFF");
    public static final Color TEXT_BLACK = Color.web("#0F1111"); // Primary text
    public static final Color TEXT_GRAY = Color.web("#565959"); // Secondary text
    public static final Color PRICE_RED = Color.web("#B12704"); // Price text

    public static final Color PRIMARY_COLOR = AMAZON_YELLOW;

    // Fonts
    public static final Font FONT_HEADER = Font.font("Arial", FontWeight.BOLD, 18);
    public static final Font FONT_TITLE = Font.font("Arial", FontWeight.NORMAL, 16);
    public static final Font FONT_PRICE = Font.font("Arial", FontWeight.NORMAL, 18);
    public static final Font FONT_PRICE_SYMBOL = Font.font("Arial", FontWeight.NORMAL, 12);
    public static final Font FONT_SMALL = Font.font("Arial", FontWeight.NORMAL, 12);
    public static final Font FONT_MEDIUM = Font.font("Arial", FontWeight.NORMAL, 16);
    public static final Font FONT_REGULAR = Font.font("Arial", FontWeight.NORMAL, 14);

    // Styles
    public static String NAV_BAR_STYLE = "-fx-background-color: #131921; -fx-padding: 10 20;";
    public static String SEARCH_BOX_STYLE = "-fx-background-color: white; -fx-background-radius: 4;";
    public static String YELLOW_BUTTON_STYLE = "-fx-background-color: #FF9900; " +
            "-fx-text-fill: black; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 5 15; " +
            "-fx-cursor: hand;";

    public static String YELLOW_BUTTON_HOVER_STYLE = "-fx-background-color: #FA8900; " +
            "-fx-text-fill: black; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 5 15; " +
            "-fx-cursor: hand;";

    // Effects
    public static DropShadow getCardShadow() {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(0, 0, 0, 0.1));
        ds.setRadius(5);
        ds.setOffsetY(2);
        return ds;
    }

    public static Background getBackground(Color color) {
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }

    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
