package com.challenge.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final int PORT = 6379;
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, Token> serverDictionary;

    public void initConnection() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverDictionary = new ConcurrentHashMap<>();
        } catch (Exception e) {
            System.err.println("Some error occured: " + e);
            System.exit(1);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public String operate(String clientMessage) {
        ArrayList<String> words = Utils.tokenizer(clientMessage);

        if (words.isEmpty()) {
            return "NO RESPONSE";
        }

        String command = words.get(0);

        switch (command) {
            case "PING":
                return "PONG";
            case "QUIT":
                return "QUIT";
            case "ECHO":
                return words.get(1);
            case "SET":
                return Utils.processSet(words, serverDictionary);
            case "GET":
                return Utils.processGet(words, serverDictionary);
            case "EXISTS":
                return Utils.processExists(words, serverDictionary);
            case "DEL":
                return Utils.processDelete(words, serverDictionary);
            case "SAVE":
                return Utils.processSave(words, serverDictionary);
            case "LOAD":
                return Utils.processLoad(words, serverDictionary);
            default:
                return "NO RESPONSE";
        }
    }

    public void communicate(DataInputStream dataIn, DataOutputStream dataOut, Socket clientSocket) throws IOException {
        String clientMessage = "", serverMessage = "";
        do {
            clientMessage = dataIn.readUTF();
            serverMessage = operate(clientMessage);
            dataOut.writeUTF(serverMessage);

            System.out.println("Client: " + clientMessage);
            System.out.println("Server: " + serverMessage);

        } while ((clientMessage != null && !serverMessage.equals("QUIT")));
    }

    public void closeResources(DataInputStream dataIn, DataOutputStream dataOut) throws IOException {
        dataIn.close();
        dataOut.close();
        serverSocket.close();
    }
}
