package com.nc.controller;

import com.nc.Main;
import com.nc.utils.Utils;
import javafx.fxml.FXML;
import javafx.stage.*;

import java.io.File;

/**
 * Контроллер для корневого макета rootLayout.fxml
 */
public class RootLayoutController implements Controller
{

    private Main main;
    private Stage stage;

    public void setParameters(Main main, Stage stage)
    {
        this.main = main;
        this.stage = stage;
    }

    @Override
    public Stage getStage()
    {
        return stage;
    }

    public void onStop()
    {
        Utils.saveAs(main);
    }

    @FXML
    private void handleOpen()
    {
        Utils.openFile(main);
    }

    @FXML
    private void handleSaveAs()
    {
        Utils.saveAs(main);
    }

    @FXML
    private void handleExit()
    {
        Utils.exitAndSaveChanges(main);
    }
}

