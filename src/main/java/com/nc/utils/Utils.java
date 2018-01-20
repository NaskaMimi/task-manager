package com.nc.utils;

import com.nc.*;
import com.nc.controller.*;
import com.nc.exception.TaskManagerException;
import com.nc.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

import javax.xml.bind.*;
import java.io.*;
import java.util.prefs.Preferences;

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

    public static void loadLastOpenedFileFromRegistry(ObservableList<Task> taskData, Stage primaryStage)
    {
        File file = getTaskFilePath();
        if (file != null)
        {
            loadTaskDataFromFile(file, taskData, primaryStage);
        }
    }

    public static void saveAs(Main main)
    {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null)
        {
            file = correctFileExtensionIfNeed(file);
            saveTaskDataToFile(file, main.getTaskData(), main.getPrimaryStage());
        }
    }

    public static void exitAndSaveChanges(Main main)
    {
        File taskFile = getTaskFilePath();
        if (taskFile != null)
        {
            saveTaskDataToFile(taskFile, main.getTaskData(), main.getPrimaryStage());
        }
        else
        {
            saveAs(main);
        }
        System.exit(0);
    }

    public static void openFile(Main main)
    {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null)
        {
            loadTaskDataFromFile(file, main.getTaskData(), main.getPrimaryStage());
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

    /**
     * Задаёт путь текущему загруженному файлу
     * Путь сохраняется в реестре
     */
    private static void setTaskFilePath(File file, Stage primaryStage)
    {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put("filePath", file.getPath());

        primaryStage.setTitle(Constants.TASK_MANAGER + " - " + file.getName());
    }

    /**
     * Выгружает в память путь последнего загруженного файла
     * Путь хранится в реестре
     */
    private static File getTaskFilePath()
    {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
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

    private static void saveTaskDataToFile(File file, ObservableList<Task> taskData, Stage primaryStage)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.setTasks(taskData);

            m.marshal(wrapper, file);

            setTaskFilePath(file, primaryStage);
        }
        catch (Exception e)
        {
            TaskManagerException.createSavingException(file.getPath(), e);
        }
    }

    private static void loadTaskDataFromFile(File file, ObservableList<Task> taskData, Stage primaryStage)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            TaskListWrapper wrapper = (TaskListWrapper)um.unmarshal(file);

            taskData.clear();
            taskData.addAll(wrapper.getTasks());

            setTaskFilePath(file, primaryStage);
        }
        catch (Exception e)
        {
            TaskManagerException.createLoadingException(file.getPath(), e);
        }
    }

    private static FileChooser createFileChooser()
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    private static File correctFileExtensionIfNeed(File file)
    {
        if (!file.getPath().endsWith(".xml"))
        {
            file = new File(file.getPath() + ".xml");
        }
        return file;
    }
}
