package it.italiandudes.ccu.server.database;

import it.italiandudes.ccu.common.WhiteCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class WhiteCardsDB {

    //Attributes
    @NotNull private final ArrayList<WhiteCard> whiteCards;

    //Constructors
    public WhiteCardsDB(@NotNull ArrayList<WhiteCard> whiteCards){
        this.whiteCards = whiteCards;
    }

    //Methods
    @NotNull public ArrayList<WhiteCard> getWhiteCards(){
        return whiteCards;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteCardsDB)) return false;

        WhiteCardsDB that = (WhiteCardsDB) o;

        return getWhiteCards().equals(that.getWhiteCards());
    }
    @Override
    public int hashCode() {
        return getWhiteCards().hashCode();
    }
    @Override
    public String toString() {
        return "WhiteCardDB{" +
                "whiteCards=" + whiteCards +
                '}';
    }
}
