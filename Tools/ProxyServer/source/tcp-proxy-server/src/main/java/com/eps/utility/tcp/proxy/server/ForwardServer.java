
package com.eps.utility.tcp.proxy.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ForwardServer implements Runnable {

	private int serverID;
	
	private String remoteServerIP;
	private Integer remoteServerPort;
	
	private Socket mClientSocket;
	private Socket mRemoteSocket;
	
	private String mClientHostPort;
	private String mServerHostPort;
	
	public ForwardServer(int serverID, String remoteServerIP, Integer remoteServerPort, Socket mClientSocket) {
		super();
		this.serverID = serverID;
		this.remoteServerIP = remoteServerIP;
		this.remoteServerPort = remoteServerPort;
		this.mClientSocket = mClientSocket;
	}
	
	
	@Override
	public void run() {
		try {
			mClientHostPort = mClientSocket.getInetAddress().getHostAddress() + ":" + mClientSocket.getPort();
			mServerHostPort = remoteServerIP + ":" + remoteServerPort;
			System.out.println(" -> " + serverID + " Connecting to Remote Server -> " + mServerHostPort);
			// Create a new socket connection to one of the servers from the list
			mRemoteSocket = createRemoteSocket();
			if (mRemoteSocket == null) {
				System.out.println(" -> " + serverID + " Cannot establish connection to remote server " + mServerHostPort);
				mClientSocket.close();
				return;
			}
			System.out.println(" -> " + serverID + " Connected successfully to remote server " + mServerHostPort);
			
		  // Obtain input and output streams of server and client
           InputStream clientIn = mClientSocket.getInputStream();
           OutputStream clientOut = mClientSocket.getOutputStream();
           InputStream serverIn = mRemoteSocket.getInputStream();
           OutputStream serverOut = mRemoteSocket.getOutputStream();

           // Start forwarding of socket data between server and client
           ForwardThread clientForward = new ForwardThread(this, clientIn, serverOut);
           ForwardThread serverForward = new ForwardThread(this, serverIn, clientOut);
           clientForward.start();
           serverForward.start();
			
           System.out.println(" -> " + serverID + " Traffic Forwarding  " + mClientHostPort + " <--> " + mServerHostPort + "  started.");

		} catch (Exception e) {
			System.out.println("Exception  run() - " + e.toString());
		} 
	}
	
	
	
	private Socket createRemoteSocket() throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(remoteServerIP, remoteServerPort);
		} catch (IOException e) {
			System.out.println(" -> " + serverID + " Exception createRemoteSocket() - " + e.toString());
		}
		return socket;
	}
	
	
	
	public synchronized void connectionBroken() {
		try {
			if (mRemoteSocket != null) {
				mRemoteSocket.close();
			}
		} catch (IOException e) {
		}

		try {
			if (mClientSocket != null) {
				mClientSocket.close();
			}
		} catch (IOException e) {
		}
		System.out.println(" -> " + serverID + " Traffic Forwarding  " + mClientHostPort + " <--> " + mServerHostPort + "  stopped.");
	}
	
	
	

}


