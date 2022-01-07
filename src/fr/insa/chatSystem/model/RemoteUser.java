package fr.insa.chatSystem.model;

import java.net.InetAddress;

public class RemoteUser extends UserID{
    
    public RemoteUser(String name, InetAddress addr){
        super(name, addr);
    }
}