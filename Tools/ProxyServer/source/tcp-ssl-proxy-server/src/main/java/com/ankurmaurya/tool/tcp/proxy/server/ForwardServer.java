
package com.ankurmaurya.tool.tcp.proxy.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class ForwardServer implements Runnable {

	private int serverID;
	
	private String remoteServerIP;
	private Integer remoteServerPort;
	
	private SSLSocket mClientSocket;
	private SSLSocket mRemoteSocket;
	
	private String mClientHostPort;
	private String mServerHostPort;
	
	
	public ForwardServer(int serverID, String remoteServerIP, Integer remoteServerPort, SSLSocket mClientSocket) {
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
           ForwardThread clientForward = new ForwardThread(this, clientIn, serverOut, "inwardstream.log");
           ForwardThread serverForward = new ForwardThread(this, serverIn, clientOut, "outwardstream.log");
           clientForward.start();
           serverForward.start();
			
           System.out.println(" -> " + serverID + " Traffic Forwarding  " + mClientHostPort + " <--> " + mServerHostPort + "  started.");

		} catch (Exception e) {
			System.out.println("Exception  run() - " + e.toString());
		} 
	}
	
	
	
	private SSLSocket createRemoteSocket() throws IOException {
		SSLSocket socket = null;
		try {
			SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
			if(sslSocketFactory == null) {
				return socket;
			}
			socket = (SSLSocket) sslSocketFactory.createSocket(remoteServerIP, remoteServerPort);
		} catch (Exception e) {
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
	
	
	private SSLSocketFactory getSSLSocketFactory() {
		SSLSocketFactory sslSocketFactory = null;
		// Creating Secure Socket factory
		try {
			File certificateFolder = new File("certificate");
			if (!certificateFolder.exists()) {
				System.out.println("'certificate' folder does not exists.");
				return sslSocketFactory;
			}

			String keystoreCertificateFileName = "ssl-remote.pfx";
			File keystoreCertificateFile = new File(certificateFolder, keystoreCertificateFileName);
			if (!keystoreCertificateFile.exists()) {
				System.out.println("Keystore File '" + keystoreCertificateFile.getPath() + "' does not found.");
				return sslSocketFactory;
			}

			String keystoreDataFileName = "ssl-remote.dat";
			File keystoreDataFile = new File(certificateFolder, keystoreDataFileName);
			if (!keystoreDataFile.exists()) {
				System.out.println("Keystore Data File '" + keystoreDataFile.getPath() + "' does not found.");
				return sslSocketFactory;
			}
			String dataLine0 = Files.readAllLines(keystoreDataFile.toPath()).get(0);
			String keystorePass = dataLine0;
			if (keystorePass == null || keystorePass.equals("")) {
				System.out.println("Keystore Password not found.");
				return sslSocketFactory;
			}

			try (FileInputStream fis = new FileInputStream(keystoreCertificateFile)) {
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(fis, keystorePass.toCharArray());

				// Create key manager
				KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
				keyManagerFactory.init(keyStore, keystorePass.toCharArray());
				KeyManager[] km = keyManagerFactory.getKeyManagers();

				// Create trust manager
				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
				trustManagerFactory.init(keyStore);
				TrustManager[] tm = trustManagerFactory.getTrustManagers();

				// Initialize SSLContext
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
				sslContext.init(km, tm, null);
				sslSocketFactory = sslContext.getSocketFactory();
			}
		} catch (Exception ex) {
			System.out.println("Exception getSSLSocketFactory() : " + ex.toString());
		}
		return sslSocketFactory;
	}
	
	
	

}




