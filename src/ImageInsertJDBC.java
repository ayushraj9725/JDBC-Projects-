import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageInsertJDBC {
    public static void main(String[] args) throws  ClassNotFoundException {
        // we are trying to insert image into the image table which is existing in th Company database available in my system
        String url = "jdbc:mysql://localhost:3306/Company" ;
        String username = "root" ;
        String password = "@Ayush1225" ;

        String img_path = "C:\\Users\\ayush\\Downloads\\world.jpg";  // this is the path where image stored in directory

        String insertQuery = "INSERT INTO image(img_data) VALUES(?) " ;

        // we are trying to load the drivers
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully !");
        }catch(ClassNotFoundException e){
             System.out.println("Error loading driver: " + e.getMessage());
        }
        // now we are trying to establish the connection between Java and Database

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection established successfully !");
            // now we are trying to convert that image file into byte array for storing the image as img_data in table
            // so we are using the FileInputStream
             try(FileInputStream fileInputStream = new FileInputStream(img_path)){
               // using byt type array we can store the data image after converting  , available method use to file exact size of that array
                 byte[] img_data = new byte[fileInputStream.available()];
                 // now we have to read the img_data what data are stored
                 fileInputStream.read(img_data);

                 try(PreparedStatement ptmt = connection.prepareStatement(insertQuery)){
                     ptmt.setBytes(1,img_data);
                     int affectrow = ptmt.executeUpdate();    // execute the SQL insertQuery

                     if(affectrow > 0 ){
                         System.out.println("Image Inserted Successfully !");
                     }else{
                         System.out.println("Failed Image insertion !");
                     }

                 }

             }catch (FileNotFoundException e){
                 System.out.println("Error file not found : "+e.getMessage());
             } catch (IOException e) {
                 System.out.println("Error img not found : "+e.getMessage());
             }
             connection.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
