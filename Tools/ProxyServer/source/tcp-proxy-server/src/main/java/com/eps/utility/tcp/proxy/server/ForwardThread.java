
package com.eps.utility.tcp.proxy.server;

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

	
	/**
	 * Creates a new traffic forward thread specifying its input stream, output
	 * stream and parent thread
	 */
	public ForwardThread(ForwardServer forwardServerP, InputStream aInputStream, OutputStream aOutputStream) {
		this.forwardServerP = forwardServerP;
		this.mInputStream = aInputStream;
		this.mOutputStream = aOutputStream;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[READ_BUFFER_SIZE];
		try {
			while (true) {
				int bytesRead = mInputStream.read(buffer);
				if (bytesRead == -1)
					break; // End of stream is reached --> exit the thread
				mOutputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// Read/write failed --> connection is broken --> exit the thread
		}

		// Notify parent thread that the connection is broken and forwarding should stop
		forwardServerP.connectionBroken();
	}

}



