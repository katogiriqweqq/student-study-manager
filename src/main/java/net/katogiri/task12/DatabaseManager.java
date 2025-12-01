package net.katogiri.task12;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:planner.db";

    public DatabaseManager() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS study_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                priority INTEGER NOT NULL,
                date TEXT NOT NULL,
                subject TEXT NOT NULL,
                item_type TEXT NOT NULL,
                type_data TEXT
            )
        """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            insertSampleData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertSampleData(Connection conn) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM study_items";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSQL)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSQL = """
                    INSERT INTO study_items (title, description, priority, date, subject, item_type, type_data) VALUES
                    ('Лекция по ООП', 'Наследование и полиморфизм', 3, '2024-03-01', 'Java', 'Lesson', 'Ауд. 101|Иванов И.И.|10:00-11:30'),
                    ('Практика по базам данных', 'SQL запросы', 4, '2024-03-02', 'БД', 'Lesson', 'Ауд. 202|Петров П.П.|12:00-13:30'),
                    ('Семинар по сетям', 'OSI модель', 2, '2024-03-03', 'Сети', 'Lesson', 'Ауд. 303|Сидоров С.С.|14:00-15:30'),
                    ('Лабораторная по Java', 'Коллекции', 5, '2024-03-04', 'Java', 'Lesson', 'Лаб. 12|Иванов И.И.|16:00-17:30'),
                    ('Лекция по Алгоритмам', 'Сортировки', 3, '2024-03-05', 'Алгоритмы', 'Lesson', 'Ауд. 105|Смирнов А.А.|09:00-10:30'),
                    ('ДЗ по ООП', 'Реализовать иерархию классов', 4, '2024-03-01', 'Java', 'Homework', '2024-03-08|false|Средняя'),
                    ('ДЗ по БД', 'Спроектировать схему базы данных', 5, '2024-03-02', 'БД', 'Homework', '2024-03-10|false|Сложная'),
                    ('ДЗ по сетям', 'Задачи по IP-адресации', 3, '2024-03-03', 'Сети', 'Homework', '2024-03-09|false|Средняя'),
                    ('ДЗ по алгоритмам', 'Реализовать быструю сортировку', 5, '2024-03-04', 'Алгоритмы', 'Homework', '2024-03-11|false|Сложная'),
                    ('Мини-проект по Java', 'Консольный TODO-список', 2, '2024-03-05', 'Java', 'Homework', '2024-03-20|false|Легкая'),
                    ('Экзамен по Java', 'ООП, коллекции, потоки', 5, '2024-06-10', 'Java', 'Exam', 'Письменный|Ауд. 301|90'),
                    ('Экзамен по БД', 'SQL, нормализация, транзакции', 5, '2024-06-12', 'БД', 'Exam', 'Письменный|Ауд. 305|90'),
                    ('Зачет по сетям', 'Маршрутизация, протоколы', 4, '2024-06-15', 'Сети', 'Exam', 'Устный|Ауд. 210|60'),
                    ('Экзамен по алгоритмам', 'Сложность, структуры данных', 5, '2024-06-18', 'Алгоритмы', 'Exam', 'Письменный|Ауд. 108|120'),
                    ('Диффзачет по математике', 'Линейная алгебра', 3, '2024-06-20', 'Математика', 'Exam', 'Тест|Ауд. 110|60')
                """;
                stmt.execute(insertSQL);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void addItem(StudyItem item) throws SQLException {
        String sql = "INSERT INTO study_items (title, description, priority, date, subject, item_type, type_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, item.getTitle());
            pst.setString(2, item.getDescription());
            pst.setInt(3, item.getPriority());
            pst.setString(4, item.getDate().toString());
            pst.setString(5, item.getSubject());
            pst.setString(6, item.getItemType());
            pst.setString(7, serializeTypeData(item));
            pst.executeUpdate();
        }
    }

    public void updateItem(StudyItem item) throws SQLException {
        String sql = "UPDATE study_items SET title = ?, description = ?, priority = ?, date = ?, subject = ?, type_data = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, item.getTitle());
            pst.setString(2, item.getDescription());
            pst.setInt(3, item.getPriority());
            pst.setString(4, item.getDate().toString());
            pst.setString(5, item.getSubject());
            pst.setString(6, serializeTypeData(item));
            pst.setInt(7, item.getId());
            pst.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM study_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<StudyItem> getAllItems() throws SQLException {
        List<StudyItem> items = new ArrayList<>();
        String sql = "SELECT * FROM study_items";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        }
        return items;
    }

    public List<StudyItem> getItemsByType(String type) throws SQLException {
        List<StudyItem> items = new ArrayList<>();
        String sql = "SELECT * FROM study_items WHERE item_type = ?";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, type);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        }
        return items;
    }

    private String serializeTypeData(StudyItem item) {
        if (item instanceof Lesson) {
            Lesson l = (Lesson) item;
            return l.getRoom() + "|" + l.getTeacher() + "|" + l.getTimeSlot();
        } else if (item instanceof Homework) {
            Homework h = (Homework) item;
            return h.getDueDate().toString() + "|" + h.isCompleted() + "|" + h.getDifficulty();
        } else if (item instanceof Exam) {
            Exam e = (Exam) item;
            return e.getExamType() + "|" + e.getRoom() + "|" + e.getDurationMinutes();
        }
        return "";
    }

    private StudyItem createItemFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        int priority = rs.getInt("priority");
        LocalDate date = LocalDate.parse(rs.getString("date"));
        String subject = rs.getString("subject");
        String itemType = rs.getString("item_type");
        String typeData = rs.getString("type_data");
        String[] data = typeData != null ? typeData.split("\\|") : new String[0];
        StudyItem item = null;

        if ("Lesson".equals(itemType)) {
            item = new Lesson(title, description, priority, date, subject,
                    data[0], data[1], data[2]);
        } else if ("Homework".equals(itemType)) {
            item = new Homework(title, description, priority, date, subject,
                    LocalDate.parse(data[0]), Boolean.parseBoolean(data[1]), data[2]);
        } else if ("Exam".equals(itemType)) {
            item = new Exam(title, description, priority, date, subject,
                    data[0], data[1], Integer.parseInt(data[2]));
        }
        if (item != null) {
            item.setId(id);
        }
        return item;
    }
}
