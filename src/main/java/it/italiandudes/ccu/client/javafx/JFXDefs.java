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
        public static final String SCENE_STARTUP_1 = FXML_DIRECTORY +"ServerSelectionStage.fxml";
        public static final String SCENE_STARTUP_2 = FXML_DIRECTORY+"ServerSelectionListStage.fxml";
        public static final String SCENE_LOADING = FXML_DIRECTORY +"SceneLoading.fxml";
    }

    public static final class ResourceDefs {
        public static final String GIF_DIRECTORY = "/gif/";
        public static final String GIF_LOADING = GIF_DIRECTORY+"loading.gif";
    }

    public static final class StylesDefs {
        public static final class Colors {
            public static final String NOT_CLICKABLE = "#777777";
            public static final String TEXT_COLOR = "#999999";
            public static final String ERROR_COLOR = "#FF4040";
        }
        public static final String ERROR_TXT_BORDER_COLOR = "-fx-border-color: "+Colors.ERROR_COLOR+";";
        public static final String BTN_OFF = "-fx-background-color: "+Colors.NOT_CLICKABLE+";\n" +
                                         "    -fx-text-fill: "+Colors.TEXT_COLOR+";";
    }
}
