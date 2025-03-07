import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;    
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry scheduleEntry) {
        connection = DBConnection.getConnection();
        
        try {
            addScheduleEntry = connection.prepareStatement("insert into app.schedule (Semester, CourseCode, StudentID, Status, Timestamp) values (?, ?, ?, ?, ?)");
            addScheduleEntry.setString(1, scheduleEntry.getSemester());
            addScheduleEntry.setString(2, scheduleEntry.getCourseCode());
            addScheduleEntry.setString(3, scheduleEntry.getStudentID());
            addScheduleEntry.setString(4, scheduleEntry.getStatus());
            addScheduleEntry.setTimestamp(5, scheduleEntry.getTimestamp());
            addScheduleEntry.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> ScheduleByStudent = new ArrayList<ScheduleEntry>();
        try {
            getScheduleByStudent = connection.prepareStatement("select Semester, CourseCode, StudentID, Status, Timestamp from app.Schedule ss order by Semester");
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next()) {
                if (studentID.equals(resultSet.getString(3))) {
                    ScheduleEntry scheduleEntry = new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5));
                    ScheduleByStudent.add(scheduleEntry);
                }
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return ScheduleByStudent;
    }

    public static int getScheduledStudentCount(String currentSemester, String courseCode) {
        connection = DBConnection.getConnection();
        int ScheduledStudentCount = 0;
        try {
            getScheduledStudentCount = connection.prepareStatement("select Semester, CourseCode from app.Class order by Semester");
            resultSet = getScheduledStudentCount.executeQuery();
            
            while(resultSet.next()) {
                ScheduledStudentCount += (currentSemester.equals(resultSet.getString(1)) && courseCode.equals(resultSet.getString(2))) ? 1 : 0;
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return ScheduledStudentCount;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByClass(String semester, String courseCode){
       ArrayList<ScheduleEntry> resultW = new ArrayList<ScheduleEntry>();
       connection=DBConnection.getConnection();
       try {
           getWaitlistedStudentsByClass = connection.prepareStatement("select * from app.schedule where app.coursecode=(?) and app.semester=(?) and app.status=(?) order by app.schedule.timestamp");
           getWaitlistedStudentsByClass.setString(1,courseCode);
           getWaitlistedStudentsByClass.setString(2,semester);
           getWaitlistedStudentsByClass.setString(3,"W");
           resultSet = getWaitlistedStudentsByClass.executeQuery();
           while(resultSet.next()){
               resultW.add(new ScheduleEntry(resultSet.getString(3),resultSet.getString(2),resultSet.getString(1),"W",resultSet.getTimestamp(5)));
           }
       } catch(SQLException sqlException) {
           sqlException.printStackTrace();
       }
       
       return resultW;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode){
        connection=DBConnection.getConnection();
        try{
            dropStudentScheduleByCourse=connection.prepareStatement("delete from app.schedule where semester = (?) and studentid = (?) and coursecode = (?)");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, studentID);
            dropStudentScheduleByCourse.setString(3, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode){
        connection=DBConnection.getConnection();
        try{
            dropScheduleByCourse=connection.prepareStatement("delete from app.schedule where semester = (?) and coursecode = (?)");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeQuery();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(String semester, ScheduleEntry entry){
        connection = DBConnection.getConnection();
        try{
            updateScheduleEntry = connection.prepareStatement("update app.schedule set status = (?) where semester = (?) and coursecode = (?) and studentID = (?)");
            updateScheduleEntry.setString(1, "S");
            updateScheduleEntry.setString(2, semester);
            updateScheduleEntry.setString(3, entry.getCourseCode());
            updateScheduleEntry.setString(4, entry.getStudentID());
            updateScheduleEntry.executeUpdate();
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
