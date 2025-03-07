
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addSchedule;
    private static PreparedStatement getSeats;
    private static PreparedStatement getSchedule;
    private static PreparedStatement dropSchedule;
    private static PreparedStatement dropScheduleByCourse;
    private static ResultSet resultSet;
    private static ResultSet resultSetSeats;
    private static PreparedStatement updateScheduleEntry;
    private static ResultSet resultSetForWaitStudents;
    private static PreparedStatement getWaitStudents;
    
    public static void addScheduleEntry(ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        try
        {
            addSchedule = connection.prepareStatement("insert into app.schedule (coursecode,semester,studentid,status) values (?,?,?,?)");
            addSchedule.setString(1, entry.getCourseCode());
            addSchedule.setString(2, entry.getSemester());
            addSchedule.setString(3, entry.getStudentID());
            addSchedule.setString(4, entry.getStatus());
            addSchedule.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> answer;
        answer = new ArrayList<ScheduleEntry>();
        try
        {
            getSchedule = connection.prepareStatement("select * from app.schedule order by studentid");
            resultSet = getSchedule.executeQuery();
            
            while(resultSet.next())
            {
                Timestamp t = null;
                answer.add(new ScheduleEntry(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4), t));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return answer;
        
    }
    public static int getScheduledStudentCount(String semester, String courseCode){
        int s=0;
        connection=DBConnection.getConnection();
        try{
            getSeats = connection.prepareStatement("select count(studentid) from app.schedule where (semester) like (?) and (coursecode) like (?) and status=(?)");
            getSeats.setString(1, semester);
            getSeats.setString(2, courseCode);
            getSeats.setString(3, "S");
            resultSetSeats=getSeats.executeQuery();
            if(resultSetSeats.next())
            s=resultSetSeats.getInt(1);
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return s;
    }
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode){
        connection=DBConnection.getConnection();
        try{
            dropSchedule=connection.prepareStatement("delete from app.schedule where semester = (?) and studentid = (?) and coursecode = (?)");
            dropSchedule.setString(1, semester);
            dropSchedule.setString(2, studentID);
            dropSchedule.setString(3, courseCode);
            dropSchedule.executeUpdate();
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
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByClass(String semester, String courseCode){
       ArrayList<ScheduleEntry> resultW = new ArrayList<ScheduleEntry>();
       connection=DBConnection.getConnection();
       try
       {
           getWaitStudents = connection.prepareStatement("select * from app.schedule where app.coursecode=(?) and app.semester=(?) and app.status=(?) order by app.schedule.timestamp");
           getWaitStudents.setString(1,courseCode);
           getWaitStudents.setString(2,semester);
           getWaitStudents.setString(3,"W");
           resultSetForWaitStudents = getWaitStudents.executeQuery();
           while(resultSetForWaitStudents.next()){
               resultW.add(new ScheduleEntry(resultSetForWaitStudents.getString(3),resultSetForWaitStudents.getString(2),resultSetForWaitStudents.getString(1),"W",resultSetForWaitStudents.getTimestamp(5)));
           }
       }
       catch(SQLException sqlException)
       {
           sqlException.printStackTrace();
       }
       return resultW;
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
}
