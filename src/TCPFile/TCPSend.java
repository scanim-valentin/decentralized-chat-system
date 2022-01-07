package TCPFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class TCPSend extends Thread {

	private final File fileToSend;
	private final int serverPort;
	private final String IPServer;
	private Socket connectionSocket;
	private BufferedOutputStream toTheClient;

	public TCPSend(File fileTx, int port, String IP) {
		super();
		fileToSend = fileTx;
		serverPort = port;
		IPServer = IP;
		connectionSocket = null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("File ready to send :" + fileToSend.getName());
				this.connectionSocket = new Socket(IPServer, serverPort);
				this.toTheClient = new BufferedOutputStream(this.connectionSocket.getOutputStream());
			} catch (IOException ex) {
				// Do exception handling
			}

			if (toTheClient != null) {
				File theFile = this.fileToSend;
				if (theFile.exists()) {
					System.out.println("File exists : " + fileToSend.getName());
				} else {
					System.out.println("ERROR !!!! File does not exists.");
					System.exit(1);
				}
				byte[] mybytearray = new byte[(int) theFile.length()];

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(theFile);
				} catch (FileNotFoundException ex) {
					// Do exception handling
				}
				BufferedInputStream bis = new BufferedInputStream(fis);

				try {
					bis.read(mybytearray, 0, mybytearray.length);
					toTheClient.write(mybytearray, 0, mybytearray.length);
					toTheClient.flush();
					toTheClient.close();// déco du client
					connectionSocket.close();// Fermeture du socket
					System.out.println("File sended: " + fileToSend.getName());
					System.out.println("Thread TCP Send closed.");
					return;
				} catch (IOException ex) {
					// Do exception handling
				}
			}
		}
	}
}