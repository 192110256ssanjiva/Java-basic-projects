package Hospital_Management_System;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class DriverHospital {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    
    private static final String userName = "rootuser";

    private static final String password = "password";

    public static void main(String ag[]){
       
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, userName, password);
            Patients patients = new Patients(connection, scanner);
            Doctor doctors = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGMENT SUSYEM");
                System.out.println("1. ADD Patients");
                System.out.println("2. view Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointments");
                System.out.println("5. Exit");
                System.out.println("Enter the choice");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1: 
                    patients.addPatient();
                    System.out.println();
                    break;

                    case 2:
                    patients.viewPatient();
                    System.out.println();
                    break;

                    case 3:
                    doctors.viewDoctors();
                    System.out.println();
                    break;

                    case 4:
                    bookAppointment(patients, doctors, connection, scanner);
                    System.out.println();
                    break;

                    case 5:
                    System.exit(0);
                    default:
                    System.out.println("Invalid choice");
                    break;
                    
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patients patients, Doctor doctor,Connection connection, Scanner scanner){
        System.out.println("Enter Patients id: ");
        int p_id = scanner.nextInt();
        System.out.println("Enter Doctors id: ");
        int d_id = scanner.nextInt();
        System.out.println("Enter Date of Appointment (yyyy-mm-dd)");
        String app_date = scanner.next();
        if (patients.getPatientById(p_id) && doctor.getDoctorById(d_id)){
            if (checkDoctorAvail(d_id, app_date, connection)){
                String appQuery = "INSERT INTO appointments(patient_id, doctor_id,appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appQuery);
                    preparedStatement.setInt(1, p_id);
                    preparedStatement.setInt(2, d_id);
                    preparedStatement.setString(3, app_date);
                    int check = preparedStatement.executeUpdate();
                    if (check >0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("Failed to Book");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor is Not Avilable");
            }

        }else{
            System.out.println("Either Doctor or patients does not exist !!!");
        }
    }
    public static boolean checkDoctorAvail(int d_id,String date,Connection connection){
        String query = "select count(*) from appointments where doctor_id = ? AND appointment_date =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, d_id);
            preparedStatement.setString(2, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if (count ==0){
                    return true;
                }else{
                    return false;
                }
            }
            
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return false;
    }

}
