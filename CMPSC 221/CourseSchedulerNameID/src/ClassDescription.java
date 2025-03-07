/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class ClassDescription {
    private String CourseCode="";
    private String Description="";
    private int Seats=0;
    
    public ClassDescription(String CourseCode, String Description, int Seats){
        this.CourseCode=CourseCode;
        this.Description=Description;
        this.Seats=Seats;
    }
    
    public String getCourseCode(){
        return this.CourseCode;
    }
    
    public String getDescription(){
        return this.Description;
    }
    
    public int getSeats(){
        return this.Seats;
    }
}
