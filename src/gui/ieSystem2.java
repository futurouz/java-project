/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.History;
import model.Person;

/**
 *
 * @author kunanan
 */
public class ieSystem2 {

    public static void main(String[] args) throws SQLException, IOException {

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
            System.out.println("Press 7 : Write Data");
            System.out.println("Press 8 : Total");
            System.out.println("Press 9 : All Summary");
            System.out.println("Press 10 : Sign Out");
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
                catalog = JOptionPane.showInputDialog("What is your save :");
                Saving = Integer.parseInt(JOptionPane.showInputDialog("How much do you do want to save?"));
                SavingTotal += Saving;
                person.addSave(person.getPerId(), day, catalog, Saving);
                person.update();
            } //History
            else if (check == 4) {
                String x = "";
                do {
                    try {
                        Connection conn = ConnectionDB.getConnection();
                        long a = person.getPerId();

                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM  HISTORY WHERE PERID = " + a);
                        ResultSet rec = ps.executeQuery();
                        System.out.println("\n");
                        while ((rec != null) && (rec.next())) {
                            System.out.print(rec.getDate("HISTORY_DATE"));
                            System.out.print("  ");
                            System.out.print(rec.getString("HISTORY_CODE"));
                            System.out.print("  ");
                            System.out.print(rec.getDouble("AMOUNT"));
                            System.out.println("  ");
                        }
                        ps.close();
                        conn.close();
                        System.out.println("");
                        System.out.print("Back to menu (0 or back) : ");
                        x = sc.next();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));

            }// Search
            else if (check == 5) {
                System.out.println("What do you want to search?");
                System.out.println("1 : Income");
                System.out.println("2 : Expense");
                System.out.println("3 : Save");
                int x = sc.nextInt();
                if (x == 1) {
                    try {
                        Connection conn = ConnectionDB.getConnection();
                        long a = person.getPerId();
                        System.out.print("Enter to search : ");
                        String NAME = sc.next();
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM  INCOME WHERE PERID = " + a + "  and CATALOG LIKE '%" + NAME + "%' ");
                        ResultSet rec = ps.executeQuery();
                        System.out.println("\n");
                        while ((rec != null) && (rec.next())) {
                            System.out.print(rec.getDate("Days"));
                            System.out.print("  ");
                            System.out.print(rec.getString("CATALOG"));
                            System.out.print("  ");
                            System.out.print(rec.getDouble("AMOUNT"));
                            System.out.println("  ");
                        }
                        ps.close();
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (x == 2) {
                    try {
                        Connection conn = ConnectionDB.getConnection();
                        long a = person.getPerId();
                        System.out.print("Enter to search : ");
                        String NAME = sc.next();
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM  EXPENSE WHERE PERID = " + a + "  and CATALOG LIKE '%" + NAME + "%' ");
                        ResultSet rec = ps.executeQuery();
                        System.out.println("\n");
                        while ((rec != null) && (rec.next())) {
                            System.out.print(rec.getDate("Days"));
                            System.out.print("  ");
                            System.out.print(rec.getString("CATALOG"));
                            System.out.print("  ");
                            System.out.print(rec.getDouble("AMOUNT"));
                            System.out.println("  ");
                        }
                        ps.close();
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (x == 3) {
                    try {
                        Connection conn = ConnectionDB.getConnection();
                        long a = person.getPerId();
                        System.out.print("Enter to search : ");
                        String NAME = sc.next();
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM  SAVE WHERE PERID = " + a + "  and CATALOG LIKE '%" + NAME + "%' ");
                        ResultSet rec = ps.executeQuery();
                        System.out.println("\n");
                        while ((rec != null) && (rec.next())) {
                            System.out.print(rec.getDate("Days"));
                            System.out.print("  ");
                            System.out.print(rec.getString("CATALOG"));
                            System.out.print("  ");
                            System.out.print(rec.getDouble("AMOUNT"));
                            System.out.println("  ");
                        }
                        ps.close();
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }//Analy 
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
            } else if (check == 7) {
                //printData
                Connection conn = ConnectionDB.getConnection();
                long a = person.getPerId();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM  PERSONS WHERE PERID = " + a);
                ResultSet rec = ps.executeQuery();
                String path = "/home/kunanan/Desktop/IncomeExpenseLogin/src/incomeexpenselogin/textFile/userData.txt";
                FileWriter writer;
                try {
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date date = calendar.getTime();

                    File file = new File(path);
                    writer = new FileWriter(file, false);  //True = Append to file, false = Overwrite
                    while ((rec != null) && (rec.next())) {
                        writer.write("User ID : ");
                        writer.write(rec.getString("PERID"));
                        writer.write("\t\t\t\t\tUser name : ");
                        writer.write(rec.getString("PERNAME"));
                        writer.write("\n------------------------------------------------------------------------\n\n\tIncome \t\t\t\t\t ");
                        writer.write(rec.getString("INCOME"));
                        writer.write(" baht\n\tExpense \t\t\t\t ");
                        writer.write(rec.getString("EXPENSE"));
                        writer.write(" baht\n\tTotal Income&Expense   \t\t\t");
                        writer.write(rec.getString("SUMIN_EX"));
                        writer.write(" baht\n\tTotal Save \t\t\t\t ");
                        writer.write(rec.getString("TOTALSAVE"));
                        writer.write(" baht\n\tTotal \t\t\t\t\t ");
                        writer.write(rec.getString("TOTAL"));
                        writer.write(" baht\n\n\n");
                        writer.write("\t\t\t\t\t" + date + "\n");
                        writer.write("------------------------------------------------------------------------");
                    }
                    writer.close();
                    ps.close();;
                    conn.close();
                    System.out.println("");
                    System.out.println("Write success!");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } //TOTAL
            else if (check
                    == 8) {
                String x;
                do {
                    System.out.println("-----------------------------------------");
                    System.out.println("TOTALSAVE             TOTAL");
                    System.out.println(person.getTotalSave() + " Baht" + "             " + person.getTotal() + " Baht");
                    System.out.println("\n\n");
                    System.out.print("Back to menu (0 or back) : ");
                    x = sc.next();
                } while (!x.equalsIgnoreCase("back") && !x.equalsIgnoreCase("0"));
            } //ALL Summy//ALL Summy
            else if (check
                    == 9) {
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
            } //LOG OUT //LOG OUT 
            else {
                System.out.println("See ya.\n");
                login = false;
                break;
            }
        } while (true);
    }

}
