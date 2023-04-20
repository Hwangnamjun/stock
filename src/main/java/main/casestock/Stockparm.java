package main.casestock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connection.Connect;

public class Stockparm {

	public String setData() {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = 	  "DELETE FROM HWANG.UNIQUE_CORPSTOCK A"
					+ " WHERE ROWID > ("
					+ "SELECT MIN(ROWID) "
					+ "  FROM HWANG.UNIQUE_CORPSTOCK b"
					+ " WHERE b.BASE_YMD = a.BASE_YMD"
					+ "   AND B.STOCK_CODE = A.STOCK_CODE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
			conn.commit();
			
			pstmt.clearParameters();
			
			sql = "UPDATE UNIQUE_CORPBLANACE C "
					+ "   SET (C.EPS, C.PER, C.BPS, C.PBR) = "
					+ "( "
					+ "SELECT ROUND(A.THS_INCOME / B.LST_GST_CNT,2)                 AS  EPS "
					+ "     , ROUND(NVL(B.CLPR / DECODE((A.THS_INCOME / B.LST_GST_CNT),0,NULL,(A.THS_INCOME / B.LST_GST_CNT)),0),2)    AS  PER "
					+ "     , ROUND(A.THS_TOT_CAP / B.LST_GST_CNT,2)                AS  BPS "
					+ "     , ROUND(NVL(B.CLPR / DECODE((A.THS_TOT_CAP / B.LST_GST_CNT),0,NULL,(A.THS_TOT_CAP / B.LST_GST_CNT)),0),2)     AS  PBR "
					+ "  FROM HWANG.UNIQUE_CORPBLANACE  A "
					+ "     , HWANG.UNIQUE_CORPSTOCK    B "
					+ " WHERE A.STOCK_CODE = B.STOCK_CODE "
					+ "   AND A.STOCK_CODE = C.STOCK_CODE "
					+ "   AND B.BASE_YMD = (SELECT MAX(BASE_YMD) FROM UNIQUE_CORPSTOCK))";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.execute();
			conn.commit();
			pstmt.clearParameters();
			
			
			Connect.setClose();
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "Stockparm finish";
	}

}
