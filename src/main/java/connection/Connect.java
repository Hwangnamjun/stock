package connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Connect {
	
    private static Connection conn;
	
	public static Connection getInstance() {
		
		if(conn == null) {
			try {
	            Context ctx = new InitialContext();
	            Context envContext = (Context) ctx.lookup("java:/comp/env");
	            DataSource dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
				
				conn = dataFactory.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static void setClose() {
		
		try {
				if(conn != null)
					if(!conn.isClosed())
						conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		conn = null;
	}
	
}
