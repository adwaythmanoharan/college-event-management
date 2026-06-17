package com.adwayth.eventmanagement;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    private String studentName;
    
    private String department;

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




}
