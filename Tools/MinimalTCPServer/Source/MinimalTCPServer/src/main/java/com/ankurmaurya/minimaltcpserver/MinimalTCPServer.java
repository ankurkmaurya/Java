
package com.ankurmaurya.minimaltcpserver;

/**
 *
 * @author Ankur Maurya
 */
public class MinimalTCPServer {

    private static String serverName = "Brahma";
    private static int serverPort = 9999;
    
    public static void main(String[] args) {
        if(args.length == 1){
            serverName = args[0];
        }
        if(args.length == 2){
            serverName = args[0];
            serverPort = Integer.parseInt(args[1]);
        }
        
        System.out.println("Server Name - " + serverName);
        System.out.println("Server Port - " + serverPort);
        
        Thread serverThread = new Thread(new PlainTCPServer(serverName, serverPort));
        serverThread.start();
        
    }
}
