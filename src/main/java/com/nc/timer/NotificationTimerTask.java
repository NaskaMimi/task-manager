package com.nc.timer;

import com.nc.Main;
import com.nc.model.Task;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.*;

public class NotificationTimerTask extends TimerTask
{
    private Main main;

    @Override
    public void run()
    {
        System.out.println("TimerTask начал свое выполнение в:" + new Date());
//        completeTask();
//        System.out.println("TimerTask закончил свое выполнение в:" + new Date());
        if(main !=null)
        {
            ObservableList<Task> taskData = main.getTaskData();
            List<Task> actualTaskList = new ArrayList<>();
            taskData.forEach(task -> {
                if (LocalDateTime.now().isBefore(task.toLocalDateTime())) {
                    actualTaskList.add(task);
                }
            });
            if(actualTaskList.size()>0)
            {
                System.out.println("TimerTask222 начал свое выполнение в:" + new Date());
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Уведомление");
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Уведомление");
//                alert.setContentText(actualTaskList.get(0).getTitle());
//
//                alert.showAndWait();
            }
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

//    private void completeTask() {
//        try {
//            // допустим, выполнение займет 20 секунд
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
