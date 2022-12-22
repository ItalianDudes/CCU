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
    private static String buffer;
    private static HashMap<String, String> configs;
    private static boolean programRunning = true;
    private static int connectionPort;

    //Start Method
    public static int start(String[] args){

        Scanner scan = new Scanner(System.in);

        configs = ServerConfig.readConfigFile();

        whiteCardsDB = DatabaseReader.readWhiteCardsDB();
        if(whiteCardsDB == null) return CCU.Defs.ReturnCodes.WHITE_DB_LOAD_ERROR;
        blackCardsDB = DatabaseReader.readBlackCardsDB();
        if(blackCardsDB == null) return CCU.Defs.ReturnCodes.BLACK_DB_LOAD_ERROR;

        OnlineServer onlineServer = OnlineServer.getInstance();

        do{
            try {
                buffer = scan.nextLine();
                if (buffer.equals(ServerDefs.Commands.STOP)) {
                    programRunning = false;
                } else {
                    CommandHandler.handleCommand(buffer);
                }
            }catch (NoSuchElementException ignored){}
        }while (programRunning);

        if(!ServerConfig.writeConfigFile(configs)) return CCU.Defs.ReturnCodes.SERVER_CONFIG_WRITE_ERROR;

        return 0;
    }

    //Methods
    public static int getConnectionPort(){
        return connectionPort;
    }

    //Server Constants
    public static final class ServerDefs {

        //Paths Constants
        public static final class Paths {
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

            //Keys
            public static final String KEY_PORT = "port";
            public static final String KEY_PASSWORD = "password";

            //Default Config File
            public static final class Default {
                public static final int VALUE_PORT = 45800;
                public static final String VALUE_PASSWORD = "";
            }
        }

        //Command Constants
        public static final class Commands {
            public static final String STOP = "stop";
        }

    }

}
