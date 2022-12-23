package it.italiandudes.ccu.client.models;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.idl.common.ConfigHandler;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;

import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;

//TODO: scrivere la documentazione e inserire la licenza.
public final class ServerSelectionModel {

    //TODO: finire il metodo.
    public void confirm(String serverName, String alias) throws PortUnreachableException, ConfigFormatException, AlreadyBoundException, UnknownHostException {
        if(hostNameValidation(serverName)){

            if(isAliasAlreadyTaken(alias)){
                throw new AlreadyBoundException("The alias is already bound to a server.");
            }

            InetAddress address = InetAddress.getByName(serverName);        //I retrieve the actual ip.

            ClientSingleton.getInstance().addServer("("+alias+","+serverName+",)");
        }
    }

    private boolean hostNameValidation(String hostName) throws PortUnreachableException {
        if(hostName.matches(CCU.Defs.Regexes.IPV6_VALIDATION_REGEX)){
            return true;
        }else if(hostName.matches(CCU.Defs.Regexes.IPV4_VALIDATION_REGEX)){
            if(hostName.contains(":") || hostName.contains("/")){
                String door = hostName.split("[:/]")[1];
                try{
                    int valDoor = Integer.parseInt(door);

                    if(valDoor>0 && valDoor<65535){
                        return true;
                    }
                    throw new PortUnreachableException("The specified port is out of range");
                }catch(NumberFormatException nf){
                    throw new NumberFormatException("The specified port is not valid or isn't a number");
                }
            }
        }
        return hostName.matches(CCU.Defs.Regexes.DNS_VALIDATION_REGEX);
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
