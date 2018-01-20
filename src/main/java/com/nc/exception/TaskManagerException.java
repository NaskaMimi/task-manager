package com.nc.exception;

import javafx.scene.control.Alert;

public class TaskManagerException
{
    public static void createException(String title, String headerText, String contentText, Exception e)
    {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
}
