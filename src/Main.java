import java.sql.*;
import java.util.Scanner;

public class Main{
    private static final String url = "jdbc:mysql://localhost:3306/TransactionDB";
    private static final String username = "root";
    private static final String password = "password";


    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            Connection connection = DriverManager.getConnection(url, username, password);


            System.out.print("Do you want to ADD new Account? (Y/N): ");
            String addAccountChoice = sc.nextLine().toLowerCase();

            if (addAccountChoice.equals("y")) {
                System.out.println("Creating Account please wait!");
                addUser(connection);
            }

            System.out.println("Do you want to transfer balance? (Y/N) :");
            String transferBalanceChoice = sc.nextLine().toLowerCase();

            if(transferBalanceChoice.equals("y")){

            System.out.print("Enter sender's Account Number: ");
            int senderAccount = sc.nextInt();
            System.out.println("Enter receiver's Account Number: ");
            int receiverAccount = sc.nextInt();
            System.out.println("Enter Amount to be transferred: ");
            double transferBalance = sc.nextDouble();

// I don't know if I should add new user from here or using command line I think I should use command line first.

            String debitQuery = "UPDATE accountDetails SET balance = balance - ? WHERE account_number = ?";
            String creditQuery = "UPDATE accountDetails SET balance = balance + ? WHERE account_number = ?";
            String stateQuery1 = "UPDATE accountDetails SET status = ? WHERE account_number = ?";
            String stateQuery2 = "UPDATE accountDetails SET status = ? WHERE account_number = ?";

            PreparedStatement debitPreparedStatement = connection.prepareStatement(debitQuery);
            PreparedStatement creditPreparedStatement = connection.prepareStatement(creditQuery);
            PreparedStatement stateQueryPreparedStatement1 = connection.prepareStatement(stateQuery1);
            PreparedStatement stateQueryPreparedStatement2 = connection.prepareStatement(stateQuery2);

            debitPreparedStatement.setDouble(1, transferBalance);
            debitPreparedStatement.setInt(2, senderAccount);

            creditPreparedStatement.setDouble(1, transferBalance);
            creditPreparedStatement.setInt(2, receiverAccount);

            stateQueryPreparedStatement1.setString(1, "COMMITTED");
            stateQueryPreparedStatement1.setInt(2, senderAccount);
            stateQueryPreparedStatement2.setString(1, "COMMITTED");
            stateQueryPreparedStatement2.setInt(2, receiverAccount);

            if (checkSufficientBalance(connection,senderAccount, transferBalance)) {
                int debitMessage = debitPreparedStatement.executeUpdate();
                int creditMessage = creditPreparedStatement.executeUpdate();


                if (debitMessage > 0 || creditMessage > 0) {
                    System.out.println("Balance Transferred!");
                }
            }else {
                System.out.println("Insufficient Balance!");
            }
}
            else {
                return;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

        static void addUser(Connection connection){

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter account Number: ");
        int accountNum = sc.nextInt();



        try {
            String addUserQuery = "Insert INTO accountDetails(account_number, status) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery);
            preparedStatement.setInt(1, accountNum);
            preparedStatement.setString(2,"COMMITTED");

            int val = preparedStatement.executeUpdate();
            if(val > 0){
                System.out.println("Account Created!");
                return;
            }else {
                System.out.println("Something went wrong!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    static boolean checkSufficientBalance(Connection connection, int accountNumber, double balance){

        try{
            String query = "SELECT balance FROM accountDetails WHERE account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                double current_balance = resultSet.getDouble("balance");
                return !(balance > current_balance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}