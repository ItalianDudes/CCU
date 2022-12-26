package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.RawSerializer;
import it.italiandudes.idl.common.StringHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public final class LobbyHandler {

    //Player List
    @NotNull private static final HashSet<NetUserData> userList = new HashSet<>();

    //Methods
    public static String addUserToLobby(@NotNull UserData userData){
        if(userList.size()>=Server.getMaxPlayers()) return CCU.Defs.Protocol.Login.SERVER_FULL;
        NetUserData netUserData = new NetUserData(userData);
        if(userList.add(netUserData)){
            LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Lobby.PLAYER_JOIN);
            LobbyHandler.broadcastMessage(netUserData.getUserData().getUsername());
            netUserData.getThread().start();
            return CCU.Defs.Protocol.Lobby.PLAYER_JOIN;
        }
        return null;
    }
    public static ArrayList<String> getUsersConnected(){
        ArrayList<String> users = new ArrayList<>();
        for(NetUserData netUserData : userList){
            users.add(netUserData.getUserData().getUsername());
        }
        return users;
    }
    public static boolean removeUserFromLobby(@NotNull String username){
        for(NetUserData netUserData : userList){
            if(netUserData.getUserData().getUsername().equals(username)){
                try {
                    RawSerializer.sendString(netUserData.getUserData().getConnection().getOutputStream(), CCU.Defs.Protocol.DISCONNECT);
                }catch (IOException ignored){}
                netUserData.getThread().interrupt();
                userList.remove(netUserData);
                LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Lobby.PLAYER_QUIT);
                LobbyHandler.broadcastMessage(netUserData.getUserData().getUsername());
                return true;
            }
        }
        return false;
    }
    public static void broadcastMessage(@NotNull String message){
        for(NetUserData netUserData : userList){
            try {
                StringHandler.sendString(netUserData.getUserData().getConnection().getOutputStream(), message);
            }catch (IOException e){
                removeUserFromLobby(netUserData.getUserData().getUsername());
            }
        }
    }
    public static boolean isConnected(@NotNull String username){
        for(NetUserData netUserData : userList){
            if(netUserData.getUserData().getUsername().equals(username))
                return true;
        }
        return false;
    }
    public static void clearList(){
        userList.clear();
    }
    @Nullable public static UserData getUserData(@NotNull String username){
        for(NetUserData netUserData : userList){
            if(netUserData.getUserData().getUsername().equals(username))
                return netUserData.getUserData();
        }
        return null;
    }
    @Nullable public static NetUserData getNetUserData(@NotNull String username){
        for(NetUserData netUserData : userList){
            if(netUserData.getUserData().getUsername().equals(username))
                return netUserData;
        }
        return null;
    }

}
