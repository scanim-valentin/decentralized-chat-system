package fr.insa.chatSystem.File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import fr.insa.chatSystem.controller.MainController;

public class TCPSend extends Thread {

	private final int clientPort; // IP du Client
	private final InetAddress IPServer; // IP du Serveur
	private final File fileToSend; // Fichier à envoyer
	private Socket connectionSocket;// Port de connexion vide
	private BufferedOutputStream outToClient; //

	public TCPSend(File fileTx, int port, InetAddress hostname) {
		clientPort = port;
		fileToSend = fileTx;
		IPServer = hostname;
		connectionSocket = null;
		outToClient = null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				MainController.debugPrint("The file is ready to send :" + fileToSend.getName());
				this.connectionSocket = new Socket(IPServer, clientPort);
				this.outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
			} catch (IOException ex) {
			}

			if (outToClient != null) {

				File theFile = this.fileToSend;

				if (theFile.exists()) {
					MainController.debugPrint("File exists: " + fileToSend.getName());
				} else {
					MainController.debugPrint("File doesn't exists.");
					System.exit(1);
				}

				byte[] mybytearray = new byte[(int) theFile.length()];
				FileInputStream fis = null;

				try {
					fis = new FileInputStream(theFile);
				} catch (FileNotFoundException ex) {
				}

				BufferedInputStream bis = new BufferedInputStream(fis);

				try {
					bis.read(mybytearray, 0, mybytearray.length);
					outToClient.write(mybytearray, 0, mybytearray.length);
					outToClient.flush();
					outToClient.close();
					connectionSocket.close();
					MainController.debugPrint("File sended : " + fileToSend.getName());
					MainController.debugPrint("The thread TCP send closed.");
					return;
				} catch (IOException ex) {
				}
			}
		}
	}
}