
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
public class StudentQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addSemester;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static PreparedStatement getSemesterList;
    private static ResultSet resultSet;
    private static ResultSet resultStudent;
    
    public static void addStudent(StudentEntry student)
    {
        connection = DBConnection.getConnection();
        try
        {
            addSemester = connection.prepareStatement("insert into app.student (firstname,lastname,id) values (?,?,?)");
            addSemester.setString(1, student.getFirstName());
            addSemester.setString(2, student.getLastName());
            addSemester.setString(3, student.getStudentID());
            addSemester.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<StudentEntry> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> semester = new ArrayList<StudentEntry>();
        try
        {
            getSemesterList = connection.prepareStatement("select id,firstname,lastname from app.student order by id");
            resultSet = getSemesterList.executeQuery();
            
            while(resultSet.next())
            {
                semester.add(new StudentEntry(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return semester;
        
    }
    public static StudentEntry getStudent(String id){
        connection=DBConnection.getConnection();
        StudentEntry student=null;
        try{
            getStudent=connection.prepareStatement("select * from app.student where id=(?)");
            getStudent.setString(1, id);
            resultStudent=getStudent.executeQuery();
            if(resultStudent.next()){
                student=new StudentEntry(resultStudent.getString(3),resultStudent.getString(1),resultStudent.getString(2));
            }
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return student;
    }
    public static void dropStudent(String id)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudent = connection.prepareStatement("delete from app.student where id=(?)");
            dropStudent.setString(1, id);
            dropStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
}
