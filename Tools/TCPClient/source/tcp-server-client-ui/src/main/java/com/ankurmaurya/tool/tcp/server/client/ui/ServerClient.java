
package com.ankurmaurya.tool.tcp.server.client.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

/**
*
* @author Ankur Maurya
* 
*/
public class ServerClient implements Runnable {
	
	
	private String serverIP;
	private int serverPort;
	private JTextArea textArea;
	private String msgToServer;
	
	
	public ServerClient(String serverIP, int serverPort, JTextArea textArea, String msgToServer) {
		super();
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.textArea = textArea;
		this.msgToServer = msgToServer;
	}



	public void run() {
		Socket socket = null;
	    DataInputStream input = null;
	    DataOutputStream out = null;
		try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(10000);
            textArea.append("Connected to IP '" + serverIP +"' ");
            textArea.append(System.lineSeparator());
            textArea.repaint();
            
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            
            if(!msgToServer.equals("")) {
            	textArea.append("Sending - " + msgToServer);
                textArea.append(System.lineSeparator());
            	out.writeUTF(msgToServer);
            	out.flush();
            	textArea.append("Data Sent");
            	textArea.append(System.lineSeparator());
            }
            
            textArea.append("Receiving - ");
            textArea.append(System.lineSeparator());
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = input.read(buffer)) != -1) {
            	byte[] bufferSlice = Arrays.copyOfRange(buffer, 0, read);
            	String line = new String(bufferSlice);
            	textArea.append(line);
            }
            textArea.append(System.lineSeparator());
        }
        catch (Exception e) {
        	textArea.append("Exception - " + e.toString());
        	textArea.append(System.lineSeparator());
        } finally {
        	try {
	        	if(socket!=null) {
	        		socket.close();
	        	}
        	}
	        catch (Exception e) {
	        	textArea.append("Exception finally - " + e.toString());
	        	textArea.append(System.lineSeparator());
	        }
        }
	}
	
	
} 



