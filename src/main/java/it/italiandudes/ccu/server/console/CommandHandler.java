package it.italiandudes.ccu.server.console;

import it.italiandudes.ccu.server.Server.ServerDefs.Command;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.StringHandler;

public final class CommandHandler {

    //Command Parser
    public static boolean handleCommand(String command){

        boolean sendStop = false;

        String[] parsedCommand = StringHandler.parseString(command);

        switch (parsedCommand[0]){

            case Command.KickPlayer.COMMAND_NAME:
                if(parsedCommand.length< Command.KickPlayer.MIN_PARAMETERS){
                    Logger.log("Error: this command needs AT LEAST one player name");
                    logHelp();
                    break;
                }
                String[] playerList = new String[parsedCommand.length-1];
                System.arraycopy(parsedCommand, 1, playerList, 0, parsedCommand.length - 1);
                for(String playerName : playerList){
                    Logger.log((CommandCenter.kickPlayer(playerName)?("\""+playerName+"\" has been kicked out from lobby"):("\""+playerName+"\" not found in lobby")));
                }
                break;

            case Command.CLEAR_LOBBY:
                CommandCenter.clearLobby();
                break;

            case Command.STOP:
                sendStop = true;
                break;

            default:
                Logger.log("Command not recognized!");
                break;
        }

        return sendStop;
    }

    private static void logHelp(){
        Logger.log("For info digit the command \"help\"");
    }

}
