package it.italiandudes.ccu.client.models;

import it.italiandudes.ccu.client.annotations.LogicalClass;
import it.italiandudes.ccu.client.annotations.LogicalOperation;
import org.jetbrains.annotations.NotNull;

@LogicalClass
public class Server {
    private String cname;
    private String alias;
    private String pwd;

    @LogicalOperation
    public Server(@NotNull String cname, @NotNull String alias, @NotNull String pwd){
        this.cname=cname;
        this.alias=alias;
        this.pwd=pwd;
    }

    @LogicalOperation
    public String getCname(){
        return cname;
    }
    @LogicalOperation
    public String getAlias(){
        return alias;
    }
    @LogicalOperation
    public String getPwd(){
        return pwd;
    }
}
