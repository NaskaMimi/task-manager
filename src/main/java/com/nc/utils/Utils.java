package com.nc.utils;

import com.nc.*;
import com.nc.controller.*;
import com.nc.model.*;
import com.nc.socket.TaskManagerSocket;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.util.List;

public class Utils
{
    public static BorderPane createMainWindow(FXMLLoader loader, String filePath, Stage primaryStage, Main main)
    {
        //TODO Строгий каст - нехорошо, но пока не знаю, как по-другому это сделать
        BorderPane pane = (BorderPane)createPaneFromFXML(loader, filePath);
        RootLayoutController controller = loader.getController();
        controller.setParameters(main, null);
        if (pane != null)
        {
            primaryStage.setScene(new Scene(pane));
        }
        primaryStage.show();
        return pane;
    }

    public static void createPaneInMainWindow(FXMLLoader loader, String filePath, BorderPane rootLayout, Main main)
    {
        Pane taskOverview = createPaneFromFXML(loader, filePath);

        rootLayout.setCenter(taskOverview);

        Controller controller = loader.getController();
        controller.setParameters(main, null);
    }

    public static boolean createLoginWindow(FXMLLoader loader, String filePath, String title, Main main)
    {
        Pane pane = createPaneFromFXML(loader, filePath);
        Stage dialogStage = createModalWindow(title, pane);
        LoginWindowController controller = loader.getController();
        controller.setParameters(main, dialogStage);
        dialogStage.showAndWait();

        return controller.isOkClicked();
    }

    public static Controller createSeparateWindow(FXMLLoader loader, String filePath, String title, Stage owner, Main main)
    {
        Pane pane = createPaneFromFXML(loader, filePath);
        Stage dialogStage = createModalWindow(title, pane, owner);
        Controller controller = loader.getController();
        controller.setParameters(main, dialogStage);

        return controller;
    }

    private static Pane createPaneFromFXML(FXMLLoader loader, String filePath)
    {
        loader.setLocation(Main.class.getResource(filePath));
        try
        {
            return loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static Stage createModalWindow(String title, Pane pane, Stage owner)
    {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setScene(new Scene(pane));
        return dialogStage;
    }

    private static Stage createModalWindow(String title, Pane pane)
    {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(pane));
        return dialogStage;
    }

    public static void loadFile(Main main)
    {
        main.getTaskData().addAll(TaskManagerSocket.sendDataToServer("load", Main.USER_NAME));
    }

    public static void saveFile()
    {
        TaskManagerSocket.sendDataToServer("save", null);
    }

    public static void exitAndSaveChanges()
    {
        TaskManagerSocket.sendDataToServer("save", null);
        System.exit(0);
    }

    /**
     * Обновляет taskData после получаения ответа от сервера
     */
    public static void initResponseTaskData(List<Task> list, Main main)
    {
        main.getTaskData().clear();
        main.getTaskData().addAll(list);
    }
}
