module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires tools.jackson.databind;
    requires io.github.cdimascio.dotenv.java;

    opens com.example to javafx.fxml;
    exports com.example;
}