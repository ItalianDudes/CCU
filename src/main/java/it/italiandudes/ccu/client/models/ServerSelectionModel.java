package it.italiandudes.ccu.client.models;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.Client;
import it.italiandudes.idl.common.ConfigHandler;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;

import java.net.PortUnreachableException;
import java.util.ArrayList;

//TODO: scrivere la documentazione e inserire la licenza.
public class ServerSelectionModel {

    //TODO: finire il metodo.
    public void confirm(String serverName, String alias) throws PortUnreachableException {
        if(hostNameValidation(serverName)){


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

    /*
    TODO: finire il metodo
    private boolean isAliasAlreadyTaken(String alias) throws ConfigFormatException {
        ArrayList<Property> properties = ConfigHandler.readConfigs(Client.Defs.Paths.CONFIG_FILE);

        for(int i=0; i){

        }
    }*/
}
