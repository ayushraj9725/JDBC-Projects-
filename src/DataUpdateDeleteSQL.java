import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataUpdateDeleteSQL {
       public static void main(String[] args){
           // DataBase URL
           String url = "jdbc:mysql://localhost:3306/Company";

           // Database credentials
           String username = "root";
           String password = "@Ayush1225";
           String query = "INSERT INTO employees (emp_id, name, job_title , salary) " +
                          "VALUES (6, 'Akshita Mehta', 'Web Developer', 77000.00); " ; // writing here input for executing query according to our purpose
           String query1 = "DELETE FROM employees WHERE emp_id = 3 ;"; // we want to perform the delete operation using jdbc .

           //Establishing the connection
           try{
               Connection connection = DriverManager.getConnection(url,username,password) ;  // connection established here
               System.out.println("Successfully Connected to the database (Establish the connection)");
               //perform database operations here
               Statement stmt = connection.createStatement(); // here we create the statement
               System.out.println("Created the statement for execution !");
               // for updating the database we have to use this executeUpdate() Method , and then check how many rows() get affected
              /*

               int rows_affected = stmt.executeUpdate(query);
               if(rows_affected > 0){
                   System.out.println("Insert Successfully " + rows_affected +" rows() affected");
               }
               else{
                   System.out.println("Insertion failed !!");
               }   */
               // here we are performing the deletion operation
               int rows_affected = stmt.executeUpdate(query1);
               if(rows_affected > 0){
                   System.out.println("DELETION Successfully " + rows_affected +" rows() affected");
               }
               else{
                   System.out.println("DELETION failed !!");
               }

               // we are closing that connection
               stmt.close();
               connection.close();
               System.out.println();
               System.out.println("Connection closed successfully ! here");
            }
           catch(SQLException e){
               System.out.println("Connection Failed :" +e.getMessage());
           }
       }
}
