import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageHandlingJDBC {
    public static void main(String[] args) throws  ClassNotFoundException{

        String url = "jdbc:mysql://localhost:3306/Company";
        String username = "root" ;
        String password = "@Ayush1225";
        String img_path1 = "C:\\Users\\ayush\\Downloads\\profileleet.jpg";
        String img_path = "C:\\Users\\ayush\\Downloads\\world.jpeg" ;

        String query = "INSERT INTO img_table(img_data) VALUES(?)" ;

        // loading the drivers here
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully !");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        // Establishing the Connections ;

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established Successfully !!");
            // here we are adding image into database img_table so we have to convert that image into byte form array !
            // using img_path we are able to do that and fileInputStream
            try {
                FileInputStream fileInputStream = new FileInputStream(img_path); // we are passed her that path into this which convert into bytr
                // we need to store that byte result so using byte type array we are able to store that img data
                byte[] img_data = new byte[fileInputStream.available()]; // this available method helps to know the exact size of the image !

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                // passing here the placeHolder value
                preparedStatement.setBytes(1,img_data);
                int affect = preparedStatement.executeUpdate();

                if(affect > 0){
                    System.out.println("Image Added successfully ! rows affected OK() ");
                }else{
                    System.out.println("Not added Image into the table ");
                }

                preparedStatement.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            connection.close();
            System.out.println();
            System.out.println("Every Things have been closed !");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        }
}
