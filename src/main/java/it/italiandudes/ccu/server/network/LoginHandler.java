package it.italiandudes.ccu.server.network;

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
        //TODO: LoginHandler
    }
}
