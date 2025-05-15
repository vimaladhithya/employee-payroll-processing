/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

/**
 *
 * @author lenovo
 */
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin extends User {

    public Integer adminID;
    public List<Payment> initiates;

    public Admin(String name, String phn, String em, String dob, String pass) {
        super(name, phn, em, dob, pass);
        initiates = new ArrayList<Payment>();
    }

    public Admin(String adminId) {
        super("", "", "", "", "");
//        retreiveDetails(adminId);
        initiates = new ArrayList<Payment>();
    }

    
    
    public int initiatePayment(){
        int status = 1;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "select empid from employee;";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()){
                System.out.println(Date.valueOf(LocalDate.now())+"  "+ rs.getInt(1));
                Payment p = new Payment( java.time.LocalDate.now().toString(), rs.getInt(1));
                initiates.add(p);
                status = p.paySalary();
                p.generateReport();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return status;
    }

    public int defineSalaryStructure(String desig, int bp, int hra, int da, int pf) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String check = "select * from salaryStructure where designation='" + desig + "';";
            System.out.println(check);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(check);
            String query;
            PreparedStatement ps;
            if (rs.next() == true) {
                
                query = "update salaryStructure set basicPay=" + bp + ", hraPercent=" + hra + ", daPercent=" + da + ", pfPercent=" + pf + " where designation='" + desig+"';";
                System.out.println(query);
                ps = con.prepareStatement(query);
                
            } else {

                query = "insert into salaryStructure values(?,?,?,?,?);";
                ps = con.prepareStatement(query);
                ps.setString(1, desig);
                ps.setInt(2, bp);
                ps.setInt(3, hra);
                ps.setInt(4, da);
                ps.setInt(5, pf);
            }

            boolean status = ps.execute();
            if (status == false) {
                return 1;
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    private void retreiveDetails(String adminId) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "SELECT * FROM admin WHERE adminid=?;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                adminID = rs.getInt("adminid");
                Name = rs.getString("adminname");
                email = rs.getString("email");
                DOB = rs.getString("dob");
                password = rs.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int addEmployee(String name, String em, String phn, String dob, String pass, String designation) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "INSERT INTO EMPLOYEE (empname, email, phoneno, dob, password, designation) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, em);
            ps.setString(3, phn);
            ps.setString(4, dob);
            ps.setString(5, pass);
            ps.setString(6, designation);
            boolean status = ps.execute();
            con.close();
            if (status == false) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginBackend.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    
    public static int updateEmployeeDetails(String empid, HashMap<String, String> details) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "update employee set ";
            int count = 0;
            for (String key : details.keySet()) {
                query += key + "='" + details.get(key) + "'";
                if (count != details.size() - 1) {
                    query += ",";
                }
                count++;
            }

            query += " where empid=" + empid + ";";

            System.out.println(query);
            Statement stmt = con.createStatement();

            boolean status = stmt.execute(query);

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
    
    public static int removeEmployee(String empid) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "delete from employee where empid=?;";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, empid);

            boolean status = ps.execute();
            if (status == false) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
        }

        return 0;
    }

}
