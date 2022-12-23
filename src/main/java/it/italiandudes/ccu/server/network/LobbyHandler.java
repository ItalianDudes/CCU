package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.RawSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashSet;

public final class LobbyHandler {

    //Player List
    @NotNull private static final HashSet<UserData> userList = new HashSet<>();

    //Methods
    public static boolean addUserToLobby(@NotNull UserData userData){
        return userList.add(userData);
    }
    public static boolean removeUserFromLobby(@NotNull String username){
        for(UserData userData : userList){
            if(userData.getUsername().equals(username)){
                try {
                    RawSerializer.sendString(userData.getConnection().getOutputStream(), Server.ServerDefs.Protocol.DISCONNECT);
                }catch (IOException ignored){}
                try {
                    userData.getConnection().close();
                }catch (IOException ignored){}
                userList.remove(userData);
                return true;
            }
        }
        return false;
    }
    public static boolean isConnected(@NotNull String username){
        for(UserData userData : userList){
            if(userData.getUsername().equals(username))
                return true;
        }
        return false;
    }
    public static void clearList(){
        userList.clear();
    }
    @Nullable public static UserData getUserData(@NotNull String username){
        for(UserData userData : userList){
            if(userData.getUsername().equals(username))
                return userData;
        }
        return null;
    }

}
