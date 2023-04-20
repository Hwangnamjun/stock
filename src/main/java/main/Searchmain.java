package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Infobean;
import bean.Resultbean;
import connection.Connect;
import main.casecorp.Codeparse;
import main.casecorp.Codeinsert;
import main.casecorp.Codeurl;
import main.casecorp.Reprtinsert;
import main.casecorp.Reprtparse;
import main.casecorp.Reprturl;
import main.casestock.Stockparse;
import main.casestock.Stockinsert;
import main.casestock.Stockparm;
import main.casestock.Stockurl;
import main.checkparameter.Checkymd;
import util.Customexception;
import util.ExchangeData;
import util.Searchchk;
import util.dupDateDel;

public class Searchmain {

	public void testall(Infobean parm) {
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String startDt = parm.getStartDT();
		String endDt = parm.getEndDT();
		int division = Integer.parseInt(parm.getTarget());
		
		ArrayList<Infobean> arr = new ArrayList<Infobean>();
		String sql = "SELECT A.CORP_CODE, A.STOCK_CODE, A.CORP_NAME, A.BIZR_NO, A.INDUTY_CODE, A.JURIR_NO "
				   + "  FROM HWANG.UNIQUE_CORPCODE A, HWANG.UNIQUE_CORPBLANACE B "
				   + " WHERE A.STOCK_CODE = B.STOCK_CODE"
				   + "   AND A.JURIR_NO IS NOT NULL";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Infobean bean = new Infobean();
				bean.setCorpCode(rs.getString(1));
				bean.setStockCode(rs.getString(2));
				bean.setTarget(rs.getString(3));
				bean.setPageRow("1000");
				bean.setPageNo("1");
				bean.setStartDT(startDt);
				bean.setEndDT(endDt);
				bean.setCrno(rs.getString(6));
				arr.add(bean);
			}

			rs.close();
			pstmt.close();
			Connect.setClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int cnt = arr.size();
		
		// 0 : 제무재표, 1 : 주식시세
		if(division == 0) {
			for (int i = 0; i < arr.size(); i++) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.print((i + 1) + "/" + cnt + " (" + arr.get(i).getTarget()+ ") : ");
				String reprtUrl = new Reprturl().chkUrl(arr.get(i));
				String reprtResult = new Reprtparse().getDate(reprtUrl);
				System.out.println(new Reprtinsert().insertData(reprtResult, arr.get(i)));
			}
		} else if(division == 1) {
			
			for (int i = 0; i < arr.size(); i++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.print((i + 1) + "/" + cnt + " (" + arr.get(i).getTarget()+ ") : ");
				String stockUrl = new Stockurl().chkUrl(arr.get(i)); 
				String stockResult = new Stockparse().getData(stockUrl); 
				new	Stockinsert().insertData(stockResult,arr.get(i));
			}
		}
		System.out.println(new Stockparm().setData());
	}

	public void Updatecode() {
		try {
			// 0.기업 재무제표 조회용 별도코드 저장(bean 데이터와 별도인 기초데이터 입력)
			String codeUrl = new Codeurl().chkUrl();
			System.out.println(new Codeparse().getData(codeUrl));
			System.out.println(new Codeinsert().insertData());
			try {
				Files.delete(Paths.get(System.getProperty("user.dir") + "\\CORPCODE.zip"));
				Files.delete(Paths.get(System.getProperty("user.dir") + "\\CORPCODE.xml"));
			} catch (IOException e) {
				System.out.println(e);
			}
			// 0.현재 환율 조회후 저장
			// System.out.println(new ExchangeData().exchangeWork());

		} catch (Customexception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Resultbean> Startwork(Infobean bean)
			throws Customexception {

		ArrayList<Resultbean> result = null;
		bean.setPageRow("1000");
		bean.setPageNo("1");
		// 1. 입력받은 기업명 검증 및 기존 검색 기록 확인
		boolean searchChk = new Searchchk().findDb(bean);
		if (searchChk) {
			// 1) 기존 검색 기록이 존재 하지 않음
			// 1-1) 기업 재무제표 입력
			String reprtUrl = new Reprturl().chkUrl(bean);
			String reprtResult = new Reprtparse().getDate(reprtUrl);
			new Reprtinsert().insertData(reprtResult, bean);

			// 1-2) 기업 주가 입력
			String stockUrl = new Stockurl().chkUrl(bean);
			String stockResult = new Stockparse().getData(stockUrl);
			new Stockinsert().insertData(stockResult, bean);

		} else {
			// 2) 기존 검색 기록이 존재 함
			new Checkymd().findDb(bean);
		}
		// 2. 중복검색 데이터 정리
		//new dupDateDel().delDb();
		// 3. 출력
		result = new printResult().print(bean);
		// 4. 검색시 사용한 문자열 재정의(대소문자,코드 검색 -> 정식명칭 변경)
		if (bean.getTarget().equals(bean.getStockCode()))
			bean.setTarget(result.get(0).getCorpName());

		return result;
	}
}
