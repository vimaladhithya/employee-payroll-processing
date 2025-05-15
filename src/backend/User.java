/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.*;

/**
 *
 * @author lenovo
 */
public class User {

    protected String Name;
    protected String phoneNo;
    protected String email;
    protected String DOB;
    protected String password;

    public User(String name, String phn, String em, String dob, String pass) {
        Name = name;
        phoneNo = phn;
        email = em;
        DOB = dob;
        password = pass;
    }

    public User() {

    }

    public String getDOB() {
        return DOB;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return Name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPassword() {
        return password;
    }
}
