package it.italiandudes.ccu.client.javafx;

import it.italiandudes.ccu.CCU;
import javafx.scene.image.Image;

import java.util.Objects;

@SuppressWarnings("unused")
public class JFXDefs {

    public static final class AppAssets {

        public static final String APP_TITLE = "Carte Contro L'Umanita'";
        public static final String APP_ICON_PATH = "/image/app-logo.png";
        public static final Image APP_ICON = new Image(Objects.requireNonNull(CCU.class.getResource(APP_ICON_PATH)).toString());

    }

    public static final class SceneDefs {
        private static final String FXML_PATH = "/fxml/";
        public static final String SCENE_START = FXML_PATH+"";
        public static final String SCENE_LOADING = FXML_PATH+"scene_loading.fxml";
    }

}
