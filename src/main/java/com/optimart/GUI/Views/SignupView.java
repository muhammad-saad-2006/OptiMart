package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Models.User;
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

public class SignupView extends StackPane {  // Changed from VBox to StackPane

    private OptimartApp app;
    private UserServices userServices;

    public SignupView(OptimartApp app, UserServices userServices) {
        this.app = app;
        this.userServices = userServices;

        // Full-screen background
        this.setStyle("-fx-background-color: " + toHex(Theme.BACKGROUND_WHITE) + ";");

        // Main centered card
        VBox centerBox = new VBox(35);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(450);
        centerBox.setPadding(new Insets(50));
        centerBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 30, 0.2, 0, 10);"
        );

        // Title
        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Theme.AMAZON_NAVY);

        // Form fields
        VBox form = new VBox(18);
        form.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label("Your name");
        nameLabel.setFont(getFontOrFallback(Theme.FONT_MEDIUM, 16));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setMaxWidth(350);
        nameField.setPrefHeight(45);

        Label emailLabel = new Label("Email");
        emailLabel.setFont(getFontOrFallback(Theme.FONT_MEDIUM, 16));
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(350);
        emailField.setPrefHeight(45);

        Label passLabel = new Label("Password");
        passLabel.setFont(getFontOrFallback(Theme.FONT_MEDIUM, 16));
        PasswordField passField = new PasswordField();
        passField.setPromptText("At least 6 characters");
        passField.setMaxWidth(350);
        passField.setPrefHeight(45);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(350);
        errorLabel.setMinHeight(30);

        Button signupBtn = new Button("Create your OptiMart account");
        signupBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        signupBtn.setMaxWidth(350);
        signupBtn.setPrefHeight(50);
        signupBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Signup logic
        signupBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields");
                errorLabel.setVisible(true);
                return;
            }

            if (password.length() < 6) {
                errorLabel.setText("Password must be at least 6 characters");
                errorLabel.setVisible(true);
                return;
            }

            User user = userServices.signup(name, password, email);
            if (user != null) {
                System.out.println("Signup successful: " + user.getUsername());
                app.showCustomerDashboard();
            } else {
                errorLabel.setText("Registration failed. Username/email may already exist.");
                errorLabel.setVisible(true);
            }
        });

        form.getChildren().addAll(
                nameLabel, nameField,
                emailLabel, emailField,
                passLabel, passField,
                errorLabel,
                signupBtn
        );

        // Sign in link
        Button signInBtn = new Button("Already have an account? Sign in");
        signInBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #0066c0; " +
                        "-fx-font-size: 15px; " +
                        "-fx-underline: true; " +
                        "-fx-cursor: hand;"
        );
        signInBtn.setOnAction(e -> app.showLoginView());

        // Assemble everything into centerBox
        centerBox.getChildren().addAll(
                title,
                form,
                signInBtn
        );

        // Perfect centering
        this.getChildren().add(centerBox);
        StackPane.setAlignment(centerBox, Pos.CENTER);

        // Safe margin for small windows
        this.setPadding(new Insets(20));
    }

    // Helper to safely get font from Theme (in case not defined yet)
    private Font getFontOrFallback(Font themeFont, double fallbackSize) {
        return themeFont != null ? themeFont : Font.font("Arial", FontWeight.NORMAL, fallbackSize);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}