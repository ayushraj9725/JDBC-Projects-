import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionHandle {
    public static void main(String[] args) throws ClassNotFoundException{
         String url = "jdbc:mysql://localhost:3306/company" ;
         String username = "root" ;
         String password = "@Ayush1225" ;
         String withdrawQuery = "UPDATE accounts SET balance = balance - (?) WHERE account_number = (?) ";
         String depositQuery = "UPDATE accounts SET balance = balance + (?) WHERE account_number = (?) ";

         // loading the Drivers
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded Successfully !");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        // here we are establishing the connection here
        try{
            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connection established successfully ");
            conn.setAutoCommit(false);  // usually instance of connection after doing work commit true that mean work is done (that is known as commit by independent transaction
                                       // but in case of here we need to check every time is we did any work that has done or not , so we are making false initial commit so that we can check transaction is completed or not

            try{
                PreparedStatement withdrawStmt = conn.prepareStatement(withdrawQuery);
                PreparedStatement depositStmt = conn.prepareStatement(depositQuery) ;
                // setting the value across the placeholder '
                withdrawStmt.setDouble(1,10000.00);  // setting here how much balance is going to withdraw from account
                withdrawStmt.setString(2,"7887389494"); // setting the account number from where rs withdrawal
                depositStmt.setDouble(1,10000.00); // setting here the same amount which are withdrawing from account ,
                depositStmt.setString(2,"8372892903"); // depositing account number setting

                int affectedW = withdrawStmt.executeUpdate();
                int affectedD = depositStmt.executeUpdate();  // both are executed here

                if(affectedW > 0 && affectedD > 0){
                    conn.commit();
                    System.out.println("Transaction Successful ");
                }else{
                    conn.rollback();
                    System.out.println("Transaction Failed ");
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

/*
   WE ARE GOING TO KNOW ABOUT THE TRANSACTION HANDLING HOW WORKS :
   It is in JDBC it's crucial because it ensures data Integrity and Consistency in database operations .
   It provides a mechanism to group multiple operations into a single , logical unit of work

   1. It Ensures Atomicity (ALL or Nothing )
   -> Transaction ensure that either all operations succeed or none of them takes effect
   -> example - in banking system application
       - Debit money from account A.
       - Credit money to account B.
     * if the debit succeeds but credits fails , the transaction is rolled back ,preventing inconsistency
     * here it is ensuring the thing if any debit is done then should be definitely Credit to other where is want to credit .
     but in case of it is not happen then the transaction do not complete , do not affect the initial state of transaction(means debit again go to the that account from it has debit)

    2. Maintains the Data Integrity
    -> Transaction handling ensure that the database doesn't enter an invalid state
     -> for example : When inserting related record into multiple table , if one insertion fails , rollback ensures no incomplete data is stored .

    3. Handles Failures Gracefully
     -> During system crashes, power outages, or network failures, transaction handling allows rolling back incomplete operations.
     -> It prevents partial updates that might corrupt the database.

    4. Manages Concurrency :
     -> Transaction handling prevents conflicts when multiple users perform operations on the same data concurrently .
     -> Concepts like locks and isolation levels ensures that one user's changes don't interface with another .

    5. Consistency Across Multiple Operations
     -> Transactions maintain the consistency of data even when performing multiple related operations across different tables or rows.
        For example, in e-commerce:
       * Deduct inventory, update the order status, and insert a record into the payment table. Transactions ensure all these steps are consistent.

    6. Adherence to ACID Properties
     -> Atomicity : Ensures all operations are completed or none are .
     -> Consistency : Transition the database from one valid state to another.
     -> Isolations : Prevents interference between concurrent transactions.
     -> Durability : Guarantees that once a transaction is committed , it persists even after a system failure .

     *** JDBC KEY FEATURES FOR TRANSACTION HANDLING
     1. Disabling Auto-Commit Mode:  conn.setAutoCommit(false);  (By default, JDBC executes every SQL statement as an independent transaction (auto-commit is true).)
     2. Commit Transactions: conn.commit();    (Explicitly commit changes after all operations succeed:)
     3. Rollback Transactions  :  conn.rollback();  (Rollback changes if any operation fails:)
     4. Isolation Levels: conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

  Common Scenarios Requiring Transaction Handling
-> Banking systems (e.g., transfers, withdrawals, deposits).
-> Order management (e.g., placing an order, deducting inventory, updating payment status).
-> Batch processing (e.g., inserting/updating multiple rows).
-> Distributed systems (e.g., coordinating updates between multiple databases).

 */