import fr.insa.chatSystem.gui.MainWindow;
import fr.insa.chatSystem.model.DistributedDataManager;
import fr.insa.chatSystem.model.MainClass;



public class Launcher {
		
		static private void debugPrint(String str) {
			System.out.println("["+Thread.currentThread().getName()+"] MainClass: "+str);
		}
	
		public static void main(String[] args){
			
			if(args.length == 0) {
				Object pseudo = null;
				
				debugPrint("Starting DDM deamon . . .") ; 
				DistributedDataManager.start_deamon();
				
				//Waiting to gather all of the user id
				debugPrint("Waiting for local ID request answer");
				
				//Waits 3 seconds to gather all of the usernames from the local network 
				MainClass.formatedDelay(3); 
				
				new MainWindow(pseudo);
			}
			
			if(args.length == 1) {
		        MainClass.nogui();
			}
	        
	    }
	    
}
