/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class CourseEntry {
    private String CourseCode="";
    private String Description="";
    
    public CourseEntry(String CourseCode, String Description){
        this.CourseCode=CourseCode;
        this.Description=Description;
    }
    
    public String getCourseCode(){
        return this.CourseCode;
    }
    
    public String getDescription(){
        return this.Description;
    }
}
