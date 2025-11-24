package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Student;

public class StudentDAO {

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Dothings4Life!";

    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Reuse mapping logic
    private Student mapRow(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setMajor(rs.getString("major"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        return student;
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code = ?, full_name = ?, email = ?, major = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search student
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();

        if (keyword == null) {
            keyword = "";
        }
        String searchPattern = "%" + keyword + "%";

        String sql = """
            SELECT * FROM students
            WHERE student_code LIKE ? 
               OR full_name LIKE ? 
               OR email LIKE ?
            ORDER BY id DESC
            """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Sort, filter

    private String validateSortBy(String sortBy) {
        if (sortBy == null) return "id";

        switch (sortBy) {
            case "id":
            case "student_code":
            case "full_name":
            case "email":
            case "major":
                return sortBy;
            default:
                return "id";
        }
    }

    private String validateOrder(String order) {
        if (order != null && order.equalsIgnoreCase("desc")) {
            return "DESC";
        }
        return "ASC";
    }

    public List<Student> getStudentsSorted(String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        String validSortBy = validateSortBy(sortBy);
        String validOrder = validateOrder(order);

        String sql = "SELECT * FROM students ORDER BY " + validSortBy + " " + validOrder;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();

        // if no major provided, just return all
        if (major == null || major.trim().isEmpty()) {
            return getAllStudents();
        }

        String sql = "SELECT * FROM students WHERE major = ? ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Pagination

    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Student> getStudentsPaginated(int offset, int limit) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}