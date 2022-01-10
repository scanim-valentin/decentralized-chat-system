package fr.insa.chatSystem.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class ChooseFile {

	public static void SendFile(String[] args) {
		JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		choose.setDialogTitle("Choose a file to send : ");

		// Ouvrez le fichier
		int res = choose.showOpenDialog(null);
		
		// Enregistrez le fichier
		// int res = choose.showSaveDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			//Optionnel à prendre et faire du tftp avec le remoteUser si OK
			System.out.println("You have chosen : " + file.getAbsolutePath());
		}
	}
}