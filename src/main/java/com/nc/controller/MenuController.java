package com.nc.controller;

import com.nc.Main;
import com.nc.exception.TaskManagerWarning;
import com.nc.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Контроллер для макета menu.fxml
 */
public class MenuController
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

    public MenuController()
    {
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

    public void setMain(Main main)
    {
        this.main = main;

        // Добавление в таблицу данных из списка
        taskTable.setItems(main.getTaskData());
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
            boolean okClicked = main.showTaskEditDialog(selectedTask);
            if (okClicked)
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
        boolean okClicked = main.showTaskEditDialog(temp);
        if (okClicked)
        {
            main.getTaskData().add(temp);
        }
    }
}
