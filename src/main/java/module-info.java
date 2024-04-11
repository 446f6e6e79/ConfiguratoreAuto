module org.example.configuratoreauto {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.configuratoreauto to javafx.fxml;
    exports org.example.configuratoreauto;
}