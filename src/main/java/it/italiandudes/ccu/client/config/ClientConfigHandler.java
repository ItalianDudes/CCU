package it.italiandudes.ccu.client.config;

import it.italiandudes.ccu.client.annotations.LogicalClass;
import it.italiandudes.ccu.client.annotations.LogicalOperation;
import it.italiandudes.idl.common.Property;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

@LogicalClass
public final class ClientConfigHandler {

    public static final String DEFAULT_REGEX = "=";
    @LogicalOperation
    public static ArrayList<Property> readConfigs(@NotNull String path) throws IOException, ConfigFormatException {
        return readConfigs(new File(path),DEFAULT_REGEX);
    }
    @LogicalOperation
    public static ArrayList<Property> readConfigs(@NotNull File configFile) throws IOException, ConfigFormatException {
        return readConfigs(configFile,DEFAULT_REGEX);
    }
    @LogicalOperation
    public static ArrayList<Property> readConfigs(@NotNull File configFile, String regex) throws IOException, ConfigFormatException {
        if(!configFile.exists()){
            throw new FileNotFoundException("The config file is either absent or has been placed in a different path than specified.");
        }else if(!configFile.canRead()){
            throw new UnsupportedOperationException("The config file can't be read.");
        }else{
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            ArrayList<Property> properties = new ArrayList<>();

            String row;
            int i=0;
            while((row=br.readLine())!=null){
                System.out.println("Row: "+row);
                String temp_keyword = row.split(regex)[0];
                if(row.split(regex).length==1){
                    properties.add(new Property(temp_keyword,""));
                }else if(row.split(regex).length>2){
                    throw new ConfigFormatException("The number of '=' in the config file at row "+i+" is >2.");
                }else if(row.split(regex).length==2){
                    properties.add(new Property(temp_keyword,row.split(DEFAULT_REGEX)[1]));
                }
                i++;
            }

            return properties;
        }
    }
}