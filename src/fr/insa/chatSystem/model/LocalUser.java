package fr.insa.chatSystem.model;

import java.net.InetAddress;

public class LocalUser extends UserID{
    
    public LocalUser(String name, InetAddress addr){
        super(name, addr);
    }
}