package it.italiandudes.ccu.client;

import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.controller.ServerSelectionController;
import it.italiandudes.ccu.client.models.ServerSelectionModel;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Client extends Application {

    //Graphic Start
    @Override
    public void start(Stage stage) throws Exception {
        ClientSingleton.getInstance().loadProperties();

        if(ClientSingleton.getInstance().isServerEmpty()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            ServerSelectionController controller = loader.getController();
            controller.setModel(new ServerSelectionModel());

            stage.setScene(scene);
            stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
            stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
            stage.show();
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_2));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            ServerSelectionController controller = loader.getController();
            controller.setModel(new ServerSelectionModel());

            stage.setScene(scene);
            stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
            stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
            stage.show();
        }
    }

    //Start Methods
    public static int start(String[] args){
        launch(args);
        return 0;
    }
    public static int noGuiStart(String[] args){
        Logger.log("Not implemented yet");
        return 0;
    }

    public static final class Defs{
        public static final class Path {
            public static final String CLIENT_DIR = "client/";
            public static final String CONFIG_FILE = CLIENT_DIR+"config.cfg";
            public static final class Resource {

            }
        }

        public static final class Config {
            public static final String USER_KEYWORD = "user";
            public static final String SERVERS_KEYWORD = "servers";
            public static final String LANG_KEYWORD = "language";
        }
    }

}
