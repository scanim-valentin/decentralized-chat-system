import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.gui.ConnectWindow;

public class Launcher {

	static public String username;

	public static void main(String[] args) {
		if(args.length == 0) //If no argument as been provided, regular execution 
			new ConnectWindow(username);
		else //If arguments are provided, debug execution
			MainController.NO_GUI_agent(args);
	}
}