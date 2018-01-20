package com.nc.controller;

import com.nc.exception.TaskManagerWarning;
import com.nc.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.format.DateTimeParseException;

/**
 * Контроллер для макета taskEditDialog.fxml
 */
public class TaskEditDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField timeField;
    @FXML
    private TextArea textArea;


    private Stage dialogStage;
    private Task task;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
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
     * Инициализирует поля данными таска для их замены
     *
     * @param task
     */
    public void setTask(Task task) {
        this.task = task;

        titleField.setText(task.getTitle());
        timeField.setText(task.getTime());
        timeField.setPromptText("yyyy-MM-dd HH:mm");
        textArea.setText(task.getText());
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void okButton() {
        if (isInputValid()) {
            task.setTitle(titleField.getText());
            task.setTime(timeField.getText());
            task.setText(textArea.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void cancelButton() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "Некорректное название!\n";
        }
        if (textArea.getText() == null || textArea.getText().length() == 0) {
            errorMessage += "Некорректный текст!\n";
        }
        try {
            if (timeField.getText() == null || timeField.getText().length() == 0) {
                errorMessage += "Некорректное время!\n";
            } else {
                if (!task.validTime(timeField.getText())) {
                }
            }
        } catch (DateTimeParseException e) {
            errorMessage += "Некорректное время. Используйте формат yyyy-MM-dd HH:mm!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            TaskManagerWarning.createWarning(
                    "Неверно заданы поля!",
                    "Пожалуйста, внесите изменения",
                    errorMessage
            );

            return false;
        }

    }
}
