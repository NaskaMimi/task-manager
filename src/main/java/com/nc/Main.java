package com.nc;

import com.nc.controller.*;
import com.nc.model.Task;
import com.nc.utils.Utils;
import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application
{
    private Stage primaryStage;
    private BorderPane rootLayout;
    private Stage taskEditDialogStage;
    private TaskEditDialogController taskEditDialogController;
    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.TASK_MANAGER);

        initRootLayout();

        createMenu();
        createNotificationWindow();
        createTaskEditingWindow();
    }

    @Override
    public void stop()
    {
        Utils.exitAndSaveChanges(this);
    }

    /**
     * Всплывающее окно редактирования
     */
    public boolean isTaskEditionDialogShowing(Task task)
    {
        taskEditDialogController.setParameters(task);
        taskEditDialogStage.showAndWait();
        return taskEditDialogController.isOkClicked();
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public ObservableList<Task> getTaskData()
    {
        return taskData;
    }

    /**
     * Основная область
     */
    private void initRootLayout()
    {
        FXMLLoader loader = new FXMLLoader();
        //TODO Строгий каст - нехорошо, но пока не знаю, как по-другому это сделать
        rootLayout = (BorderPane)Utils.createPaneFromFXML(loader, "/view/RootLayout.fxml");

        if (rootLayout != null)
        {
            RootLayoutController controller = loader.getController();
            controller.setMain(this);

            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();

            // Загрузка последнего открытого файла из реестра
            Utils.loadLastOpenedFileFromRegistry(taskData, primaryStage);
        }
    }

    private void createMenu()
    {
        FXMLLoader loader = new FXMLLoader();
        Pane taskOverview = Utils.createPaneFromFXML(loader, "/view/menu.fxml");

        rootLayout.setCenter(taskOverview);

        MenuController controller = loader.getController();
        controller.setMain(this);
    }

    /**
     * Всплывающее окно уведомления
     */
    private void createNotificationWindow()
    {
        FXMLLoader loader = new FXMLLoader();
        Pane pane = Utils.createPaneFromFXML(loader, "/view/notification.fxml");
        Stage dialogStage = createModalWindow(Constants.NOTIFICATION, pane);

        NotificationController controller = loader.getController();
        controller.setParameters(this, dialogStage);
        controller.createTimerForNotifications();
    }

    private void createTaskEditingWindow()
    {
        FXMLLoader loader = new FXMLLoader();
        Pane pane = Utils.createPaneFromFXML(loader, "/view/taskEditDialog.fxml");
        Stage dialogStage = createModalWindow(Constants.TASK_EDITION, pane);

        TaskEditDialogController controller = loader.getController();
        controller.setStage(dialogStage);

        taskEditDialogStage = dialogStage;
        taskEditDialogController = controller;
    }

    private Stage createModalWindow(String title, Pane pane)
    {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setScene(new Scene(pane));
        return dialogStage;
    }
}
