package com.nc.controller;

import com.nc.Main;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Контроллер для корневого макета rootLayout.fxml
 */
public class RootLayoutController {

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    public void onStop()
    {
        handleSaveAs();
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null) {
            main.loadTaskDataFromFile(file);
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveTaskDataToFile(file);
        }
    }


    @FXML
    private void handleExit() {
        File taskFile = main.getTaskFilePath();
        if (taskFile != null) {
            main.saveTaskDataToFile(taskFile);
        } else {
            handleSaveAs();
        }

        System.exit(0);
    }
}

