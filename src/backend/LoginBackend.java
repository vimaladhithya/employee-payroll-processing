/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;

/**
 *
 * @author lenovo
 */
public class LoginBackend {

    public LoginBackend() {
    }

    public int login(int uname, String password, String accountType) {
        try {
            System.out.println("function kulla");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query;
            if (accountType.equals("Admin")) {
                query = "SELECT * FROM admin WHERE adminid=?;";
            } else {
                query = "SELECT * FROM employee WHERE empid=?";
            }
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, uname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                if (rs.getInt(1) == uname && password.equals(rs.getString("password"))) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginBackend.class.getName()).log(Level.SEVERE, null, e);
            int errorCode = e.getErrorCode();
        }
        return 0;
    }
    
    public int register(Admin user){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "INSERT INTO ADMIN (adminname, email, phoneno, dob, password) VALUES(?,?,?,?,?)";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNo());
            ps.setString(4, user.getDOB());
            ps.setString(5, user.getPassword());
            boolean status = ps.execute();
            if(status == false){
                return 1;
            }else{
                return 0;
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginBackend.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
}
