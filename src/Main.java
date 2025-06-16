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


            System.out.print("Enter sender's Account Number: ");
            int senderAccount = sc.nextInt();
            System.out.println("Enter receiver's Account Number: ");
            int receiverAccount = sc.nextInt();
            System.out.println("Enter Amount to be transferred: ");
            double transferBalance = sc.nextDouble();

// I don't know if I should add new user from here or using command line I think I should use command line first.

            String debitQuery = "UPDATE accountDetails SET balance = balance - ? WHERE account_number = ?";
            String creditQuery = "UPDATE accountDetails SET balance = balance + ? WHERE account_number = ?";
            String timeQuery1 = "UPDATE accountDetails SET time = CURTIME() WHERE account_number = ?";
            String timeQuery2 = "UPDATE accountDetails SET time = CURTIME() WHERE account_number = ?";
            String dateQuery1 = "UPDATE accountDetails SET date = CURDATE() WHERE account_number = ?";
            String dateQuery2 = "UPDATE accountDetails SET date = CURDATE() WHERE account_number = ?";
            String stateQuery1 = "UPDATE accountDetails SET status = ? WHERE account_number = ?";
            String stateQuery2 = "UPDATE accountDetails SET status = ? WHERE account_number = ?";

            PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
            PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);
            PreparedStatement timeQueryPreparedStatement1 = connection.prepareStatement(timeQuery1);
            PreparedStatement timeQueryPreparedStatement2 = connection.prepareStatement(timeQuery2);
            PreparedStatement dateQueryPreparedStatement1 = connection.prepareStatement(dateQuery1);
            PreparedStatement dateQueryPreparedStatement2 = connection.prepareStatement(dateQuery2);
            PreparedStatement stateQueryPreparedStatement1 = connection.prepareStatement(stateQuery1);
            PreparedStatement stateQueryPreparedStatement2 = connection.prepareStatement(stateQuery2);

            debitPreparedStatement.setDouble(1,transferBalance);
            debitPreparedStatement.setInt(2,senderAccount);

            creditPreparedStatement.setDouble(1, transferBalance);
            creditPreparedStatement.setInt(2,receiverAccount);
            timeQueryPreparedStatement1.setTime(1,senderAccount);
            timeQueryPreparedStatement2.setTime(1,senderAccount);
            dateQueryPreparedStatement1.setDate(1,senderAccount);
            dateQueryPreparedStatement2.setDate(1,senderAccount);
            stateQueryPreparedStatement1.setString(1,"COMMITTED");
            stateQueryPreparedStatement1.setInt(2,senderAccount);
            stateQueryPreparedStatement2.setString(1,"COMMITTED");
            stateQueryPreparedStatement2.setInt(2,receiverAccount);


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