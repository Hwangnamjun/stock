package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Infobean;
import connection.Connect;

public class Searchchk {

	public boolean findDb(Infobean bean) throws Customexception{
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		bean.setKonglish(new Changekonglish().changeWord(bean.getTarget()));
		String sql = "SELECT CORP_CODE,STOCK_CODE,BIZR_NO, MODIFY_YMD "
					 + "FROM HWANG.UNIQUE_CORPCODE "
					+ "WHERE CORP_NAME IN (?,?) "
					+ "UNION "
				   + "SELECT CORP_CODE,STOCK_CODE,BIZR_NO, MODIFY_YMD "
					 + "FROM HWANG.UNIQUE_CORPCODE "
					+ "WHERE STOCK_CODE = ? "
					+ "ORDER BY MODIFY_YMD DESC";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getTarget());
			pstmt.setString(2, bean.getKonglish());
			pstmt.setString(3, bean.getTarget());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setCorpCode(rs.getString("CORP_CODE"));
				bean.setStockCode(rs.getString("STOCK_CODE"));
				
				if(rs.getString("BIZR_NO") == null)
					return true;
					
			} else {
				throw new Customexception("코스피 미등록 회사명입니다 : " + bean.getTarget());
			}
		} catch (SQLException e) {
			e.getStackTrace();
		}
		return false;
	}

}
