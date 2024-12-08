package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connection.Connect;

public class dupDateDel {

	public void delDb() {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = 	  "DELETE FROM DBA.UNIQUE_CORPSTOCK A"
					+ " WHERE ROWID > ("
					+ "SELECT MIN(ROWID) "
					+ "  FROM DBA.UNIQUE_CORPSTOCK b"
					+ " WHERE b.BASE_YMD = a.BASE_YMD"
					+ "   AND B.STOCK_CODE = A.STOCK_CODE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
			conn.commit();
			
			Connect.setClose();
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
