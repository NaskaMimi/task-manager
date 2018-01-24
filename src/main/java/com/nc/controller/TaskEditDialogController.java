package com.nc.controller;

import com.nc.*;
import com.nc.exception.TaskManagerWarning;
import com.nc.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeParseException;

/**
 * Контроллер для макета taskEditDialog.fxml
 */
public class TaskEditDialogController implements Controller
{
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
    private void initialize()
    {
    }

    /**
     * Устанавливает сцену для этого окна и инициализирует поля данными таска для их замены
     */
    public void setParameters(Main main, Stage stage)
    {
        this.dialogStage = stage;
    }

    @Override
    public Stage getStage()
    {
        return dialogStage;
    }

    /**
     * Устанавливает сцену для этого окна и инициализирует поля данными таска для их замены
     */
    public void setParameters(Task task)
    {
        okClicked = false;
        this.task = task;

        titleField.setText(task.getTitle());
        timeField.setText(task.getTime());
        timeField.setPromptText("yyyy-MM-dd HH:mm");
        textArea.setText(task.getText());
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false
     */
    public boolean isOkClicked()
    {
        return okClicked;
    }

    @FXML
    private void okButton()
    {
        if (isInputValid())
        {
            task.setTitle(titleField.getText());
            task.setTime(timeField.getText());
            task.setText(textArea.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void cancelButton()
    {
        dialogStage.close();
    }

    private boolean isInputValid()
    {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0)
        {
            errorMessage += Constants.INCORRECT_NAME;
        }
        if (textArea.getText() == null || textArea.getText().length() == 0)
        {
            errorMessage += Constants.INCORRECT_DESCRIPTION;
        }
        try
        {
            if (timeField.getText() == null || timeField.getText().length() == 0)
            {
                errorMessage += Constants.INCORRECT_TIME;
            }
            else
            {
                if (!Task.validTime(timeField.getText()))
                {
                    errorMessage += Constants.INCORRECT_TIME_FORMAT;
                }
            }
        }
        catch (DateTimeParseException e)
        {
            errorMessage += Constants.INCORRECT_TIME_FORMAT;
        }

        if (errorMessage.length() == 0)
        {
            return true;
        }
        else
        {
            TaskManagerWarning.invalidInputFieldsWarning(errorMessage);
            return false;
        }
    }
}
