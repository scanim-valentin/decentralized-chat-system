import java.util.List;

public class MainClass {
	
	static public List<UserID> userlist = null; //List of users to fill with other UsersID
	
	static public String username = "" ; 
	
	static private void debugPrint(String str) {
		System.out.println("MainClass: "+str);
	}
	
	public static void main(String[] s) {
		debugPrint("Starting DDM deamon . . .") ; 
		DistributedDataManager.start_deamon();
	}
}
