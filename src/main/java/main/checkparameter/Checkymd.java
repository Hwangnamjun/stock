package main.checkparameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bean.Infobean;
import connection.Connect;
import main.casestock.Stockinsert;
import main.casestock.Stockparse;
import main.casestock.Stockurl;

public class Checkymd {

	public void findDb(Infobean bean) {
		
		String startYmd = null;
		String endYmd = null;
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT MIN(BASE_YMD),MAX(BASE_YMD) FROM HWANG.UNIQUE_CORPSTOCK WHERE STOCK_CODE = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getStockCode());
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				startYmd = rs.getString(1);
				endYmd = rs.getString(2);
			}
			
			rs.close();
			Connect.setClose();
			pstmt.close();
			SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
			Date startDbYmd = dtFormat.parse(startYmd);
			Date endDbYmd = dtFormat.parse(endYmd);
			Date startPramYmd = dtFormat.parse(bean.getStartDT());
			Date endParmYmd = dtFormat.parse(bean.getEndDT());
			Calendar cal = Calendar.getInstance();
			
			//DB < parmeter ? true : false 
			boolean startChk = startDbYmd.equals(startPramYmd) ? true : startDbYmd.before(startPramYmd);
			//DB < parmeter ? false : true
			boolean endChk = endDbYmd.equals(endParmYmd) ? true : endDbYmd.after(endParmYmd);
			
			Infobean upBean = new Infobean();
			upBean.setPageRow(bean.getPageRow());
			upBean.setPageNo(bean.getPageNo());
			upBean.setStockCode(bean.getStockCode());
			upBean.setCrno(bean.getCrno());
			
			if(!startChk) {
				cal.setTime(startDbYmd);
				cal.add(Calendar.DATE, 1);
				upBean.setStartDT(bean.getStartDT());
				upBean.setEndDT(startYmd);
				ymdInsert(upBean);
			}
			
			if(!endChk) {
				cal.setTime(endParmYmd);
				cal.add(Calendar.DATE, 1);
				upBean.setStartDT(endYmd);
				upBean.setEndDT(dtFormat.format(cal.getTime()));
				ymdInsert(upBean);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void ymdInsert(Infobean upBean) {
		String stockUrl = new Stockurl().chkUrl(upBean);
		String stockResult = new Stockparse().getData(stockUrl);
		new Stockinsert().insertData(stockResult,upBean);
	}
}
