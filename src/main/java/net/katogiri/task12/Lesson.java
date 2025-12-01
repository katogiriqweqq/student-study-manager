package net.katogiri.task12;

import java.time.LocalDate;

public class Lesson extends StudyItem {
    private String room;
    private String teacher;
    private String timeSlot;

    public Lesson(String title, String description, int priority, LocalDate date, String subject,
                  String room, String teacher, String timeSlot) {
        super(title, description, priority, date, subject);
        this.room = room;
        this.teacher = teacher;
        this.timeSlot = timeSlot;
    }

    @Override
    public String getItemType() {
        return "Lesson";
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
