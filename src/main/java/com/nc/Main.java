package com.nc;

import com.nc.controller.*;
import com.nc.model.Task;
import com.nc.socket.TaskManagerSocket;
import com.nc.utils.Utils;
import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends Application
{
    private static final String VIEW_TASK_EDIT_DIALOG_FXML = "/view/taskEditDialog.fxml";
    private static final String VIEW_NOTIFICATION_FXML = "/view/notification.fxml";
    private static final String VIEW_MENU_FXML = "/view/menu.fxml";
    private static final String VIEW_ROOT_LAYOUT_FXML = "/view/rootLayout.fxml";
    private static final String VIEW_LOGIN_WINDOW_FXML = "/view/login.fxml";

    public static String USER_NAME = "Unknown";

    private BorderPane rootLayout;
    private Stage primaryStage;
    private TaskEditDialogController taskEditDialogController;
    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    /*public static List<Task> main(String action, Object task)
    {
        List<Task> request = new ArrayList<>();
        try (Socket socket = new Socket("localhost", 11111)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            HashMap<String, Object> map = new HashMap<>();
            map.put(action, task);

            outputStream.writeObject(map);
            outputStream.flush();

            request = (List<Task>) inputStream.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return request;
    }*/

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.TASK_MANAGER);

        if (createLoginWindow()) {
            initRootLayout();

            createMenu();
            createNotificationWindow();
            createTaskEditingWindow();
        }
    }

    @Override
    public void stop()
    {
        Utils.exitAndSaveChanges();
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

    private boolean createLoginWindow()
    {
        FXMLLoader loader = new FXMLLoader();
        return Utils.createLoginWindow(loader, VIEW_LOGIN_WINDOW_FXML, Constants.LOGIN,this);
    }

    /**
     * Основная область
     */
    private void initRootLayout()
    {
        FXMLLoader loader = new FXMLLoader();
        rootLayout = Utils.createMainWindow(loader, VIEW_ROOT_LAYOUT_FXML, primaryStage, this);
        Utils.loadFile(this);
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
