package com.nc.controller;

import com.nc.Main;
import com.nc.utils.Utils;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Контроллер для корневого макета rootLayout.fxml
 */
public class RootLayoutController
{

    private Main main;

    public void setMain(Main main)
    {
        this.main = main;
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

