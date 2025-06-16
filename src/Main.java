import java.sql.*;
import java.util.Scanner;

public class Main{
    private static final String url = "jdbc:mysql://localhost:3306/TransactionDB";
    private static final String username = "root";
    private static final String password = "password";


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        try{
            Connection connection = DriverManager.getConnection(url,username,password);

            Scanner sc = new Scanner(System.in);

            System.out.println("Do you want to ADD new Account? (Y/N)");
            String choice = sc.nextLine().toLowerCase();

            if (choice.equals("Y")){
                System.out.println("Creating Account please wait!");
                addUser(connection);
            }


            int senderAccount = sc.nextInt();
            int receiverAccount = sc.nextInt();
            double transferBalance = sc.nextDouble();

// I don't know if I should add new user from here or using command line I think I should use command line first.
            String addUserQuery = "Insert INTO accountDetails(account_number, date, time, status) VALUES(101,CURDATE(),CURTIME(),'COMMITTED')";

            String debitQuery = "UPDATE accountDetails SET balance = balance - ? WHERE account_number = ?";
            String creditQuery = "UPDATE accountDetails SET balance = balance + ? WHERE account_number = ?";
            String timeQuery = "UPDATE accountDetails SET time = CURTIME() WHERE account_number = ?";
            String dateQuery = "UPDATE accountDetails SET date = CURDATE() WHERE account_number = ?";
            String stateQuery = "UPDATE accountDetails SET status = ? WHERE account_number = ?";

            PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
            PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);
            PreparedStatement timeQueryPreparedStatement = connection.prepareStatement(timeQuery);
            PreparedStatement dateQueryPreparedStatement = connection.prepareStatement(dateQuery);
            PreparedStatement stateQueryPreparedStatement = connection.prepareStatement(stateQuery);



            debitPreparedStatement.setDouble(1,);


        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            sc.close();
        }
    }

    static void addUser(Connection connection){

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter account Number: ");
        int accountNum = sc.nextInt();



        try {
            String addUserQuery = "Insert INTO accountDetails(account_number, date, time, status) VALUES(?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery);
            preparedStatement.setInt(1, accountNum);
            preparedStatement.setDate(2,CURDATE());
            preparedStatement.setTime(3,CURTIME());
            preparedStatement.setString("COMMITTED");

            int val = preparedStatement.executeUpdate();
            if(val > 0){
                System.out.println("Account Created!");
            }else {
                System.out.println("Something went wrong!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

}