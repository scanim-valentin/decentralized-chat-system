package fr.insa.chatSystem.File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

//Coté Emetteur
public class TCPSend extends Thread {

	private final int clientPort; // IP du Client
	private final String IPServer; // IP du Serveur
	private final File fileToSend;
	private Socket connectionSocket;
	private BufferedOutputStream outToClient;

	public TCPSend(File fileTx, int port, String IP) {
		super();
		clientPort = port;
		fileToSend = fileTx;
		IPServer = IP;
		connectionSocket = null;
		outToClient = null;
	}

	@Override
	public void run() {

		while (true) {

			try {

				System.out.println("The file is ready to send :" + fileToSend.getName());
				this.connectionSocket = new Socket(IPServer, clientPort);
				this.outToClient = new BufferedOutputStream(this.connectionSocket.getOutputStream());
			} catch (IOException ex) {
				// Do exception handling
			}

			if (outToClient != null) {
				File myFile = this.fileToSend;
				if (myFile.exists()) {
					System.out.println("File exists: " + fileToSend.getName());
				} else {
					System.out.println("File doesn't exists.");
					System.exit(1);
				}
				byte[] mybytearray = new byte[(int) myFile.length()];

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(myFile);
				} catch (FileNotFoundException ex) {
					// Do exception handling
				}
				BufferedInputStream bis = new BufferedInputStream(fis);

				try {
					bis.read(mybytearray, 0, mybytearray.length);
					outToClient.write(mybytearray, 0, mybytearray.length);
					outToClient.flush();
					outToClient.close();
					connectionSocket.close();
					System.out.println("File sended : " + fileToSend.getName());
					System.out.println("The Thread TCP send closed.");

					// File sent, exit the main method
					return;
				} catch (IOException ex) {
					// Do exception handling
				}
			}
		}
	}
}