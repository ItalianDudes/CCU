package it.italiandudes.ccu.server.console;

import it.italiandudes.ccu.server.network.LobbyHandler;
import it.italiandudes.idl.common.Logger;

public final class CommandCenter {

    //TODO: Implements commands
    public static boolean kickPlayer(String playerName){
        //TODO: Implement kickPlayer()
        return true;
    }
    public static void clearLobby(){
        //TODO: Implement clearLobby()
        LobbyHandler.clearList();
        Logger.log("Lobby cleared");
    }

}
