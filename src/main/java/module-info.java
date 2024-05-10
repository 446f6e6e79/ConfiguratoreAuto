module org.example.configuratoreauto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    exports org.example.configuratoreauto;
    exports org.example.configuratoreauto.Utenti;
    opens org.example.configuratoreauto.Utenti to com.google.gson;
    opens org.example.configuratoreauto to com.google.gson, javafx.fxml;
    exports org.example.configuratoreauto.Controllers;
    opens org.example.configuratoreauto.Controllers to com.google.gson, javafx.fxml;
}