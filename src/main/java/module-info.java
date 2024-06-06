module org.example.configuratoreauto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;


    exports org.example.configuratoreauto;
    exports org.example.configuratoreauto.Utenti;
    opens org.example.configuratoreauto to javafx.fxml;
    exports org.example.configuratoreauto.Controllers;
    opens org.example.configuratoreauto.Controllers to javafx.fxml;
}