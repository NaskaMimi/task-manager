package com.nc.exception;

import javafx.scene.control.Alert;

public class TaskManagerWarning
{
    public static void createWarning(String title, String headerText, String contentText)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
}
