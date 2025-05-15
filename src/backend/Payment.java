/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.io.File;
import java.sql.*;
import java.util.Date;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;

/**
 *
 * @author lenovo
 */
public class Payment {

    private int PaymentID;
    private String date;
    private int paid_to;
    private int payAmount;

    public Payment(String d, int empid) {
        date = d;
        paid_to = empid;
    }

    public int paySalary() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "select basicPay, hraPercent, daPercent, pfPercent from (select designation from employee where empid=?) as d inner join salaryStructure as s on d.designation=s.designation;";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, paid_to);
            String insertquery = "insert into payment (paymentDate, paid_to_emp, salary) values (?,?,?);";
            PreparedStatement ps = con.prepareStatement(insertquery);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int bp = rs.getInt("basicPay");
                int hra = rs.getInt("hraPercent");
                int da = rs.getInt("daPercent");
                int pf = rs.getInt("pfPercent");
                int salary = calculateSalary(bp, hra, da, pf);
                payAmount = salary;
                ps.setString(1, date);
                //System.out.println(date.getYear() + "-" + date.getMonth() + "-" + date.getDate());
                ps.setInt(2, paid_to);
                ps.setInt(3, salary);
                ps.execute();
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int calculateSalary(int bp, int hra, int da, int pf) {
        int hraAmt = bp * hra / 100;
        int daAmt = bp * da / 100;
        int pfAmt = bp * pf / 100;
        return (bp + hraAmt + daAmt - pfAmt);
    }

    public void generateReport() {
        //generate pdf and store it
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.beginText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3350/employee_payroll_processing", "root", "root");
            String query = "select empid, empname, e.designation, basicPay, hraPercent, daPercent, pfPercent, paymentid, paymentDate, salary from employee as e inner join salaryStructure as s inner join payment as p on e.designation=s.designation and e.empid=p.paid_to_emp where empid=?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, paid_to);
            ResultSet rs = ps.executeQuery();
            rs.next();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 700);

            contentStream.showText("Employee ID: " + rs.getString("empid"));
            contentStream.newLine();
            contentStream.showText("Employee Name: " + rs.getString("empname"));
            contentStream.newLine();
            contentStream.showText("Designation: " + rs.getString("designation"));
            contentStream.newLine();
            contentStream.showText("Payment ID: " + rs.getString("paymentId"));
            contentStream.newLine();
            contentStream.showText("Basic Pay: " + rs.getString("basicPay"));
            contentStream.newLine();
            contentStream.showText("HRA: +" + (rs.getInt("hraPercent") * rs.getInt("basicPay") / 100));
            contentStream.newLine();
            contentStream.showText("DA: +" + (rs.getInt("daPercent") * rs.getInt("basicPay") / 100));
            contentStream.newLine();
            contentStream.showText("PF: -" + (rs.getInt("pfPercent") * rs.getInt("basicPay") / 100));
            contentStream.newLine();
            contentStream.showText("Net Pay: " + rs.getInt("salary"));
            contentStream.newLine();
            contentStream.showText("Payment Date: -" + rs.getString("paymentDate"));

            contentStream.endText();
            contentStream.close();

            doc.save("reports/"+paid_to + "-" + date + ".pdf");
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
