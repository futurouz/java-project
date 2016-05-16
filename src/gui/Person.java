/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import data.ConnectionDB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author narut
 */
public class Person {

    private int perId;
    private String perName;
    private String perPass;
    private double income;
    private double expense;
    private double sumin_ex;
    private double totalSave;
    private double total;
    private List<History> history = null;

    public Person() {
    }

    public Person(String perName) throws SQLException {
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM PERSONS WHERE PERNAME=? ");
        ps.setString(1, perName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            this.perId = rs.getInt("perid");
            this.perName = rs.getString("perName");
            this.perPass = rs.getString("perPass");
            this.income = rs.getDouble("income");
            this.expense = rs.getDouble("expense");
            this.sumin_ex = rs.getDouble("sumin_ex");
            this.totalSave = rs.getDouble("totalSave");
            this.total = rs.getDouble("total");
            this.history = History.getHistory(getPerId());
        }
    }

    public List<History> getHistory() {
        return  history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getPerPass() {
        return perPass;
    }

    public void setPerPass(String perPass) {
        this.perPass = perPass;
    }

    public double getTotalSave() {
        return totalSave;
    }

    public void setTotalSave(double totalSave) {
        this.totalSave = totalSave;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double amount) {
        this.total = amount;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getSumin_ex() {
        return sumin_ex;
    }

    public void setSumin_ex(double sumin_ex) {
        this.sumin_ex = sumin_ex;
    }

    public static int forgetPass(int accountId, String newPass) throws SQLException {
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE PERSONS SET perpass='?' WHERE perid=?");
        ps.setString(1, newPass);
        ps.setInt(2, accountId);
        return ps.executeUpdate();
    }

    public static int createUser(int id, String user, String password, double income, double expense, double sumin_ex, double totalSave, double total) throws SQLException {
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO PERSONS (PERID,PERNAME,PERPASS,INCOME,EXPENSE,SUMIN_EX,TOTALSAVE,TOTAL) VALUES (?,?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, user);
        ps.setString(3, password);
        ps.setDouble(4, income);
        ps.setDouble(5, expense);
        ps.setDouble(6, sumin_ex);
        ps.setDouble(7, totalSave);
        ps.setDouble(8, total);
        return ps.executeUpdate();
    }

    public static boolean checkUser(String user, String password) throws SQLException {
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("select PERNAME,PERPASS from PERSONS where PERNAME=? and PERPASS=?");
        ps.setString(1, user);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public double addIncome(int id, Date day, String catalog, double amount) throws SQLException {
        setIncome(this.income + amount);
        setSumin_ex(this.sumin_ex + amount);
        setTotal(this.total + amount);
        History h = new History();
        h.setAmount(amount);
        h.setHistoryCode(History.HistoryCode.IN);
        h.setHistoryDateTime(new java.sql.Date(System.currentTimeMillis()));
        addHistory(h);
        
        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO INCOME (PERID,DAYS,catalog,amount) VALUES (?,?,?,?)");
        ps.setInt(1, id);
        ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ps.setString(3, catalog);
        ps.setDouble(4, amount);
        return ps.executeUpdate();
    }

    public double addExpense(int id, Date day, String catalog, double amount) throws SQLException {
        setExpense(this.expense + amount);
        setSumin_ex(this.sumin_ex + amount);
        setTotal(this.total - amount);
        History h = new History();
        h.setAmount(amount);
        h.setHistoryCode(History.HistoryCode.EX);
        h.setHistoryDateTime(new java.sql.Date(System.currentTimeMillis()));
        addHistory(h);

        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO EXPENSE (PERID,DAYS,catalog,amount) VALUES (?,?,?,?)");
        ps.setObject(1, id);
        ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ps.setString(3, catalog);
        ps.setDouble(4, amount);
        return ps.executeUpdate();
    }

  


public double addSave(int id, Date day,String catalog, double amount) throws SQLException {
        setTotal(this.total - amount);
        setTotalSave(this.totalSave + amount);
        History h = new History();
        h.setAmount(amount);
        h.setHistoryCode(History.HistoryCode.SA);
        h.setHistoryDateTime(new java.sql.Date(System.currentTimeMillis()));
        addHistory(h);

        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO SAVE (PERID,DAYS,CATALOG,AMOUNT) VALUES (?,?,?,?)");
        ps.setInt(1, id);
        ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ps.setString(3, catalog);
        ps.setDouble(4, amount);
        return ps.executeUpdate();
    }

    public void update() {
        try {
            Connection conn = ConnectionDB.getConnection();
            String sqlCmd = null;
            sqlCmd = "UPDATE PERSONS SET INCOME=?, EXPENSE=?,SUMIN_EX=?, TOTALSAVE=?, TOTAL=? WHERE PERID=?";
            PreparedStatement pstm = conn.prepareStatement(sqlCmd);
            pstm.setDouble(1, this.income);
            pstm.setDouble(2, this.expense);
            pstm.setDouble(3, this.sumin_ex);
            pstm.setDouble(4, this.totalSave);
            pstm.setDouble(5, this.total);
            pstm.setInt(6, this.perId);
            int row = pstm.executeUpdate();
            conn.close();
            if (getHistory() != null) {
                for (History h : getHistory()) {
                    if (h.getHistoryId() == 0) {
                        h.writeHistory(perId);
                    }
                }
                history.clear();
            }
        } catch (SQLException ex) {
            System.err.println("Can't create/update account !!!!");
            System.err.println(ex);
        }

    }

    private static void orm(ResultSet rs, Person per) throws SQLException {
        per.setPerId(rs.getInt("perid"));
        per.setPerName(rs.getString("perName"));
        per.setPerPass(rs.getString("perPass"));
        per.setIncome(rs.getDouble("income"));
        per.setExpense(rs.getDouble("Expense"));
        per.setSumin_ex(rs.getDouble("sumin_ex"));
        per.setTotalSave(rs.getDouble("totalSave"));
        per.setTotal(rs.getDouble("total"));
        per.setHistory(History.getHistory(per.getPerId()));
    }

    private void addHistory(History h) {
        if (history == null) {
            history = new ArrayList<History>();
        }
        history.add(h);
    }

    private void addHistory(int x, History h) {
        if (history == null) {
            history = new ArrayList<History>();
        }
        history.add(x, h);
    }

    @Override
        public String toString() {
        return "Person{" + "perId=" + perId + ", perName=" + perName + ", perPass=" + perPass + ", income=" + income + ", expense=" + expense + ", sumin_ex=" + sumin_ex + ", totalSave=" + totalSave + ", total=" + total + ", history=" + history + '}';
    }

}
