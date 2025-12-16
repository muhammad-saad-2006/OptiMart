package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class SimpleMessageView extends VBox {

    public SimpleMessageView(OptimartApp app, String titleText, String messageText) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label(titleText);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        Label message = new Label(messageText);
        message.setFont(Theme.FONT_TITLE);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setWrapText(true);

        Button backBtn = new Button("Back");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard()); // Default return

        this.getChildren().addAll(title, message, backBtn);
    }
}
