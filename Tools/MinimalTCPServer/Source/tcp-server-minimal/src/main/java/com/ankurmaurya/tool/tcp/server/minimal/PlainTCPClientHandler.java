package com.ankurmaurya.tool.tcp.server.minimal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Ankur Maurya
 *
 */
public class PlainTCPClientHandler implements Runnable {

    private final String serverName;
    private final Socket clientSocket;

    public PlainTCPClientHandler(String serverName, Socket clientSocket) {
        this.serverName = serverName;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String clientCommand;
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  
                  DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
                dos.writeUTF("Hello from - " + serverName + "\r\n");
                dos.writeUTF("To Close the Session, Enter /e" + "\r\n");
                dos.flush();
                while ((clientCommand = br.readLine()) != null) {
                    if (clientCommand.equals("echo")) {
                        String echoData = br.readLine();
                        dos.writeUTF(serverName + " : " + echoData + "\r\n");
                        dos.flush();
                    } else if (clientCommand.equals("/e")) {
                        dos.writeUTF("Closing Session with server : " + serverName + "\r\n");
                        dos.flush();
                        break;
                    } else {
                        dos.writeUTF("Invalid Command - " + clientCommand + "\r\n");
                        dos.flush();
                    }
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
