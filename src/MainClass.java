import java.util.ArrayList;
import java.util.List;

public class MainClass {
	
	static public List<UserID> userlist = new ArrayList<UserID>() ; //List of users to fill with other UsersID
	
	static public String username = "" ; 
	
	static private void debugPrint(String str) {
		System.out.println("MainClass: "+str);
	}
	
	public static void main(String[] s) {
		if(s.length > 0) {
			debugPrint("New username = "+s[0]) ; 
			username = s[0] ; 
		}
		debugPrint("Starting DDM deamon . . .") ; 
		DistributedDataManager.start_deamon();
	}
}
