package com.nc.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task
{

    private final StringProperty title;
    private final StringProperty time;
    private final StringProperty text;
    private Boolean read = false;

    public Task()
    {
        this(null, null, null);
    }

    public Task(String title, String text, String timeString)
    {
        this.title = new SimpleStringProperty(title);
        this.time = new SimpleStringProperty(timeString);
        this.text = new SimpleStringProperty(text);
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public void setTime(String timeString)
    {
        this.time.set(timeString);
    }

    public void setText(String text)
    {
        this.text.set(text);
    }

    public StringProperty getTitleProperty()
    {
        return title;
    }

    public StringProperty getTextProperty()
    {
        return text;
    }

    public StringProperty getTimeProperty()
    {
        return time;
    }

    public String getTime()
    {
        return time.get();
    }

    public LocalDateTime toLocalDateTime()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(time.get(), formatter);
    }

    public String getText()
    {
        return text.get();
    }

    public String getTitle()
    {
        return title.get();
    }

    public Boolean getRead()
    {
        return read;
    }

    public void setRead(Boolean read)
    {
        this.read = read;
    }

    public static boolean validTime(String timeString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(timeString, formatter);
        return time != null;
    }
}
