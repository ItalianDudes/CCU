package it.italiandudes.ccu.client;

import it.italiandudes.ccu.client.annotations.LogicalClass;
import it.italiandudes.ccu.client.annotations.LogicalOperation;
import it.italiandudes.ccu.client.config.ClientConfigHandler;
import it.italiandudes.ccu.client.models.Server;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@LogicalClass
public final class ClientSingleton {
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
    private ArrayList<Server> servers;
    private UserData user;

    //Common Client's methods
    public UserData getUser(){
        return user;
    }
    public void setUser(UserData user){
        this.user = user;
    }

    @LogicalOperation
    public void loadProperties() throws IOException, ConfigFormatException {
        //I initialize the hashmap
        System.out.println("Properties initialization");
        properties = new HashMap<>();
        servers = new ArrayList<>();
        ArrayList<Property> temp_properties = ClientConfigHandler.readConfigs(Client.Defs.Path.CONFIG_FILE);
        for (Property tempProperty : temp_properties) {
            if(!tempProperty.getKey().equals(Client.Defs.Config.SERVERS_KEYWORD)){
                properties.put(tempProperty.getKey(), tempProperty.getValue());
            }else{
                String[] servers = tempProperty.getValue().split("\\),\\(");
                for(String temp_server : servers){
                    temp_server = temp_server.replaceAll("\\(","").trim();
                    String alias = temp_server.split(",")[0].trim();
                    String cname = temp_server.split(",")[1].trim();
                    String pwd = temp_server.split(",").length<3 ? "" : temp_server.split(",")[2];

                    this.addServer(new Server(alias,cname,pwd));
                }
            }
        }
        System.out.println("Properties initialization finished");
    }
    @LogicalOperation
    public Property getProperty(@NotNull String keyword) throws IOException, ConfigFormatException {
        if(properties==null){
            loadProperties();
        }
        if(properties.containsKey(keyword)){
            return new Property(keyword, properties.get(keyword));
        }
        return null;    //The specified property doesn't exist. The config file has probably been corrupted and needs to be reloaded.
    }
    @LogicalOperation
    public void addServer(@NotNull Server server) {
        servers.remove(new Server(server.getAlias(),server.getCname(),""));         //deletes every possible copy
        servers.remove(server);
        servers.add(new Server(server.getAlias(),server.getCname(),server.getPwd()));    //adds or updates the HashSet
    }
    @LogicalOperation
    public void deleteServer(@NotNull Server server) throws IOException, ConfigFormatException {
        if(servers == null){
            loadProperties();
        }
        servers.remove(server);
    }
    @LogicalOperation
    public Server getServer(@NotNull int i) throws IOException, ConfigFormatException {
        if(servers == null){
            loadProperties();
        }

        if(servers.size()==0){
            throw new ArrayIndexOutOfBoundsException("No server is saved, so "+i+" is out of bounds.");
        }else if(servers.size()<=i){
            throw new ArrayIndexOutOfBoundsException("The number of available servers is less than the index("+i+") provided.");
        }else{
            return servers.get(i);
        }
    }
    @LogicalOperation
    public int getServersNum(){
        if(isServerEmpty()){
            return 0;
        }else{
            return servers.size();
        }
    }
    @LogicalOperation
    public boolean isServerEmpty(){
        return servers == null || servers.isEmpty();
    }
}
