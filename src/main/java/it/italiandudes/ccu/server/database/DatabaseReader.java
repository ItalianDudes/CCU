package it.italiandudes.ccu.server.database;

import it.italiandudes.ccu.common.BlackCard;
import it.italiandudes.ccu.common.WhiteCard;
import it.italiandudes.ccu.server.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public final class DatabaseReader {

    //Methods
    public static WhiteCardsDB readWhiteCardsDB(){

        File databaseDirectory = new File(Server.ServerDefs.Paths.DB_DIRECTORY);

        if(! databaseDirectory.exists() || !databaseDirectory.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            databaseDirectory.mkdirs();
        }

        File whiteDatabaseDB = new File(Server.ServerDefs.Paths.WHITE_CARDS_DB);

        if(!whiteDatabaseDB.exists() || !whiteDatabaseDB.isFile()){
            return null;
        }

        Scanner inFile;
        try {
            inFile = new Scanner(whiteDatabaseDB);
        }catch (FileNotFoundException e){
            return null;
        }

        ArrayList<WhiteCard> whiteCardsList = new ArrayList<>();

        int cardID = 1;
        while(inFile.hasNext()){
            whiteCardsList.add(new WhiteCard(cardID, inFile.nextLine()));
            cardID++;
        }

        inFile.close();

        return new WhiteCardsDB(whiteCardsList);
    }
    public static BlackCardsDB readBlackCardsDB(){

        File databaseDirectory = new File(Server.ServerDefs.Paths.DB_DIRECTORY);

        if(! databaseDirectory.exists() || !databaseDirectory.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            databaseDirectory.mkdirs();
        }

        File blackDatabaseDB = new File(Server.ServerDefs.Paths.BLACK_CARDS_DB);

        if(!blackDatabaseDB.exists() || !blackDatabaseDB.isFile()){
            return null;
        }

        Scanner inFile;
        try {
            inFile = new Scanner(blackDatabaseDB);
        }catch (FileNotFoundException e){
            return null;
        }

        ArrayList<BlackCard> blackCardsList = new ArrayList<>();

        char fieldKey;
        if(inFile.hasNext()){
            fieldKey = inFile.nextLine().charAt(0);
        }else{
            inFile.close();
            return null;
        }

        if(!inFile.hasNext()){
            inFile.close();
            return null;
        }

        int cardID = 1;
        while(inFile.hasNext()){
            blackCardsList.add(new BlackCard(cardID, inFile.nextLine(), fieldKey));
            cardID++;
        }

        inFile.close();

        return new BlackCardsDB(blackCardsList, fieldKey);

    }

}
