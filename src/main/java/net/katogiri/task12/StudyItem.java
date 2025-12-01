package net.katogiri.task12;

import java.time.LocalDate;

public abstract class StudyItem {
    protected int id;
    protected String title;
    protected String description;
    protected int priority;
    protected LocalDate date;
    protected String subject;

    public StudyItem(String title, String description, int priority, LocalDate date, String subject) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.subject = subject;
    }

    public abstract String getItemType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
