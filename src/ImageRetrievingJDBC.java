import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class ImageRetrievingJDBC {
    public static void main(String[] args) throws ClassNotFoundException{
        String url = "jdbc:mysql://localhost:3306/Company";
        String username = "root";
        String password = "@Ayush1225" ;
        // we want to extract image from database so we need to locate somewhere , i am providing folder path in my system
        String img_folder = "C:\\Users\\ayush\\Downloads\\";

        String query = "SELECT img_data FROM img_table WHERE img_ID = (?)" ;

        // loading the driver

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Load Successfully !");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        // Establishing the Connections
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Established Connection successfully !");
            // here we are retrieving image from database of img_table so
            // we are using preparedStatement so that we can execute the query and after retrieving locate in that folder
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,2);  // at img_ID what image available , we want to retrieve and locate here
            ResultSet resultSet = preparedStatement.executeQuery();
            // because we are retrieving so use the resultSet interface to store and then locate .
            if(resultSet.next()){
                // we need to store img data in array byte array .
                byte[] img_data = resultSet.getBytes("img_data");
                // that byte type array stored img_data we have to locate after converted into file so use OutPutStram
                // we have to pass the img_folder path into the constructor  so we have to make exact path like this
              //  String img_path = img_folder + "extracted.jpg" ;
                String img_path = img_folder+"world.jpg" ;
                OutputStream outputStream = new FileOutputStream(img_path); // creating object so that we can locate it using this to pass the img_path
                outputStream.write(img_data); // this method helps to write the img_data that stored into byte array , it refers to img_path
                System.out.println("Image Extracted Successfully !" );

                resultSet.close();
                preparedStatement.close();
            }else{
                System.out.println("Image Not Found !");
            }
            connection.close();
            System.out.println();
            System.out.println("EveryThing Successfully Closed !");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
