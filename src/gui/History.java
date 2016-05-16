package gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gui.ConnectionDB;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kunanan
 */
public class History implements Serializable {

    public static final Map<String, String> historyDescription = new HashMap<String, String>();

    static {
        historyDescription.put("IN", "INCOME");
        historyDescription.put("EX", "EXPENSE");
        historyDescription.put("SA", "SAVE");
    }

    public enum HistoryCode {
        IN, EX, SA
    };

    private long historyId;
    private HistoryCode historyCode;
    private Date historyDateTime;
    private double amount;

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public HistoryCode getHistoryCode() {
        return historyCode;
    }

    public void setHistoryCode(HistoryCode historyCode) {
        this.historyCode = historyCode;
    }

    public Date getHistoryDateTime() {
        return historyDateTime;
    }

    public void setHistoryDateTime(Date historyDateTime) {
        this.historyDateTime = historyDateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getHistoryDescription() {
        return historyDescription.get(historyCode.name());
    }

    public static List<History> getHistory(long acid) {
        List<History> history = new ArrayList<History>();
        try {
            Connection conn = ConnectionDB.getConnection();
            History h = null;
            String sqlCmd = "SELECT * FROM History WHERE perid = ?";
            PreparedStatement pstm = conn.prepareStatement(sqlCmd);
            pstm.setLong(1, acid);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                h = new History();
                orm(rs, h);
                history.add(h);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return history;
    }

    private static void orm(ResultSet rs, History h) throws SQLException {
        h.setAmount(rs.getDouble("amount"));
        h.setHistoryDateTime(rs.getDate("History_date"));
        h.setHistoryId(rs.getLong("History_id"));
        h.setHistoryCode(HistoryCode.valueOf(rs.getString("History_code")));
    }

    public boolean writeHistory(long acid) {
        int x = 0;
        try {
            Connection conn = ConnectionDB.getConnection();
            History h = null;
            String sqlCmd = "INSERT INTO history (perid, history_code, history_date, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement pstm = conn.prepareStatement(sqlCmd);
            pstm.setLong(1, acid);
            pstm.setString(2, this.historyCode.name());
            pstm.setTimestamp(3, new java.sql.Timestamp(this.historyDateTime.getTime()));
            //pstm.setDate(3, this.transactionDateTime);
            pstm.setDouble(4, amount);
            x = pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return x > 0;
    }
    
    ///// test
     public static ArrayList<History> execute (String sql) throws SQLException {
        Connection con = ConnectionDB.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ArrayList<History> h1 = new ArrayList<History>();
        while(rs.next()){
            History h = new History();
            orm(rs,h);
            System.out.println(h);
            h1.add(h);
        }
        con.close();
        return h1;
    }

    @Override
    public String toString() {
        return "History{" + "historyCode=" + historyCode + ", historyDateTime=" + historyDateTime + ", amount=" + amount + "/n";
    }
     
}
