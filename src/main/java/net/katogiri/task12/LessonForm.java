package net.katogiri.task12;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class LessonForm extends JFrame {
    private JTextField titleField;
    private JTextField descField;
    private JTextField priorityField;
    private JTextField dateField;
    private JTextField subjectField;
    private JTextField roomField;
    private JTextField teacherField;
    private JTextField timeField;
    private MainFrame mainFrame;
    private Lesson editingLesson;

    public LessonForm(MainFrame mainFrame) {
        this(mainFrame, null);
    }

    public LessonForm(MainFrame mainFrame, Lesson lesson) {
        this.mainFrame = mainFrame;
        this.editingLesson = lesson;
        initComponents();
        if (editingLesson != null) fillFields();
    }

    private void initComponents() {
        setTitle(editingLesson == null ? "Добавить занятие" : "Редактировать занятие");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 5, 5));

        add(new JLabel("Название:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Описание:"));
        descField = new JTextField();
        add(descField);

        add(new JLabel("Приоритет (1-5):"));
        priorityField = new JTextField();
        add(priorityField);

        add(new JLabel("Дата (YYYY-MM-DD):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Предмет:"));
        subjectField = new JTextField();
        add(subjectField);

        add(new JLabel("Аудитория:"));
        roomField = new JTextField();
        add(roomField);

        add(new JLabel("Преподаватель:"));
        teacherField = new JTextField();
        add(teacherField);

        add(new JLabel("Время:"));
        timeField = new JTextField();
        add(timeField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveLesson());
        add(saveButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    private void fillFields() {
        titleField.setText(editingLesson.getTitle());
        descField.setText(editingLesson.getDescription());
        priorityField.setText(String.valueOf(editingLesson.getPriority()));
        dateField.setText(editingLesson.getDate().toString());
        subjectField.setText(editingLesson.getSubject());
        roomField.setText(editingLesson.getRoom());
        teacherField.setText(editingLesson.getTeacher());
        timeField.setText(editingLesson.getTimeSlot());
    }

    private void saveLesson() {
        try {
            String title = titleField.getText();
            String desc = descField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            LocalDate date = LocalDate.parse(dateField.getText());
            String subject = subjectField.getText();
            String room = roomField.getText();
            String teacher = teacherField.getText();
            String time = timeField.getText();

            DatabaseManager db = new DatabaseManager();

            if (editingLesson == null) {
                Lesson lesson = new Lesson(title, desc, priority, date, subject, room, teacher, time);
                db.addItem(lesson);
            } else {
                editingLesson.setTitle(title);
                editingLesson.setDescription(desc);
                editingLesson.setPriority(priority);
                editingLesson.setDate(date);
                editingLesson.setSubject(subject);
                editingLesson.setRoom(room);
                editingLesson.setTeacher(teacher);
                editingLesson.setTimeSlot(time);
                db.updateItem(editingLesson);
            }
            mainFrame.refreshData();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }
}
