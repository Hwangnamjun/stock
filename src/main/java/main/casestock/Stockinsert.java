package main.casestock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.Infobean;
import bean.Stockbean;
import connection.Connect;
import util.ConstantValue;
import util.Customexception;

public class Stockinsert {

	public void insertData(String parm, Infobean bean) {
		
        try {
				JSONParser parser = new JSONParser();
				JSONObject jsonParse = (JSONObject)parser.parse(parm);
				JSONObject jsonObj = (JSONObject)((JSONObject)((JSONObject)jsonParse.get("response")).get("body")).get("items");
				JSONArray jsonArray = (JSONArray)jsonObj.get("item");
				ArrayList<Stockbean> arr = new ArrayList<Stockbean>();
				long prefStock = prefStock(bean);
				for(int i = 0; i < jsonArray.size(); i++) {
					JSONObject result = (JSONObject)jsonArray.get(i);
					Stockbean sb = new Stockbean();
					
					sb.setBaseYmd((String)result.get("basDt"));
					sb.setStockCode((String)result.get("srtnCd"));
					sb.setCorpName((String)result.get("itmsNm"));
					sb.setClpr(Long.parseLong((String)result.get("clpr")));
					sb.setVs(Long.parseLong((String)result.get("vs")));
					sb.setFltRate((String)result.get("fltRt"));
					sb.setMkp(Long.parseLong((String)result.get("mkp")));
					sb.setHipr(Long.parseLong((String)result.get("hipr")));
					sb.setLopr(Long.parseLong((String)result.get("lopr")));
					sb.setTrqu(Long.parseLong((String)result.get("trqu")));
					sb.setTrprc(Long.parseLong((String)result.get("trPrc")));
					sb.setLstGstCnt(Long.parseLong((String)result.get("lstgStCnt"))+prefStock);
					sb.setMktTotAmt(Long.parseLong((String)result.get("mrktTotAmt")));
					
					arr.add(sb);
				}
				insertDb(arr);
			} catch (ParseException e) {
				if(parm.substring(0, 1).equals("{"))
					throw new Customexception("API 서버오류");
			}
	}
	private void insertDb(ArrayList<Stockbean> arr) {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "INSERT INTO DBA.UNIQUE_CORPSTOCK "
					   + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			for(int i = 0; i < arr.size(); i++) {
				pstmt.setString(1, arr.get(i).getBaseYmd());
				pstmt.setString(2, arr.get(i).getStockCode());
				pstmt.setString(3, arr.get(i).getCorpName());
				pstmt.setLong(4, arr.get(i).getClpr());
				pstmt.setLong(5, arr.get(i).getVs());
				pstmt.setString(6, arr.get(i).getFltRate());
				pstmt.setLong(7, arr.get(i).getMkp());
				pstmt.setLong(8, arr.get(i).getHipr());
				pstmt.setLong(9, arr.get(i).getLopr());
				pstmt.setLong(10, arr.get(i).getTrqu());
				pstmt.setLong(11, arr.get(i).getTrprc());
				pstmt.setLong(12, arr.get(i).getLstGstCnt());
				pstmt.setLong(13, arr.get(i).getMktTotAmt());
				
				pstmt.addBatch();
				pstmt.clearParameters();
			}
			
			pstmt.executeBatch();
			pstmt.clearBatch();
			conn.commit();
			
			pstmt.close();
			Connect.setClose();
			
			System.out.println("job sucsess");
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	private long prefStock(Infobean bean) {
		
		long result = 0;
		String addr;
	    URL url = null;
	    String readLine = null;
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    HttpURLConnection urlConnection = null;

		try {
			addr = "https://apis.data.go.kr/1160100/service/GetStocIssuInfoService/getStocIssuStat?"
					+ "serviceKey="+ConstantValue.GOV_SERVICE_KEY
					+ "&pageNo=1"
					+ "&numOfRows=100"
					+ "&resultType=json"
					+ "&basDt="+bean.getStartDT()
					+ "&crno="+bean.getCrno();
			
			url = new URL(addr);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json;");
			urlConnection.setRequestProperty("Accept", "*/*;q=0.9");
			
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
			    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));

			    while((readLine = bufferedReader.readLine()) != null) 
			    {
			        buffer.append(readLine).append("\n");
			    }
				JSONParser parser = new JSONParser();
				JSONObject jsonParse = (JSONObject)parser.parse(buffer.toString());
				JSONArray jsonArray = (JSONArray)((JSONObject)((JSONObject)((JSONObject)jsonParse.get("response")).get("body")).get("items")).get("item");
				result = Long.parseLong((String)((JSONObject)jsonArray.get(0)).get("pfstTisuCnt"));
			}
			else 
			{
				throw new Customexception("message : "+urlConnection.getResponseMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
		return result;
	}
}










