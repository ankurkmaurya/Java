
package com.ankurmaurya.tool.tcp.proxy.server;

import java.io.FileOutputStream;

/*
 * ForwardThread handles the TCP forwarding between a socket input stream (source)
 * and a socket output stream (destination). It reads the input stream and forwards
 * everything to the output stream. If some of the streams fails, the forwarding
 * is stopped and the parent thread is notified to close all its connections.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ForwardThread extends Thread {

	private static final int READ_BUFFER_SIZE = 8192;

	private ForwardServer forwardServerP = null;
	private InputStream mInputStream = null;
	private OutputStream mOutputStream = null;
	private String streamLoggerPath = null;
	

	
	/**
	 * Creates a new traffic forward thread specifying its input stream, output
	 * stream and parent thread
	 */
	public ForwardThread(ForwardServer forwardServerP, InputStream aInputStream, OutputStream aOutputStream, String streamLoggerPath) {
		this.forwardServerP = forwardServerP;
		this.mInputStream = aInputStream;
		this.mOutputStream = aOutputStream;
		this.streamLoggerPath = streamLoggerPath;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[READ_BUFFER_SIZE];
		try {
			FileOutputStream fos = new FileOutputStream(streamLoggerPath, true);
			while (true) {
				int bytesRead = mInputStream.read(buffer);
				System.out.println("Bytes Read - " + bytesRead);
				if (bytesRead == -1) {
					break; // End of stream is reached --> exit the thread
				}
				mOutputStream.write(buffer, 0, bytesRead);
				
				synchronized(this) {
				   fos.write(buffer, 0, bytesRead);
				}
			}
			fos.close();
		} catch (IOException e) {
			// Read/write failed --> connection is broken --> exit the thread
		}

		// Notify parent thread that the connection is broken and forwarding should stop
		forwardServerP.connectionBroken();
	}

}



