module com.example.back {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.back to javafx.fxml;
    exports com.example.back;
}