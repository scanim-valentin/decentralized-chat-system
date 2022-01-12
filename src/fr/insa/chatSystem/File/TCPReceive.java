package fr.insa.chatSystem.File;

import java.io.*;
import java.net.*;

//Côté recepteur
public class TCPReceive extends Thread {

	private final int serverPort; // IP du Server
	private final String fileOutput; // Lien du fichier
	private ServerSocket serverSocket; // Connexion Port
	private Socket connectionSocket; // Port de connexion

	public TCPReceive(int port, String fileRx) throws IOException {
		super();
		serverPort = port;
		fileOutput = fileRx;
		serverSocket = null;
		connectionSocket = null;
	}

	@Override
	public void run() {
		System.out.println("The File is ready to be received : " + fileOutput);
		byte[] aByte = new byte[1];
		int bytesRead;

		InputStream is = null;

		try {
			serverSocket = new ServerSocket(serverPort);
			connectionSocket = serverSocket.accept();
			is = connectionSocket.getInputStream();
		} catch (IOException ex) {
			// Do exception handling
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (is != null) {

			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				fos = new FileOutputStream(fileOutput);
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(aByte, 0, aByte.length);

				do {
					baos.write(aByte);
					bytesRead = is.read(aByte);
				} while (bytesRead != -1);

				bos.write(baos.toByteArray());
				bos.flush();
				bos.close();
				serverSocket.close();
				System.out.println("File received : " + fileOutput);
				System.out.println("The thread TCP Receive closed.");
			} catch (IOException ex) {
				// Do exception handling
			}
		}
	}
}