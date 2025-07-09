package com.project;


import java.sql.*;
import java.util.Scanner;

public class Team {
    Scanner sc = new Scanner(System.in);

    public void addTeam() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter student name: ");
            String student = sc.nextLine();
            System.out.print("Enter team members (comma separated): ");
            String members = sc.nextLine();
            System.out.print("Enter project topic: ");
            String topic = sc.nextLine();
            System.out.print("Enter guide name: ");
            String guide = sc.nextLine();

            String sql = "INSERT INTO teams (student_name, team_members, project_topic, guide_name) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student);
            ps.setString(2, members);
            ps.setString(3, topic);
            ps.setString(4, guide);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int batchNo = rs.getInt(1);
                System.out.println("✅ Team added successfully. Batch Number: " + batchNo);
            }
        } catch (Exception e) {
            System.out.println("❌ Error while adding team: " + e.getMessage());
        }
    }

    public void searchTeam() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("\n📋 Available Teams:");
            String listSql = "SELECT batch_no, student_name FROM teams";
            PreparedStatement listPs = conn.prepareStatement(listSql);
            ResultSet listRs = listPs.executeQuery();

            boolean foundAny = false;
            while (listRs.next()) {
                System.out.println("🆔 Batch No: " + listRs.getInt("batch_no") +
                        " | 👤 Student: " + listRs.getString("student_name"));
                foundAny = true;
            }

            if (!foundAny) {
                System.out.println("❌ No teams found in database.");
                return;
            }

            System.out.print("\n🔍 Enter Batch No or Student Name to Search: ");
            String input = sc.nextLine();

            String sql = "SELECT * FROM teams WHERE batch_no = ? OR student_name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            try {
                ps.setInt(1, Integer.parseInt(input));
            } catch (NumberFormatException e) {
                ps.setInt(1, -1); // Will not match any batch number
            }
            ps.setString(2, "%" + input + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n✅ Team Found:");
                System.out.println("Batch No     : " + rs.getInt("batch_no"));
                System.out.println("Student      : " + rs.getString("student_name"));
                System.out.println("Team Members : " + rs.getString("team_members"));
                System.out.println("Topic        : " + rs.getString("project_topic"));
                System.out.println("Guide        : " + rs.getString("guide_name"));
            } else {
                System.out.println("❌ No matching team found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error while searching: " + e.getMessage());
        }
    }

    public void updateWeekendReport() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Batch Number: ");
            int batchNo = Integer.parseInt(sc.nextLine());
            System.out.print("Was report submitted? (true/false): ");
            boolean submitted = Boolean.parseBoolean(sc.nextLine());
            System.out.print("Enter report date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            String sql = "INSERT INTO weekend_reports (batch_no, report_submitted, report_date) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, batchNo);
            ps.setBoolean(2, submitted);
            ps.setDate(3, Date.valueOf(date));
            ps.executeUpdate();

            System.out.println("✅ Weekend report updated.");
        } catch (Exception e) {
            System.out.println("❌ Error while updating weekend report: " + e.getMessage());
        }
    }

    public void updateReviewAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Batch Number: ");
            int batchNo = Integer.parseInt(sc.nextLine());
            System.out.print("Did student attend review? (true/false): ");
            boolean attended = Boolean.parseBoolean(sc.nextLine());
            System.out.print("Enter review date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            String sql = "INSERT INTO review_attendance (batch_no, attended, review_date) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, batchNo);
            ps.setBoolean(2, attended);
            ps.setDate(3, Date.valueOf(date));
            ps.executeUpdate();

            System.out.println("✅ Review attendance updated.");
        } catch (Exception e) {
            System.out.println("❌ Error while updating review attendance: " + e.getMessage());
        }
    }

    public void displayDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Batch Number: ");
            int batchNo = Integer.parseInt(sc.nextLine());

            String sqlTeam = "SELECT * FROM teams WHERE batch_no = ?";
            PreparedStatement psTeam = conn.prepareStatement(sqlTeam);
            psTeam.setInt(1, batchNo);
            ResultSet rsTeam = psTeam.executeQuery();

            if (rsTeam.next()) {
                System.out.println("\n📄 --- Team Information ---");
                System.out.println("Batch No     : " + batchNo);
                System.out.println("Student      : " + rsTeam.getString("student_name"));
                System.out.println("Team Members : " + rsTeam.getString("team_members"));
                System.out.println("Topic        : " + rsTeam.getString("project_topic"));
                System.out.println("Guide        : " + rsTeam.getString("guide_name"));
            } else {
                System.out.println("❌ No team found.");
                return;
            }

            String sqlReport = "SELECT * FROM weekend_reports WHERE batch_no = ?";
            PreparedStatement psReport = conn.prepareStatement(sqlReport);
            psReport.setInt(1, batchNo);
            ResultSet rsReport = psReport.executeQuery();

            while (rsReport.next()) {
                System.out.println("\n📆 --- Weekend Report ---");
                System.out.println("Submitted: " + rsReport.getBoolean("report_submitted"));
                System.out.println("Date     : " + rsReport.getDate("report_date"));
            }

            String sqlReview = "SELECT * FROM review_attendance WHERE batch_no = ?";
            PreparedStatement psReview = conn.prepareStatement(sqlReview);
            psReview.setInt(1, batchNo);
            ResultSet rsReview = psReview.executeQuery();

            while (rsReview.next()) {
                System.out.println("\n👥 --- Review Attendance ---");
                System.out.println("Attended: " + rsReview.getBoolean("attended"));
                System.out.println("Date    : " + rsReview.getDate("review_date"));
            }

            System.out.println("\n----------------------------");

        } catch (Exception e) {
            System.out.println("❌ Error while displaying details: " + e.getMessage());
        }
    }
}
