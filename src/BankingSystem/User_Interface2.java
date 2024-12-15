package BankingSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User_Interface2 {
    // Database credentials

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "@Ayush1225";
    public static void main(String[] args)  throws ClassNotFoundException, SQLException  {


       try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 Scanner scanner = new Scanner(System.in)) {

                User user = new User(connection, scanner);
                Account accounts = new Account(connection, scanner);
                AccountManager accountManager = new AccountManager(connection, scanner);

               while (true) {
                    printMainMenu();
                    int choice1 = getValidInteger(scanner);
                    switch (choice1) {
                        case 1:
                            user.register();
                            break;
                        case 2:
                            handleUserLogin(user, accounts, accountManager, scanner);
                            break;
                        case 3:
                            System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                            System.out.println("Exiting System!");
                            return;
                        default:
                            System.out.println("Invalid Choice! Please enter a valid option.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Class Not Found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Print main menu
    private static void printMainMenu() {
        System.out.println("\n*** WELCOME TO BANKING SYSTEM ***");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // Input validation for integers
    private static int getValidInteger(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    // Handle user login
    private static void handleUserLogin(User user, Account accounts, AccountManager accountManager, Scanner scanner) throws SQLException {
        String email = user.login();
        if (email != null) {
            System.out.println("User Logged In Successfully!");

            long account_number;
            if (!accounts.account_exist(email)) {
                System.out.println("\nYou do not have a bank account.");
                System.out.println("1. Open a new Bank Account");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");

                if (getValidInteger(scanner) == 1) {
                    account_number = accounts.open_account(email);
                    System.out.println("Account Created Successfully!");
                    System.out.println("Your Account Number is: " +account_number);
                }
            } else {
                System.out.print("Enter the security pin to get Account Number or Operations : ");
                String security_pin = scanner.nextLine();
                account_number = accounts.getAccount_number(email,security_pin);
                handleAccountOperations(account_number, accountManager, scanner);
            }
        } else {
            System.out.println("Login Failed! Incorrect Email or Password.");
        }
    }

    // Handle account operations after login
    private static void handleAccountOperations(long account_number, AccountManager accountManager, Scanner scanner) throws SQLException {
        int choice2 = 0;
        while (choice2 != 5) {
            System.out.println("\n1. Debit Money");
            System.out.println("2. Credit Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Log Out");
            System.out.print("Enter your choice: ");

            choice2 = getValidInteger(scanner);
            switch (choice2) {
                case 1:
                    accountManager.debit_money(account_number);
                    break;
                case 2:
                    accountManager.credit_money(account_number);
                    break;
                case 3:
                    accountManager.transfer_money(account_number);
                    break;
                case 4:
                    accountManager.getBalance(account_number);
                    break;
                case 5:
                    System.out.println("Logged out successfully!");
                    break;
                default:
                    System.out.println("Invalid Choice! Please enter a valid option.");
            }
        }
    }
}

