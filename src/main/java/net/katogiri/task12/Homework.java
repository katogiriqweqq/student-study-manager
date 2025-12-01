package net.katogiri.task12;

import java.time.LocalDate;

public class Homework extends StudyItem {
    private LocalDate dueDate;
    private boolean completed;
    private String difficulty;

    public Homework(String title, String description, int priority, LocalDate date, String subject,
                    LocalDate dueDate, boolean completed, String difficulty) {
        super(title, description, priority, date, subject);
        this.dueDate = dueDate;
        this.completed = completed;
        this.difficulty = difficulty;
    }

    @Override
    public String getItemType() {
        return "Homework";
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
