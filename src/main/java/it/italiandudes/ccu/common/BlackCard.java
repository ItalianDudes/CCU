package it.italiandudes.ccu.common;

import org.jetbrains.annotations.NotNull;

public final class BlackCard extends GameCard {

    //Attributes
    private int fieldAmount;

    //Constructors
    public BlackCard(@NotNull Integer cardID, @NotNull String content, @NotNull Character keyChar) {
        super(cardID, content);

        fieldAmount = 0;
        for(int i=0; i<content.length(); i++){
            if(content.charAt(i)==keyChar){
                if(i>0){
                    if(content.charAt(i-1)!='\\')
                        fieldAmount++;
                }else{
                    fieldAmount++;
                }
            }
        }

        if(fieldAmount == 0)
            throw new RuntimeException("Black Card #"+cardID+" it's invalid: doesn't contain empty fields");
    }

    //Methods
    public int getFieldAmount(){
        return fieldAmount;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackCard)) return false;
        if (!super.equals(o)) return false;

        BlackCard blackCard = (BlackCard) o;

        return getFieldAmount() == blackCard.getFieldAmount();
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getFieldAmount();
        return result;
    }
    @Override
    public String toString() {
        return "BlackCard{" +
                "fieldAmount=" + fieldAmount +
                "} " + super.toString();
    }
}
