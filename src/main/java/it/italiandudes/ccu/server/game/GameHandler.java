package it.italiandudes.ccu.server.game;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.common.BlackCard;
import it.italiandudes.ccu.common.GameCard;
import it.italiandudes.ccu.common.UserData;
import it.italiandudes.ccu.common.WhiteCard;
import it.italiandudes.ccu.server.Server;
import it.italiandudes.ccu.server.network.LobbyHandler;
import it.italiandudes.ccu.server.network.NetUserData;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.RawSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public final class GameHandler {

    public static boolean startNewGame(){

        phaseOne();

        @NotNull String cardMaster = randomCardMaster();
        @NotNull BlackCard blackCard = randomBlackCard();

        sendRoles(cardMaster);

        prepareWhiteCards();

        if(!broadcastBlackCard(blackCard)) return false;

        HashMap<String, int[]> choices = waitUntilPlayersHaveDoneTheirChoices(cardMaster);

        if(choices == null) return false;

        int[] winningChoice = getWinningChoice(cardMaster);

        broadcastWinner(cardMaster, winningChoice, choices);

        //TODO: Implement startNewGame
        return true;
    }

    //Methods
    @NotNull
    private static String randomCardMaster(){
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Game.Phase.ELECTING_CARD_MASTER);
        ArrayList<String> usersConnected = LobbyHandler.getUsersConnected();
        return usersConnected.get(new Random().nextInt()%usersConnected.size());
    }
    private static boolean broadcastChoicesToMaster(String cardMaster, Collection<int[]> choices){
        UserData userData = LobbyHandler.getUserData(cardMaster);
        assert userData!=null;
        try {
            RawSerializer.sendInt(userData.getConnection().getOutputStream(), choices.size());
            for (int[] choice : choices) {
                RawSerializer.sendObject(userData.getConnection().getOutputStream(), choice);
            }
        }catch (IOException e){
            return false;
        }
        return true;
    }
    private static boolean broadcastWinner(String cardMaster, int[] winningChoice, HashMap<String, int[]> choices){
        String winner = null;
        for(String user : choices.keySet()){
            if(Arrays.equals(winningChoice, choices.get(user))) {
                winner = user;
                break;
            }
        }
        if(winner == null) return false;

        LobbyHandler.broadcastMessage(winner);
        LobbyHandler.broadcastObject(winningChoice);

        return true;
    }
    private static int[] getWinningChoice(String cardMaster){
        UserData userData = LobbyHandler.getUserData(cardMaster);
        assert userData!=null;
        try {
            return (int[]) RawSerializer.receiveObject(userData.getConnection().getInputStream());
        }catch (IOException | ClassNotFoundException e){
            return null;
        }
    }
    private static boolean sendRoles(String cardMasterName){
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Game.Phase.SENDING_ROLES);
        try {
            for (NetUserData netUserData : LobbyHandler.getNetUserData()) {
                if (netUserData.getUserData().getUsername().equals(cardMasterName)) {
                    RawSerializer.sendString(netUserData.getUserData().getConnection().getOutputStream(), CCU.Defs.Protocol.Game.Role.MASTER);
                } else {
                    RawSerializer.sendString(netUserData.getUserData().getConnection().getOutputStream(), CCU.Defs.Protocol.Game.Role.PLAYER);
                }
            }
        }catch (IOException e) {
            return false;
        }
        return true;
    }
    private static HashMap<String, int[]> waitUntilPlayersHaveDoneTheirChoices(String cardMaster){

        HashMap<String, int[]> userChoices = new HashMap<>();

        WaitListener[] listeners = new WaitListener[LobbyHandler.getUsersConnectedCount()];
        ArrayList<String> users = LobbyHandler.getUsersConnected();

        for(int i=0;i<listeners.length; i++){
            if(!users.get(i).equals(cardMaster)) {
                listeners[i] = new WaitListener(LobbyHandler.getUserData(users.get(i)), Server.getGivenWhiteCardsAmount());
                listeners[i].start();
            }
        }

        for(int i=0;i<listeners.length;){
            if(listeners[i].haveDone) {
                userChoices.put(listeners[i].userData.getUsername(), listeners[i].chosenCards);
                i++;
            }else if(listeners[i].hasErrored()){
                WaitListener.interruptList(listeners);
                return null;
            }else{
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    Logger.log(e);
                }
            }
        }

        return userChoices;

    }
    private static boolean prepareWhiteCards(){
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Game.Phase.PREPARING_DECK);
        try {
            for (NetUserData netUserData : LobbyHandler.getNetUserData()) {
                fillWhiteCardsDeck(netUserData.getUserData());
                sendCardsToPlayer(netUserData.getUserData());
            }
        }catch (IOException e){
            return false;
        }
        return true;
    }
    private static void sendCardsToPlayer(UserData userData) throws IOException {
        RawSerializer.sendInt(userData.getConnection().getOutputStream(), Server.getGivenWhiteCardsAmount());
        for(GameCard whiteCard : userData.getCurrentCards()){
            RawSerializer.sendObject(userData.getConnection().getOutputStream(), whiteCard);
        }
    }
    private static void fillWhiteCardsDeck(UserData userData){
        int numCards = Server.getGivenWhiteCardsAmount()-userData.getCurrentCards().size();
        ArrayList<WhiteCard> whiteCards = Server.getWhiteCardsDB().getWhiteCardsList();
        Random random = new Random();
        for(int i=numCards;i<Server.getGivenWhiteCardsAmount();){
            WhiteCard whiteCard = whiteCards.get(random.nextInt()%whiteCards.size());
            if(!userData.getCurrentCards().contains(whiteCard)) {
                userData.getCurrentCards().add(whiteCard);
                i++;
            }
        }
    }
    private static boolean broadcastBlackCard(BlackCard blackCard){
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Game.Phase.SEND_BLACK_CARD);
        return LobbyHandler.broadcastObject(blackCard)==0;
    }
    @NotNull
    private static BlackCard randomBlackCard(){
        ArrayList<BlackCard> blackCardList = Server.getBlackCardsDB().getBlackCardsList();
        return blackCardList.get(new Random().nextInt()%blackCardList.size());
    }
    private static void phaseOne(){
        //Notice the players that the game started
        LobbyHandler.broadcastMessage(CCU.Defs.Protocol.Lobby.GAME_START);

        //Notice the players who are connected
        ArrayList<String> usersConnected = LobbyHandler.getUsersConnected();
        LobbyHandler.broadcastMessage(String.valueOf(usersConnected.size()));
        for (String username : usersConnected) {
            LobbyHandler.broadcastMessage(username);
        }
    }

    //Waiter Class
    private static final class WaitListener extends Thread {

        //Attributes
        private final UserData userData;
        private boolean haveDone;
        private boolean hasErrored;
        private final int cardsIDAmountToWait;
        private final int[] chosenCards;

        //Constructors
        public WaitListener(UserData userData, int cardsIDAmountToWait){
            this.userData = userData;
            haveDone = false;
            hasErrored = false;
            this.cardsIDAmountToWait = cardsIDAmountToWait;
            chosenCards = new int[cardsIDAmountToWait];
        }

        //Methods
        public static void interruptList(WaitListener[] listeners){
            for(WaitListener listener : listeners){
                listener.interrupt();
            }
        }
        public boolean haveDone(){
            return haveDone;
        }
        public boolean hasErrored(){
            return hasErrored;
        }
        public int[] getChosenCards(){
            return chosenCards;
        }

        //Thread Method
        @Override
        public void run(){

            try {
                for (int i = 0; i < cardsIDAmountToWait; i++) {
                    chosenCards[i] = RawSerializer.receiveInt(userData.getConnection().getInputStream());
                }
                haveDone = true;
            }catch (IOException e){
                hasErrored = true;
            }
        }

    }

}
