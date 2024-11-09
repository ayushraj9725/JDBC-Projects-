import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException
    {
        /*
         Class.forName is a method in Java that dynamically loads a class at runtime, which is particularly useful when working with JDBC drivers.
         Here’s a breakdown of how it works and its specific use in JDBC:
         The syntax for using Class.forName is : Class.forName(com.mysql.jdbc.Drivers) .
         When this line executes:
         1. Loads the Class: It loads the specified class into memory by its fully qualified name (package name + class name).
         2. Initializes the Class: It initializes the static blocks and fields within the class, which is critical in cases where the loaded class needs to perform certain actions at load time.
         3. Returns Class Object: It returns a Class object representing the loaded class.

         Why Class.forName is Used in JDBC
         In older JDBC versions, Class.forName was necessary to explicitly load the JDBC driver class before establishing a connection to the database.
         The driver class contains a static initializer block that registers the driver with DriverManager, enabling the driver to manage database connections.

         Is Class.forName Still Necessary?
         For most databases, you don’t need to use Class.forName explicitly anymore because:

         JDBC 4.0 and newer versions (Java 6+) automatically load and register drivers located on the classpath.
         If your driver is included in your project (like through a .jar file or Maven dependency), it should auto-register.

         However, using Class.forName can still be useful in:
         Legacy code: Ensures compatibility if your code may run in older environments.
         Exception Handling: You might want to catch ClassNotFoundException specifically, to handle the case when the driver is missing.

         */
        System.out.println("Introduction of JDBC");
        // loading the jdbc drivers
        try {
            Class.forName("com.mysql.jdbc.Drivers");
            System.out.println("All the drivers have successfully Loaded");
        }catch(ClassNotFoundException e){
            System.out.println("Loading Drivers Failed " +e.getMessage());
        }

        // Now after loading the Drivers ,we are ready to establish the connection
        // for that should require 3 things one is database URL which comes from here ("jdbc:mysql://localhost:3306/Database_name");
        // 2nd things is Database username and third thing is Password of database .
        // now we are going to establish the connection .

        String url = "jdbc:mysql://localhost:3306/Company";   // database Url
        String username = "root" ;                            // username of database
        String password = "@Ayush1225" ;                      // this is password of my database

        // here handling the SQLException ,if it comes in program handled by try catch ;
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Successfully Established Connection");
            System.out.println("The connection add from where it established that stored in instance of Connection Interface :"+connection);
            // we are trying to execute query , so we have to create statement using statement interface and connection instance
            Statement stmt = connection.createStatement(); // this method created statement for executing query , it does not take any parameter
            ResultSet rs = stmt.executeQuery("select * from employees"); // in this we passed query as input ,what we want to execute ,
                                                                            // we have to print that so we need to variable which hold it , that provided by ResultSet interface
            // to print we have to run loop using next() method
            // here we done the How the data we can retrieve data from database using java
            while(rs.next()){
                int id = rs.getInt("emp_id");
                String name = rs.getString("name");
                String job_title = rs.getString("job_title");
                double salary = rs.getDouble("salary");

                System.out.println();
                System.out.println("............................");
                System.out.println("Id :"+id);
                System.out.println("Name :"+name);
                System.out.println("Job_title :"+job_title);
                System.out.println("Salary :"+salary);

            }

            // now we have to close these instances as being a responsible developer
            rs.close();
            stmt.close();
            connection.close();

        }
        catch(SQLException e){
            System.out.println("Failed Connection "+e.getMessage());
        }


    }
}