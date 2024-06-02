package org.example.configuratoreauto.Utenti;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {
    UserModel u = UserModel.getInstance();
    @Test
    @DisplayName("Registrazione utenti")
    void registraCliente(){
        //Registro una mail già presente
        assertFalse(u.registraCliente(new Cliente("davide@gmail.com", "1234", "A", "A")));
        assertFalse(u.registraCliente(new Cliente("DaViDe@GmAiL.cOm", "1234", "A", "A")));

        //Registro una mail non ancora presente
        assertTrue(u.registraCliente(new Cliente("aaa@gmail.com", "1234", "a", "b")));

        // TODO: Registrazione con email non valide / nomi non validi;
        assertThrowsExactly(IllegalArgumentException.class, () -> new Cliente("@gmail.com", "1234", "a", "b"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Cliente("aaa@.com", "1234", "a", "b"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Cliente("aaa@gmail.com", "1234", "12adad12", "AA"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Cliente("aaa@gmail.com", "1234", "aa", "121dad29"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Cliente("aaa@gmail.com", "1234", "aa", "121dad29"));
        assertDoesNotThrow(() -> new Cliente("aaa@gmail.com", "1234", "ABc-D'efG", "Donà"));
    }

    @Test
    @DisplayName("Login")
    void validation() {
        assertThrowsExactly(IllegalArgumentException.class, () -> u.validation("", ""));
        assertFalse(u.validation(null, null));
        assertTrue(u.validation("segretario@gmail.com", "1234"));
        assertTrue(u.validation("davide@gmail.com", "1234"));
        assertTrue(u.validation("DaViDe@GmAiL.cOm", "1234"));

        //Login con password errate
        assertFalse(u.validation("davide@gmail.com", ""));
        assertFalse(u.validation("davide@gmail.com", "12"));
    }

    @Test
    @DisplayName("Current user check")
    void getCurrentUser() {
        assertTrue(u.validation("davide@gmail.com", "1234"));
        assertInstanceOf(Cliente.class, u.getCurrentUser());
        assertFalse(u.getCurrentUser() instanceof Impiegato);
        u.validation("segretario@gmail.com", "1234");
        assertInstanceOf(Segretario.class, u.getCurrentUser());

    }
    @Test
    @DisplayName("validEmail")
    void checkValidEmail(){
        assertFalse(Persona.isValidEmail(""));
        assertTrue(Persona.isValidEmail("davide@gmail.com"));
        assertFalse(Persona.isValidEmail("andrea@.com"));
        assertFalse(Persona.isValidEmail("@gmail.com"));
    }
}