import java.sql.*;

public class PreparedStatementUSe {
    public static void main(String[] args) throws ClassNotFoundException {

        String url = "jdbc:mysql://localhost:3306/Company";
        String username = "root" ;
        String password = "@Ayush1225";
        String query = "SELECT * FROM employees where emp_id = ? " ;
        String query1 = "SELECT * FROM employees where salary BETWEEN ? AND ? " ;
        // loading the drivers here
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully !");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        // Establishing the Connections ;

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully !!") ;

            // creating the preparedStatement now for execute query to avoid the SQL INJECTION ( PROTECTION ) , BETTER PERFORMANCE , CODE READABILITY AND MAINTAINABILITY , ATOMIC DATA TYPE HANDLING ,Portability
            PreparedStatement preparedStatement = connection.prepareStatement(query1);
            // preparedStatement.setString(1 , "10" );
            preparedStatement.setString(1 , "99000" );
            preparedStatement.setString(2 , "140000" );

            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0 ;
            while(resultSet.next()){
                System.out.println("****************************");
                System.out.println("Emp_ID : "+resultSet.getInt("emp_id"));
                System.out.println("Name : " +resultSet.getString("name"));
                System.out.println("Job_title : " +resultSet.getString("job_title"));
                System.out.println("Salary : " +resultSet.getInt("salary"));
                System.out.println();
                count++ ;
            }
            System.out.println(count + " rows retrieve from database !");

            resultSet.close();
            preparedStatement.close();
            connection.close();
            System.out.println();
            System.out.println("Connection and interfaces Closed Successfully !");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
