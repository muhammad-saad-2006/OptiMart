package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView extends StackPane {

    private OptimartApp app;
    private UserServices userServices;

    public LoginView(OptimartApp app, UserServices userServices) {
        this.app = app;
        this.userServices = userServices;

        setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND_WHITE) + ";");

        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(420);
        centerBox.setPadding(new Insets(50));
        centerBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 30, 0.2, 0, 10);"
        );

        // Title
        Label title = new Label("Sign in");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Form container
        VBox form = new VBox(18);
        form.setAlignment(Pos.CENTER_LEFT);

        Label userLabel = new Label("Username");
        userLabel.setFont(Theme.FONT_MEDIUM != null ? Theme.FONT_MEDIUM : Font.font("Arial", 16));
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        userField.setMaxWidth(320);
        userField.setPrefHeight(45);

        Label passLabel = new Label("Password");
        passLabel.setFont(Theme.FONT_MEDIUM != null ? Theme.FONT_MEDIUM : Font.font("Arial", 16));
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setMaxWidth(320);
        passField.setPrefHeight(45);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(320);

        Button loginBtn = new Button("Sign In");
        loginBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        loginBtn.setMaxWidth(320);
        loginBtn.setPrefHeight(50);
        loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                errorLabel.setVisible(true);
                return;
            }

            if (userServices.login(username, password)) {
                System.out.println("Login Success");
                if (userServices.isAdmin()) {
                    app.showAdminDashboard();
                } else {
                    app.showCustomerDashboard();
                }
            } else {
                errorLabel.setText("Invalid username or password");
                errorLabel.setVisible(true);
            }
        });

        form.getChildren().addAll(
                userLabel, userField,
                passLabel, passField,
                errorLabel,
                loginBtn
        );

        // New to OptiMart section
        Label newToLabel = new Label("New to OptiMart?");
        newToLabel.setTextFill(Theme.TEXT_GRAY);
        newToLabel.setFont(Theme.FONT_SMALL != null ? Theme.FONT_SMALL : Font.font("Arial", 14));

        Button createAccountBtn = new Button("Create your OptiMart account");
        createAccountBtn.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #adb1b8; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 12 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px;"
        );
        createAccountBtn.setMaxWidth(320);
        createAccountBtn.setPrefHeight(50);
        createAccountBtn.setOnAction(e -> app.showSignupView());

        // CRITICAL: Add everything to centerBox in correct order
        centerBox.getChildren().addAll(
                title,
                form,           // ← This was missing before!
                newToLabel,     // ← This was missing before!
                createAccountBtn
        );

        // Add centerBox to StackPane and center it perfectly
        this.getChildren().add(centerBox);
        StackPane.setAlignment(centerBox, Pos.CENTER);

        // Optional: slight margin on very small windows
        this.setPadding(new Insets(20));
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}