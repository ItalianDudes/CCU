package it.italiandudes.ccu.server.config;

import it.italiandudes.ccu.server.Server;
import it.italiandudes.ccu.server.Server.ServerDefs.Configuration;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class ServerConfig {

    //Default Configs
    @NotNull private static final HashMap<String, String> DEFAULT_CONFIG = getDefaultConfigs();

    //Methods
    @NotNull private static HashMap<String, String> getDefaultConfigs(){
        HashMap<String, String> configMap = new HashMap<>();

        //Port
        configMap.put(Configuration.KEY_PORT, String.valueOf(Configuration.Default.VALUE_PORT));

        //Password
        configMap.put(Configuration.KEY_PASSWORD, Configuration.Default.VALUE_PASSWORD);

        //Max Players
        configMap.put(Configuration.KEY_MAX_PLAYERS, String.valueOf(Configuration.Default.VALUE_MAX_PLAYERS));

        //White Cards Given
        configMap.put(Configuration.KEY_WHITE_CARDS_GIVEN, String.valueOf(Configuration.Default.VALUE_WHITE_CARDS_GIVEN));

        //Keep Unused White Cards
        configMap.put(Configuration.KEY_KEEP_CARDS, String.valueOf(Configuration.Default.VALUE_KEEP_CARDS));

        return configMap;
    }
    @NotNull public static HashMap<String, String> readConfigFile(){
        File configFile = new File(Server.ServerDefs.Path.CONFIG_FILE);
        if(!configFile.exists() || !configFile.isFile())
            return DEFAULT_CONFIG;

        Scanner inFile;
        try {
            inFile = new Scanner(configFile);
        }catch (FileNotFoundException e){
            return DEFAULT_CONFIG;
        }

        HashMap<String, String> configMap = new HashMap<>();

        String buffer;
        String[] parsedBuffer;
        while(inFile.hasNext()){
            buffer = inFile.nextLine();
            if(!buffer.startsWith("#")) {
                parsedBuffer = buffer.split("=");
                configMap.put(parsedBuffer[0], parsedBuffer[1]);
            }
        }

        inFile.close();

        return (checkConfigFileIntegrity(configMap)?configMap:getDefaultConfigs());
    }
    private static boolean checkConfigFileIntegrity(@NotNull HashMap<String, String> configMap){
        for(String key : configMap.keySet()){
            if(Arrays.stream(Configuration.KEYS).noneMatch(Predicate.isEqual(key)))
                return false;
        }
        return true;
    }
    public static boolean writeConfigFile(@NotNull HashMap<String, String> configs){

        File serverDirectory = new File(Server.ServerDefs.Path.SERVER_DIRECTORY);

        if(!serverDirectory.exists() || !serverDirectory.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            serverDirectory.mkdirs();
        }

        File configFile = new File(Server.ServerDefs.Path.CONFIG_FILE);

        if(!configFile.exists() || !configFile.isFile()){
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
            }catch (IOException e){
                Logger.log(e);
                return false;
            }
        }

        BufferedWriter outFile = null;
        try {

            outFile = new BufferedWriter(new FileWriter(configFile));

            //Port
            appendConfig(outFile, Configuration.COMMENT_PORT, Configuration.KEY_PORT, configs);

            //Password
            appendConfig(outFile, Configuration.COMMENT_PASSWORD, Configuration.KEY_PASSWORD, configs);

            //Max Lobby Size
            appendConfig(outFile, Configuration.COMMENT_MAX_PLAYERS, Configuration.KEY_MAX_PLAYERS, configs);

            //White Cards Given
            appendConfig(outFile, Configuration.COMMENT_WHITE_CARDS_GIVEN, Configuration.KEY_WHITE_CARDS_GIVEN, configs);

            //Keep Unused White Cards
            appendConfig(outFile, Configuration.COMMENT_KEEP_CARDS, Configuration.KEY_KEEP_CARDS, configs);

            //Flush & Close
            outFile.flush();
            outFile.close();

        }catch (IOException e){
            Logger.log(e);
            try{
                if(outFile!=null) outFile.close();
            }catch (Exception ignored){}
            return false;
        }

        return true;
    }

    //Utility Methods
    private static void appendConfig(@NotNull BufferedWriter outFile, @NotNull String comment, @NotNull String key, @NotNull HashMap<String, String> configs) throws IOException {
        outFile.append(comment).
                append('\n').
                append(key).
                append('=').
                append(configs.get(key)).
                append('\n');
    }
}
