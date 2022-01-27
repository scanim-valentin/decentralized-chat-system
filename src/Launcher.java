import fr.insa.chatSystem.controller.DistributedDataController;
import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.gui.ConnectWindow;

// Lanceur d'application
public class Launcher {

	static public String username;

	public static void main(String[] args) {
		if (args.length == 0) // If no argument as been provided, regular execution
		{
			DistributedDataController.start_deamon();
			// Ouvrir la premier page
			new ConnectWindow(username);
		}

		else// If arguments are provided, debug execution
			// Lancement du mode Back End
			MainController.debug(args) ; 
	}
}