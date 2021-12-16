import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.gui.MainWindow;

public class Launcher {

	static public String pseudo;

	public static void main(String[] args) {
		// Ouvrir la fenetre de connexion
		if(args.length == 0)
			new MainWindow(pseudo);
		else
			MainController.NO_GUI_agent();
	}
}