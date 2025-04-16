
package com.ankurmaurya.tool.tcp.server.minimal;

import java.util.Scanner;

import com.ankurmaurya.tool.tcp.server.minimal.producer.ProducerTCPServer;
import com.ankurmaurya.tool.tcp.server.minimal.producerconsumer.ProducerConsumerTCPServer;

/**
 *
 * @author Ankur Maurya
 * 
 */

public class MinimalTCPServer {

	
	private static String serverName = "Brahma";
	private static int serverPort = 9999;
	private static int serverType = 1;

	
	public static void main(String[] args) {
		System.out.println("Default -->");
		System.out.println("Server Name - " + serverName);
		System.out.println("Port No.    - " + serverPort);
		System.out.println("Server Type - " + serverType);
		System.out.println("");

		System.out.println("Usage Parameters -->");
		System.out.println("Parameter 1 : Server Name");
		System.out.println("Parameter 2 : Port No.");
		System.out.println("Parameter 3 : Server Type [1 - Producer Consumer], [2 - Producer]");
		System.out.println("Usage Ex: java -jar MinimalTCPServer.jar Vishnu 9800 1");
		System.out.println("");

		if (args.length > 0) {
			serverName = args[0];
		}
		if (args.length > 1) {
			serverPort = Integer.parseInt(args[1]);
		}
		if (args.length > 2) {
			serverType = Integer.parseInt(args[2]);
		}

		System.out.println("Configured -->");
		System.out.println("Server Name - " + serverName);
		System.out.println("Server Port - " + serverPort);
		System.out.println("Server Type - " + serverType);
		
		switch (serverType) {
			case 1:
				Thread producerConsumerThread = new Thread(new ProducerConsumerTCPServer(serverName, serverPort));
				producerConsumerThread.start();
				break;
			case 2:
				Scanner scanner = new Scanner(System.in);
				System.out.println("Provide Message to sent to client");
				String mesageToSentToClient =  scanner.nextLine();
				
				Thread producerThread = new Thread(new ProducerTCPServer(serverName, serverPort, mesageToSentToClient));
				producerThread.start();
				break;
			default:
				System.out.println("Invalid Server Type, Valid - [1 - Producer Consumer], [2 - Producer]");
				break;
		}
		
		

		

	}
	
	
}


