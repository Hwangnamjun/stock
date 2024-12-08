package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Infobean;
import bean.Resultbean;
import connection.Connect;

public class printResult {

	public ArrayList<Resultbean> print(Infobean bean) {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<Resultbean> arr = new ArrayList<>();
		String sql =  "SELECT /*+ APPEND PARALLEL(8) */ B.BASE_YMD"
					+ "     , C.CORP_NAME"
					+ "     , B.STOCK_CODE"
					+ "     , B.CLPR"
					+ "     , B.VS"
					+ "     , B.FLT_RATE"
					+ "     , B.MKP"
					+ "     , B.HIPR"
					+ "     , B.LOPR"
					+ "     , B.TRQU"
					+ "     , B.TRPRC"
					+ "     , A.THS_INCOME"
					+ "     , A.FRM_INCOME"
					+ "     , A.BFE_FRM_INCOME"
					+ "     , A.THS_ASSETS"
					+ "     , A.FRM_ASSETS"
					+ "     , A.BFE_FRM_ASSETS"
					+ "     , A.THS_DEBT"
					+ "     , A.FRM_DEBT"
					+ "     , A.BFE_FRM_DEBT"
					+ "     , A.THS_TOT_CAP"
					+ "     , A.FRM_TOT_CAP"
					+ "     , A.BFE_FRM_TOT_CAP"
					+ "     , B.LST_GST_CNT"
					+ "     , B.MKT_TOT_AMT"
					+ "     , ROUND(A.THS_INCOME / B.LST_GST_CNT,2)                 AS EPS "
					+ "     , ROUND(NVL(B.CLPR / DECODE((A.THS_INCOME / B.LST_GST_CNT),0,NULL,(A.THS_INCOME / B.LST_GST_CNT)),0),2)    AS PER "
					+ "     , ROUND(A.THS_TOT_CAP / B.LST_GST_CNT,2)                AS BPS "
					+ "     , ROUND(NVL(B.CLPR / DECODE((A.THS_TOT_CAP / B.LST_GST_CNT),0,NULL,(A.THS_TOT_CAP / B.LST_GST_CNT)),0),2)     AS PBR "
					+ "  FROM DBA.UNIQUE_CORPBLANACE  A"
					+ "     , DBA.UNIQUE_CORPSTOCK    B"
					+ "		, DBA.UNIQUE_CORPCODE 	C"
					+ " WHERE A.STOCK_CODE = B.STOCK_CODE"
					+ "	  AND A.STOCK_CODE = C.STOCK_CODE"
					+ "   AND A.STOCK_CODE = ?"
					+ "	  AND B.BASE_YMD BETWEEN ? AND ?"
					+ "	  AND C.JURIR_NO IS NOT NULL"
					+ "	ORDER BY 1, 2 DESC";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getStockCode());
			pstmt.setString(2, bean.getStartDT());
			pstmt.setString(3, bean.getEndDT());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Resultbean rb = new Resultbean();
				
				rb.setBaseYmd(rs.getString(1));
				rb.setCorpName(rs.getString(2));
				rb.setStockCode(rs.getString(3));
				rb.setClpr(rs.getLong(4));
				rb.setVs(rs.getLong(5));
				rb.setFltRate(String.format("%.2f%%", Float.valueOf(rs.getString(6))));
				rb.setMkp(rs.getLong(7));
				rb.setHipr(rs.getLong(8));
				rb.setLopr(rs.getLong(9));
				rb.setTrqu(rs.getLong(10));
				rb.setTrprc(rs.getLong(11));
				rb.setThsIncome(rs.getLong(12));
				rb.setFrmIncome(rs.getLong(13));
				rb.setBfefrmIncome(rs.getLong(14));
				rb.setThsAssets(rs.getLong(15));
				rb.setFrmAssets(rs.getLong(16));
				rb.setBfefrmAssets(rs.getLong(17));
				rb.setThsDebt(rs.getLong(18));
				rb.setFrmDebt(rs.getLong(19));
				rb.setBfefrmDebt(rs.getLong(20));
				rb.setThsTotCap(rs.getLong(21));
				rb.setFrmTotCap(rs.getLong(22));
				rb.setBfefrmTotCap(rs.getLong(23));
				rb.setLstGstCnt(rs.getLong(24));
				rb.setMktTotAmt(rs.getLong(25));
				rb.setEps(rs.getLong(26));
				rb.setPer(rs.getLong(27));
				rb.setBps(rs.getLong(28));
				rb.setPbr(rs.getLong(29));
				
				arr.add(rb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return arr;
	}

}
