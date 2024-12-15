import java.sql.*;
import java.util.Scanner;

public class BatchProcessing {
    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/Company" ;
        String username = "root" ;
        String password = "@Ayush1225" ;

        // drivers loading
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded successfully !!");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        // Establishing the connection
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established succesfully !");
            con.setAutoCommit(false);

         /*   // using simple statement we will pass the message or set the data into database table using batch process
            Statement stmt = con.createStatement();  // id in my database not autoincrement
            stmt.addBatch("INSERT INTO employees (emp_id, name, job_title , salary)  VALUES (8, 'Aakansha Saini', 'Web Developer', 57000.00); ");
            stmt.addBatch("INSERT INTO employees (emp_id, name, job_title , salary)  VALUES (11, 'Dev Mittal', 'DevOps Engineer', 97000.00); ");
            stmt.addBatch("INSERT INTO employees (emp_id, name, job_title , salary)  VALUES (12, 'Shantanu Raj', 'UI/UX Designer', 92000.00); ");

             // it all gonna set in database together using batch processing system
             int[] resultBatch = stmt.executeBatch();
             con.commit();
             System.out.println("Batch processed Successfully !!");
          */

            // using prepared statement we will process the batch .
            String insertQuery = "INSERT INTO employees (emp_id ,name, job_title, salary ) VALUES(?,?,?,?)" ;
            PreparedStatement ptmt = con.prepareStatement(insertQuery);
            // we are adding manually data using scanner class from user and tell end it will go and add until user doesn't want entering
            Scanner sc = new Scanner(System.in);
            while(true){
                 System.out.print("Emp_id : ");
                 int emp_id = sc.nextInt();
                 sc.nextLine(); // consume the new line character
                 System.out.print("Name : ");
                 String name = sc.nextLine();
                 System.out.print("Job_title : ");
                 String job_title = sc.nextLine();
                 System.out.print("Salary : ");
                 double salary = sc.nextDouble();
                 // adding here the placeholder value what we want to enter
                 ptmt.setInt(1,emp_id);
                 ptmt.setString(2,name);
                 ptmt.setString(3,job_title);
                 ptmt.setDouble(4,salary);
                 ptmt.addBatch(); // we have to add the Batch after taking
                 System.out.println();
                 System.out.print("You want to enter again tell me Y/N : ");
                 String choice = sc.next();
                 if(choice.toUpperCase().equals("N")){
                     break; // here we are setting the choice if come No then we break and end while loop for not taking data and execute batch
                 }
            }
            int[] executeBatch = ptmt.executeBatch();
            con.commit();
            System.out.println("Batch Processed Successfully !");




             con.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
