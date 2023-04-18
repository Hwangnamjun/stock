package main.casecorp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.Infobean;
import bean.Reprtbean;
import connection.Connect;

public class Reprtinsert {

	double exchangeVal = 1.0;
	
	public String insertData(String parm, Infobean infobean) {

		Reprtbean bean = new Reprtbean();
		
		String res = "";
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonParse = (JSONObject) parser.parse(parm);
			Map<String, Double> mapper = exchange();
			if (!jsonParse.get("status").equals("000")) {
				
				//updatedb(infobean);
				return "재무제표 API error - " + infobean.getTarget() + " : " + jsonParse.get("message");
				//throw new Customexception("재무제표 API error - " + infobean.getTarget() + " : " + jsonParse.get("message"));
			}
			
			JSONArray jsonArray = (JSONArray) jsonParse.get("list"); 

			bean.setStockCode(infobean.getStockCode());
			boolean incomechk = false, assetschk = false, debtchk = false, totcapchk = false, scechk = false, dupchk = false;
			for (int i = 0; i < jsonArray.size(); i++) {
				
				JSONObject result = (JSONObject) jsonArray.get(i);
				
				if(!result.get("currency").toString().toUpperCase().equals("KRW"))
					exchangeVal = mapper.get(result.get("currency").toString().toUpperCase());
					
				if (result.get("sj_div").equals("BS")) {
					if(result.get("account_id").equals("-표준계정코드 미사용-")) {
						switch ((String)result.get("account_nm").toString().replaceAll(" ", "")) {
						case "자산총계" : {
							if(!assetschk) {
								assets(result, bean);
								assetschk = true;
							}
							break;
						}
						
						case "부채총계" : {
							if(!debtchk) {
								debt(result, bean);
								debtchk = true;
							}
							break;
						}
						
						case "자본총계" : {
							if(!totcapchk) {
								totcap(result, bean);
								totcapchk = true;
							}
							break;
						}
						}
					} else {
						switch ((String) result.get("account_id")) {
						case "ifrs-full_Assets": {
							if(!assetschk) {
								assets(result, bean);
								assetschk = true;
							}
							break;
						}
						
						case "ifrs-full_Liabilities": {
							if(!debtchk) {
								debt(result, bean);
								debtchk = true;
							}
							break;
						}

						case "ifrs-full_Equity": {
							if(!totcapchk) {
								totcap(result, bean);
								totcapchk = true;
							}
							break;
						}
						}
					}
					
				} else if (result.get("sj_div").equals("CIS") || result.get("sj_div").equals("IS")) {
					
					if((result.get("account_nm")+"").equals("당기순이익") || result.get("account_id").equals("ifrs-full_ProfitLoss")) {
						JSONObject obj = (JSONObject)jsonArray.get(i+1);
						String nm = obj.get("account_nm")+"";
						if((nm.contains("지배") && nm.indexOf("비") == -1) || nm.equals("ifrs-full_ProfitLossAttributableToOwnersOfParent")) {
							incomechk = true;
							income(obj, bean);
							res = nm + "/Parent";
							scechk = true;
						}
					}
					if(!incomechk) {
						if (result.get("account_id").equals("ifrs-full_ProfitLossAttributableToOwnersOfParent") 
								&& result.get("account_nm").toString().contains("지배") 
								&& !(result.get("account_nm").toString().contains("비"))
								&& result.get("account_nm").toString().contains("순이익")) {
							income(result, bean);
							res = result.get("account_nm").toString() + "/Child_1";
							scechk = true;
							
						} else if(result.get("account_id").equals("ifrs-full_ProfitLoss") && !scechk) {
							income(result, bean);
							res = result.get("account_nm").toString() + "/Child_2";
						}
					}
					
				} else if(result.get("sj_div").equals("SCE") && !scechk) {
					if(result.get("account_id").equals("ifrs-full_ProfitLoss") || (result.get("account_nm")+"").contains("당기순이익")) {
						if((result.get("account_detail")+"").contains("지배") && (result.get("account_detail")+"").indexOf("비") == -1 && !dupchk) {
							income(result, bean);
							res = result.get("account_detail").toString() + "/SCE";
							dupchk = true;
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Insertdb(bean);

		return res;
	}
	
	public void Updateparm() {
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE UNIQUE_CORPBLANACE "
					   + "   SET THS_INCOME       = THS_INCOME / 1000000 "
					   + "     , FRM_INCOME       = FRM_INCOME / 1000000 "
					   + "     , BFE_FRM_INCOME   = BFE_FRM_INCOME / 1000000 "
					   + "     , THS_ASSETS       = THS_ASSETS / 1000000 "
					   + "     , FRM_ASSETS       = FRM_ASSETS / 1000000 "
					   + "     , BFE_FRM_ASSETS   = BFE_FRM_ASSETS / 1000000 "
					   + "     , THS_DEBT         = THS_DEBT / 1000000 "
					   + "     , FRM_DEBT         = FRM_DEBT / 1000000 "
					   + "     , BFE_FRM_DEBT     = BFE_FRM_DEBT / 1000000 "
					   + "     , THS_TOT_CAP      = THS_TOT_CAP / 1000000 "
					   + "     , FRM_TOT_CAP      = FRM_TOT_CAP / 1000000 "
					   + "     , BFE_FRM_TOT_CAP  = BFE_FRM_TOT_CAP / 1000000 "
					   + "WHERE STOCK_CODE = '032680'";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
			pstmt.clearParameters();
			
			sql = "UPDATE UNIQUE_CORPBLANACE "
				+ "   SET THS_INCOME = 5439414713 "
				+ "     , FRM_INCOME = 3321786667 "
				+ "     , BFE_FRM_INCOME = 1653483263 "
				+ " WHERE STOCK_CODE = '404990'";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
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
			pstmt.clearParameters();
			
			
			conn.commit();
			pstmt.close();
			Connect.setClose();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void Insertdb(Reprtbean rb) {

		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		String sql = " MERGE "
				   + "  INTO HWANG.UNIQUE_CORPBLANACE A "
				   + " USING DUAL "
				   + "    ON (A.STOCK_CODE = ?) "
				   + "  WHEN MATCHED THEN "
				   + "UPDATE SET A.THS_INCOME      = ? "
				   + "         , A.FRM_INCOME      = ? "
				   + "         , A.BFE_FRM_INCOME  = ? "
				   + "         , A.THS_ASSETS      = ? "
				   + "         , A.FRM_ASSETS      = ? "
				   + "         , A.BFE_FRM_ASSETS  = ? "
				   + "         , A.THS_DEBT        = ? "
				   + "         , A.FRM_DEBT        = ? "
				   + "         , A.BFE_FRM_DEBT    = ? "
				   + "         , A.THS_TOT_CAP     = ? "
				   + "         , A.FRM_TOT_CAP     = ? "
				   + "         , A.BFE_FRM_TOT_CAP = ? "
				   + "  WHEN NOT MATCHED THEN "
				   + "INSERT (A.THS_INCOME, A.FRM_INCOME, A.BFE_FRM_INCOME, "
				   + "        A.THS_ASSETS, A.FRM_ASSETS, A.BFE_FRM_ASSETS, "
				   + "        A.THS_DEBT, A.FRM_DEBT, A.BFE_FRM_DEBT, "
				   + "        A.THS_TOT_CAP, A.FRM_TOT_CAP, A.BFE_FRM_TOT_CAP) "
				   + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rb.getStockCode());
			pstmt.setLong(2, rb.getThsIncome());
			pstmt.setLong(3, rb.getFrmIncome());
			pstmt.setLong(4, rb.getBfeFrmIncome());
			pstmt.setLong(5, rb.getThsAssets());
			pstmt.setLong(6, rb.getFrmAssets());
			pstmt.setLong(7, rb.getBfeFrmAssets());
			pstmt.setLong(8, rb.getThsDebt());
			pstmt.setLong(9, rb.getFrmDebt());
			pstmt.setLong(10, rb.getBfeFrmDebt());
			pstmt.setLong(11, rb.getThsTotCap());
			pstmt.setLong(12, rb.getFrmTotCap());
			pstmt.setLong(13, rb.getBfeFrmTotCap());
			pstmt.setLong(14, rb.getThsIncome());
			pstmt.setLong(15, rb.getFrmIncome());
			pstmt.setLong(16, rb.getBfeFrmIncome());
			pstmt.setLong(17, rb.getThsAssets());
			pstmt.setLong(18, rb.getFrmAssets());
			pstmt.setLong(19, rb.getBfeFrmAssets());
			pstmt.setLong(20, rb.getThsDebt());
			pstmt.setLong(21, rb.getFrmDebt());
			pstmt.setLong(22, rb.getBfeFrmDebt());
			pstmt.setLong(23, rb.getThsTotCap());
			pstmt.setLong(24, rb.getFrmTotCap());
			pstmt.setLong(25, rb.getBfeFrmTotCap());
			pstmt.execute();
			conn.commit();

			pstmt.close();
			Connect.setClose();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void updatedb(Infobean ib) {

		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;

		try {
			String sql = "UPDATE HWANG.UNIQUE_CORPCODE SET JURIR_NO='', BIZR_NO=''" + "WHERE CORP_CODE=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, ib.getCorpCode());

			pstmt.execute();

			pstmt.close();
			Connect.setClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void income(JSONObject result, Reprtbean bean) {
		// 당기순이익(지배)
		if (result.containsKey("thstrm_amount") && result.get("thstrm_amount").toString() != "")
			bean.setThsIncome((long)(Long.valueOf(result.get("thstrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("frmtrm_amount") && result.get("frmtrm_amount").toString() != "")
			bean.setFrmIncome((long)(Long.valueOf(result.get("frmtrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("bfefrmtrm_amount") && result.get("bfefrmtrm_amount").toString() != "")
			bean.setBfeFrmIncome((long)(Long.valueOf(result.get("bfefrmtrm_amount").toString()).longValue() * exchangeVal));
	}
	private void assets(JSONObject result, Reprtbean bean) {
		// 자산총계
		if (result.containsKey("thstrm_amount") && result.get("thstrm_amount").toString() != "")
			bean.setThsAssets((long)(Long.valueOf(result.get("thstrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("frmtrm_amount") && result.get("frmtrm_amount").toString() != "")
			bean.setFrmAssets((long)(Long.valueOf(result.get("frmtrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("bfefrmtrm_amount") && result.get("bfefrmtrm_amount").toString() != "")
			bean.setBfeFrmAssets((long)(Long.valueOf(result.get("bfefrmtrm_amount").toString()).longValue() * exchangeVal));
	}
	private void debt(JSONObject result, Reprtbean bean) {
		// 부채총계
		if (result.containsKey("thstrm_amount") && result.get("thstrm_amount").toString() != "")
			bean.setThsDebt((long)(Long.valueOf(result.get("thstrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("frmtrm_amount") && result.get("frmtrm_amount").toString() != "")
			bean.setFrmDebt((long)(Long.valueOf(result.get("frmtrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("bfefrmtrm_amount") && result.get("bfefrmtrm_amount").toString() != "")
			bean.setBfeFrmDebt((long)(Long.valueOf(result.get("bfefrmtrm_amount").toString()).longValue() * exchangeVal));
	}
	private void totcap(JSONObject result, Reprtbean bean) {
		// 자본총계
		if (result.containsKey("thstrm_amount") && result.get("thstrm_amount").toString() != "")
			bean.setThsTotCap((long)(Long.valueOf(result.get("thstrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("frmtrm_amount") && result.get("frmtrm_amount").toString() != "")
			bean.setFrmTotCap((long)(Long.valueOf(result.get("frmtrm_amount").toString()).longValue() * exchangeVal));
		if (result.containsKey("bfefrmtrm_amount") && result.get("bfefrmtrm_amount").toString() != "")
			bean.setBfeFrmTotCap((long)(Long.valueOf(result.get("bfefrmtrm_amount").toString()).longValue() * exchangeVal));
	}
	private HashMap<String, Double> exchange() throws SQLException {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT CUR_UNIT, DEAL_BAS_R FROM HWANG.EXCHANGE_MONEY";
		
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery();
		HashMap<String, Double> mapper = new HashMap<String, Double>();
		while(rs.next()) {
			mapper.put(rs.getString(1), Double.parseDouble(rs.getString(2).replaceAll(",", "")));
		}
		
		return mapper;
	}
}
