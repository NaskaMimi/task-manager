package com.nc.controller;

import com.nc.Main;
import javafx.stage.Stage;

public interface Controller
{
    void setParameters(Main main, Stage stage);

    Stage getStage();
}
