package net.katogiri.task12;

import java.time.LocalDate;

public class Exam extends StudyItem {
    private String examType;
    private String room;
    private int durationMinutes;

    public Exam(String title, String description, int priority, LocalDate date, String subject,
                String examType, String room, int durationMinutes) {
        super(title, description, priority, date, subject);
        this.examType = examType;
        this.room = room;
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String getItemType() {
        return "Exam";
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
