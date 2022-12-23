package it.italiandudes.ccu.client.models;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.common.annotations.LogicalClass;
import it.italiandudes.ccu.common.annotations.LogicalOperation;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.ConfigHandler;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

//TODO: scrivere la documentazione e inserire la licenza.
@LogicalClass
public final class ServerSelectionModel {

    //TODO: finire il metodo.
    @LogicalOperation
    public boolean confirm(@NotNull String serverName,@NotNull String alias) throws IOException, ConfigFormatException, AlreadyBoundException {
        int type;
        if((type = hostNameValidation(serverName))>=0){

            if(isAliasAlreadyTaken(alias)){
                throw new AlreadyBoundException("The alias is already bound to a server.");
            }

            if(type==0){
                String ipAddress = serverName.split("]:")[0].replaceAll("([\\[\\]])","").trim();
                int port = Integer.parseInt(serverName.split("]:")[1].trim());
                ClientSingleton.getInstance().setUser(new UserData("","",ipAddress,port));
            }else if(type==1){
                String ipAddress = serverName.split(":")[0].trim();
                int port = Integer.parseInt(serverName.split(":")[1].trim());
                ClientSingleton.getInstance().setUser(new UserData("","",ipAddress,port));
            }else{
                String address = serverName.split(":")[0].trim();
                int port = Integer.parseInt(serverName.split(":"));
                ClientSingleton.getInstance().setUser(new Socket(address.getHostAddress(),));
            }

            String server = "("+alias+","+serverName+",)";
            ClientSingleton.getInstance().addServer(server);
        }
        throw new InvalidParameterException("The server address is not a valid address.");
    }

    private int hostNameValidation(String hostName) throws PortUnreachableException {
        if(hostName.matches(CCU.Defs.Regexes.IPV6_VALIDATION_REGEX)){
            if(hostName.contains("]:")){
                String port = hostName.split("]:")[1];
                try {
                    int valPort = Integer.parseInt(port);

                    if(valPort>=0 && valPort<=65535){
                        return 0;
                    }

                    throw new PortUnreachableException("The specified port is out of range");
                }catch(NumberFormatException nf){
                    throw new NumberFormatException("The specified port is not valid or isn't a number");
                }
            }
        }else if(hostName.matches(CCU.Defs.Regexes.IPV4_VALIDATION_REGEX)){
            if(hostName.contains(":")){
                String port = hostName.split(":")[1];
                try{
                    int valPort = Integer.parseInt(port);

                    if(valPort>=0 && valPort<=65535){
                        return 1;
                    }
                    throw new PortUnreachableException("The specified port is out of range");
                }catch(NumberFormatException nf){
                    throw new NumberFormatException("The specified port is not valid or isn't a number");
                }
            }
        }else if(hostName.matches(CCU.Defs.Regexes.DNS_VALIDATION_REGEX)){
            if(hostName.contains(":")){
                String port = hostName.split(":")[1];
                try {
                    int valPort = Integer.parseInt(port);

                    if(valPort>=0 && valPort<=65535){
                        return 0;
                    }

                    throw new PortUnreachableException("The specified port is out of range");
                }catch(NumberFormatException nf){
                    throw new NumberFormatException("The specified port is not valid or isn't a number");
                }
            }
        }
        return -1;
    }

    private boolean isAliasAlreadyTaken(String alias) throws ConfigFormatException {
        String[] serverAliases;
        if(ClientSingleton.getInstance().getProperty(Client.Defs.Config.SERVERS_KEYWORD).getValue()==null){
            return false;
        }else{
            serverAliases = ClientSingleton.getInstance().getProperty(Client.Defs.Config.SERVERS_KEYWORD).getValue().split("\\),\\(");

            if(serverAliases.length==0){
                return false;
            }else{
                for (String serverAlias : serverAliases) {
                    String temp_alias = serverAlias.replace("(", "").trim().split(",")[0];

                    if (alias.equals(temp_alias)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
