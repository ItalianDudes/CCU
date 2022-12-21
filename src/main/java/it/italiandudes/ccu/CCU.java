package it.italiandudes.ccu;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.idl.common.Logger;

import java.util.Arrays;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class CCU {

    //App Common Data
    public static final class AppData {

        //Attributes

        //Constructors
        private AppData(){
            throw new UnsupportedOperationException("Can't be instanced!");
        }

        //Methods

    }

    //App Shutdown Hook
    private static Thread getShutdownHook(){
        return new Thread(() -> {
            Logger.close();
        });
    }

    //Main Method
    public static void main(String[] args) {

        //Check if the user want to run the app even if the Logger initialization fails
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
        }catch (Exception e) {
            System.err.println("An exception has occurred during Logger initialization.");
            e.printStackTrace();
            if(!logOnDefaultStreamIfLoggerFail)
                System.exit(Defs.ReturnCodes.LOGGER_INIT_ERROR);
        }

        Runtime.getRuntime().addShutdownHook(getShutdownHook()); //Add the App Shutdown Hook

        if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_SERVER))){ //Start the server
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_SERVER)).
                    toArray(String[]::new);
            Server.start(args);
        }else if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP))){ //Start the client in textual-mode
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP)).
                    toArray(String[]::new);
            Client.noGuiStart(args);
        }else{ //Start the client in graphic-mode
            Client.start(args);
        }
    }

    //App Generic Constants
    public static class Defs {

        //Launch Arguments
        public static final class LaunchArgs {
            public static final String LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL = "-LogOnDefaultStreamIfLoggerInitFail";
            public static final String START_SERVER = "-server";
            public static final String START_TEXTUAL_APP = "-nogui";
        }

        //Return Codes
        public static final class ReturnCodes {

            //<0 -> Pre-Launch Errors
            //>0 -> Post-Launch Errors

            //Pre Launch Codes
            public static final int LOGGER_INIT_ERROR = -100;

            //Post-Launch Codes

        }

    }


}
