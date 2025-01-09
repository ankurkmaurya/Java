
package com.ankurmaurya.tool.tcp.proxy.server;


import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;


/**
*
* @author Ankur Maurya
* 
* Usage:
*        java -jar SSLProxyServerMain.jar 8080,192.168.75.01,9090 1234,192.168.75.23,8080 
* 
* 
*/

public class SSLProxyServerMain {

	private static Map<Integer, SSLRemoteServer>  proxyRouteMap = new HashMap<>();	
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				initializeCommandLineConfiguration(args);
			} else {
				initializeManualConfiguration();
			}
		} catch (Exception e) {
			System.out.println("Exception main() : " + e.toString());
		}
	}
	
	public static void initializeCommandLineConfiguration(String[] args) {
		try {
			System.out.println("Proxy Server Command Line Configuration Started.");
			for (String arg : args) {
				if (arg.contains(",")) {
					String[] argSplt = arg.split(",");
					int localPort = Integer.parseInt(argSplt[0]);
					String remoteServerIP = argSplt[1];
					int remoteServerPort = Integer.parseInt(argSplt[2]);
					SSLRemoteServer remoteServer = new SSLRemoteServer(remoteServerIP, remoteServerPort);
					proxyRouteMap.put(localPort, remoteServer);
				}
			}
			System.out.println("Proxy Server Command Line Configuration Completed.");
			System.out.println("");
			System.out.println("");
			System.out.println("Starting all the configured Proxy Servers.");
			startProxyServers();
		} catch (Exception e) {
			System.out.println("Exception initializeCommandLineConfiguration() : " + e.toString());
		}
	}
	
	
	public static void initializeManualConfiguration() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			
			System.out.println("Proxy Server Manual Configuration Started.");
			System.out.println("Please provide Count of Proxy Servers to be created. (Value : Min. - 1, Max. - 10)");
			int proxyCnt = Integer.parseInt(scanner.nextLine());

			for (int i = 1; i <= proxyCnt; i++) {
				System.out.println("Proxy Server Input ===> " + i);
				System.out.println("Provide Local Port No.");
				int localPort = Integer.parseInt(scanner.nextLine());
				System.out.println("Provide Remote Server IP.");	
				String remoteServerIP = scanner.nextLine();
				System.out.println("Provide Remote Server Port.");
				int remoteServerPort = Integer.parseInt(scanner.nextLine());
				
				SSLRemoteServer remoteServer = new SSLRemoteServer(remoteServerIP, remoteServerPort);
				proxyRouteMap.put(localPort, remoteServer);
						
			}
			System.out.println("Proxy Server Configuration Completed.");
			System.out.println("");
			System.out.println("");
			System.out.println("Starting all the configured Proxy Servers.");
			startProxyServers();
			
		} catch (Exception e) {
			System.out.println("Exception initializeManualConfiguration() : " + e.toString());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}		
	}
	
    
	public static void startProxyServers() {
		try {
			int serverID = 1;
			for(Map.Entry<Integer, SSLRemoteServer> proxyRouteEntry : proxyRouteMap.entrySet()) {
				Integer localPort = proxyRouteEntry.getKey();
				
				SSLRemoteServer remoteServer = proxyRouteEntry.getValue();
				Thread t = new Thread(new SSLProxyServer(serverID, localPort, remoteServer.getServerIP(), remoteServer.getServerPort()));
				t.start();
				
			    serverID++;
			}
		} catch (Exception e) {
			System.out.println("Exception startProxyServers() : " + e.toString());
		} 
	}
	
	
	
	
}
