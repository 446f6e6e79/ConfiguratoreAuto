package org.example.configuratoreauto.Utenti;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {
    UserModel u = UserModel.getInstance();

    @Test
    @DisplayName("Registrazione utenti")
    void registraCliente(){
        Assertions.assertEquals(false, u.registraCliente(new Cliente("davidedona@gmail.com", "1234", "A", "A")));
        Assertions.assertEquals(true, u.registraCliente(new Cliente("aaa@gmail.com", "1234", "a", "b")));
    }

    @Test
    @DisplayName("Login")
    void validation() {
        Assertions.assertEquals(false, u.validation("", ""));
        Assertions.assertEquals(false, u.validation(null, null));
        Assertions.assertEquals(true, u.validation("segretario@gmail.com", "1234"));
        Assertions.assertEquals(true, u.validation("guest", "guest"));
    }

    @Test
    @DisplayName("Current user check")
    void getCurrentUser() {
        Assertions.assertEquals(true, u.validation("guest", "guest"));
        Assertions.assertEquals(true, u.getCurrentUser() instanceof Cliente);
        Assertions.assertEquals(false, u.getCurrentUser() instanceof Impiegato);
        u.validation("segretario@gmail.com", "1234");
        Assertions.assertEquals(true, u.getCurrentUser() instanceof Segretario);

    }
}