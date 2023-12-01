package com.bichotitas.votacionesmvc.utils;

import com.bichotitas.votacionesmvc.Main;
import com.bichotitas.votacionesmvc.models.StageAndController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class JavaFXUtils {

    private JavaFXUtils() {
    }

    /**
     * @param path        The fxml route relative to the Main.class.getResource
     * @param windowTitle The title to assign to the window
     * @return StageAndController {Stage} - The instance of the stage and the instance of controller specified in the fxml
     */
    public static StageAndController openNewWindow(String path, String windowTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource(path)
        );

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/styles.css")).toString());

        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();

        return new StageAndController() {
            @Override
            public <T> T getController() {
                return fxmlLoader.getController();
            }

            @Override
            public Stage getStage() {
                return stage;
            }
        };
    }
}
