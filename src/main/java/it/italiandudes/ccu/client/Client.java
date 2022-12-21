package it.italiandudes.ccu.client;

import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Client extends Application {

    //Graphic Start
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_START));

        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
        stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
        stage.show();

    }

    //Start Methods
    public static void start(String[] args){
        launch(args);
    }
    public static void noGuiStart(String[] args){
        Logger.log("Not implemented yet");
    }

}
