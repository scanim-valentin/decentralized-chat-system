import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.gui.ConnectWindow;

public class Launcher {

	static public String username;

	public static void main(String[] args) {
		if(args.length == 0)
			new ConnectWindow(username);
		else
			MainController.NO_GUI_agent();
	}
}