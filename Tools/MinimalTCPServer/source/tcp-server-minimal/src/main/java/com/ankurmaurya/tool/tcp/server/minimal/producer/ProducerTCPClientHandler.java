package com.ankurmaurya.tool.tcp.server.minimal.producer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ProducerTCPClientHandler implements Runnable {

    private final String serverName;
    private final Socket clientSocket;
    private final String mesageToSent;

    public ProducerTCPClientHandler(String serverName, Socket clientSocket, String mesageToSent) {
        this.serverName = serverName;
        this.clientSocket = clientSocket;
        this.mesageToSent = mesageToSent;
    }

    @Override
    public void run() {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  
                  DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
                dos.writeBytes(mesageToSent + System.lineSeparator());
                dos.flush();
                while(true) {
                	
                }
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.toString());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Exception finally : " + ex.toString());
            }
        }
    }

}