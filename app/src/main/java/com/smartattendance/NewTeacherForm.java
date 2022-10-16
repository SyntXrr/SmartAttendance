package com.smartattendance;

public class
NewTeacherForm {

    String FirstName, LastName, TeacherUID, Pass;

    public NewTeacherForm(String fName, String lName, String teacherUID, String pass){
        FirstName = fName;
        LastName = lName;
        TeacherUID = teacherUID;
        Pass = pass;
    }


    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getTeacherUID() {
        return TeacherUID;
    }

    public String getPass() {
        return Pass;
    }
}
