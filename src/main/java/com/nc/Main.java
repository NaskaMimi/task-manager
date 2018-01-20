package com.nc;

import com.nc.controller.*;
import com.nc.model.Task;
import com.nc.utils.Utils;
import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application
{
    private static final String VIEW_TASK_EDIT_DIALOG_FXML = "/view/taskEditDialog.fxml";
    private static final String VIEW_NOTIFICATION_FXML = "/view/notification.fxml";
    private static final String VIEW_MENU_FXML = "/view/menu.fxml";
    private static final String VIEW_ROOT_LAYOUT_FXML = "/view/RootLayout.fxml";

    private BorderPane rootLayout;
    private Stage primaryStage;
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
        taskEditDialogController.getStage().showAndWait();
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
        rootLayout = Utils.createMainWindow(loader, VIEW_ROOT_LAYOUT_FXML, primaryStage, this);
        Utils.loadLastOpenedFileFromRegistry(taskData, primaryStage);
    }

    private void createMenu()
    {
        FXMLLoader loader = new FXMLLoader();
        Utils.createPaneInMainWindow(loader, VIEW_MENU_FXML, rootLayout, this);
    }

    /**
     * Всплывающее окно уведомления
     */
    private void createNotificationWindow()
    {
        NotificationController controller = (NotificationController)Utils.
                createSeparateWindow(
                        new FXMLLoader(),
                        VIEW_NOTIFICATION_FXML,
                        Constants.NOTIFICATION,
                        primaryStage,
                        this
                );
        controller.createTimerForNotifications();
    }

    private void createTaskEditingWindow()
    {
        taskEditDialogController = (TaskEditDialogController)Utils.
                createSeparateWindow(
                        new FXMLLoader(),
                        VIEW_TASK_EDIT_DIALOG_FXML,
                        Constants.TASK_EDITION,
                        primaryStage,
                        this
                );
    }
}
