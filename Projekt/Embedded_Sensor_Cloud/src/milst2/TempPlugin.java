/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Angelika Reithmayer
 */
public class TempPlugin {
    
    public static void main(String[] args)
    {
        try {
            //Datenbank Treiber laden
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }
        
        //********************try statt db.close() od cmd.close()... was besser? oder egal? ***********************************\\
        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test"); 
            PreparedStatement cmd = db.prepareStatement("SELECT[id], [date], [temp] FROM [temperature] WHERE [date] = ?"))
        {
            // Parameter setzen
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse("2008-12-12");
            cmd.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rd = cmd.executeQuery())
            {
                while(rd.next())
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append(rd.getInt(1));
                    sb.append(' ');
                    sb.append(rd.getDate(2));
                    sb.append(' ');
                    sb.append(rd.getFloat(3));

                    System.out.println(sb);
                }
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }



    
    }
}
