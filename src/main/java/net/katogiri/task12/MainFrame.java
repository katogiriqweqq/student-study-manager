package net.katogiri.task12;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {
    private DatabaseManager dbManager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> filterComboBox;

    public MainFrame() {
        dbManager = new DatabaseManager();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Планирование учебного процесса");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        typeComboBox = new JComboBox<>(new String[]{"Lesson", "Homework", "Exam"});
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> openAddForm());

        filterComboBox = new JComboBox<>(new String[]{"Все", "Lesson", "Homework", "Exam"});
        filterComboBox.addActionListener(e -> applyFilter());

        JButton editButton = new JButton("Изменить");
        editButton.addActionListener(e -> editSelected());

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(e -> deleteSelected());

        JButton sortByDateButton = new JButton("Сортировать по дате");
        sortByDateButton.addActionListener(e -> sortByDate());

        JButton sortByPriorityButton = new JButton("Сортировать по приоритету");
        sortByPriorityButton.addActionListener(e -> sortByPriority());

        topPanel.add(new JLabel("Тип:"));
        topPanel.add(typeComboBox);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(new JLabel("Фильтр:"));
        topPanel.add(filterComboBox);
        topPanel.add(sortByDateButton);
        topPanel.add(sortByPriorityButton);

        String[] columns = {"ID", "Тип", "Название", "Предмет", "Дата", "Приоритет"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<StudyItem> items = dbManager.getAllItems();
            displayItems(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + e.getMessage());
        }
    }

    private void displayItems(List<StudyItem> items) {
        tableModel.setRowCount(0);
        for (StudyItem item : items) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getItemType(),
                    item.getTitle(),
                    item.getSubject(),
                    item.getDate(),
                    item.getPriority()
            });
        }
    }

    private void openAddForm() {
        String type = (String) typeComboBox.getSelectedItem();
        if ("Lesson".equals(type)) {
            new LessonForm(this).setVisible(true);
        } else if ("Homework".equals(type)) {
            new HomeworkForm(this).setVisible(true);
        } else if ("Exam".equals(type)) {
            new ExamForm(this).setVisible(true);
        }
    }

    private void applyFilter() {
        String filter = (String) filterComboBox.getSelectedItem();
        try {
            List<StudyItem> items;
            if ("Все".equals(filter)) {
                items = dbManager.getAllItems();
            } else {
                items = dbManager.getItemsByType(filter);
            }
            displayItems(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка фильтрации: " + e.getMessage());
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        try {
            List<StudyItem> items = dbManager.getAllItems();
            for (StudyItem item : items) {
                if (item.getId() == id) {
                    if ("Lesson".equals(type) && item instanceof Lesson) {
                        new LessonForm(this, (Lesson) item).setVisible(true);
                    } else if ("Homework".equals(type) && item instanceof Homework) {
                        new HomeworkForm(this, (Homework) item).setVisible(true);
                    } else if ("Exam".equals(type) && item instanceof Exam) {
                        new ExamForm(this, (Exam) item).setVisible(true);
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage());
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            dbManager.deleteItem(id);
            refreshData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage());
        }
    }

    private void sortByDate() {
        try {
            List<StudyItem> items = dbManager.getAllItems();
            items.sort(Comparator.comparing(StudyItem::getDate));
            displayItems(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка сортировки: " + e.getMessage());
        }
    }

    private void sortByPriority() {
        try {
            List<StudyItem> items = dbManager.getAllItems();
            items.sort(Comparator.comparingInt(StudyItem::getPriority).reversed());
            displayItems(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка сортировки: " + e.getMessage());
        }
    }

    public void refreshData() {
        loadData();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
