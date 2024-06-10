
package com.ankurmaurya.minimaltcpserver;

/**
 *
 * @author Ankur Maurya
 */
public class MinimalTCPServer {

	private static String serverName = "Brahma";
	private static int serverPort = 9999;

	public static void main(String[] args) {
		System.out.println("Default -->");
		System.out.println("Server Name - " + serverName);
		System.out.println("Port No.    - " + serverPort);
		System.out.println("");

		System.out.println("Usage Parameters -->");
		System.out.println("Parameter 1 : Server Name");
		System.out.println("Parameter 2 : Port No.");
		System.out.println("Usage Ex: java -jar MinimalTCPServer.jar Vishnu 9800");
		System.out.println("");

		if (args.length == 1) {
			serverName = args[0];
		}
		if (args.length == 2) {
			serverName = args[0];
			serverPort = Integer.parseInt(args[1]);
		}

		System.out.println("Configured -->");
		System.out.println("Server Name - " + serverName);
		System.out.println("Server Port - " + serverPort);

		Thread serverThread = new Thread(new PlainTCPServer(serverName, serverPort));
		serverThread.start();

	}
	
	

}
