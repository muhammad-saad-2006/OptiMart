module com.optimart {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;



    opens com.optimart.GUI to javafx.fxml;
    opens com.optimart.Models to com.google.gson;
    opens com.optimart.DataStructures to com.google.gson;


    exports com.optimart.DataStructures;
    exports com.optimart.Models;
    exports com.optimart.Utils;
    exports com.optimart.Services;
    exports com.optimart.GUI;
}