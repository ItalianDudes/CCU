package it.italiandudes.ccu.common;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public final class UserData {

    //Connection Data
    @NotNull private final String username;
    @NotNull private final String serverPassword;
    @NotNull private final Socket connection;

    //Game Data
    private boolean isCardMaster;
    private int roundWon;
    @NotNull private final ArrayList<GameCard> currentCards = new ArrayList<>();

    //Constructors
    public UserData(@NotNull String username, @NotNull String serverPassword, @NotNull Socket connection){
        resetGameData();
        this.username = username;
        this.serverPassword = serverPassword;
        this.connection = connection;
    }
    public UserData(@NotNull String username, @NotNull String serverPassword, @NotNull String hostname, int port){

        resetGameData();

        this.username = username;
        this.serverPassword = serverPassword;

        try {
            this.connection = new Socket(hostname, port);
        }catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException("Can't connect to server: invalid port number, must be between 0 and 65535, inclusive.");
        }catch (UnknownHostException unknownHostException){
            throw new RuntimeException("Can't resolve host name.");
        }catch (IOException ioException){
            throw new RuntimeException("Connection error.");
        }
    }
    public UserData(@NotNull String username, @NotNull String serverPassword, @NotNull String connectionData){

        resetGameData();

        this.username = username;
        this.serverPassword = serverPassword;

        if(connectionData.split(":").length!=2)
            throw new RuntimeException("Connection data Domain:Port / IP:Port are not valid.");

        String[] data = connectionData.split(":");
        String hostname = data[0];

        int port;
        try{
            port = Integer.parseInt(data[1]);
        }catch (NumberFormatException e){
            throw new RuntimeException("Invalid port", e);
        }

        try {
            this.connection = new Socket(hostname, port);
        }catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException("Can't connect to server: invalid port number, must be between 0 and 65535, inclusive.", illegalArgumentException);
        }catch (UnknownHostException unknownHostException){
            throw new RuntimeException("Can't resolve host name.", unknownHostException);
        }catch (IOException ioException){
            throw new RuntimeException("Connection error.", ioException);
        }
    }

    //Methods
    public void resetGameData(){
        isCardMaster = false;
        roundWon = 0;
        currentCards.clear();
    }
    @NotNull public String getUsername(){
        return username;
    }
    @NotNull public String getServerPassword(){
        return serverPassword;
    }
    @NotNull public Socket getConnection(){
        return connection;
    }
    public void setCardMaster(boolean isCardMaster){
        this.isCardMaster = isCardMaster;
    }
    public void setRoundWon(int roundWon){
        this.roundWon = roundWon;
    }
    public boolean isCardMaster(){
        return isCardMaster;
    }
    public int getRoundWon(){
        return roundWon;
    }
    @NotNull public ArrayList<GameCard> getCurrentCards(){
        return currentCards;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;

        UserData userData = (UserData) o;

        if (isCardMaster() != userData.isCardMaster()) return false;
        if (getRoundWon() != userData.getRoundWon()) return false;
        if (!getUsername().equals(userData.getUsername())) return false;
        if (!getServerPassword().equals(userData.getServerPassword())) return false;
        if (!getConnection().equals(userData.getConnection())) return false;
        return getCurrentCards().equals(userData.getCurrentCards());
    }
    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getServerPassword().hashCode();
        result = 31 * result + getConnection().hashCode();
        result = 31 * result + (isCardMaster() ? 1 : 0);
        result = 31 * result + getRoundWon();
        result = 31 * result + getCurrentCards().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", serverPassword='" + serverPassword + '\'' +
                ", connection=" + connection +
                ", isCardMaster=" + isCardMaster +
                ", roundWon=" + roundWon +
                ", currentCards=" + currentCards +
                '}';
    }
}
