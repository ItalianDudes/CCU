package it.italiandudes.ccu.server.config;

import it.italiandudes.ccu.server.Server;
import it.italiandudes.ccu.server.Server.ServerDefs.Configuration;
import it.italiandudes.idl.common.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

@SuppressWarnings("unused")
public final class ServerConfig {

    //Default Configs
    private static final HashMap<String, String> DEFAULT_CONFIG = getDefaultConfigs();

    //Methods
    private static HashMap<String, String> getDefaultConfigs(){
        HashMap<String, String> configMap = new HashMap<>();

        //Port
        configMap.put(Configuration.KEY_PORT, String.valueOf(Configuration.Default.VALUE_PORT));

        //Password
        configMap.put(Configuration.KEY_PASSWORD, Configuration.Default.VALUE_PASSWORD);

        return configMap;
    }
    public static HashMap<String, String> readConfigFile(){
        File configFile = new File(Server.ServerDefs.Paths.CONFIG_FILE);
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

        return configMap;


    }
    public static boolean writeConfigFile(HashMap<String, String> configs){

        File serverDirectory = new File(Server.ServerDefs.Paths.SERVER_DIRECTORY);

        if(!serverDirectory.exists() || !serverDirectory.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            serverDirectory.mkdirs();
        }

        File configFile = new File(Server.ServerDefs.Paths.CONFIG_FILE);

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
    private static void appendConfig(BufferedWriter outFile, String comment, String key, HashMap<String, String> configs) throws IOException {
        outFile.append(comment).
                append('\n').
                append(key).
                append('=').
                append(configs.get(key)).
                append('\n');
    }

}
