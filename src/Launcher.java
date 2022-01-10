import fr.insa.chatSystem.controller.ChattingSessionController;
import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.gui.ConnectWindow;

// Lanceur d'application
public class Launcher {

	static public String username;

	public static void main(String[] args) {
		if (args.length == 0) // If no argument as been provided, regular execution
		// Lancement du client
		{
			new ConnectWindow(username);
			ChattingSessionController.start_deamon();
		}

		else // If arguments are provided, debug execution
			MainController.NO_GUI_agent(args);
	}
}