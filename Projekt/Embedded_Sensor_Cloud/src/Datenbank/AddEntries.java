/*
 * 10000 Einträge (verteilt auf die letzten 10 Jahre) in der temperature Tabelle erzeugen
 */
package Datenbank;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Angelika Reithmayer
 */
public class AddEntries {
    public static void main(String[] args) throws SQLException
    {
    
        try
        {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch(ClassNotFoundException ex)
        {
                JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }
        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test")) {
            long time = 1388783237064L; //03.01.2014 in millisek. seit 1970
            Date date = new Date(time);
            Random rand = new Random();
            try (PreparedStatement cmd = db.prepareStatement("INSERT INTO [temperature] ([date], [temp]) VALUES (?, ?)")) {
                for(int i = 0; i < 10000; i++)
                {
                    
                    cmd.setDate(1, date);
                    cmd.setFloat(2, rand.nextFloat() * 30); //Random Zahl für Temperatur zw. 0 und 30

                    int rows = cmd.executeUpdate();
                    System.out.println(rows);
                    time -= 31556925; //10 Jahre (in Sek.) / 10000 -> Gleichverteilung...
                    date.setTime(time);
                
                }
            }
        }
    }
}
