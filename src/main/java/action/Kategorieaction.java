package action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import connection.Connect;

public class Kategorieaction implements Action {
	
	NumberFormat numberFormat = NumberFormat.getInstance();
	public boolean chk;
	
	@Override
	public Actionforward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String parm = request.getParameter("parm");
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		JSONArray arr = new JSONArray();
		
		if(chk) {
	       sql = "SELECT B.CORP_NAME, B.CORP_CODE, A.* "
			   + "  FROM DBA.UNIQUE_CORPBLANACE A, DBA.UNIQUE_CORPCODE B "
			   + " WHERE A.STOCK_CODE = B.STOCK_CODE "
			   + "   AND B.INDUTY_CODE_SIMPLE = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parm);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				JSONObject obj = new JSONObject();
				
				obj.put("기업명",rs.getString("CORP_NAME"));
				obj.put("EPS", rs.getLong("EPS"));
				obj.put("PER", formatting(rs.getLong("PER")));
				obj.put("BPS", formatting(rs.getLong("BPS")));
				obj.put("PBR", formatting(rs.getLong("PBR")));
				obj.put("당기순이익", formatting(rs.getLong("THS_INCOME")));
				obj.put("당기자산총계", formatting(rs.getLong("THS_ASSETS")));
				obj.put("당기부채총계", formatting(rs.getLong("THS_DEBT")));
				obj.put("당기자본총계", formatting(rs.getLong("THS_TOT_CAP")));
				
				arr.add(obj);
			}
		}
		else {
			sql = "SELECT * "
					+ "FROM DBA.UNIQUE_CORPSTOCK A, DBA.UNIQUE_CORPCODE B "
					+ "WHERE A.STOCK_CODE = B.STOCK_CODE "
					+ "  AND SUBSTR(A.BASE_YMD,0,6) = TO_CHAR(ADD_MONTHS(SYSDATE,-1) ,'YYYYMM') "
					+ "  AND B.INDUTY_CODE_SIMPLE = ? "
					+ "ORDER BY VS DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parm);
			rs = pstmt.executeQuery();
			
			for(int i = 1; i < 6; i++) {
				JSONObject obj = new JSONObject();
				rs.next();
				obj.put("순번",i);
				obj.put("일자",rs.getString("BASE_YMD"));
				obj.put("기업명",rs.getString("CORP_NAME"));
				obj.put("종가",formatting(rs.getString("CLPR")));
				obj.put("전일대비등락",formatting(rs.getString("VS")));
				obj.put("등락비율",rs.getString("FLT_RATE"));
				
				arr.add(obj);
			}
		}
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(arr.toString());
		
		return null;
	}

	public Action settingbool(boolean val){
		
		chk = val;
		
		return this;
	}
	
	private String formatting(Long num) {
		
		return numberFormat.format(num);
	}
	private String formatting(String num) {
		
		return numberFormat.format(Long.parseLong(num));
	}
}
