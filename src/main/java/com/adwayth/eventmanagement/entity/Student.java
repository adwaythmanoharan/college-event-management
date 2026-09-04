package com.adwayth.eventmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    private String studentName;
    
    private String department;

    private String password;

    public Student()
    {

    }

    public int getStudentId()
    {
        return studentId;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setStudentId(int studentId)
    {
        this.studentId= studentId;
    }

    public void setStudentName(String studentName)
    {
        this.studentName= studentName;
    }

    public void setDepartment(String department)
    {
        this.department=department;
    }

    public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}



}
