package com.ankurmaurya.tool.tcp.server.minimal.producer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ankurmaurya.tool.tcp.server.minimal.producerconsumer.ProducerConsumerTCPClientHandler;

public class ProducerTCPServer implements Runnable {

	private final String serverName;
    private final int serverPort;
    private final boolean serverListening;
    private final String mesageToSentToClient;


    public ProducerTCPServer(String serverName, int serverPort, String mesageToSentToClient) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.mesageToSentToClient = mesageToSentToClient;
        this.serverListening = true;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server (" + serverName + ") started listening on port - " + serverPort);
            while (serverListening) {
                socket = serverSocket.accept();
                System.out.println("Client connected - " + socket.getInetAddress().getHostAddress());

                Thread clientThread = new Thread(new ProducerTCPClientHandler(serverName, socket, mesageToSentToClient));
                clientThread.start();
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.toString());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.out.println("Exception finally() : " + ex.toString());
                }
            }
        }
    }

	
}
