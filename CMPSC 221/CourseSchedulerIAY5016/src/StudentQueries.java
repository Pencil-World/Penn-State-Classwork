import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry studentEntry) {
        connection = DBConnection.getConnection();
        
        try {
            addStudent = connection.prepareStatement("insert into app.student (StudentID, FirstName, LastName) values (?, ?, ?)");
            addStudent.setString(1, studentEntry.getStudentID());
            addStudent.setString(2, studentEntry.getFirstName());
            addStudent.setString(3, studentEntry.getLastName());
            addStudent.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getAllStudents() {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> AllStudents = new ArrayList<StudentEntry>();
        try {
            getAllStudents = connection.prepareStatement("select StudentID, FirstName, LastName from app.student order by StudentID");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next()) {
                StudentEntry student = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                AllStudents.add(student);
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return AllStudents;
    }
    
    public static StudentEntry getStudent(String studentid) {
        connection=DBConnection.getConnection();
        StudentEntry student=null;
        try {
            getStudent=connection.prepareStatement("select * from app.student where studentid=(?)");
            getStudent.setString(1, studentid);
            resultSet=getStudent.executeQuery();
            if(resultSet.next()){
                student=new StudentEntry(resultSet.getString(3),resultSet.getString(1),resultSet.getString(2));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        
        return student;
    }
    
    public static void dropStudent(String studentid) {
        connection = DBConnection.getConnection();
        try {
            dropStudent = connection.prepareStatement("delete from app.student where studentid=(?)");
            dropStudent.setString(1, studentid);
            dropStudent.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
