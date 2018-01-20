package com.nc.controller;

import com.nc.Main;
import com.nc.model.Task;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер для макета notification.fxml
 */
public class NotificationController {

    @FXML
    private Label message;

    private static List<Timer> timers = new ArrayList<>();
    private static List<Task> taskList = new ArrayList<>();

    private Stage dialogStage;
    private Main main;
    private ObservableList<Task> taskData = null;
    private int i;
    private boolean flag = false;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        message.setText("");
    }

    /**
     * Устанавливает сцену для этого окна
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Запускает таймеры для актуальных тасков
     */
    public void setNotification() {
        //System.out.println("____________________________");
        //System.out.println(timers.size());
        //System.out.println(taskList.size());
        // Закрытие активных таймеров
        if (!timers.isEmpty()) {
            for (int j = 0; j < timers.size(); j++) {
                timers.get(j).purge();
                timers.get(j).cancel();
            }
            timers.clear();
        }
        taskList.clear();

        // Отбор актуальных тасков
        taskData = main.getTaskData();
        taskData.forEach(task -> {
            if (LocalDateTime.now().isBefore(task.toLocalDateTime())) {
                taskList.add(task);
            }
        });

        //System.out.println("taskList");
        //System.out.println(taskList);

        i = 0;
        for (Task task : taskList) {
            timers.add(new Timer());
            timers.get(i++).schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        message.setText(task.getTitle());
                        dialogStage.showAndWait();

                        if (flag) {
                            flag = false;
                        } else {
                            task.setTime(task.toLocalDateTime().plusMinutes(3).
                                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                            main.showNotification();
                        }
                    });
                }
            }, LocalDateTime.now().until( task.toLocalDateTime(),
                    ChronoUnit.SECONDS ) * 1000);
        }
        //System.out.println(timers.size());
    }

    @FXML
    private void okButton() {
        flag = true;
        dialogStage.close();
    }

    @FXML
    private void delayButton() {
        flag = false;
        dialogStage.close();
    }
}