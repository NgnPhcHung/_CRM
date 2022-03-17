package CRM_APP.Model;

import java.time.LocalDate;

public class Task {
    public static String id;
    public static String task;
    public static String status;
    public static String color;
    public static LocalDate date;
    public static LocalDate end;

    public Task()
    {}
    public Task(String color, String task, LocalDate date) {
        this.color = color;
        this.task = task;
        this.date = date;
    }

    public static LocalDate getEnd() {
        return end;
    }

    public static void setEnd(LocalDate end) {
        Task.end = end;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Task.status = status;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Task.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
