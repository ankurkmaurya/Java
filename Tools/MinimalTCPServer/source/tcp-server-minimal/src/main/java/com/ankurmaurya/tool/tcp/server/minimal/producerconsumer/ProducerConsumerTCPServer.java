package com.ankurmaurya.tool.tcp.server.minimal.producerconsumer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Ankur Maurya
 *
 */
public class ProducerConsumerTCPServer implements Runnable {

    private final String serverName;
    private final int serverPort;
    private final boolean serverListening;


    public ProducerConsumerTCPServer(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
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

                Thread clientThread = new Thread(new ProducerConsumerTCPClientHandler(serverName, socket));
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


