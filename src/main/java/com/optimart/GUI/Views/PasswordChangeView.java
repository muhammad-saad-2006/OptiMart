package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import com.optimart.Services.UserServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PasswordChangeView extends VBox {

    private OptimartApp app;
    private UserServices userServices;

    public PasswordChangeView(OptimartApp app, UserServices userServices) {
        this.app = app;
        this.userServices = userServices;
        initialize();
    }

    private void initialize() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

        Label title = new Label("Change Password");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Theme.AMAZON_NAVY);

        VBox form = new VBox(15);
        form.setMaxWidth(400);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 4;");

        Label oldPassLabel = new Label("Current Password");
        PasswordField oldPassField = new PasswordField();

        Label newPassLabel = new Label("New Password");
        PasswordField newPassField = new PasswordField();

        Label confirmPassLabel = new Label("Confirm New Password");
        PasswordField confirmPassField = new PasswordField();

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        Button changeBtn = new Button("Change Password");
        changeBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        changeBtn.setMaxWidth(Double.MAX_VALUE);

        changeBtn.setOnAction(e -> {
            String oldPass = oldPassField.getText();
            String newPass = newPassField.getText();
            String confirmPass = confirmPassField.getText();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                errorLabel.setText("Please fill all fields");
                errorLabel.setVisible(true);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                errorLabel.setText("New passwords don't match");
                errorLabel.setVisible(true);
                return;
            }

            boolean success = userServices.changePassword(oldPass, newPass);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Password changed successfully!");
                alert.showAndWait();
                app.showCustomerDashboard();
            } else {
                errorLabel.setText("Current password is incorrect");
                errorLabel.setVisible(true);
            }
        });

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle(Theme.YELLOW_BUTTON_STYLE);
        backBtn.setOnAction(e -> app.showCustomerDashboard());

        form.getChildren().addAll(
                oldPassLabel, oldPassField,
                newPassLabel, newPassField,
                confirmPassLabel, confirmPassField,
                errorLabel, changeBtn);

        this.getChildren().addAll(title, form, backBtn);
    }
}
