package it.italiandudes.ccu.client.renderers;

import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.models.renderer.LobbyRendererModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;

/**
 * This is a custom List Cell Renderer created specifically for the Lobby Stage's list view.
 * */
public class LobbyListRenderer implements Callback<ListView<LobbyRendererModel>, ListCell<LobbyRendererModel>> {
    @Override
    public ListCell<LobbyRendererModel> call(ListView<LobbyRendererModel> param) {
        return new ListCell<LobbyRendererModel>(){
            @Override
            public void updateItem(LobbyRendererModel model, boolean empty){
                super.updateItem(model,empty);

                if(empty || model==null){
                    setGraphic(null);
                }else{
                    HBox hBox = new HBox();

                    Label username = new Label(model.getUsername());
                    if(model.getUsername().equals(ClientSingleton.getInstance().getUser().getUsername())){
                        username.setStyle(JFXDefs.StylesDefs.USERNAME_COLOR);
                    }

                    SVGPath readyCheck = new SVGPath();
                    readyCheck.setContent(JFXDefs.ResourceDefs.SVGS_PATHS.READY_CHECK);
                    readyCheck.setFill(Color.web(JFXDefs.StylesDefs.Colors.READY_GREEN));

                    readyCheck.setVisible(model.isReady());
                    HBox.setHgrow(readyCheck, Priority.ALWAYS);

                    hBox.getChildren().addAll(username,readyCheck);
                }
            }
        };
    }
}
