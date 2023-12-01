package com.bichotitas.votacionesmvc.client;

import com.bichotitas.votacionesmvc.exception.BrokerIsDeadException;
import com.bichotitas.votacionesmvc.exception.UnableToSendMessageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientToBroker {
    private final PrintWriter sender;
    private final BufferedReader receiver;
    private final Socket clientSocket;
    private static ClientToBroker instance;
    private static String host = "127.0.0.1";
    private static int port = 90;

    private ClientToBroker(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            sender = new PrintWriter(clientSocket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            throw new BrokerIsDeadException("Oh no! The broker is dead D:");
        }
    }

    public static void configureInstance(String host, int port) {
        ClientToBroker.host = host;
        ClientToBroker.port = port;

        System.out.println("Conectando al broker " + host + ":" + port);
    }

    public static ClientToBroker getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ClientToBroker(host, port);
        }

        return instance;
    }

    public String sendMessage(String message) {
        String response = "";
        try {
            sender.println(message);
            response = receiver.readLine();
        } catch (Exception e) {
            throw new UnableToSendMessageException("Oh no! Something was wrong while sending the last message D:");
        }

        return response;
    }

    public void stopConnection() {
        try {
            sender.close();
            receiver.close();
            clientSocket.close();
        } catch (Exception e) {
            throw new BrokerIsDeadException("Oh no! Unable to stop the socket connection D:");
        }
    }
}