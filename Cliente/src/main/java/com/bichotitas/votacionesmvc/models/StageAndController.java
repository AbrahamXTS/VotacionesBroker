package com.bichotitas.votacionesmvc.models;

import javafx.stage.Stage;

public interface StageAndController {
    <T> T getController();

    Stage getStage();
}