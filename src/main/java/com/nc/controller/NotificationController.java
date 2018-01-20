package com.nc.controller;

import com.nc.Main;
import com.nc.model.Task;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Контроллер для макета notification.fxml
 */
/**
 * На данный момент окошко нотификации простое:
 * все таски выводятся в лейбл, и при нажатии на кнопку Ок/Отложить
 * они все либо помечаются, как прочитанные, либо меняется их время
 * TODO Можно усложнить логику, заменив Label(titleLabel) в notification.fxml на TableView, чтобы при выборе определенной таски можно было работать именно с ней
 *
 */
public class NotificationController
{

    @FXML
    private Label titleLabel;

    private Stage notificationDialogStage;
    private Main main;

    public void setMain(Main main)
    {
        this.main = main;
    }

    @FXML
    private void initialize()
    {
    }

    public void setDialogStage(Stage dialogStage)
    {
        this.notificationDialogStage = dialogStage;
    }

    public void createTimerForNotifications()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                ev -> createNotificationWindowIfNeed()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void createNotificationWindowIfNeed()
    {
        List<String> titleList = defineActualTasks().stream().map(Task::getTitle).collect(Collectors.toList());
        if (titleList.size() > 0)
        {
            String contentText = String.join("\n", titleList);
            titleLabel.setText(contentText);
            if (!notificationDialogStage.isShowing())
            {
                notificationDialogStage.show();
            }
        }
    }


    private List<Task> defineActualTasks()
    {
        List<Task> taskList = new ArrayList<>();
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

    @FXML
    private void okButton()
    {
        main.getTaskData().forEach(task -> task.setRead(true));
        notificationDialogStage.close();
    }

    @FXML
    private void delayButton()
    {
        main.getTaskData().forEach(task -> task.setTime(task.toLocalDateTime().plusMinutes(3).
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        notificationDialogStage.close();
    }
}