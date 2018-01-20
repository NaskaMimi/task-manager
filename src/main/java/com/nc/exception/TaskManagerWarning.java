package com.nc.exception;

import com.nc.Constants;
import javafx.scene.control.Alert;

public class TaskManagerWarning
{
    public static void taskNotChosenWarning()
    {
        createWarning(
                Constants.WARNING,
                Constants.TASK_NOT_CHOSEN,
                Constants.PLEASE_CHOSE_THE_TASK
        );
    }

    public static void invalidInputFieldsWarning(String errorMessage)
    {
        createWarning(
                Constants.INVALID_INPUT_FIELDS,
                Constants.PLEASE_MAKE_CHANGES,
                errorMessage
        );
    }

    private static void createWarning(String title, String headerText, String contentText)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
}
