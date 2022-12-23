package it.italiandudes.ccu.client;

import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.common.annotations.LogicalClass;
import it.italiandudes.idl.common.ConfigHandler;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@LogicalClass
public class ClientSingleton {
    private static ClientSingleton instance;

    //Common Client's constants
    public ClientSingleton() {
        System.out.println("Client's singleton instantiated");
    }

    synchronized public static ClientSingleton getInstance() {
        if(instance==null){
            instance=new ClientSingleton();
        }
        return instance;
    }

    //Common Client's parameters
    private Map<String, String> properties;
    private UserData user;

    //Common Client's methods
    public UserData getUser(){
        return user;
    }
    public void setUser(UserData user){
        this.user = user;
    }

    public void loadProperties() throws ConfigFormatException {
        //I initialize the hashmap
        System.out.println("Properties initialization");
        properties = new HashMap<>();

        ArrayList<Property> temp_properties = ConfigHandler.readConfigs(Client.Defs.Paths.CONFIG_FILE);
        for (Property tempProperty : temp_properties) {
            properties.put(tempProperty.getKey(), tempProperty.getValue());
        }
        System.out.println("Properties initialization finished");
    }
    public Property getProperty(@NotNull String keyword) throws ConfigFormatException {
        if(properties==null){
            loadProperties();
        }
        if(properties.containsKey(keyword)){
            return new Property(keyword,properties.get(keyword));
        }
        return null;    //The specified property doesn't exist. The config file has probably been corrupted and needs to be reloaded.
    }
    public void addServer(@NotNull String value){
        String servers = properties.get(Client.Defs.Config.SERVERS_KEYWORD);
        if(servers.length()==0){
            //value is the only server in the servers' list.
            properties.put(Client.Defs.Config.SERVERS_KEYWORD,value);
        }else{
            properties.put(Client.Defs.Config.SERVERS_KEYWORD,","+value);
        }
    }
    public void deleteServer(@NotNull String value){
        String servers = properties.get(Client.Defs.Config.SERVERS_KEYWORD);
        if(servers.contains(","+value)){
            //value is between two servers or at the end of the servers' list.
            properties.put(Client.Defs.Config.SERVERS_KEYWORD,servers.replace(",value",""));
        }else if(servers.contains(value+",")){
            //value is at the start of the servers' list.
            properties.put(Client.Defs.Config.SERVERS_KEYWORD,servers.replace("value,",""));
        }else{
            //value is the only server saved in the servers' list.
            properties.put(Client.Defs.Config.SERVERS_KEYWORD,servers.replace("value",""));
        }
    }
    public boolean isServerEmpty(){
        return properties.get(Client.Defs.Config.SERVERS_KEYWORD) == null || properties.get(Client.Defs.Config.SERVERS_KEYWORD).isEmpty();
    }
}
