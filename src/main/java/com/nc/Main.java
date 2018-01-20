package com.nc;

import com.nc.controller.*;
import com.nc.exception.TaskManagerException;
import com.nc.model.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

import javax.xml.bind.*;
import java.io.*;
import java.util.prefs.Preferences;

public class Main extends Application
{

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Task> taskData = FXCollections.observableArrayList();
    private RootLayoutController controller;

    public Main()
    {
    }

    public ObservableList<Task> getTaskData()
    {
        return taskData;
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.TASK_MANAGER);

        initRootLayout();

        showMenu();

        showNotification();
    }

    /**
     * Основная область
     */
    private void initRootLayout()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            controller = loader.getController();
            controller.setMain(this);

            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Загрузка последнего открытого файла из реестра
        File file = getTaskFilePath();
        if (file != null)
        {
            loadTaskDataFromFile(file);
        }
    }

    private void showMenu()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/menu.fxml"));
            AnchorPane taskOverview = loader.load();

            rootLayout.setCenter(taskOverview);

            MenuController controller = loader.getController();
            controller.setMain(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Всплывающее окно уведомления
     */
    public void showNotification()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/notification.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(Constants.NOTIFICATION);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NotificationController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.createTimerForNotifications();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    /**
     * Всплывающее окно редактирования
     */
    public boolean showTaskEditDialog(Task task)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/taskEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(Constants.TASK_EDITION);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TaskEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTask(task);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Выгружает в память путь последнего загруженного файла
     * Путь хранится в реестре
     */
    public File getTaskFilePath()
    {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String filePath = prefs.get("filePath", null);
        if (filePath != null)
        {
            return new File(filePath);
        }
        else
        {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу
     * Путь сохраняется в реестре
     */
    private void setTaskFilePath(File file)
    {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put("filePath", file.getPath());

        primaryStage.setTitle(Constants.TASK_MANAGER+ " - " + file.getName());
    }

    public void loadTaskDataFromFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            TaskListWrapper wrapper = (TaskListWrapper)um.unmarshal(file);

            taskData.clear();
            taskData.addAll(wrapper.getTasks());

            setTaskFilePath(file);
        }
        catch (Exception e)
        {
            TaskManagerException.createLoadingException(file.getPath(), e);
        }
    }

    public void saveTaskDataToFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.setTasks(taskData);

            m.marshal(wrapper, file);

            setTaskFilePath(file);
        }
        catch (Exception e)
        {
            TaskManagerException.createSavingException(file.getPath(), e);
        }
    }

    public void stop()
    {
        File taskFile = getTaskFilePath();
        if (taskFile != null) {
            saveTaskDataToFile(taskFile);
        } else {
            controller.onStop();
        }

        System.exit(0);
    }
}
