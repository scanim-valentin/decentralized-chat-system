package TCPFile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceive extends Thread {

	private final int serverPort;
	private final String fileOutput;
	private Socket connectionSocket;
	private ServerSocket serverSocket;

	public TCPReceive(String fileRx, int port) throws IOException {
		super();
		fileOutput = fileRx;
		serverPort = port;
		connectionSocket = null;
		serverSocket = null;
	}

	@Override
	public void run() {
		System.out.println("File ready to be received : " + fileOutput);
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
				serverSocket.close();// Fermeture du socket
				System.out.println("File received : " + fileOutput);
				System.out.println("Thread TCP Receive closed.");
			} catch (IOException ex) {
				// Do exception handling
			}
		}
	}
}