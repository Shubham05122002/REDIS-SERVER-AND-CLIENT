package com.challenge.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            dataIn = new DataInputStream(clientSocket.getInputStream());
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error creating streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
           server.communicate(dataIn, dataOut, clientSocket);
        } catch (IOException e) {
            System.err.println("Connection error with client " 
                               + clientSocket.getInetAddress() + ": " + e.getMessage());
        } finally {
            try {
                server.closeResources(dataIn, dataOut);
                System.out.println("Connection closed for client " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
