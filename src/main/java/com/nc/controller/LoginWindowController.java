package com.nc.controller;

import com.nc.Main;
import com.nc.exception.TaskManagerException;
import com.nc.exception.TaskManagerWarning;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController implements Controller
{
    @FXML
    private TextField loginField;

    private Stage dialogStage;
    private boolean okClicked = false;

    @Override
    public void setParameters(Main main, Stage stage)
    {
        this.dialogStage = stage;
    }

    @Override
    public Stage getStage()
    {
        return null;
    }

    @FXML
    private void okButton()
    {
        if (!loginField.getText().equals("")) {
            Main.USER_NAME = loginField.getText();
            okClicked = true;
            dialogStage.close();
        }
        else
        {
            TaskManagerWarning.createLoginWarning();
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
