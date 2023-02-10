package it.italiandudes.ccu.server.network;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.RawSerializer;

import java.io.IOException;
import java.net.Socket;

public final class LoginHandler implements Runnable {

    //Attributes
    private final Socket connection;

    //Constructors
    public LoginHandler(Socket connection){
        this.connection = connection;
    }

    //Thread Method
    @Override
    public void run() {
        try {
            String password;
            String username;
            if(Server.getConnectionPassword().equals(Server.ServerDefs.Configuration.Default.VALUE_PASSWORD)){
                RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.PWD_NOT_REQUIRED);
                password = Server.ServerDefs.Configuration.Default.VALUE_PASSWORD;
            }else{
                RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.PWD_REQUIRED);
                password = RawSerializer.receiveString(connection.getInputStream());
                if(password.equals(Server.getConnectionPassword())){
                    RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.PWD_CORRECT);
                }else{
                    RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.PWD_INCORRECT);
                    throw new IOException("Incorrect Password");
                }
            }

            String result;
            UserData userData;
            boolean alreadyConnected;
            do {
                alreadyConnected = false;

                username = RawSerializer.receiveString(connection.getInputStream());

                userData = new UserData(username, password, connection);

                result = LobbyHandler.addUserToLobby(userData);
                if(result.equals(CCU.Defs.Protocol.Login.AUTH_ERROR_SAME_USER_LOGGED)) {
                    alreadyConnected = true;
                    RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.AUTH_ERROR_SAME_USER_LOGGED);
                }
            } while (alreadyConnected);

            if(result.equals(CCU.Defs.Protocol.Login.SERVER_FULL)){
                RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.SERVER_FULL);
                throw new RuntimeException("The server is full");
            }

            RawSerializer.sendString(connection.getOutputStream(), CCU.Defs.Protocol.Login.AUTH_OK);

        }catch (IOException e){
            try{
                connection.close();
            }catch (IOException ignored){}
        }
    }
}
