import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static ResultSet resultSet;
    
    public static void addCourse(CourseEntry courseEntry) {
        connection = DBConnection.getConnection();
        
        try {
            addCourse = connection.prepareStatement("insert into app.course (CourseCode, Description) values (?, ?)");
            addCourse.setString(1, courseEntry.getCourseCode());
            addCourse.setString(2, courseEntry.getDescription());
            addCourse.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes() {
        connection = DBConnection.getConnection();
        ArrayList<String> AllCourseCodes = new ArrayList<String>();
        try {
            getAllCourseCodes = connection.prepareStatement("select CourseCode from app.course order by CourseCode");
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next()) {
                AllCourseCodes.add(resultSet.getString(1));
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return AllCourseCodes;
    }   
}
