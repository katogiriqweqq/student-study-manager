package net.katogiri.task12;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ExamForm extends JFrame {
    private JTextField titleField;
    private JTextField descField;
    private JTextField priorityField;
    private JTextField dateField;
    private JTextField subjectField;
    private JTextField typeField;
    private JTextField roomField;
    private JTextField durationField;
    private MainFrame mainFrame;
    private Exam editingExam;

    public ExamForm(MainFrame mainFrame) {
        this(mainFrame, null);
    }

    public ExamForm(MainFrame mainFrame, Exam exam) {
        this.mainFrame = mainFrame;
        this.editingExam = exam;
        initComponents();
        if (editingExam != null) fillFields();
    }

    private void initComponents() {
        setTitle(editingExam == null ? "Добавить экзамен" : "Редактировать экзамен");
        setSize(400, 420);
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

        add(new JLabel("Тип экзамена:"));
        typeField = new JTextField();
        add(typeField);

        add(new JLabel("Аудитория:"));
        roomField = new JTextField();
        add(roomField);

        add(new JLabel("Длительность (мин):"));
        durationField = new JTextField();
        add(durationField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveExam());
        add(saveButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    private void fillFields() {
        titleField.setText(editingExam.getTitle());
        descField.setText(editingExam.getDescription());
        priorityField.setText(String.valueOf(editingExam.getPriority()));
        dateField.setText(editingExam.getDate().toString());
        subjectField.setText(editingExam.getSubject());
        typeField.setText(editingExam.getExamType());
        roomField.setText(editingExam.getRoom());
        durationField.setText(String.valueOf(editingExam.getDurationMinutes()));
    }

    private void saveExam() {
        try {
            String title = titleField.getText();
            String desc = descField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            LocalDate date = LocalDate.parse(dateField.getText());
            String subject = subjectField.getText();
            String type = typeField.getText();
            String room = roomField.getText();
            int duration = Integer.parseInt(durationField.getText());

            DatabaseManager db = new DatabaseManager();

            if (editingExam == null) {
                Exam exam = new Exam(title, desc, priority, date, subject, type, room, duration);
                db.addItem(exam);
            } else {
                editingExam.setTitle(title);
                editingExam.setDescription(desc);
                editingExam.setPriority(priority);
                editingExam.setDate(date);
                editingExam.setSubject(subject);
                editingExam.setExamType(type);
                editingExam.setRoom(room);
                editingExam.setDurationMinutes(duration);
                db.updateItem(editingExam);
            }
            mainFrame.refreshData();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }
}
