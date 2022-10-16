package com.smartattendance;

public class NewStudentForm{

    String FirstName, LastName, StdPRN, StdCourse, Pass;

    public NewStudentForm(String fName, String lName, String stdPRN,String stdCourse, String pass){
        FirstName = fName;
        LastName = lName;
        StdPRN = stdPRN;
        StdCourse = stdCourse;
        Pass = pass;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getStdPRN() {
        return StdPRN;
    }

    public String getStdCourse() {
        return StdCourse;
    }

    public String getPass() {
        return Pass;
    }
}
