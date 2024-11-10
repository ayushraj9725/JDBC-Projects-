import java.sql.DriverManager;
import java.sql.*;

public class DataUpdatingJDBC {
    public static void main(String[] args)  throws ClassNotFoundException {
         System.out.println("Data updating of DataBase Using JDBC : ");
         // connecting and loading the drivers
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers Load Successfully ");
        }catch(ClassNotFoundException e){
            System.out.println("Driver loading Failed throwing "+e.getMessage());
        }

        // establishing the connection for updating data from database using jdbc

        String url = "jdbc:mysql://localhost:3306/Company";
        String username = "root" ;
        String password = "@Ayush1225" ;
        String query = "UPDATE employees SET job_title = 'Java Full Stack Developer' , salary = 135000.00 WHERE emp_id = 10 " ;

        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully !!");

            // now we have to create statement to execute query for updating
            Statement stmt = con.createStatement();
            int rowAffect = stmt.executeUpdate(query);

            if(rowAffect > 0){
                System.out.println("update database here : " +rowAffect+ " row affected");
            }else{
                System.out.println("database not update !");
            }

            stmt.close();
            con.close();
            System.out.println("Successfully closed everything ");
        }
        catch(SQLException e){
            System.out.println("Connection Failed if connection established then any SQLException occur :!!! :" +e.getMessage());
        }

    }
}
