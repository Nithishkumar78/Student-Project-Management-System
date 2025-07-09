package com.project;

import java.util.Scanner;

public class Mainconsole{
    public static void main(String[] args) {
        Team team = new Team();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Student Project Management Console ---");
            System.out.println("1. Add New Team");
            System.out.println("2. Search Team");
            System.out.println("3. Update Weekend Report");
            System.out.println("4. Update Review Attendance");
            System.out.println("5. Display Team Details");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: team.addTeam(); break;
                case 2: team.searchTeam(); break;
                case 3: team.updateWeekendReport(); break;
                case 4: team.updateReviewAttendance(); break;
                case 5: team.displayDetails(); break;
                case 6: System.out.println("✅ Exiting..."); break;
                default: System.out.println("❌ Invalid choice.");
            }
        } while (choice != 6);

        sc.close();
    }
}
