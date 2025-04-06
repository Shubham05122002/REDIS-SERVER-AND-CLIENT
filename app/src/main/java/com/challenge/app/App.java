package com.challenge.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        Server server = new Server();
        server.initConnection();
        System.out.println("Server started");
        try {
            ServerSocket serverSocket = server.getServerSocket();
            // Continuously accept new client connections.
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " 
                        + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                // For each connection, start a new client handler thread.
                new Thread(new ClientHandler(clientSocket, server)).start();
            }
        } catch (IOException e) {
            System.err.println("Error while accepting connection: " + e.getMessage());
        }
    }
}
