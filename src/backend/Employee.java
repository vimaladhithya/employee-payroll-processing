/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lenovo
 */
public class Employee extends User {

    private String employeeId;
    private int salary;
    private String designation;

    public Employee(String name, String phn, String em, String dob, String pass, String desig) {
        super(name, phn, em, dob, pass);
        designation = desig;
    }
    
    public Employee(String emp){
        employeeId = emp;
    }

    public ResultSet requestReport() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "select empid, empname, e.designation, basicPay, hraPercent, daPercent, pfPercent, paymentid, paymentDate, salary from employee as e inner join salaryStructure as s inner join payment as p on e.designation=s.designation and e.empid=p.paid_to_emp where empid=?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();            
            return rs;
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getAllUsers() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "SELECT empid, empname, email, phoneno, dob, designation, password FROM EMPLOYEE";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
        }
        return null;
    }

    public static int changePassword(String empid, String oldPass, String newPass) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "UPDATE EMPLOYEE SET password=? WHERE empid=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newPass);
            ps.setString(2, empid);
            ps.setString(3, oldPass);
            boolean status = ps.execute();
            if (status == false) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
}
