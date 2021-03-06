package com.nc.controller;

import com.nc.*;
import com.nc.exception.TaskManagerWarning;
import com.nc.model.Task;
import com.nc.socket.TaskManagerSocket;
import com.nc.utils.Utils;
import javafx.animation.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Контроллер для макета notification.fxml
 */

public class NotificationController implements Controller
{

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> timeColumn;

    private Stage notificationDialogStage;
    private Main main;
    private Task selectedTask;

    public void setParameters(Main main, Stage dialogStage)
    {
        this.main = main;
        this.notificationDialogStage = dialogStage;
    }

    @Override
    public Stage getStage()
    {
        return notificationDialogStage;
    }

    @FXML
    private void initialize()
    {
        // Инициализация таблицы с двумя столбцами
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());

        // Делаем таску выделенной по клику
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectTask(newValue));
    }

    @FXML
    private void okButton()
    {
        if (selectedTask != null)
        {
            selectedTask.setRead(true);
            Utils.initResponseTaskData(TaskManagerSocket.sendDataToServer("edit",  selectedTask), main);
            notificationDialogStage.close();
        }
        else
        {
            TaskManagerWarning.taskNotChosenWarning();
        }
    }

    @FXML
    private void delayButton()
    {
        if (selectedTask != null)
        {
            selectedTask.setTime(selectedTask.toLocalDateTime().plusMinutes(1).
                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            Utils.initResponseTaskData(TaskManagerSocket.sendDataToServer("edit",  selectedTask), main);
            notificationDialogStage.close();
        }
        else
        {
            TaskManagerWarning.taskNotChosenWarning();
        }
    }

    public void createTimerForNotifications()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                ev -> createNotificationWindowIfNeed()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void selectTask(Task task)
    {
        selectedTask = task;
    }

    private void createNotificationWindowIfNeed()
    {
        if (defineActualTasks().size() > 0)
        {
            taskTable.setItems(defineActualTasks());
            if (!notificationDialogStage.isShowing())
            {
                notificationDialogStage.show();
            }
        }
    }

    private ObservableList<Task> defineActualTasks()
    {
        ObservableList<Task> taskList = FXCollections.observableArrayList();
        ObservableList<Task> taskData = main.getTaskData();
        taskData.forEach(task -> {
            if (LocalDateTime.now().isBefore(task.toLocalDateTime()) // дедлайн задачи в будущем
                    && task.toLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(1)) // время до дедлайна - 1 минута
                    && !task.getRead() // пользователь еще не получал уведомление о задаче
                    )
            {
                taskList.add(task);
            }
        });
        return taskList;
    }
}