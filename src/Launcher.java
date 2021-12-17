import fr.insa.chatSystem.gui.MainWindow;

public class Launcher {

	static public String username;

	public static void main(String[] args) {
		// Ouvrir la fenetre de connexion
		new MainWindow(username);
	}
}