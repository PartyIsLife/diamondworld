package ru.party;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SQLite_Manager {
    private static String url= "jdbc:sqlite:plugins/ocelot/db.db";

	 public static void SQLiteManager() throws Exception {
	        
	        Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
	        
	        Connection c = getConnection();
	        Statement s = c.createStatement();
	        s.executeUpdate("CREATE TABLE IF NOT EXISTS ocelot ('nickname' STRING, 'time' DATETIME, 'mobNick' STRING)");
	        
	        s.close();
	        c.close();
	        
	    }
	 
	 public static Connection getConnection() throws Exception {
	        return DriverManager.getConnection(url);
	    }
	 
	 public static void addParams(Long time, String nick, String mobNick) {
		 	
		 try { 
			 Connection c = SQLite_Manager.getConnection();
			 Statement s = c.createStatement();

			 
			 s.executeUpdate("INSERT INTO ocelot ('nickname','time','mobNick') VALUES ('"+nick+"', '"+vremya(time)+"', '"+mobNick+"')"); 
		        
			 s.close();
			 c.close();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 
	 }
	 
	 public static String vremya(long time){
		    Calendar cal = Calendar.getInstance();
		    cal.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
		    cal.setTimeInMillis(time);
		    return (cal.get(Calendar.YEAR) + " " + (cal.get(Calendar.MONTH) + 1) + " " 
		            + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
		            + cal.get(Calendar.MINUTE));

		}
	 
	
}
