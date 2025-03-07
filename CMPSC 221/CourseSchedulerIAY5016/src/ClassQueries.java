import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement dropClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats; 
    private static ResultSet resultSet;
    
    public static void addClass(ClassEntry classEntry) {
        connection = DBConnection.getConnection();
        
        try {
            addClass = connection.prepareStatement("insert into app.class (Semester, CourseCode, Seats) values (?, ?, ?)");
            addClass.setString(1, classEntry.getSemester());
            addClass.setString(2, classEntry.getCourseCode());
            addClass.setInt(3, classEntry.getSeats());
            addClass.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes(String target_semester) {
        connection = DBConnection.getConnection();
        ArrayList<String> AllCourseCodes = new ArrayList<String>();
        try {
            getAllCourseCodes = connection.prepareStatement("select Semester, CourseCode from app.Class order by Semester");
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next()) {
                String actual_semester = resultSet.getString(1);
                if (actual_semester.equals(target_semester))
                    AllCourseCodes.add(resultSet.getString(2));
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return AllCourseCodes;
    }

    public static ArrayList<String> getClassSeats(String target_semester, String target_courseCode) {
        connection = DBConnection.getConnection();
        ArrayList<String> AllCourseCodes = new ArrayList<String>();
        try {
            getClassSeats = connection.prepareStatement("select Semester, CourseCode, Seats from app.Class order by Semester");
            resultSet = getClassSeats.executeQuery();
            
            while(resultSet.next()) {
                String actual_semester = resultSet.getString(1);
//                String actual_semester = resultSet.getString(3);
//                if (actual_semester.equals(target_semester) && )
//                    AllCourseCodes.add(resultSet.getString(2));
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return AllCourseCodes;
    }
    
    public static void dropClass(String semester, String courseCode){
        connection=DBConnection.getConnection();
        
        try {
            dropClass = connection.prepareStatement("delete from app.class where semester like (?) and coursecode=(?)");
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
