package org.example.configuratoreauto.Utenti;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {
    UserModel u = UserModel.getInstance();
    @Test
    @DisplayName("Registrazione utenti")
    void registraCliente(){
        //Registro una mail non ancora presente
        assertTrue(u.registraCliente(new Cliente("aaa@gmail.com", "1234", "a", "b")));

        //Registro una mail già presente
        assertFalse(u.registraCliente(new Cliente("davide@gmail.com", "1234", "A", "A")));
        assertFalse(u.registraCliente(new Cliente("DaViDe@GmAiL.cOm", "1234", "A", "A")));
        assertFalse(u.registraCliente(new Cliente("segretario@GmAiL.cOm", "1234", "A", "A")));

        //Registrazione con email non valide / nomi non validi;
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

        u.registraCliente(new Cliente("aaa@gmail.com", "MArio", "a", "b"));
        assertFalse(u.validation("aaa@gmail.com", "Mario"));

        //Login con password errate
        assertFalse(u.validation("davide@gmail.com", ""));
        assertFalse(u.validation("davide@gmail.com", null));
        assertFalse(u.validation("davide@gmail.com", "12"));
    }

    @Test
    @DisplayName("Current user check")
    void getCurrentUser() {
        //LOGIN CLIENTE -> Current user è cliente
        assertTrue(u.validation("davide@gmail.com", "1234"));
        assertTrue(u.isCliente());
        assertFalse(u.isSegretario());
        assertFalse(u.isImpiegato());

        //LOGIN SEGRETARIO -> Current user è segretario
        u.validation("segretario@gmail.com", "1234");
        assertTrue(u.isSegretario());
        assertFalse(u.isImpiegato());
        assertFalse(u.isCliente());

        //LOGIN IMPIEGATO -> Current user è segretario
        u.validation("impiegato@gmail.com", "1234");
        assertFalse(u.isSegretario());
        assertTrue(u.isImpiegato());
        assertFalse(u.isCliente());

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