package com.optimart.GUI.Views;

import com.optimart.GUI.OptimartApp;
import com.optimart.GUI.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainMenuView extends ScrollPane {

        private final OptimartApp app;

        public MainMenuView(OptimartApp app) {
                this.app = app;

                // ---------- ROOT CONTENT ----------
                VBox root = new VBox(25);
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(30));
                root.setStyle("-fx-background-color: " + Theme.toHex(Theme.BACKGROUND_WHITE) + ";");

                // Make VBox resize properly inside ScrollPane
                root.minWidthProperty().bind(widthProperty());

                // ---------- LOGO ----------
                StackPane logo = createLogo();
                logo.setMaxSize(160, 160);

                // ---------- TITLES ----------
                Label welcome = new Label("Welcome to OptiMart");
                welcome.setFont(Font.font("Arial", FontWeight.BOLD, 28));
                welcome.setTextFill(Theme.AMAZON_NAVY);

                Label subtitle = new Label("Your One-Stop Shopping Destination");
                subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
                subtitle.setTextFill(Theme.TEXT_GRAY);

                // ---------- BUTTONS ----------
                Button loginBtn = createMenuButton("🔐 Login");
                Button signupBtn = createMenuButton("📝 Signup");
                Button browseGuestBtn = createMenuButton("🛍️ Browse Products (Guest)");
                Button searchBtn = createMenuButton("🔍 Search Products (Guest)");
                Button sortBtn = createMenuButton("📊 View Sorted Products (Guest)");
                Button viewDealsBtn = createMenuButton("🎉 View Deals of the Day");
                Button exitBtn = createMenuButton("❌ Exit");

                // ---------- MENU BOX ----------
                VBox menuBox = new VBox(15);
                menuBox.setAlignment(Pos.CENTER);
                menuBox.setPadding(new Insets(15));

                // Responsive width
                menuBox.maxWidthProperty().bind(root.widthProperty().multiply(0.7));

                menuBox.getChildren().addAll(
                        loginBtn,
                        signupBtn,
                        browseGuestBtn,
                        searchBtn,
                        sortBtn,
                        viewDealsBtn,
                        exitBtn
                );

                // ---------- ACTIONS ----------
                loginBtn.setOnAction(e -> app.showLoginView());
                signupBtn.setOnAction(e -> app.showSignupView());
                browseGuestBtn.setOnAction(e -> app.showHomeView(true));
                searchBtn.setOnAction(e -> app.showSearch(true));
                sortBtn.setOnAction(e -> app.showProductSort(true));
                viewDealsBtn.setOnAction(e -> app.showDealsView(true));
                exitBtn.setOnAction(e -> System.exit(0));

                // ---------- ADD ALL ----------
                root.getChildren().addAll(logo, welcome, subtitle, menuBox);

                // ---------- SCROLLPANE SETTINGS ----------
                setContent(root);
                setFitToWidth(true);
                setFitToHeight(true);
                setHbarPolicy(ScrollBarPolicy.NEVER);
                setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
                setStyle("-fx-background-color: transparent;");
        }

        // ---------- LOGO ----------
        private StackPane createLogo() {
                StackPane logoPane = new StackPane();

                Circle bgCircle = new Circle(55);
                bgCircle.setFill(Theme.AMAZON_NAVY);

                Rectangle cart1 = new Rectangle(44, 6);
                cart1.setFill(Theme.AMAZON_YELLOW);
                cart1.setTranslateY(-10);

                Rectangle cart2 = new Rectangle(6, 26);
                cart2.setFill(Theme.AMAZON_YELLOW);
                cart2.setTranslateX(-16);
                cart2.setTranslateY(6);

                Rectangle cart3 = new Rectangle(6, 26);
                cart3.setFill(Theme.AMAZON_YELLOW);
                cart3.setTranslateX(16);
                cart3.setTranslateY(6);

                Circle wheel1 = new Circle(5);
                wheel1.setFill(Theme.AMAZON_YELLOW);
                wheel1.setTranslateX(-12);
                wheel1.setTranslateY(22);

                Circle wheel2 = new Circle(5);
                wheel2.setFill(Theme.AMAZON_YELLOW);
                wheel2.setTranslateX(12);
                wheel2.setTranslateY(22);

                logoPane.getChildren().addAll(bgCircle, cart1, cart2, cart3, wheel1, wheel2);
                return logoPane;
        }

        // ---------- BUTTON ----------
        private Button createMenuButton(String text) {
                Button btn = new Button(text);
                btn.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
                btn.setTextFill(Color.WHITE);

                btn.setPrefWidth(320);
                btn.setMaxWidth(320);
                btn.setPrefHeight(55);

                btn.setStyle(
                        "-fx-background-color: " + Theme.toHex(Theme.AMAZON_NAVY) + ";" +
                                "-fx-background-radius: 14;" +
                                "-fx-padding: 12 25;" +
                                "-fx-cursor: hand;" +
                                "-fx-alignment: CENTER_LEFT;"
                );

                btn.setOnMouseEntered(e ->
                        btn.setStyle(
                                "-fx-background-color: #FF9900;" +
                                        "-fx-background-radius: 14;" +
                                        "-fx-padding: 12 25;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-alignment: CENTER_LEFT;"
                        )
                );

                btn.setOnMouseExited(e ->
                        btn.setStyle(
                                "-fx-background-color: " + Theme.toHex(Theme.AMAZON_NAVY) + ";" +
                                        "-fx-background-radius: 14;" +
                                        "-fx-padding: 12 25;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-alignment: CENTER_LEFT;"
                        )
                );

                return btn;
        }
}
