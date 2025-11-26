package database;

import com.kaif.gpacalculator.model.Course;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Database manager with Observable pattern and JSON support
 * Handles all CRUD operations for courses and calculations
 */
public class db {
    private static Connection con;
    private static db instance;
    private Logger logger = Logger.getLogger(db.class.getName());
    private Gson gson;
    
    // Observable lists for real-time UI updates
    private ObservableList<Course> observableCourses;
    
    private db() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        observableCourses = FXCollections.observableArrayList();
        getConnection();
        createTables();
        loadCoursesFromDB();
    }
    
    /**
     * Singleton pattern - ensures only one database instance
     */
    public static synchronized db getInstance() {
        if (instance == null) {
            instance = new db();
        }
        return instance;
    }
    
    /**
     * Establish database connection
     */
    public void getConnection() {
        try {
            if(con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:sqlite:gpa_data.db");
                logger.info("Database connection established");
            }
        }
        catch (SQLException e){
            logger.severe("Database connection error: " + e.toString());
        }
    }
    
    /**
     * Create database tables if they don't exist
     */
    public void createTables() {
        getConnection();
        
        String createCoursesTable = """
            CREATE TABLE IF NOT EXISTS courses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                course_name TEXT NOT NULL,
                course_code TEXT NOT NULL,
                course_credit REAL NOT NULL,
                teacher1_name TEXT,
                teacher2_name TEXT,
                grade TEXT NOT NULL,
                grade_points REAL NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createCalculationsTable = """
            CREATE TABLE IF NOT EXISTS calculations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                gpa REAL NOT NULL,
                total_credits REAL NOT NULL,
                total_courses INTEGER NOT NULL,
                courses_json TEXT NOT NULL,
                calculation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createSessionsTable = """
            CREATE TABLE IF NOT EXISTS sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                session_name TEXT NOT NULL,
                semester TEXT,
                year INTEGER,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = con.createStatement()) {
            stmt.execute(createCoursesTable);
            stmt.execute(createCalculationsTable);
            stmt.execute(createSessionsTable);
            logger.info("Database tables created/verified successfully");
        } catch (SQLException e) {
            logger.severe("Error creating tables: " + e.toString());
        }
    }
    
    // ==================== CREATE Operations ====================
    
    /**
     * Insert a single course into database
     */
    public boolean insertCourse(Course course) {
        getConnection();
        String sql = """
            INSERT INTO courses (course_name, course_code, course_credit, 
                                teacher1_name, teacher2_name, grade, grade_points)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getCourseCode());
            pstmt.setDouble(3, course.getCourseCredit());
            pstmt.setString(4, course.getTeacher1Name());
            pstmt.setString(5, course.getTeacher2Name());
            pstmt.setString(6, course.getGrade());
            pstmt.setDouble(7, course.getGradePoints());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update observable list
                observableCourses.add(course);
                logger.info("Course inserted successfully: " + course.getCourseName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error inserting course: " + e.toString());
        }
        return false;
    }
    
    /**
     * Insert multiple courses at once
     */
    public boolean insertCourses(List<Course> courses) {
        boolean allSuccess = true;
        for (Course course : courses) {
            if (!insertCourse(course)) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }
    
    /**
     * Save GPA calculation with JSON data
     */
    public boolean insertCalculation(double gpa, double totalCredits, List<Course> courses) {
        getConnection();
        String sql = """
            INSERT INTO calculations (gpa, total_credits, total_courses, courses_json)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setDouble(1, gpa);
            pstmt.setDouble(2, totalCredits);
            pstmt.setInt(3, courses.size());
            pstmt.setString(4, gson.toJson(courses));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Calculation saved successfully");
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error saving calculation: " + e.toString());
        }
        return false;
    }
    
    // ==================== READ Operations ====================
    
    /**
     * Get all courses as ObservableList (for TableView binding)
     */
    public ObservableList<Course> getObservableCourses() {
        return observableCourses;
    }
    
    /**
     * Fetch all courses from database
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        getConnection();
        String sql = "SELECT * FROM courses ORDER BY created_at DESC";
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getDouble("course_credit"),
                    rs.getString("teacher1_name"),
                    rs.getString("teacher2_name"),
                    rs.getString("grade")
                );
                courses.add(course);
            }
            logger.info("Fetched " + courses.size() + " courses from database");
        } catch (SQLException e) {
            logger.severe("Error fetching courses: " + e.toString());
        }
        return courses;
    }
    
    /**
     * Load courses into observable list
     */
    private void loadCoursesFromDB() {
        observableCourses.clear();
        observableCourses.addAll(getAllCourses());
    }
    
    /**
     * Get course by ID
     */
    public Course getCourseById(int id) {
        getConnection();
        String sql = "SELECT * FROM courses WHERE id = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Course course = new Course(
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getDouble("course_credit"),
                    rs.getString("teacher1_name"),
                    rs.getString("teacher2_name"),
                    rs.getString("grade")
                );
                return course;
            }
        } catch (SQLException e) {
            logger.severe("Error fetching course by ID: " + e.toString());
        }
        return null;
    }
    
    /**
     * Search courses by name or code
     */
    public List<Course> searchCourses(String query) {
        List<Course> courses = new ArrayList<>();
        getConnection();
        String sql = "SELECT * FROM courses WHERE course_name LIKE ? OR course_code LIKE ? ORDER BY created_at DESC";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getDouble("course_credit"),
                    rs.getString("teacher1_name"),
                    rs.getString("teacher2_name"),
                    rs.getString("grade")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            logger.severe("Error searching courses: " + e.toString());
        }
        return courses;
    }
    
    /**
     * Get calculation history
     */
    public List<CalculationRecord> getCalculationHistory(int limit) {
        List<CalculationRecord> history = new ArrayList<>();
        getConnection();
        String sql = "SELECT * FROM calculations ORDER BY calculation_date DESC LIMIT ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CalculationRecord record = new CalculationRecord(
                    rs.getInt("id"),
                    rs.getDouble("gpa"),
                    rs.getDouble("total_credits"),
                    rs.getInt("total_courses"),
                    rs.getString("calculation_date")
                );
                history.add(record);
            }
            logger.info("Fetched " + history.size() + " calculation records");
        } catch (SQLException e) {
            logger.severe("Error fetching calculation history: " + e.toString());
        }
        return history;
    }
    
    /**
     * Get courses from a calculation as JSON
     */
    public String getCalculationCoursesJSON(int calculationId) {
        getConnection();
        String sql = "SELECT courses_json FROM calculations WHERE id = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, calculationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("courses_json");
            }
        } catch (SQLException e) {
            logger.severe("Error fetching courses JSON: " + e.toString());
        }
        return null;
    }
    
    // ==================== UPDATE Operations ====================
    
    /**
     * Update a course
     */
    public boolean updateCourse(int id, Course updatedCourse) {
        getConnection();
        String sql = """
            UPDATE courses SET 
                course_name = ?, 
                course_code = ?, 
                course_credit = ?, 
                teacher1_name = ?, 
                teacher2_name = ?, 
                grade = ?, 
                grade_points = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, updatedCourse.getCourseName());
            pstmt.setString(2, updatedCourse.getCourseCode());
            pstmt.setDouble(3, updatedCourse.getCourseCredit());
            pstmt.setString(4, updatedCourse.getTeacher1Name());
            pstmt.setString(5, updatedCourse.getTeacher2Name());
            pstmt.setString(6, updatedCourse.getGrade());
            pstmt.setDouble(7, updatedCourse.getGradePoints());
            pstmt.setInt(8, id);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Reload observable list
                loadCoursesFromDB();
                logger.info("Course updated successfully: " + updatedCourse.getCourseName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating course: " + e.toString());
        }
        return false;
    }
    
    /**
     * Update course grade only
     */
    public boolean updateCourseGrade(int id, String newGrade, double newGradePoints) {
        getConnection();
        String sql = "UPDATE courses SET grade = ?, grade_points = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newGrade);
            pstmt.setDouble(2, newGradePoints);
            pstmt.setInt(3, id);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                loadCoursesFromDB();
                logger.info("Course grade updated successfully");
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating course grade: " + e.toString());
        }
        return false;
    }
    
    // ==================== DELETE Operations ====================
    
    /**
     * Delete a course by ID
     */
    public boolean deleteCourse(int id) {
        getConnection();
        String sql = "DELETE FROM courses WHERE id = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Reload observable list
                loadCoursesFromDB();
                logger.info("Course deleted successfully");
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error deleting course: " + e.toString());
        }
        return false;
    }
    
    /**
     * Delete all courses (clear database)
     */
    public boolean deleteAllCourses() {
        getConnection();
        String sql = "DELETE FROM courses";
        
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            observableCourses.clear();
            logger.info("All courses deleted");
            return true;
        } catch (SQLException e) {
            logger.severe("Error deleting all courses: " + e.toString());
        }
        return false;
    }
    
    /**
     * Delete a calculation record
     */
    public boolean deleteCalculation(int id) {
        getConnection();
        String sql = "DELETE FROM calculations WHERE id = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Calculation deleted successfully");
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error deleting calculation: " + e.toString());
        }
        return false;
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Get total course count
     */
    public int getCourseCount() {
        getConnection();
        String sql = "SELECT COUNT(*) as count FROM courses";
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.severe("Error getting course count: " + e.toString());
        }
        return 0;
    }
    
    /**
     * Export all courses to JSON string
     */
    public String exportCoursesToJSON() {
        return gson.toJson(getAllCourses());
    }
    
    /**
     * Import courses from JSON string
     */
    public boolean importCoursesFromJSON(String json) {
        try {
            Course[] courses = gson.fromJson(json, Course[].class);
            return insertCourses(List.of(courses));
        } catch (Exception e) {
            logger.severe("Error importing courses from JSON: " + e.toString());
            return false;
        }
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.severe("Error closing connection: " + e.toString());
        }
    }
    
    /**
     * Refresh observable list from database
     */
    public void refreshObservableList() {
        loadCoursesFromDB();
    }
    
    /**
     * Inner class for calculation records
     */
    public static class CalculationRecord {
        private int id;
        private double gpa;
        private double totalCredits;
        private int totalCourses;
        private String date;
        
        public CalculationRecord(int id, double gpa, double totalCredits, int totalCourses, String date) {
            this.id = id;
            this.gpa = gpa;
            this.totalCredits = totalCredits;
            this.totalCourses = totalCourses;
            this.date = date;
        }
        
        public int getId() { return id; }
        public double getGpa() { return gpa; }
        public double getTotalCredits() { return totalCredits; }
        public int getTotalCourses() { return totalCourses; }
        public String getDate() { return date; }
        
        @Override
        public String toString() {
            return String.format("GPA: %.2f | Credits: %.1f | Courses: %d | Date: %s",
                gpa, totalCredits, totalCourses, date);
        }
    }
}
