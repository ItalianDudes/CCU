package it.italiandudes.ccu.client.models.controllers;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.LogicalClass;
import it.italiandudes.ccu.client.annotations.LogicalOperation;
import it.italiandudes.ccu.client.exception.ServerIsFullException;
import it.italiandudes.ccu.client.exception.UsernameAlreadyBoundException;
import it.italiandudes.ccu.client.models.Server;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.idl.common.RawSerializer;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.PortUnreachableException;
import java.rmi.AlreadyBoundException;

@LogicalClass
public final class ServerSelectionListModel {
    @LogicalOperation
    public boolean confirm(@NotNull String serverName, @NotNull String alias) throws IOException, AlreadyBoundException, ConfigFormatException {

        int type=hostNameValidation(serverName);

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
            int port = Integer.parseInt(serverName.split(":")[1].trim());
            ClientSingleton.getInstance().setUser(new UserData("","",address,port));
        }

        String isPwdRequired = RawSerializer.receiveString(ClientSingleton.getInstance().getUser().getConnection().getInputStream());
        if(isPwdRequired.equals(CCU.Defs.Protocol.Login.PWD_NOT_REQUIRED)){
            return true;
        }else if(isPwdRequired.equals(CCU.Defs.Protocol.Login.PWD_REQUIRED)){
            return false;
        }else if(isPwdRequired.equals(CCU.Defs.Protocol.Login.SERVER_FULL)){
            throw new ServerIsFullException("Connection rejected because the server is full");
        }
        throw new ConnectException("Unknown error while trying to establish a connection to the server");
    }

    @LogicalOperation
    public boolean pwdValidation(@NotNull String insertedPwd) throws IOException {

        RawSerializer.sendString(ClientSingleton.getInstance().getUser().getConnection().getOutputStream(), insertedPwd);
        String answer = RawSerializer.receiveString(ClientSingleton.getInstance().getUser().getConnection().getInputStream());

        if(answer!=null && answer.equals(CCU.Defs.Protocol.Login.PWD_CORRECT)){
            ClientSingleton.getInstance().getUser().setServerPassword(insertedPwd);
            return true;
        }else if(answer!=null && answer.equals(CCU.Defs.Protocol.Login.PWD_INCORRECT)){
            return false;
        }else{
            throw new ConnectException("There has been a sudden disconnection from the server. Trying to establish a new connection.");
        }
    }

    @LogicalOperation
    public boolean nameValidation(@NotNull String insertedName) throws IOException, UsernameAlreadyBoundException {
        RawSerializer.sendString(ClientSingleton.getInstance().getUser().getConnection().getOutputStream(), insertedName);
        String answer = RawSerializer.receiveString(ClientSingleton.getInstance().getUser().getConnection().getInputStream());

        if(answer!=null && answer.equals(CCU.Defs.Protocol.Login.AUTH_OK)){
            ClientSingleton.getInstance().getUser().setUsername(insertedName);
            return true;
        }else if(answer!=null && answer.equals(CCU.Defs.Protocol.Login.AUTH_ERROR_SAME_USER_LOGGED)){
            throw new UsernameAlreadyBoundException("The username is already present in the server");
        }else{
            throw new ConnectException("There has been a sudden disconnection from the server. Trying to establish a new connection.");
        }
    }

    @LogicalOperation
    public void savePwd(@NotNull String pwd){
        ClientSingleton.getInstance().addServer(new Server(ClientSingleton.getInstance().getSelectedServer().getCname(),ClientSingleton.getInstance().getSelectedServer().getCname(), pwd));
        ClientSingleton.getInstance().setSelectedServer(new Server(ClientSingleton.getInstance().getSelectedServer().getCname(),ClientSingleton.getInstance().getSelectedServer().getCname(), pwd));
    }

    @LogicalOperation
    public void saveName(@NotNull String name){
        ClientSingleton.getInstance().modifyProperty(Client.Defs.Config.LANG_KEYWORD,name);
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
}
