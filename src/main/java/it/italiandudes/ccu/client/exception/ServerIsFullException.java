package it.italiandudes.ccu.client.exception;

import java.net.ConnectException;

public class ServerIsFullException extends ConnectException {
    public ServerIsFullException(String msg){
        super(msg);
    }
}
