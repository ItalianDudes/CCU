package it.italiandudes.ccu.client.exception;

import java.rmi.AlreadyBoundException;

public class UsernameAlreadyBoundException extends AlreadyBoundException {
    public UsernameAlreadyBoundException(String msg){
        super(msg);
    }
}
