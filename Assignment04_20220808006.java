import java.util.*;
import java.util.stream.Stream;

public class Assignment04_20220808006 {

}

class Department {

    private String code;
    private String name;
    private Teacher chair;

    public Department(String code, String name) {
        setCode(code);
        setName(name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code.length() == 3 || code.length() == 4) {
            this.code = code;
        } else {
            throw new InvalidValueAttemptedException(code, "3 or 4");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Teacher getChair() {
        return chair;
    }

    public void setChair(Teacher chair) {

        if (chair == null || hashCode() == chair.getDepartment().hashCode()) {
            this.chair = chair;
        } else {
            throw new DepartmentMismatchException(this, chair);
        }
    }

}

class Course {

    private Department department;
    private Teacher teacher;
    private String title;
    private String description;
    private int AKTS;
    private int courseNumber;

    public Course(Department department, int courseNumber, String title, String description, int AKTS,
            Teacher teacher) {

        if (teacher.getDepartment().equals(department)) {
            this.department = department;
            setCourseNumber(courseNumber);
            this.title = title;
            this.description = description;
            setAKTS(AKTS);
            setTeacher(teacher);
        } else {
            throw new DepartmentMismatchException(department, teacher);
        }

    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        if (department == null || teacher == null || teacher.getDepartment().equals(department)) {
            this.department = department;
        } else {
            throw new DepartmentMismatchException(department, teacher);
        }
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        if (teacher == null || teacher.getDepartment().equals(this.department)) {
            this.teacher = teacher;
        } else {
            throw new DepartmentMismatchException(this.department, teacher);
        }
    }

    public int getAKTS() {
        return AKTS;
    }

    public void setAKTS(int aKTS) {
        if (aKTS > 0)
            AKTS = aKTS;
        else
            throw new InvalidValueAttemptedException(aKTS, "must be grather than 0");
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        if ((courseNumber >= 100 && courseNumber <= 999) ||
                (courseNumber >= 5000 && courseNumber <= 5999) ||
                (courseNumber >= 7000 && courseNumber <= 7999)) {
            this.courseNumber = courseNumber;
        } else {
            throw new InvalidValueAttemptedException(courseNumber, description);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String courseCode() {
        return getDepartment() + "" + getCourseNumber();
    }

    @Override
    public String toString() {
        return String.format("%s - %s(%d)", courseCode(), getTitle(), AKTS);
    }

}

abstract class Person {

    private Department department;
    private String name;
    private String email;
    private long ID;

    public Person(String name, String email, long ID, Department department) {
        setName(name);
        setEmail(email);
        setID(ID);
        setDepartment(department);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmailAddress(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException(" invalid email!");
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]"
                + "+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"
                + "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public long getID() {
        return ID;
    }

    public void setID(long iD) {
        this.ID = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {

        return name + " (" + ID + ")" + " - " + email;
    }

}

class Teacher extends Person {

    private int rank;

    public Teacher(String name, String email, long ID, Department department, int rank) {
        super(name, email, ID, department);
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void promote() {
        if (this.rank < 5) {
            rank++;
        } else {
            throw new InvalidRankException(rank);
        }
    }

    public void demote() {
        if (1 < this.rank) {
            rank--;
        } else {
            throw new InvalidRankException(rank);
        }
    }

    @Override
    public void setDepartment(Department department) {
        Department currentDepartment = getDepartment();
        if (currentDepartment != null && currentDepartment.getChair() != null
                && currentDepartment.getChair().equals(this)) {
            currentDepartment.setChair(null);
        }
        super.setDepartment(department);
    }

    public String getTitle() {

        switch (rank) {
            case 1:
                return "Lecturer";
            case 2:
                return "Adjunct Instructor";
            case 3:
                return "Assistant Professor";
            case 4:
                return "Associate Professor";
            case 5:
                return "Professor";
            default:
                throw new InvalidValueAttemptedException(rank, "1 to 5");

        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", getName(), getID(), getEmail());
    }
}

class Student extends Person {

    /*
     * private List<Course> coursesTaken = new ArrayList<>(); // taken lessons
     * private List<Double> gradesTaken = new ArrayList<>(); //
     */// taken grades
    private Map<Course, Map<Semester, Double>> coursesMap; // courses and semseter + grades

    public Student(String name, String email, long ID, Department department) {
        super(name, email, ID, department);
        this.coursesMap = new HashMap<>();
    }

    public int getAKTS() { // only courses
        Set<Course> courseSet = new HashSet<>(coursesMap.keySet());
        int akts = 0;
        for (Course course : courseSet) {
            akts += course.getAKTS();
        }
        return akts;
    }

    public int getAttemptedAKTS() {
        return getAKTS();
    }

    public void addCourse(Course course, Semester semester, double grade) {
        if (grade < 0.0 || grade > 100.0) {
            throw new InvalidGradeException(grade);
        } else {
            coursesMap.putIfAbsent(course, new HashMap<>());
            coursesMap.get(course).put(semester, grade);
        }
    }

    public double courseGPAPoints(Course course) {
        Map<Semester, Double> gradeMap = coursesMap.getOrDefault(course, null);
        double highestGrade = 0;
        for (Double grade : gradeMap.values()) {
            if (grade > highestGrade) {
                highestGrade = grade;
            }
        }
        double gpa = togpaPoints(highestGrade);
        return gpa;
    }

    private static double togpaPoints(double grade) {
        if (grade >= 88.0) {
            return 4.0;
        } else if (grade >= 81.0) {
            return 3.5;
        } else if (grade >= 74.0) {
            return 3.0;
        } else if (grade >= 67.0) {
            return 2.5;
        } else if (grade >= 60.0) {
            return 2.0;
        } else if (grade >= 53.0) {
            return 1.5;
        } else if (grade >= 46.0) {
            return 1.0;
        } else if (grade >= 35.0) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    public String courseGradeLetter(Course course) {
        Map<Semester, Double> gradeMap = coursesMap.getOrDefault(course, new HashMap<>());
        double highestGrade = 0;
        for (Double grade : gradeMap.values()) {
            if (grade > highestGrade) {
                highestGrade = grade;
            }
        }
        String strLetter = togradeLetter(highestGrade);
        return strLetter;
    }

    private static String togradeLetter(double grade) {
        if (grade >= 88.0) {
            return "AA";
        } else if (grade >= 81.0) {
            return "BA";
        } else if (grade >= 74.0) {
            return "BB";
        } else if (grade >= 67.0) {
            return "CB";
        } else if (grade >= 60.0) {
            return "CC";
        } else if (grade >= 53.0) {
            return "DC";
        } else if (grade >= 46.0) {
            return "DD";
        } else if (grade >= 35.0) {
            return "FD";
        } else {
            return "FF";
        }
    }

    public String courseResult(Course course) {
        Map<Semester, Double> gradeMap = coursesMap.getOrDefault(course, new HashMap<>());
        double highestGrade = 0;
        for (Double grade : gradeMap.values()) {
            if (grade > highestGrade) {
                highestGrade = grade;
            }
        }
        String strLetter = toresult(highestGrade);
        return String.format("%s: %s", course.getTitle(), strLetter);
    }

    private static String toresult(double grade) {
        if (grade >= 60) {
            return "Passed";
        } else if (grade >= 53.0) {
            return "Conditionally Passed";
        } else if (grade >= 46.0) {
            return "Conditionally Passed";
        } else if (grade >= 35.0) {
            return "Failed";
        } else {
            return "Failed";
        }
    }

}

class GradStudent extends Student {

    private int rank;
    private String thesisTopic;

    public GradStudent(String name, String email,
            long ID, Department department, int rank, String thesisTopic) {
        super(name, email, ID, department);
        setRank(rank);
        setThesisTopic(thesisTopic);
    }

    public void setRank(int rank) {
        if (rank == 1 || rank == 2 || rank == 3) {
            this.rank = rank;
        } else {
            throw new InvalidRankException(rank);
        }
    }

    public int getRank() {
        return rank;
    }

    public void setThesisTopic(String thesisTopic) {
        this.thesisTopic = thesisTopic;
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public static double gpaPoints(double grade) {
        if (grade >= 88.0) {
            return 4.0;
        } else if (grade >= 81.0) {
            return 3.5;
        } else if (grade >= 74.0) {
            return 3.0;
        } else if (grade >= 67.0) {
            return 2.5;
        } else if (grade >= 60.0) {
            return 2.0;
        } else if (grade >= 53.0) {
            return 1.5;
        } else if (grade >= 46.0) {
            return 1.0;
        } else if (grade >= 35.0) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    public String getLevel() {

        switch (rank) {
            case 1:
                return "Master's Student";
            case 2:
                return "Doctoral Student";
            case 3:
                return "Doctoral Candidate";
            default:
                throw new InvalidValueAttemptedException(rank, "1, 2 or 3");

        }
    }

}

final class Semester {
    private final int season;
    private final int year;

    public Semester(int season, int year) {
        if (season < 1 || season > 3) {
            throw new InvalidValueAttemptedException(season, "must be 1, 2 or 3");
        }
        if (year < 0) {
            throw new InvalidValueAttemptedException(year, "must be positive");
        }
        this.season = season;
        this.year = year;
    }

    public String getSeason() {
        switch (season) {
            case 1:
                return "Fall";
            case 2:
                return "Spring";
            case 3:
                return "Summer";
            default:
                throw new InvalidValueAttemptedException(season, "must be 1, 2 or 3");
        }
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return getSeason() + " - " + getYear();
    }
}

class SemesterNotFoundException extends RuntimeException {
    private Student student;
    private Semester semester;

    public SemesterNotFoundException(Student student, Semester semester) {
        this.student = student;
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "SemesterNotFoundException: " + student.getID() + " has not taken any courses in " + semester;
    }
}

class CourseNotFoundException extends RuntimeException {

    private Student student;
    private Course course;

    public CourseNotFoundException(Student student, Course course) {
        this.student = student;
        this.course = course;

    }

    @Override
    public String toString() {

        return "CourseNotFoundException: " + student.getID() + " has not yet taken" + course.courseCode();
    }

}

class DepartmentMismatchException extends RuntimeException {

    private Department department;
    private Teacher person;
    private Course course;

    public DepartmentMismatchException(Course course, Teacher person) {
        this.course = course;
        this.person = person;
        this.department = null;
    }

    public DepartmentMismatchException(Department department, Teacher person) {
        this.department = department;
        this.person = person;
        this.course = null;
    }

    @Override
    public String toString() {
        if (course == null) {
            return "DepartmentMismatchException: " + person.getName() + "(" + person.getID() + ") cannot be chair of "
                    + department.getCode() + " because he/she is currently assigned to "
                    + person.getDepartment().getName();
        } else {
            return "DepartmentMismatchException: " + person.getName() + "(" + person.getID() + ") cannot teach "
                    + course.courseCode() + " because he/she is currently assigned to "
                    + person.getDepartment().getName();
        }
    }
}

class InvalidGradeException extends RuntimeException {

    private double grade;

    public InvalidGradeException(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "InvalidGradeException: " + grade;
    }

}

class InvalidRankException extends RuntimeException {

    private int rank;

    public InvalidRankException(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "InvalidRankException: " + rank;
    }
}

class InvalidValueAttemptedException extends RuntimeException {

    private Object invalid;
    private String message;

    public InvalidValueAttemptedException(Object invalid, String message) {
        this.invalid = invalid;
        this.message = message;
    }

    @Override
    public String toString() {
        return "InvalidValueAttemptedException: " + invalid.getClass().getSimpleName() + ", " + invalid
                + ", valid values: " + message;
    }
}