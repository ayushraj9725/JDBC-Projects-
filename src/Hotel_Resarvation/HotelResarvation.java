package Hotel_Resarvation;
import java.sql.*;
import java.util.Scanner;

public class HotelResarvation {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_resarvation" ;
    private static final String username = "root" ;
    private static final String password = "@Ayush1225" ;

    public static void main(String[] args) throws ClassNotFoundException , SQLException{
        // loading the Drivers here
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println("Driver not load !! "+e.getMessage());
        }
        // establishing the connection
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM ");
                System.out.println("1 : Reserve The Room ");
                System.out.println("2 : View Reservation ");
                System.out.println("3 : Get Room Number ");
                System.out.println("4 : Update Reservation");
                System.out.println("5 : Delete Reservation");
                System.out.println("6 : Exits");
                Scanner  sc = new Scanner(System.in);
                System.out.print("Choose any of them to do : ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1 :
                        reserveRoom(connection,sc);
                        break ;
                    case 2 :
                        viewReservation(connection);
                        break ;
                    case 3 :
                        getRoomNumber(connection,sc);
                        break ;
                    case 4 :
                        updateReservation(connection,sc);
                        break ;
                    case 5 :
                        deleteReservation(connection,sc);
                        break ;
                    case 6 :
                        exit();
                        sc.close();
                        return ;
                    default : System.out.println(" Invalid choice : please try again !");
                }
            }
        }catch(SQLException e){
            System.out.println("Connection Failed !! "+e.getMessage());
      } catch(InterruptedException e){
            throw new RuntimeException(e);  // occur run time exp if thread exp found
      }



    }
    private static void reserveRoom(Connection connection , Scanner scanner){
       try {
           Scanner scanner1 = new Scanner(System.in);
           System.out.print("Enter the guest name : ");
           String guest_name = scanner1.nextLine().trim();  // why not that scanner object does not working to read and take input string name so manually for taking name we created object

           scanner.nextLine();
           System.out.println("Name : "+guest_name);

           System.out.print("Enter the room number : ");
           int room_number = scanner.nextInt();
           scanner.nextLine();  // Consume the newline character

           System.out.println(room_number);
           System.out.print("Enter the contact detail : ");
           String contact_number = scanner.nextLine();

           String sql = "INSERT INTO resarvation(guest_name , room_number , contact_number)" +
                   "VALUES('" +guest_name+ "' , " +room_number+", '"+contact_number+ "')";

           try{
               // creating statement
               Statement statement  = connection.createStatement() ;
               int affect = statement.executeUpdate(sql); // here our sql query will execute

               if(affect > 0){
                   System.out.println("Reserve room successfully !");
               }else{
                   System.out.println("Reserve unsuccessful ! try again later !");
               }
           }

           catch(SQLException e){
               e.printStackTrace();
               scanner1.close();
           }
       }catch(Exception e){
           System.out.println(e.getMessage());
       }
    }

    private static void viewReservation(Connection connection) throws SQLException{
       String sql = "SELECT * from resarvation" ;

       try(Statement statement = connection.createStatement()){
           ResultSet resultSet = statement.executeQuery(sql);

           while(resultSet.next()){
               System.out.println("Current Reservation :");
               System.out.println("*********************");
               System.out.println("Reservation_id : "+resultSet.getInt("res_id"));
               System.out.println("Guest Name : "+resultSet.getString("guest_name"));
               System.out.println("Room Number : "+resultSet.getInt("room_number"));
               System.out.println("Contact Number : "+resultSet.getString("contact_number"));
               System.out.println("Allocated Date : "+resultSet.getTimestamp("res_date").toString()) ;
               System.out.println("**********************");
               System.out.println();
           }
       }
    }
    private static void getRoomNumber(Connection connection , Scanner scanner){
        try{
             // we need to any guest to know her/his room_number corresponding it reservation id
             System.out.print("Enter the reservation id : ");
             int reservation_id = scanner.nextInt();
             scanner.nextLine();
             System.out.print("Enter the guest name : ");
             String guest_name = scanner.nextLine();

             String sql = "SELECT room_number FROM resarvation WHERE res_id = "+reservation_id+" AND guest_name =  '" +guest_name+ "' ";
             try{
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql);
                 if(resultSet.next()){
                     System.out.println();
                     System.out.println("Guest room number "+resultSet.getInt("room_number")+ " whose reservation_id : "+reservation_id+" and guest name : "+guest_name);
                 }else{
                     System.out.println("Not found please try again Later !");
                 }
             }catch(SQLException e){
                 System.out.println(e.getMessage());
             }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private static void updateReservation(Connection connection , Scanner scanner){
        try{
            // we want to update reservation , so there is we need to know where space or which room has available , so that
            // we are uniquely identifying bye reservationID from our database
            System.out.print("Enter here reservationID to check for room available : ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();
            if(!reservationExists(connection,reservationID)){
                System.out.println("Reservation not found ! Please Reserve ");
                return ;
            }
            else{
                System.out.println("Reservation already exists for the given ReservationID so update after coming new guest ");
                System.out.print("Enter the new guest name : ");
                String guest_name = scanner.nextLine();
                System.out.print("Enter the room number : ");
                int room_number = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the new contact number : ");
                String contact_number = scanner.nextLine();

                String sql = "UPDATE resarvation SET guest_name = '"+guest_name +"' , room_number = "+room_number+" , " +
                        "contact_number = '"+contact_number+"' WHERE res_id = "+reservationID ;

                try(Statement statement = connection.createStatement()){
                     int rowAffect = statement.executeUpdate(sql);
                     if(rowAffect > 0){
                         System.out.println("Update Successfully ! ThankYou for Booking ");
                     }
                     else{
                         System.out.println("Update unsuccessful ");
                     }
                }
            }
        }catch(SQLException e){
             System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection , Scanner scanner){
        try{
            // we are deleting the data from database , if reservation id exits
            System.out.print("Enter the reservation id checking for existing : ");
            int resID = scanner.nextInt();
            scanner.nextLine(); // it is for the read/consume newline character
           // writing sql query for the deleting data
            String sql = "DELETE FROM resarvation WHERE res_id = " +resID ;

            if(!reservationExists(connection,resID)){  // if reservation not exist then we can not delete so return from here
                System.out.println("Reservation not found ! please reserve once ");
                return ;
            } // but if reservation exist then we delete it
            else{
                try(Statement statement  = connection.createStatement()){
                    int rowAffect = statement.executeUpdate(sql);

                    if(rowAffect > 0){
                        System.out.println("Data deleted successfully ");
                    }
                    else{
                        System.out.println("Deletion failed ");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection connection , int resID){
        // here we are checking for the resId exists or not in the hotel
        try{
            String sql = "SELECT res_id FROM resarvation WHERE res_id = " +resID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql) ;

            return resultSet.next() ;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false ; // this is for the return boolean purpose if by any chance above code not execute


    }

    private static void exit() throws InterruptedException {

        // here the program or system will be exiting after performing all task

        System.out.print("Exiting System : ");
           int i = 0 ;
           while(i < 5){
               System.out.print(". ");
               Thread.sleep(450); // it will create gap in flow of program execution
               i++ ;
           }
           System.out.println();
           System.out.println("Thank You For Using Hotel Reservation System ! ");

    }

    // to secure filling or allocating details of customer we are using this preparedStatement so that we can save with SQL injection

    private static void reserveRoomNow(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter the guest name : ");
            String guest_name = scanner.nextLine();
            scanner.nextLine();
            System.out.print("Enter the room number : ");
            int room_number = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter the contact detail : ");
            String contact = scanner.nextLine();

            // Prepared SQL insert statement
            String sql = "INSERT INTO resarvation (guest_name, room_number, contact_number) " +
                    "VALUES (?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Set parameters for the placeholders
                statement.setString(1, guest_name);
                statement.setInt(2, room_number);
                statement.setString(3, contact);

                // Execute update
                int rowsAffected = statement.executeUpdate();
               // connection.commit();
                if (rowsAffected > 0) {
                    System.out.println("Room reserved successfully! ");
                } else {
                    System.out.println("Reservation unsuccessful. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateReservationNow(Connection connection, Scanner scanner) {
        try {
            // Prompt for reservation ID
            System.out.print("Enter here reservationID to check for room availability: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();

            if (!reservationExists(connection, reservationID)) {
                System.out.println("Reservation not found! Please reserve.");
                return;
            } else {
                System.out.println("Reservation already exists for the given ReservationID, updating for new guest.");
                System.out.print("Enter the new guest name: ");
                String guest_name = scanner.nextLine();
                System.out.print("Enter the room number: ");
                int room_number = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the new contact number: ");
                String contact_number = scanner.nextLine();

                // Use a PreparedStatement to avoid SQL injection and formatting issues
                String sql = "UPDATE resarvation SET guest_name = ?, room_number = ?, contact_number = ? WHERE res_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Set parameters
                    statement.setString(1, guest_name.trim()); // Trim spaces to avoid accidental spacing
                    statement.setInt(2, room_number);
                    statement.setString(3, contact_number.trim()); // Trim spaces to ensure clean data
                    statement.setInt(4, reservationID);

                    int rowAffected = statement.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Update successfully completed! Thank you for booking.");
                    } else {
                        System.out.println("Update unsuccessful.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
