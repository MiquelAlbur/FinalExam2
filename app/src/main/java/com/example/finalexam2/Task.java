package com.example.finalexam2;

public class Task {
    private int id;
    private String name, date, done;

    public Task(int id, String name, String date, String done) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}
