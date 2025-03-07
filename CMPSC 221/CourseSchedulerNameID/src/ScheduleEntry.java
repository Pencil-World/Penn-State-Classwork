
import java.sql.Timestamp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class ScheduleEntry {
    private String Semester="";
    private String CourseCode="";
    private String StudentID="";
    private String Status="";
    private Timestamp TimeStamp;
    
    public ScheduleEntry(String Semester, String CourseCode, String StudentID, String Status, Timestamp TimeStamp){
        this.Semester=Semester;
        this.CourseCode=CourseCode;
        this.StudentID=StudentID;
        this.Status=Status;
        this.TimeStamp=TimeStamp;
    }
    
    
    
    public String getSemester(){
        return this.Semester;
    }
    public String getStudentID(){
        return this.StudentID;
    }
    public String getStatus(){
        return this.Status;
    }
    public String getCourseCode(){
        return this.CourseCode;
    }
    public Timestamp getTimeStamp(){
        return this.TimeStamp;
    }
}
