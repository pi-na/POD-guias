package ar.edu.itba.pod.socket.server;

import ar.edu.itba.pod.socket.service.GenericService;
import ar.edu.itba.pod.socket.service.GenericServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GenericSocketServer implements Closeable {
    private static Logger logger = LoggerFactory.getLogger(GenericSocketServer.class);

    public static final int PORT = 6666;
    private int visitCount = 0;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    GenericService genericService = new GenericServiceImpl();

    public void start(int port) throws IOException {
        logger.info("starting server on port {}", port);
        boolean loop = true;

        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();   // Me quedo bloqueado en accept hasta que un cliente solicite una conexion
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while (loop && (inputLine = in.readLine()) != null) {   //readLine() es bloqueante
            /*
            * Si me envian un punto cierro el loop
            * Si me envian un "1" aumento el counter
            */
            logger.debug("received message {}", inputLine);
            if (".".equals(inputLine)) {
                loop = false;
            } else if ("1".equals(inputLine)) {
//                ++visitCount;
                genericService.addVisit();
            }
            out.println(genericService.getVisitCount());
        }
    }

    @Override
    public void close() throws IOException {
        // Faltan validaciones de que nada de esto sea null!
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        /*
        * Bloque try with resources: si instanciamos uno o mas elementos dentro del try, y son 'Closeable', al salir
        * del bloque try, se encarga de hacer close() y controlar los null.
        * */
        try (GenericSocketServer server = new GenericSocketServer()) {
            server.start(PORT);
        } // Como cualquier bloque try, si no pongo catch las excepciones que haya se tiran para arriba
    }

}