package fr.insa.chatSystem.model;

import java.util.LinkedList;


public class Model {
	private LinkedList<RemoteUser> user_list;
	private LocalUser local_user;

	public Model(LocalUser l_user){
	       user_list = new LinkedList<RemoteUser>();
	       local_user = l_user;
	    }

	public LocalUser getLocalUser() {
		return local_user;
	}

	public void setNameLocalUser(String name) {
		local_user.setName(name);
	}

	public LinkedList<RemoteUser> getRemoteUsers() {
		return user_list;
	}

	public void addRemoteUser(RemoteUser rem_user) {
		if (!user_list.contains(rem_user)) {
			user_list.add(rem_user);
		}
		System.out.println(user_list.toString());
	}

	public void delRemoteUser(RemoteUser rem_user) {
		user_list.remove(rem_user);
		System.out.println(user_list.toString());
	}

	public RemoteUser checkRemoteUser(String hostAddress) {
		RemoteUser rem = null;
		for (RemoteUser rem_user : user_list) {
			if (rem_user.getAddress().getHostAddress() != null)
				rem = rem_user;
		}
		return rem;
	}

}
