module org.example.configuratoreauto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.example.configuratoreauto to javafx.fxml;
    exports org.example.configuratoreauto;
    exports org.example.configuratoreauto.Utenti;
    opens org.example.configuratoreauto.Utenti to com.google.gson;
}