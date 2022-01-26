package fr.insa.chatSystem.File;

import java.io.*;
import java.net.*;

import fr.insa.chatSystem.controller.MainController;

public class TCPReceive extends Thread {

	private final int serverPort; // IP du Server
	private final String fileOutput; // Lien du fichier à envoyer
	private ServerSocket serverSocket; // Connexion server au Port
	private Socket connectionSocket; // Port de connexion

	public TCPReceive(int port, String fileRx) throws IOException {
		serverPort = port;
		fileOutput = fileRx;
		serverSocket = null;
		connectionSocket = null;
	}

	@Override
	public void run() {
		byte[] aByte = new byte[1];
		int bytesRead;
		InputStream is = null;

		try {
			// MainController.debugPrint("The file's ready to be received : " + fileOutput);
			serverSocket = new ServerSocket(serverPort);
			connectionSocket = serverSocket.accept();
			is = connectionSocket.getInputStream();
		} catch (IOException ex) {
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
				MainController.debugPrint("File received : " + fileOutput);
				MainController.debugPrint("The thread TCP receive closed.");
			} catch (IOException ex) {
			}
		}
	}
}