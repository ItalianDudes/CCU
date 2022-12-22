package it.italiandudes.ccu.server.console;

import it.italiandudes.idl.common.StringHandler;

public final class CommandHandler {

    public static void handleCommand(String command){

        String[] parsedCommand = StringHandler.parseString(command);

        //TODO: Recognize command and run the right command from "Commands" class

    }

}
