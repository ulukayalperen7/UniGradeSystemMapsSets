# Assignment04 - University Management System

This project is a basic University Management System implemented in Java, which manages departments, courses, and personnel like teachers, students, and graduate students, including their relationships and roles within a university context.

## Classes Overview

### 1. `Department`
- **Attributes:** `code`, `name`, `chair` (head of the department)
- **Methods:**
  - **Getters/Setters:** Standard getters and setters for code, name, and chair.
  - **Validations:** Ensures that the department code is either 3 or 4 characters and that the assigned chair belongs to the department.

### 2. `Course`
- **Attributes:** `department`, `teacher`, `title`, `description`, `AKTS` (credits), `courseNumber`
- **Methods:**
  - **Course Code Generation:** Combines department code and course number.
  - **Getters/Setters:** Enforces constraints on course number and credits.
  - **Validations:** Ensures the assigned teacher is part of the course’s department.

### 3. `Person` (Abstract Class)
- **Attributes:** `department`, `name`, `email`, `ID`
- **Methods:**
  - **Email Validation:** Validates the email format.
  - **Getters/Setters:** For setting and retrieving personal information.

### 4. `Teacher`
- Inherits from `Person`
- **Attributes:** `rank`
- **Methods:**
  - **Promotion and Demotion:** Allows changing the teacher’s rank.
  - **Rank-Based Titles:** Returns the title based on the teacher's rank.
  - **Department Update:** If the teacher is the chair of a department, removes them from that role upon reassignment.

### 5. `Student`
- Inherits from `Person`
- **Attributes:** `coursesMap` (a map of courses and corresponding grades)
- **Methods:**
  - **AKTS Calculation:** Calculates total AKTS credits for completed courses.
  - **Add Course and Grade:** Adds courses taken by the student along with the grades.
  - **GPA Calculation and Grading:** Determines GPA points, letter grades, and course pass/fail status based on grades.

### 6. `GradStudent`
- Inherits from `Student`
- **Attributes:** `rank`, `thesisTopic`, `teachingAssistantCourse`
- **Methods:**
  - **Thesis and TA Assignment:** Manages thesis topic and assignment as a teaching assistant if the student meets grade criteria.
  - **GPA Calculation and Grading:** Custom grading and GPA calculation specifically for graduate students.

### 7. `Semester`
- **Attributes:** `season`, `year`
- **Methods:** Season validation (Fall, Spring, Summer).

### Exceptions

- **`SemesterNotFoundException`** - Thrown if a student has no records for a specific semester.
- **`CourseNotFoundException`** - Thrown if a student has not taken a particular course.
- **`DepartmentMismatchException`** - Thrown if a teacher or course does not match the department.
- **`InvalidGradeException`** - Thrown if an invalid grade is attempted.
- **`InvalidRankException`** - Thrown for invalid teacher or student rank.
- **`InvalidValueAttemptedException`** - Thrown if values outside the expected range are used (e.g., course codes, ranks, AKTS credits).

## Usage
To use this system, instantiate `Department`, `Course`, `Teacher`, and `Student` objects as needed. You can add courses to students, assign teachers to courses, set chairs for departments, and handle other typical operations within a university.

## Example Usage

```java
Department compSci = new Department("CS", "Computer Science");
Teacher profJohn = new Teacher("John Doe", "johndoe@university.edu", 123456, compSci, 5);
compSci.setChair(profJohn);

Course algo = new Course(compSci, 101, "Algorithms", "An introduction to algorithms.", 6, profJohn);

Student alice = new Student("Alice Smith", "alice@university.edu", 234567, compSci);
alice.addCourse(algo, new Semester(1, 2023), 90.0);

System.out.println(alice.courseGradeLetter(algo)); // Output: AA 
