package fr.insa.chatSystem.gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class ChooseFile {
	
	public static void SendFile () {
		JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		choose.setDialogTitle("Choisissez un fichier : ");
		choose.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int res = choose.showSaveDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			if (choose.getSelectedFile().isDirectory()) {
				System.out.println("Vous avez selectionne le repertoire: " + choose.getSelectedFile());
			}
		}
	}
}