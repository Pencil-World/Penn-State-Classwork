/**
 *
 * @author iayfn
 */
public class CourseEntry {
    private final String courseCode;
    private final String description;

    public CourseEntry(String courseCode, String description) {
        this.courseCode = courseCode;
        this.description = description;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getDescription() {
        return description;
    }
}
