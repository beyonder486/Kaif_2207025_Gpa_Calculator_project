package com.kaif.gpacalculator.util;

import com.kaif.gpacalculator.model.Course;
import java.util.List;

/**
 * Utility class for GPA calculations
 */
public class GpaCalculator {
    
    /**
     * Calculate weighted GPA from a list of courses
     * GPA = Sum(Credit * Grade Points) / Sum(Credits)
     */
    public static double calculateGPA(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0.0;
        }

        double totalWeightedPoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : courses) {
            totalWeightedPoints += course.getWeightedGradePoints();
            totalCredits += course.getCourseCredit();
        }

        return totalCredits > 0 ? totalWeightedPoints / totalCredits : 0.0;
    }

    /**
     * Calculate total credits from a list of courses
     */
    public static double calculateTotalCredits(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0.0;
        }

        return courses.stream()
                .mapToDouble(Course::getCourseCredit)
                .sum();
    }

    /**
     * Format GPA to 2 decimal places
     */
    public static String formatGPA(double gpa) {
        return String.format("%.2f", gpa);
    }
}
