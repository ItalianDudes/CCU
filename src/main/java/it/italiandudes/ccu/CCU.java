package it.italiandudes.ccu;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.Logger;

import java.util.Arrays;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class CCU {

    //Main Method
    public static void main(String[] args) {

        if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.TEST_START))){
            return;
        }

        int exitCode;

        //Check if the user wants to run the app even if the Logger initialization fails
        boolean logOnDefaultStreamIfLoggerFail = Arrays.stream(args).
                anyMatch(Predicate.isEqual(Defs.LaunchArgs.LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL));
        if(logOnDefaultStreamIfLoggerFail)
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL)).
                    toArray(String[]::new);

        try{ //Attempt to instantiate the Logger
            if(!Logger.init()){
                System.err.println("An unknown error has occurred during Logger initialization.");
                if(!logOnDefaultStreamIfLoggerFail)
                    System.exit(Defs.ReturnCodes.LOGGER_INIT_ERROR);
            }
            Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        }catch (Exception e) {
            System.err.println("An exception has occurred during Logger initialization.");
            e.printStackTrace();
            if(!logOnDefaultStreamIfLoggerFail)
                System.exit(Defs.ReturnCodes.LOGGER_INIT_ERROR);
        }

        if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_SERVER))){ //Start the server
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_SERVER)).
                    toArray(String[]::new);
            exitCode = Server.start(args);
        }else if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP))){ //Start the client in textual-mode
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP)).
                    toArray(String[]::new);
            exitCode = Client.noGuiStart(args);
        }else{ //Start the client in graphic-mode
            exitCode = Client.start(args);
        }

        Logger.log("Application terminating with code: "+exitCode);

        System.exit(exitCode);
    }

    //App Generic Constants
    public static class Defs {

        public static final String JAR_POSITION = CCU.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);

        //Regexes
        public static final class Regexes{
            public final static String IPV6_VALIDATION_REGEX = "^\\[([0-9A-Fa-f]{1,4}:){5}[0-9A-Fa-f]{1,4}\\]";
            public final static String IPV4_VALIDATION_REGEX = "^((0|(25[0-5]|2[0-4][0-9])|1[0-9][0-9]|([1-9][0-9]|[0-9]))\\.){3}(0|(25[0-5]|2[0-4][0-9])|1[0-9][0-9]|([1-9][0-9]|[0-9]))";
            public final static String DNS_VALIDATION_REGEX = "^([A-Za-z0-9]+[A-Za-z0-9\\-]*[A-Za-z0-9]?(?<!-))(\\.[A-Za-z0-9]+[A-Za-z0-9\\-]*[A-Za-z0-9]?(?<!-))+";
        }

        //Launch Arguments
        public static final class LaunchArgs {
            public static final String LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL = "-LogOnDefaultStreamIfLoggerInitFail";
            public static final String START_SERVER = "-server";
            public static final String START_TEXTUAL_APP = "-nogui";
            public static final String TEST_START = "-test";
        }

        //Return Codes
        public static final class ReturnCodes {

            //<0 -> Pre-Launch Errors
            //>0 -> Post-Launch Errors

            //Pre Launch Codes
            public static final int LOGGER_INIT_ERROR = -100;
            public static final int WHITE_DB_LOAD_ERROR = -201;
            public static final int BLACK_DB_LOAD_ERROR = -202;
            public static final int SERVER_START_FAIL = -81;

            //Post-Launch Codes
            public static final int SERVER_CONFIG_WRITE_ERROR = 307;
        }

        //Protocol Strings
        public static final class Protocol {
            public static final class Login {
                public static final String PWD_REQUIRED = "needpwd";
                public static final String PWD_NOT_REQUIRED = "nopwd";
                public static final String SERVER_FULL = "sfull";
                public static final String PWD_CORRECT = "pwdok";
                public static final String PWD_INCORRECT = "pwderr";
                public static final String AUTH_ERROR_SAME_USER_LOGGED = "sameuserlogged";
                public static final String AUTH_OK = "authok";
            }
            public static final class Lobby {
                public static final String PLAYER_JOIN = "pjoin";
                public static final String PLAYER_QUIT = "pquit";
                public static final String GAME_START = "gstart";
            }
            public static final String DISCONNECT = "disc";
        }

    }


}
