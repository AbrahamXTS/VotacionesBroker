package com.bichotitas.votacionesmvc;

import com.bichotitas.votacionesmvc.utils.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Logger.log(this.getClass().getSimpleName(), "Initializing the main view");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/votes-count.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/styles.css")).toString());

        stage.setTitle("Sistema de votaciones MVC");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }
}