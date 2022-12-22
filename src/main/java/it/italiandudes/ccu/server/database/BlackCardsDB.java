package it.italiandudes.ccu.server.database;

import it.italiandudes.ccu.common.BlackCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class BlackCardsDB {

    //Attributes
    @NotNull private final ArrayList<BlackCard> blackCards;
    @NotNull private final Character fieldKey;

    //Constructors
    public BlackCardsDB(@NotNull ArrayList<BlackCard> blackCards, @NotNull Character fieldKey){
        this.blackCards = blackCards;
        this.fieldKey = fieldKey;
    }

    //Methods
    @NotNull public ArrayList<BlackCard> getBlackCards(){
        return blackCards;
    }
    @NotNull public Character getFieldKey(){
        return fieldKey;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackCardsDB)) return false;

        BlackCardsDB that = (BlackCardsDB) o;

        if (!getBlackCards().equals(that.getBlackCards())) return false;
        return getFieldKey().equals(that.getFieldKey());
    }
    @Override
    public int hashCode() {
        int result = getBlackCards().hashCode();
        result = 31 * result + getFieldKey().hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "BlackCardsDB{" +
                "blackCards=" + blackCards +
                ", fieldKey=" + fieldKey +
                '}';
    }
}
