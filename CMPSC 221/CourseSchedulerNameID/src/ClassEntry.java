/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class ClassEntry {
    private String Semester="";
    private String CourseCode="";
    private int Seats=0;
    
    public ClassEntry(String Semester, String CourseCode, int Seats){
        this.Semester=Semester;
        this.CourseCode=CourseCode;
        this.Seats=Seats;
    }
    
    public String getSemester(){
        return this.Semester;
    }
    
    public String getCourseCode(){
        return this.CourseCode;
    }
    public int getSeats(){
        return this.Seats;
    }
}
