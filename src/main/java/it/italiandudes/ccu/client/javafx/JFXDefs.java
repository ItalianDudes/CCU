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
        private static final String FXML_DIRECTORY = "/fxml/";
        public static final String SCENE_START = FXML_DIRECTORY +"";
        public static final String SCENE_LOADING = FXML_DIRECTORY +"scene_loading.fxml";
    }

    public static final class ResourceDefs {
        public static final String GIF_DIRECTORY = "/gif/";
        public static final String GIF_LOADING = GIF_DIRECTORY+"loading.gif";
    }

}
