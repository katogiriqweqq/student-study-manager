package net.katogiri.task12;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class HomeworkForm extends JFrame {
    private JTextField titleField;
    private JTextField descField;
    private JTextField priorityField;
    private JTextField dateField;
    private JTextField subjectField;
    private JTextField dueDateField;
    private JCheckBox completedBox;
    private JTextField difficultyField;
    private MainFrame mainFrame;
    private Homework editingHomework;

    public HomeworkForm(MainFrame mainFrame) {
        this(mainFrame, null);
    }

    public HomeworkForm(MainFrame mainFrame, Homework hw) {
        this.mainFrame = mainFrame;
        this.editingHomework = hw;
        initComponents();
        if (editingHomework != null) fillFields();
    }

    private void initComponents() {
        setTitle(editingHomework == null ? "Добавить ДЗ" : "Редактировать ДЗ");
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

        add(new JLabel("Дата создания (YYYY-MM-DD):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Предмет:"));
        subjectField = new JTextField();
        add(subjectField);

        add(new JLabel("Дедлайн (YYYY-MM-DD):"));
        dueDateField = new JTextField();
        add(dueDateField);

        add(new JLabel("Выполнено:"));
        completedBox = new JCheckBox();
        add(completedBox);

        add(new JLabel("Сложность:"));
        difficultyField = new JTextField();
        add(difficultyField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveHomework());
        add(saveButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    private void fillFields() {
        titleField.setText(editingHomework.getTitle());
        descField.setText(editingHomework.getDescription());
        priorityField.setText(String.valueOf(editingHomework.getPriority()));
        dateField.setText(editingHomework.getDate().toString());
        subjectField.setText(editingHomework.getSubject());
        dueDateField.setText(editingHomework.getDueDate().toString());
        completedBox.setSelected(editingHomework.isCompleted());
        difficultyField.setText(editingHomework.getDifficulty());
    }

    private void saveHomework() {
        try {
            String title = titleField.getText();
            String desc = descField.getText();
            int priority = Integer.parseInt(priorityField.getText());
            LocalDate date = LocalDate.parse(dateField.getText());
            String subject = subjectField.getText();
            LocalDate due = LocalDate.parse(dueDateField.getText());
            boolean completed = completedBox.isSelected();
            String diff = difficultyField.getText();

            DatabaseManager db = new DatabaseManager();

            if (editingHomework == null) {
                Homework hw = new Homework(title, desc, priority, date, subject, due, completed, diff);
                db.addItem(hw);
            } else {
                editingHomework.setTitle(title);
                editingHomework.setDescription(desc);
                editingHomework.setPriority(priority);
                editingHomework.setDate(date);
                editingHomework.setSubject(subject);
                editingHomework.setDueDate(due);
                editingHomework.setCompleted(completed);
                editingHomework.setDifficulty(diff);
                db.updateItem(editingHomework);
            }
            mainFrame.refreshData();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }
}
