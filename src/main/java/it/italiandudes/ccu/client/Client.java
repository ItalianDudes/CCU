package it.italiandudes.ccu.client;

import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.controller.ControllerSceneLoading;
import it.italiandudes.ccu.client.javafx.controller.ServerSelectionController;
import it.italiandudes.ccu.client.javafx.controller.ServerSelectionListController;
import it.italiandudes.ccu.client.javafx.scene.SceneLoading;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionListModel;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionModel;
import it.italiandudes.idl.common.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public final class Client extends Application {

    //Stage
    private static Stage stage;
    private static Stage loadingStage;
    private static ControllerSceneLoading loadingController;

    //Graphic Start
    @Override
    public void start(Stage stage) throws Exception {

        Client.stage = stage;
        Client.loadingStage = new Stage();
        loadingStage.setScene(SceneLoading.getScene());
        loadingController = SceneLoading.getController();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setResizable(false);

        Service<Void> clientLauncher = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("Inside service");
                        Platform.runLater(()->loadingController.show(Defs.LoadingTexts.LOADING_PROPERTIES_TEXT));
                        System.out.println("Loading stage shown");
                        ClientSingleton.getInstance().loadProperties();
                        System.out.println("Properties loaded");
                        if(ClientSingleton.getInstance().isServerEmpty()){
                            Platform.runLater(()->{
                                try{
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));

                                    Parent root = loader.load();

                                    Scene scene = new Scene(root);

                                    ServerSelectionController controller = loader.getController();
                                    controller.setModel(new ServerSelectionModel());

                                    stage.setScene(scene);
                                    stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
                                    stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
                                    loadingStage.close();
                                    System.out.println("Loading stage closed");
                                    stage.show();
                                } catch (IOException e) {
                                    loadingStage.close();
                                    throw new RuntimeException(e);
                                }
                            });
                        }else{
                            Platform.runLater(()->{
                                try{
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_2));

                                    Parent root = loader.load();

                                    Scene scene = new Scene(root);

                                    ServerSelectionListController controller = loader.getController();
                                    System.out.println("Controller: "+controller);
                                    controller.setModel(new ServerSelectionListModel());

                                    stage.setScene(scene);
                                    stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
                                    stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
                                    loadingStage.close();
                                    System.out.println("Loading stage closed");
                                    stage.show();
                                } catch (IOException e) {
                                    loadingStage.close();
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                        return null;
                    }
                };
            }
        };
        System.out.println("Service to be launched");
        clientLauncher.start();
    }

    //Get Stage Method
    public static Stage getStage(){
        return stage;
    }
    //Get Loading Stage Method
    public static Stage getLoadingStage(){
        return loadingStage;
    }
    //Get Loading Controller Method
    public static ControllerSceneLoading getLoadingController(){
        return loadingController;
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

    //Constants
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

        public static final class LoadingTexts{
            public static final String LOADING_PROPERTIES_TEXT = "Properties are being loaded";
            public static final String DELETING_SERVER_TEXT = "Deleting server";
            public static final String REQUEST_PWD_TEXT = "Password entry check";
            public static final String PWD_CONTROL_TEXT = "Checking the password";
            public static final String NAME_REQUEST_LOADING_TEXT = "Name request panel loading";
            public static final String CHECKING_NAME_VALIDITY_TEXT = "Checking name validity";
            public static final String LOADIG_LOBBY_TEXT = "Loading the lobby";
        }
    }

}
