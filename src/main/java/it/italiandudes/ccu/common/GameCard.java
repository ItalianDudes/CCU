package it.italiandudes.ccu.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class GameCard implements Serializable {

    //Attributes
    private final int cardID;
    private final String content;

    //Constructors
    public GameCard(@NotNull Integer cardID, @NotNull String content){
        this.cardID = cardID;
        this.content = content;
    }

    //Methods
    public int getCardID(){
        return cardID;
    }
    public String getContent(){
        return content;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameCard)) return false;

        GameCard gameCard = (GameCard) o;

        if (getCardID() != gameCard.getCardID()) return false;
        return getContent().equals(gameCard.getContent());
    }
    @Override
    public int hashCode() {
        int result = getCardID();
        result = 31 * result + getContent().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "GameCard{" +
                "cardID=" + cardID +
                ", content='" + content + '\'' +
                '}';
    }
}
