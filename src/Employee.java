import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JTable table1;
    private JTable table2;
    private JTextField txtid;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/company", "root", "");
            System.out.println("success");
        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }


    void table_load(){
        try{
            pst = con.prepareStatement("select employee.empname, employee.mobile, employee.salary from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        try{
            pst = con.prepareStatement("SELECT employee.empname FROM employee WHERE specialemp = 1");
            ResultSet rs = pst.executeQuery();
            table2.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }



    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname, salary, mobile;

                empname = txtName.getText();  // Use getText() instead of getName()
                salary = txtSalary.getText(); // Use getText() instead of getName()
                mobile = txtMobile.getText(); // Use getText() instead of getName()

                try {
                    pst = con.prepareStatement("insert into employee(empname, salary, mobile) values(?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String empid = txtid.getText();

                    pst = con.prepareStatement("select empname, salary, mobile from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next() == true){
                        String empname = rs.getString(1);
                        String salary = rs.getString(2);
                        String mobile = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(salary);
                        txtMobile.setText(mobile);
                    }

                    else{
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        JOptionPane.showMessageDialog(null, "invalid employee No");
                    }
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid, empname, salary, mobile;

                empname = txtName.getText();  // Use getText() instead of getName()
                salary = txtSalary.getText(); // Use getText() instead of getName()
                mobile = txtMobile.getText(); // Use getText() instead of getName()
                empid = txtMobile.getText(); // Use getText() instead of getName()

                try {
                    pst = con.prepareStatement("update employee set empname = ?, salary = ?, mobile = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.setString(4, empid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record updated!");
                    table_load();

                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid;

                empid = txtid.getText();  // Use getText() instead of getName()


                try {
                    pst = con.prepareStatement("delete from employee where id = ?");
                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record deleted!");
                    table_load();

                    txtid.setText("");
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
