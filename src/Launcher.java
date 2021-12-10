import fr.insa.chatSystem.gui.MainWindow;
import fr.insa.chatSystem.model.MainClass;

public class Launcher {
	
		public static void main(String[] args){
			
			if(args.length == 0) {
				Object pseudo = null;
				new MainWindow(pseudo);
			}
			
			if(args.length == 1) {
		        MainClass.nogui();
			}
	        
	    }
	    
}
