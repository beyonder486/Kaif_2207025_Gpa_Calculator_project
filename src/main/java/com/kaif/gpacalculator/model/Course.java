package com.kaif.gpacalculator.model;

/**
 * Model class representing a Course with all its details
 */
public class Course {
    private String courseName;
    private String courseCode;
    private double courseCredit;
    private String teacher1Name;
    private String teacher2Name;
    private String grade;
    private double gradePoints;

    public Course(String courseName, String courseCode, double courseCredit, 
                  String teacher1Name, String teacher2Name, String grade) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.teacher1Name = teacher1Name;
        this.teacher2Name = teacher2Name;
        this.grade = grade;
        this.gradePoints = calculateGradePoints(grade);
    }

    /**
     * Convert letter grade to grade points
     */
    private double calculateGradePoints(String grade) {
        return switch (grade) {
            case "A+" -> 4.0;
            case "A" -> 3.75;
            case "A-" -> 3.5;
            case "B+" -> 3.25;
            case "B" -> 3.0;
            case "B-" -> 2.75;
            case "C+" -> 2.5;
            case "C" -> 2.25;
            case "D" -> 2.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public double getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(double courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getTeacher1Name() {
        return teacher1Name;
    }

    public void setTeacher1Name(String teacher1Name) {
        this.teacher1Name = teacher1Name;
    }

    public String getTeacher2Name() {
        return teacher2Name;
    }

    public void setTeacher2Name(String teacher2Name) {
        this.teacher2Name = teacher2Name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        this.gradePoints = calculateGradePoints(grade);
    }

    public double getGradePoints() {
        return gradePoints;
    }

    /**
     * Calculate weighted grade points (credit * grade points)
     */
    public double getWeightedGradePoints() {
        return courseCredit * gradePoints;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Credit: %.1f - Grade: %s - Teachers: %s, %s",
                courseName, courseCode, courseCredit, grade, teacher1Name, teacher2Name);
    }
}
