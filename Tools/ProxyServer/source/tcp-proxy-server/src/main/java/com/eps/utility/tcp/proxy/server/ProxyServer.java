
package com.eps.utility.tcp.proxy.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer implements Runnable {

	private int serverID;
	private Integer localPort;
	private String remoteServerIP;
	private Integer remoteServerPort;
	
	
	public ProxyServer(int serverID, Integer localPort, String remoteServerIP, Integer remoteServerPort) {
		super();
		this.serverID = serverID;
		this.localPort = localPort;
		this.remoteServerIP = remoteServerIP;
		this.remoteServerPort = remoteServerPort;
	}
	

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			System.out.println("Starting Proxy Server  - " + serverID);
			serverSocket = new ServerSocket(localPort);
			System.out.println("Listening on Local Port  - " + localPort);
			
			// Accept client connections and process them until stopped
	        while(true) {
	           try {
	               Socket clientSocket = serverSocket.accept();
	               String clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
	               System.out.println(" -> " + serverID + " Accepted client from " + clientHostPort);
	               Thread t = new Thread(new ForwardServer(serverID, remoteServerIP, remoteServerPort, clientSocket));
	               t.start();
	           } catch (Exception e) {
	               System.out.println("Unexpected Error - " + e.toString());
	           }
	        }
		} catch (Exception e) {
			System.out.println("Exception  run() - " + e.toString());
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (Exception e) {
				System.out.println("Exception  run() finally - " + e.toString());
			}
		}

	}

	
	
	
	
}


