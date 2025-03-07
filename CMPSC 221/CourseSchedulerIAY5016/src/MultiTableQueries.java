import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MultiTableQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement getDesc;
    private static ResultSet resultSet;
    private static PreparedStatement getScheduledStudents;
    private static ResultSet resultSetForScheduledStudents;
    private static PreparedStatement getWaitStudents;
    private static ResultSet resultSetForWaitStudents;

    public static ArrayList<ClassDescription> getAllClassDescriptions(String currentSemester) {
        connection = DBConnection.getConnection();
        ArrayList<ClassDescription> answer = new ArrayList<ClassDescription>();
        
        try {
            getDesc = connection.prepareStatement("select app.class.coursecode,app.course.description,app.class.seats,app.course.coursecode from app.class,app.course where app.class.coursecode = app.course.coursecode");
            resultSet = getDesc.executeQuery();
            
            while(resultSet.next()) {
                answer.add(new ClassDescription(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3)));
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return answer;
    }
    
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode){
       ArrayList<StudentEntry> result = new ArrayList<StudentEntry>();
       connection=DBConnection.getConnection();
       try {
           getScheduledStudents=connection.prepareStatement("select app.student.firstname,app.student.lastname,app.student.studentid from app.student inner join app.schedule on app.student.studentid=app.schedule.studentid where app.schedule.coursecode like (?) and app.schedule.semester like (?) and app.schedule.status like (?) order by app.schedule.timestamp");
           getScheduledStudents.setString(1,courseCode);
           getScheduledStudents.setString(2,semester);
           getScheduledStudents.setString(3,"S");
           resultSetForScheduledStudents=getScheduledStudents.executeQuery();
           while(resultSetForScheduledStudents.next()){
               result.add(new StudentEntry(resultSetForScheduledStudents.getString(3),resultSetForScheduledStudents.getString(1),resultSetForScheduledStudents.getString(2)));
           }
       } catch(SQLException sqlException) {
           sqlException.printStackTrace();
       }
       return result;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode){
       ArrayList<StudentEntry> resultW = new ArrayList<StudentEntry>();
       connection=DBConnection.getConnection();
       try {
           getWaitStudents=connection.prepareStatement("select app.student.firstname,app.student.lastname,app.student.studentid from app.student inner join app.schedule on app.student.studentid=app.schedule.studentid and app.schedule.coursecode like (?) and app.schedule.semester like (?) and app.schedule.status like (?) order by app.schedule.timestamp");
           getWaitStudents.setString(1,courseCode);
           getWaitStudents.setString(2,semester);
           getWaitStudents.setString(3,"W");
           resultSetForWaitStudents=getWaitStudents.executeQuery();
           while(resultSetForWaitStudents.next()){
               resultW.add(new StudentEntry(resultSetForWaitStudents.getString(3),resultSetForWaitStudents.getString(1),resultSetForWaitStudents.getString(2)));
           }
       } catch(SQLException sqlException) {
           sqlException.printStackTrace();
       }
       
       return resultW;
    }
}
