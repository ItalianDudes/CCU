package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class NetUserData implements Runnable {

    //Attributes
    private @NotNull final UserData userData;
    private @NotNull final Thread thread;

    //Constructors
    public NetUserData(@NotNull UserData userData){
        this.userData = userData;
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
    }

    //Methods
    @NotNull
    public Thread getThread(){
        return thread;
    }
    @NotNull
    public String getUsername() {
        return userData.getUsername();
    }
    @NotNull
    public UserData getUserData(){
        return userData;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetUserData that = (NetUserData) o;

        return getUserData().getUsername().equals(that.getUserData().getUsername());
    }
    @Override
    public int hashCode() {
        return getUserData().getUsername().hashCode();
    }
    @Override
    public String toString() {
        return userData.toString();
    }

    //Thread Method
    @Override
    public void run(){
        try {
            while (!userData.getConnection().isClosed()) {
                //noinspection BusyWait
                Thread.sleep(Server.ServerDefs.KEEP_ALIVE_MILLIS);
                userData.getConnection().sendUrgentData(1);
            }
        }catch (InterruptedException | IOException e){
            if(!userData.getConnection().isClosed()){
                if(LobbyHandler.isConnected(userData.getUsername())){
                    LobbyHandler.removeUserFromLobby(userData.getUsername());
                }else{
                    try{
                        userData.getConnection().close();
                    }catch (IOException ignored){}
                }
            }
        }
    }
}
