package fr.insa.chatSystem.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import fr.insa.chatSystem.controller.*;
import fr.insa.chatSystem.model.UserID;

public class ChooseFile {

	public static UserID currentUser = null;

	public static void chooseFile() {
		JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		choose.setAutoscrolls(true);
		choose.setDialogTitle("Choose a file to send : ");

		// Ouvrez le fichier
		int res = choose.showOpenDialog(null);

		// Enregistrez le fichier
		// int res = choose.showSaveDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			// Option à prendre et faire du tftp avec le remoteUser si OK
			MainController.NO_GUI_debugPrint("You have chosen : " + file.getAbsolutePath());
			// Vérifie si le receveur est présent
			if (currentUser != null) {
				ChattingSessionController.sendFile(file, currentUser.getAddress());
			}
		}
	}
}