package connection;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class Connect {
	
    private static Connection conn;
	
	public static Connection getInstance() {

		if(conn == null) {
			try {
				String url = "jdbc:mysql://192.168.50.69:3306/DBA";
				String user = "dudrn0585";
				String password = "wjsdur2015!";

				// JDBC 드라이버 로드
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(url, user, password);
				System.out.println("연결 완료");

			} catch(ClassNotFoundException e) {
				System.out.println("JDBC 드라이버 로드하는데 문제 발생 : " + e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("연결 오류 : " + e.getMessage());
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
