package com.optimart.GUI.Components;

import com.optimart.Services.ProductServices;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.function.Consumer;


public class AutocompleteSearchField extends TextField {

    private ProductServices productServices;
    private ContextMenu suggestionMenu;
    private String currentSuggestion = "";
    private Consumer<String> onSearchCallback;

    public AutocompleteSearchField(ProductServices productServices, Consumer<String> onSearchCallback) {
        super();
        this.productServices = productServices;
        this.onSearchCallback = onSearchCallback;
        this.suggestionMenu = new ContextMenu();

        setupAutocomplete();
    }

    private void setupAutocomplete() {
        // Listen for text changes to provide suggestions
        this.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                suggestionMenu.hide();
                currentSuggestion = "";
                return;
            }

            // Get suggestions from Trie
            List<String> suggestions = productServices.getTopSuggestions(newVal.trim(), 5);

            if (!suggestions.isEmpty()) {
                showSuggestions(suggestions, newVal);
                currentSuggestion = suggestions.get(0);
            } else {
                suggestionMenu.hide();
                currentSuggestion = "";
            }
        });

        // Handle keyboard events
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB && !currentSuggestion.isEmpty()) {
                // Complete with suggestion on TAB
                event.consume();
                this.setText(currentSuggestion);
                this.positionCaret(currentSuggestion.length());
                suggestionMenu.hide();

                // Trigger search
                if (onSearchCallback != null) {
                    onSearchCallback.accept(currentSuggestion);
                }
            } else if (event.getCode() == KeyCode.ENTER) {
                // Search on ENTER
                suggestionMenu.hide();
                if (onSearchCallback != null) {
                    onSearchCallback.accept(this.getText());
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Close suggestions on ESCAPE
                suggestionMenu.hide();
            }
        });
    }

    private void showSuggestions(List<String> suggestions, String currentText) {
        suggestionMenu.getItems().clear();

        for (String suggestion : suggestions) {
            // Create visual suggestion with highlighting
            TextFlow textFlow = createHighlightedSuggestion(suggestion, currentText);

            CustomMenuItem item = new CustomMenuItem(textFlow, true);
            item.setOnAction(e -> {
                this.setText(suggestion);
                this.positionCaret(suggestion.length());
                suggestionMenu.hide();

                if (onSearchCallback != null) {
                    onSearchCallback.accept(suggestion);
                }
            });

            suggestionMenu.getItems().add(item);
        }

        if (!suggestionMenu.isShowing()) {
            suggestionMenu.show(this, Side.BOTTOM, 0, 0);
        }
    }

    private TextFlow createHighlightedSuggestion(String fullText, String typedText) {
        TextFlow textFlow = new TextFlow();

        int matchIndex = fullText.toLowerCase().indexOf(typedText.toLowerCase());

        if (matchIndex >= 0) {
            // Text before match
            if (matchIndex > 0) {
                Text before = new Text(fullText.substring(0, matchIndex));
                before.setFont(Font.font("Arial", 14));
                before.setFill(Color.BLACK);
                textFlow.getChildren().add(before);
            }

            Text matched = new Text(fullText.substring(matchIndex, matchIndex + typedText.length()));
            matched.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
            matched.setFill(Color.BLACK);
            textFlow.getChildren().add(matched);

            if (matchIndex + typedText.length() < fullText.length()) {
                Text after = new Text(fullText.substring(matchIndex + typedText.length()));
                after.setFont(Font.font("Arial", 14));
                after.setFill(Color.GRAY);
                textFlow.getChildren().add(after);
            }
        } else {
            Text text = new Text(fullText);
            text.setFont(Font.font("Arial", 14));
            textFlow.getChildren().add(text);
        }

        return textFlow;
    }

    public void hideSuggestions() {
        suggestionMenu.hide();
    }
}