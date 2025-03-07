/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dev Pratap Singh
 */
public class StudentEntry {
    private String StudentID="";
    private String FirstName="";
    private String LastName="";
    
    public StudentEntry(String id, String frist, String last){
        this.StudentID=id;
        this.FirstName=frist;
        this.LastName=last;
    }
    
    public String getStudentID(){
        return this.StudentID;
    }
    public String getFirstName(){
        return this.FirstName;
    }
    public String getLastName(){
        return this.LastName;
    }
}
