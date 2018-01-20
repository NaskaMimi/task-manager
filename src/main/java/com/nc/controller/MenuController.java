package com.nc.controller;

import com.nc.Main;
import com.nc.exception.TaskManagerWarning;
import com.nc.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Контроллер для макета menu.fxml
 */
public class MenuController implements Controller
{
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> timeColumn;
    @FXML
    private Label titleLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label textLabel;

    private Main main;
    private Stage stage;

    public void setParameters(Main main, Stage stage)
    {
        this.main = main;
        this.stage = stage;
        // Добавление в таблицу данных из списка
        taskTable.setItems(main.getTaskData());
    }

    @Override
    public Stage getStage()
    {
        return stage;
    }

    /**
     * Инициализация класса-контроллера.
     */
    @FXML
    private void initialize()
    {
        // Инициализация таблицы с двумя столбцами
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());

        showTaskDetails(null);

        // Обновление таблицы при изменении
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));
    }

    @FXML
    private void deleteButton()
    {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0)
        {
            taskTable.getItems().remove(selectedIndex);
        }
        else
        {
            TaskManagerWarning.taskNotChosenWarning();
        }
    }

    @FXML
    private void editButton()
    {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null)
        {
            if (main.isTaskEditionDialogShowing(selectedTask))
            {
                showTaskDetails(selectedTask);
            }
        }
        else
        {
            TaskManagerWarning.taskNotChosenWarning();
        }
    }

    @FXML
    private void newButton()
    {
        Task temp = new Task();
        if (main.isTaskEditionDialogShowing(temp))
        {
            main.getTaskData().add(temp);
        }
    }

    /**
     * Инициализация полей выделенным таском
     */
    private void showTaskDetails(Task task)
    {
        if (task != null)
        {
            titleLabel.setText(task.getTitle());
            timeLabel.setText(task.getTime());
            textLabel.setText(task.getText());
        }
        else
        {
            titleLabel.setText("");
            timeLabel.setText("");
            textLabel.setText("");
        }
    }
}
