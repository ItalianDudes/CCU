package it.italiandudes.ccu.client.models.renderer;

import it.italiandudes.ccu.client.annotations.LogicalClass;
import it.italiandudes.ccu.client.annotations.LogicalOperation;

@LogicalClass
public class LobbyRendererModel {
    private String username;
    private boolean isReady;

    @LogicalOperation
    public void setUsername(String username){
        this.username=username;
    }
    @LogicalOperation
    public void setReady(boolean isReady){
        this.isReady=isReady;
    }

    @LogicalOperation
    public String getUsername(){
        return username;
    }
    @LogicalOperation
    public boolean isReady(){
        return isReady;
    }
}
