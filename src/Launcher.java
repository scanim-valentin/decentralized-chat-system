import fr.insa.chatSystem.gui.MainWindow;


public class Launcher {
	
		public static String pseudo;

		public static void main(String[] args){
                if(args.length == 0){
                    // Ouvrir la fenetre de connexion
                    new MainWindow(pseudo);
                }else{
                    MainClass.nogui();
                }
			}
	    }
