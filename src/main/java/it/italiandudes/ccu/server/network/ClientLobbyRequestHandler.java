package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.CCU.Defs.Protocol;
import it.italiandudes.idl.common.RawSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ClientLobbyRequestHandler extends Thread {

    //Attributes
    private final UserData userData;

    //Constructors
    public ClientLobbyRequestHandler(@NotNull UserData userData) {
        this.userData = userData;
        this.setDaemon(true);
        this.p
    }

    //Methods
    public UserData getUserData() {
        return userData;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientLobbyRequestHandler)) return false;

        ClientLobbyRequestHandler that = (ClientLobbyRequestHandler) o;

        return getUserData().equals(that.getUserData());
    }
    @Override
    public int hashCode() {
        return getUserData().hashCode();
    }
    @Override
    public String toString() {
        return "ClientLobbyRequestHandler{" +
                "userData=" + userData +
                "} " + super.toString();
    }

    //Main Method
    @Override
    public void run() {

        try {

            String msg;

            while (true) {
                msg = RawSerializer.receiveString(userData.getConnection().getInputStream());

                switch (msg) {
                    case Protocol.DISCONNECT:
                        LobbyHandler.removeUserFromLobby(userData.getUsername());
                        break;

                    case Protocol.Lobby.PLAYER_READY:
                        userData.setPlayerReady(true);
                        boolean loopExit = false;
                        break;

                    case Protocol.Lobby.PLAYER_NOT_READY:
                        break;

                    default:
                        throw new RuntimeException("Protocol not Respected!");
                }

            }
        }catch (Exception e) {
            try{
                LobbyHandler.removeUserFromLobby(userData.getUsername());
            }catch (Exception ignored) {}
        }

    }
}
