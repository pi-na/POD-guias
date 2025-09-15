package ar.edu.itba.pod.socket.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GenericSocketClientTest {

    public static final int PORT = 6666;
    private GenericSocketClient client;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        client = new GenericSocketClient();
        client.startConnection("127.0.0.1", PORT);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws IOException {
        client.stopConnection();
    }

    @Test
    public final void test() throws IOException {

        //client.sendMessage() devuelve el counter
        assertEquals("1", client.sendMessage("1")); // Como manda "1", aumenta el counter de 0 a 1
        assertEquals("2", client.sendMessage("1")); // Aumenta el counter de 1 a 2

        assertEquals("2", client.sendMessage("2")); // No aumenta el counter!
        assertEquals("2", client.sendMessage(".")); // Envia el punto y se cierra el server

    }
}