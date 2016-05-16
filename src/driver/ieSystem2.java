/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

import data.ConnectionDB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.History;
import model.Person;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 *
 * @author kunanan
 */
public class ieSystem2 extends JFrame {

    public static void main(String[] args) throws SQLException {

        Scanner sc = new Scanner(System.in);
        boolean login = false;
        int check = 0;
        int id = 0;
        String user = "";
        String pass = "";
        Person person = null;
        Date day = null;

        JOptionPane.showMessageDialog(null, "WELCOME TO SYSTEM", "Starting Project", JOptionPane.INFORMATION_MESSAGE);
        do {
            System.out.println("What do you want?");
            System.out.println("press 1 : Login");
            System.out.println("press 2 : Create New User");
            System.out.println("Press 3 : Exit ");
            System.out.println("");
            do {
                try {
                    System.out.print("Enter: ");
                    check = sc.nextInt();
                } catch (InputMismatchException e) {
                    JOptionPane.showMessageDialog(null, "Invalid Input", "Message", JOptionPane.WARNING_MESSAGE);
                    sc.next();
                }
            } while (check <= 1 && check >= 3);

            // EXIT 
            if (check == 3) {
                System.out.println("Close Application");
                System.exit(0);
            }
            // CREATE USER
            if (check == 2) {
                System.out.println("-----------------------------------------");
                System.out.println("Create New User");
                System.out.println("-----------------------------------------");
                System.out.print("Account ID: ");
                id = sc.nextInt();
                System.out.print("Username: ");
                user = sc.next();
                System.out.print("Password: ");
                pass = sc.next();
                try {
                    Person.createUser(id, user, pass, 0, 0, 0, 0, 0);
                    System.out.println("-----------------------------------------");
                    System.out.println("Create Complete");
                    System.out.println("-----------------------------------------");
                } catch (Exception e) {
                    System.out.println("-----------------------------------------");
                    System.out.println("Error, Try again");
                    System.out.println("-----------------------------------------");
                }
            } else if (check == 1) {
                // LOGIN
                do {
                    System.out.println("-----------------------------------------");
                    System.out.println("LOGIN ");
                    System.out.print("Username: ");
                    user = sc.next();
                    System.out.print("Password: ");
                    pass = sc.next();
                    if (Person.checkUser(user, pass)) {
                        System.out.println("-----------------------------------------");
                        System.out.println("Login Complete");
                    } else {
                        System.out.println("-----------------------------------------");
                        System.out.println("Invalid Username / Password");
                    }
                } while (!Person.checkUser(user, pass));
            }
        } while (check != 1);
        login = true;

        person = new Person(user);
        do {
            System.out.println("-----------------------------------------");
            System.out.println("Hi " + person.getPerName());
            System.out.println("Press 1 : Add Income");
            System.out.println("Press 2 : Add Expense");
            System.out.println("Press 3 : Add Save");
            System.out.println("Press 4 : History");
            System.out.println("Press 5 : Search");
            System.out.println("Press 6 : Analytics");
            System.out.println("Press 7 : Total");
            System.out.println("Press 8 : All Summary");
            System.out.println("Press 9 : Sign Out");
            do {
                try {
                    System.out.print("Enter : ");
                    check = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input");
                    sc.next();
                }
            } while (check <= 1 && check >= 5);
            // Add Income
            if (check == 1) {
                double Income;
                String catalog = "";
                double IncomeTotal = 0;
                catalog = JOptionPane.showInputDialog("What is your income : ");
                Income = Integer.parseInt(JOptionPane.showInputDialog("How much is it   "));

                person.addIncome(person.getPerId(), day, catalog, Income);
                person.update();
            } //Add Expense
            else if (check == 2) {
                double Expense;
                String catalog = "";
                catalog = JOptionPane.showInputDialog("What is your expense :");
                Expense = Integer.parseInt(JOptionPane.showInputDialog("How much is it   "));
                person.addExpense(person.getPerId(), day, catalog, Expense);
                person.update();
            } //Add Save 
            else if (check == 3) {
                double Saving;
                double SavingTotal = 0;
                String catalog = "";

                Saving = Integer.parseInt(JOptionPane.showInputDialog("How much is it "));
                SavingTotal += Saving;
                person.addSave(person.getPerId(), day, catalog, Saving);
                person.update();
            } //History
            else if (check == 4) {
                String x;
                do {
                    System.out.println("-----------------------------------------");
                    System.out.println("YOUR HISTORY");
                    System.out.println("Date                Type           Amount");
                    System.out.println("-----------------------------------------");
                    List<History> history = person.getHistory();
                    if (history != null) {
                        int count = 1;
                        for (History h : history) {
                            if (count++ <= 1) {
                                System.out.println(h.getHistoryDateTime() + "   " + h.getHistoryDescription() + "           " + h.getAmount());
                            } else {
                                System.out.println(h.getHistoryDateTime() + "   " + h.getHistoryDescription() + "           " + h.getAmount());
                            }
                        }
                    }
                    System.out.println("-----------------------------------------");
                    System.out.print("Back to menu (0 or back) : ");
                    x = sc.next();
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));
            } //Searh
            else if (check == 5) {
                try {
                    Connection conn = ConnectionDB.getConnection();
                    long a = person.getPerId();
                    String NAME = "Salary";
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM  INCOME WHERE PERID = " + a + "  and CATALOG LIKE '%" + NAME + "%' ");
                    ResultSet rec = ps.executeQuery();

                    while ((rec != null) && (rec.next())) {
                        System.out.print(rec.getDate("Days"));
                        System.out.print(" - ");
                        System.out.print(rec.getString("CATALOG"));
                        System.out.print(" - ");
                        System.out.print(rec.getDouble("AMOUNT"));
                        System.out.print(" - ");
                    }
                    ps.close();
                    conn.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } //Analy 
            else if (check == 6) {
                String x;
                do {
                    DecimalFormat df = new DecimalFormat("##.##");
                    double in = person.getIncome();
                    double ex = person.getExpense();
                    double sum = person.getSumin_ex();
                    System.out.println("-----------------------------------------");
                    System.out.println("Income : " + df.format((in / sum) * 100) + "%");
                    System.out.println("Expense : " + df.format((ex / sum) * 100) + "%");
                    System.out.println("\n\n");
                    System.out.print("Back to menu (0 or back) : ");
                    x = sc.next();
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));
            }//TOTAL
            else if (check == 7) {
                String x;
                do {
                    System.out.println("-----------------------------------------");
                    System.out.println("TOTALSAVE             TOTAL");
                    System.out.println(person.getTotalSave() + " Baht" + "             " + person.getTotal() + " Baht");
                    System.out.println("\n\n");
                    System.out.print("Back to menu (0 or back) : ");
                    x = sc.next();
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));
            } //ALL Summy
            else if (check == 8) {
                String x;
                do {
                    DecimalFormat df = new DecimalFormat("##.##");
                    double in = person.getIncome();
                    double ex = person.getExpense();
                    double sum = person.getSumin_ex();
                    double a = ((in / sum) * 100);
                    double b = ((ex / sum) * 100);
                    System.out.println("-----------------------------------------");
                    System.out.println("ALL SUMMARY");
                    System.out.println("Account: " + person.getPerName());
                    System.out.println("");
                    System.out.println("Total Save ------------- Total");
                    System.out.println(person.getTotalSave() + " Baht               " + person.getTotal() + " Baht");
                    System.out.println("");
                    
                    System.out.println("INCOME --------------- EXPENSE");
                    System.out.println(df.format(a) + "%" + "                  " + df.format(b) + "%");

                    System.out.println("-----------------------------------------");
                    System.out.println("\n\n");
                    System.out.print("Back to menu (0 or back) : ");
                    x = sc.next();
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));
            } //LOG OUT 
            else {
                System.out.println("See ya.\n");
                login = false;
                break;
            }
        } while (true);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

    private static boolean isNumber(String x) {
        try {
            Long.parseLong(x);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
