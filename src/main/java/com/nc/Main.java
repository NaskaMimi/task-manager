package com.nc;

import com.nc.controller.MenuController;
import com.nc.controller.NotificationController;
import com.nc.controller.RootLayoutController;
import com.nc.controller.TaskEditDialogController;
import com.nc.model.Task;
import com.nc.model.TaskListWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    public Main() {
    }

    public ObservableList<Task> getTaskData() {
        return taskData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Менеджер задач");

        initRootLayout();

        showMenu();

        showNotification();
    }

    /**
     * Основная область
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout =  loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMain(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Загрузка последнего открытого файла из реестра
        File file = getTaskFilePath();
        if (file != null) {
            loadTaskDataFromFile(file);
        }
    }

    public void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/menu.fxml"));
            AnchorPane taskOverview = loader.load();

            rootLayout.setCenter(taskOverview);

            MenuController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Всплывающее окно уведомления
     */
    public void showNotification() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/notification.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Уведомление");
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NotificationController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Всплывающее окно редактирования
     *
     * @param task - выделенная задача в списке (или новая)
     * @return
     */
    public boolean showTaskEditDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/taskEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование задачи");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TaskEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTask(task);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Выгружает в память путь последнего загруженного файла
     * Путь хранится в реестре
     *
     * @return
     */
    public File getTaskFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Задаёт путь текущему загруженному файлу
     * Путь сохраняется в реестре
     *
     * @param file
     */
    public void setTaskFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put("filePath", file.getPath());

        primaryStage.setTitle("Менеджер задач - " + file.getName());
    }

    public void loadTaskDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            TaskListWrapper wrapper = (TaskListWrapper) um.unmarshal(file);

            taskData.clear();
            taskData.addAll(wrapper.getTasks());

            setTaskFilePath(file);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось загрузить данные");
            alert.setContentText("Не удалось загрузить данные из \n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void saveTaskDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.setTasks(taskData);

            m.marshal(wrapper, file);

            setTaskFilePath(file);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить данные");
            alert.setContentText("Не удалось сохранить данные в \n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void stop(){
        File taskFile = getTaskFilePath();
        if (taskFile != null) {
            saveTaskDataToFile(taskFile);
        }

        System.exit(0);
    }
}
