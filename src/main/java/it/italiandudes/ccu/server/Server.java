package it.italiandudes.ccu.server;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.server.config.ServerConfig;
import it.italiandudes.ccu.server.console.CommandHandler;
import it.italiandudes.ccu.server.database.BlackCardsDB;
import it.italiandudes.ccu.server.database.DatabaseReader;
import it.italiandudes.ccu.server.database.WhiteCardsDB;
import it.italiandudes.ccu.server.network.OnlineServer;
import it.italiandudes.idl.common.Logger;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Server {

    //Attributes
    private static WhiteCardsDB whiteCardsDB;
    private static BlackCardsDB blackCardsDB;
    private static HashMap<String, String> configs;
    private static boolean stopRequest = false;

    //Start Method
    public static int start(String[] args){

        Scanner scan = new Scanner(System.in);

        //Retrieve Server Configs
        configs = ServerConfig.readConfigFile();

        //Retrieve Cards from DBs
        whiteCardsDB = DatabaseReader.readWhiteCardsDB();
        if(whiteCardsDB == null) return CCU.Defs.ReturnCodes.WHITE_DB_LOAD_ERROR;
        blackCardsDB = DatabaseReader.readBlackCardsDB();
        if(blackCardsDB == null) return CCU.Defs.ReturnCodes.BLACK_DB_LOAD_ERROR;

        //Turn the server Online
        OnlineServer onlineServer = OnlineServer.getInstance();

        if(onlineServer==null) return CCU.Defs.ReturnCodes.SERVER_START_FAIL;

        Logger.log("Server Online on port: "+getConnectionPort());

        do{
            try {
                String buffer = scan.nextLine();
                stopRequest = CommandHandler.handleCommand(buffer);
            }catch (NoSuchElementException ignored){}
        }while (!stopRequest);

        if(!ServerConfig.writeConfigFile(configs)) return CCU.Defs.ReturnCodes.SERVER_CONFIG_WRITE_ERROR;

        return 0;
    }

    //Methods
    public static WhiteCardsDB getWhiteCardsDB(){
        return whiteCardsDB;
    }
    public static BlackCardsDB getBlackCardsDB(){
        return blackCardsDB;
    }
    public static int getConnectionPort(){
        return Integer.parseInt(configs.get(ServerDefs.Configuration.KEY_PORT));
    }
    public static boolean setConnectionPort(int port){
        if(port<0 || port>65535) return false;
        configs.put(ServerDefs.Configuration.KEY_PORT, String.valueOf(port));
        return true;
    }
    public static String getConnectionPassword(){
        return configs.get(ServerDefs.Configuration.KEY_PASSWORD);
    }
    public static boolean setConnectionPassword(String password){
        if(password==null) password = "";
        configs.put(ServerDefs.Configuration.KEY_PASSWORD, password);
        return true;
    }
    public static int getMaxPlayers(){
        return Integer.parseInt(configs.get(ServerDefs.Configuration.KEY_MAX_PLAYERS));
    }
    public static boolean setMaxPlayers(int maxPlayers){
        if(maxPlayers < 3) return false;
        configs.put(ServerDefs.Configuration.KEY_MAX_PLAYERS, String.valueOf(maxPlayers));
        return true;
    }
    public static int getGivenWhiteCardsAmount(){
        return Integer.parseInt(configs.get(ServerDefs.Configuration.KEY_WHITE_CARDS_GIVEN));
    }
    public static boolean setMaxWhiteCardsAmount(int whiteCardsGiven){
        if(whiteCardsGiven < 2) return false;
        configs.put(ServerDefs.Configuration.KEY_WHITE_CARDS_GIVEN, String.valueOf(whiteCardsGiven));
        return true;
    }
    public static boolean isKeepWhiteCardsAfterGame(){
        return Boolean.parseBoolean(configs.get(ServerDefs.Configuration.KEY_KEEP_CARDS));
    }
    public static boolean setKeepWhiteCardsAfterGame(boolean keepWhiteCards){
        configs.put(ServerDefs.Configuration.KEY_KEEP_CARDS, String.valueOf(keepWhiteCards));
        return true;
    }

    //Server Constants
    public static final class ServerDefs {

        //Paths Constants
        public static final class Path {
            public static final String SERVER_DIRECTORY = "server/";
            public static final String CONFIG_FILE = SERVER_DIRECTORY+"config.cfg";
            public static final String DB_DIRECTORY = SERVER_DIRECTORY+"db/";
            public static final String WHITE_CARDS_DB = DB_DIRECTORY+"whiteDB.txt";
            public static final String BLACK_CARDS_DB = DB_DIRECTORY+"blackDB.txt";
        }

        //Config Constants
        public static final class Configuration {

            //Comments
            public static final String COMMENT_PORT = "#Porta in cui il server attende le connessioni";
            public static final String COMMENT_PASSWORD = "#Password per la connessione al server, lasciare vuoto per connessione senza password";
            public static final String COMMENT_MAX_PLAYERS = "#Numero massimo di giocatori che possono partecipare alla partita";
            public static final String COMMENT_WHITE_CARDS_GIVEN = "#Numero di carte bianche distribuite a partita";
            public static final String COMMENT_KEEP_CARDS = "#Mantenere le carte bianche non usate dai giocatori per il turno successivo?";

            //Keys
            public static final String KEY_PORT = "port";
            public static final String KEY_PASSWORD = "password";
            public static final String KEY_MAX_PLAYERS = "players";
            public static final String KEY_WHITE_CARDS_GIVEN = "wcards";
            public static final String KEY_KEEP_CARDS = "keepcards";
            public static final String[] KEYS = {
                    KEY_PORT,
                    KEY_PASSWORD,
                    KEY_MAX_PLAYERS,
                    KEY_WHITE_CARDS_GIVEN,
                    KEY_KEEP_CARDS
            };

            //Default Config File
            public static final class Default {
                public static final int VALUE_PORT = 45800;
                public static final String VALUE_PASSWORD = "";
                public static final int VALUE_MAX_PLAYERS = 8;
                public static final int VALUE_WHITE_CARDS_GIVEN = 10;
                public static final boolean VALUE_KEEP_CARDS = true;
            }
        }

        //Command Constants
        public static final class Command {
            public static final String STOP = "stop";
            public static final class KickPlayer {
                public static final String COMMAND_NAME = "kick";
                public static final int MIN_PARAMETERS = 2; //Command Name+Parameters
            }
            public static final String CLEAR_LOBBY = "clobby";
        }

        //Protocol Strings
        public static final class Protocol {
            public static final String DISCONNECT = "disc";
            public static final String GAME_START = "game_start";
        }

    }

}
