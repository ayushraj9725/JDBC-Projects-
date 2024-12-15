package BankingSystem;


import java.sql.*;
import java.util.Scanner;

public class Account {
    private Connection connection;
    private Scanner scanner;

    public Account(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;

    }


    public long open_account(String email){
        if(!account_exist(email)) {
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                   // System.out.println("Welcome Your Account Has Created !");
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");

    }

    public long getAccount_number(String email, String security_pin) {
        String query = "SELECT account_number FROM Accounts WHERE email = ? AND security_pin = ?";
        try {
            // Validate email and security_pin inputs
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty!");
            }
            if (security_pin == null || security_pin.trim().isEmpty()) {
                throw new IllegalArgumentException("Security PIN cannot be null or empty!");
            }

            System.out.println("Checking account for email: '" + email.trim() + "' and Security PIN: '" + security_pin + "'");

            // Prepare the query with trimmed inputs
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email.trim());
            preparedStatement.setString(2, security_pin.trim());

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check the result
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Throw exception if no account is found
        throw new RuntimeException("Account Number Doesn't Exist or Invalid Security PIN!");
    }


    private long generateAccountNumber() {
        String query = "SELECT MAX(account_number) AS last_account_number FROM Accounts";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("last_account_number");
                return last_account_number > 0 ? last_account_number + 1 : 10000100;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error generating account number: " + e.getMessage(), e);
        }
        // Default account number in case of no records or an error
        return 10000100;
    }


    public boolean account_exist(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String query = "SELECT account_number FROM Accounts WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // True if at least one row exists
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if account exists: " + e.getMessage(), e);
        }
    }

//    public long getAccount_number(String email) {
//        String query = "SELECT account_number from Accounts WHERE email = ? AND security_pin = ? ";
//        try{
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, email);
//            // preparedStatement.setString(2, security_pin);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if(resultSet.next()){
//                return resultSet.getLong("account_number");
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        throw new RuntimeException("Account Number Doesn't Exist!");
//    }

//    private long generateAccountNumber() {
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
//            if (resultSet.next()) {
//                long last_account_number = resultSet.getLong("account_number");
//                return last_account_number+1;
//            } else {
//                return 10000100;
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return 10000100;
//    }

//    public boolean account_exist(String email){
//        String query = "SELECT account_number from Accounts WHERE email = ? ";
//        try{
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, email);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if(resultSet.next()){
//                return true;
//            }else{
//                return false;
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return false;
//
//    }
}