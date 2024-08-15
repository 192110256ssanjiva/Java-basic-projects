package Hospital_Management_System;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patients{

    private Connection connection;

    private Scanner scanner;
    Patients(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.println("Enter Patients name: ");
        String name = scanner.next();
        System.out.println("Enter patients age: ");
        int age = scanner.nextInt();
        System.out.println("Enter the Gender: ");
        String gender = scanner.next();

        try{
            // inserting into tables;
            String query = "INSERT INTO patients (name, age, gender) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int check = preparedStatement.executeUpdate();
            if (check>0){
                System.out.println("ADDED TO DB");
            }else{
                System.out.println("failed to add");
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void viewPatient(){
        String query = "SELECT * from patients";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+-------------+-------------------+----------+-----------+");
            System.out.println("| Patients id |   Name            | Age      | Gender     | ");
            System.out.println("+--------------+------------------+----------+-----------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12d|%-20s|%-10d|%-11s\n",id,name,age,gender);
                System.out.println("+-------------+-------------------+----------+-----------+");
            }

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query = "select * from patients where id=?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    
}
