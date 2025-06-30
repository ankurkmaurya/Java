
package com.ankurmaurya.tool.tcp.proxy.server;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLProxyServer implements Runnable {

	private int serverID;
	private Integer localPort;
	private String remoteServerIP;
	private Integer remoteServerPort;
	
	
	public SSLProxyServer(int serverID, Integer localPort, String remoteServerIP, Integer remoteServerPort) {
		super();
		this.serverID = serverID;
		this.localPort = localPort;
		this.remoteServerIP = remoteServerIP;
		this.remoteServerPort = remoteServerPort;
	}
	

	@Override
	public void run() {
		try {
			System.out.println("Starting Proxy Server  - " + serverID);
			
			// Create SSL Context
			SSLContext sslContext = createSSLContext();
			if(sslContext == null) {
            	System.out.println("Failed to create SSL-Context");
            	return;
            }
			
			// Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            // Create server socket
            try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(localPort);) {
            	System.out.println("Listening on Local Port : " + localPort);
            	System.out.println("Server '" + Thread.currentThread().getName() + "' started.");

                while (true) {
                    try {
                        SSLSocket clientSocket = (SSLSocket) sslServerSocket.accept();

                        String clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
     	                System.out.println(" -> " + serverID + " Accepted client from " + clientHostPort);

                        Thread clientRequest = new Thread(new ForwardServer(serverID, remoteServerIP, remoteServerPort, clientSocket));
                        clientRequest.start();
                    } catch (Exception e) {
                    	System.out.println("Error in connection attempt listening() : " + e.toString());
                    } 
               }
            }
		} catch (Exception e) {
			System.out.println("Exception  run() - " + e.toString());
		} 
	}
	
	
    //Create and initialize the SSLContext
    private static SSLContext createSSLContext() {
        SSLContext sslContext = null;
        try {
        	File certificateFolder = new File("certificate");
        	if(!certificateFolder.exists()) {
        		System.out.println("'certificate' folder does not exists.");
        		return sslContext;
        	}
        	

        	String keystoreCertificateFileName = "ssl-local.pfx";
        	File keystoreCertificateFile = new File(certificateFolder, keystoreCertificateFileName);
        	if(!keystoreCertificateFile.exists()) {
        		System.out.println("Keystore File '" + keystoreCertificateFile.getPath()+ "' does not found.");
        		return sslContext;
        	}
        	
        	
        	String keystoreDataFileName = "ssl-local.dat";
        	File keystoreDataFile = new File(certificateFolder, keystoreDataFileName);
        	if(!keystoreDataFile.exists()) {
        		System.out.println("Keystore Data File '" + keystoreDataFile.getPath()+ "' does not found.");
        		return sslContext;
        	}
        	String dataLine0 = Files.readAllLines(keystoreDataFile.toPath()).get(0);
        	String keystorePass = dataLine0;
        	if(keystorePass == null || keystorePass.equals("")) {
        		System.out.println("Keystore Password not found.");
        		return sslContext;
        	}

            try (FileInputStream fis = new FileInputStream(keystoreCertificateFile)) {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(fis, keystorePass.toCharArray());

                //Create key manager
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, keystorePass.toCharArray());
                KeyManager[] km = keyManagerFactory.getKeyManagers();

                //Create trust manager
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(keyStore);
                TrustManager[] tm = trustManagerFactory.getTrustManagers();

                //Initialize SSLContext
                sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(km, tm, null);
            }
        } catch (Exception ex) {
        	 System.out.println("Exception createSSLContext() : " + ex.toString());
        }
        return sslContext;
    }

	
	
	
	
}


