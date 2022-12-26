package it.italiandudes.ccu.server.game;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.server.network.LobbyHandler;

public final class GameHandler {

    public static boolean startNewGame(){
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Lobby.GAME_START);
        //TODO: Implement startNewGame
        return false;
    }

}
