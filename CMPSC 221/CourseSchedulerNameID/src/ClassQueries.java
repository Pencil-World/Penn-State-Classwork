
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
public class ClassQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addClass;
    private static PreparedStatement dropClass;
    private static PreparedStatement getClassList;
    private static ResultSet resultSet;
    private static ResultSet resultSetForSeats;
    private static PreparedStatement getSeats;
    
    public static void addClass(ClassEntry classroom)
    {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement("insert into app.class (coursecode, semester, seats) values (?,?,?)");
            addClass.setString(1, classroom.getCourseCode());
            addClass.setString(2, classroom.getSemester());
            addClass.setInt(3, classroom.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes(String s)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> semester = new ArrayList<String>();
        try
        {
            getClassList = connection.prepareStatement("select coursecode from app.class where (semester) like (?) order by coursecode");
            getClassList.setString(1,s);
            resultSet = getClassList.executeQuery();
            
            while(resultSet.next())
            {
                semester.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return semester;
        
    }
    public static int getClassSeats(String semester, String courseCode){
        int s=0;
        connection=DBConnection.getConnection();
        try{
            getSeats = connection.prepareStatement("select seats from app.class where (semester) like (?) and where (coursecode) like (?)");
            getSeats.setString(1, semester);
            getSeats.setString(2, courseCode);
            resultSetForSeats=getSeats.executeQuery();
            s=resultSetForSeats.getInt(1);
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return s;
    }
    public static void dropClass(String semester, String courseCode){
        connection=DBConnection.getConnection();
        try{
            dropClass=connection.prepareStatement("delete from app.class where semester like (?) and coursecode=(?)");
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
