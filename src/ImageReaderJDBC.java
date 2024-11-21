import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
public class ImageReaderJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Company";
        String user = "root";
        String password = "@Ayush1225";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT img_data FROM image WHERE img_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, 1);  // Example: assuming you want to get the image with ID 1
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                File imageFile = new File("C:\\Users\\ayush\\Downloads\\databaseExt\\world.jpg");
                try (InputStream input = rs.getBinaryStream("img_data");
                     FileOutputStream output = new FileOutputStream(imageFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Image successfully saved!");
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
