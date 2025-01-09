
package com.ankurmaurya.tool.tcp.proxy.server;

public class SSLRemoteServer {
    
	private String serverIP;
	private Integer serverPort;
	
	public SSLRemoteServer(String serverIP, Integer serverPort) {
		super();
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	

}


