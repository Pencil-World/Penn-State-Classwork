/**
 *
 * @author iayfn
 */
public class ClassEntry {
    private final String semester;
    private final String courseCode;
    private final int seats;

    public ClassEntry(String semester, String courseCode, int seats) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.seats = seats;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public int getSeats() {
        return seats;
    }
}
