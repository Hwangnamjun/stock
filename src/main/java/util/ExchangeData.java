package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.Exchangebean;
import connection.Connect;

public class ExchangeData {

	public String exchangeWork() {
		
		String addr = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?"
				   + "authkey="+ConstantValue.EXCHANGE_SERVICE_KEY
				   + "&searchdate=20221230"
				   + "&data=AP01";
	    URL url = null;
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    HttpURLConnection urlConnection = null;
	    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
	    
		try {
			url = new URL(addr);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept", "application/json;");
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
				
		        buffer.append(bufferedReader.readLine());
		        
				JSONParser parser = new JSONParser();
				JSONArray jsonArr = (JSONArray)parser.parse(buffer.toString());
				
				String result = ((JSONObject)jsonArr.get(0)).get("result").toString();
				ArrayList<Exchangebean> arr = new ArrayList<Exchangebean>();
				
				if(!result.equals("1"))
					return "Exchange API error";
					
				for(int i = 0; i < jsonArr.size(); i++) {
					
					JSONObject jobj = (JSONObject)jsonArr.get(i);
					Exchangebean bean = new Exchangebean();
					
					bean.setCUR_UNIT(jobj.get("cur_unit").toString());
					bean.setCUR_NM(jobj.get("cur_nm").toString());
					bean.setTTS(jobj.get("tts").toString());
					bean.setTTB(jobj.get("tts").toString());
					bean.setDEAL_BAS_R(jobj.get("deal_bas_r").toString());
					bean.setBKPR(jobj.get("bkpr").toString());
					bean.setKFTC_DEAL_BAS_R(jobj.get("kftc_deal_bas_r").toString());
					bean.setKFTC_BKPR(jobj.get("kftc_bkpr").toString());
					
					arr.add(bean);
				}
				
				Connection conn = Connect.getInstance();
				PreparedStatement pstmt = null;
				String sql = " MERGE "
						+ "  INTO HWANG.EXCHANGE_MONEY A "
						+ " USING DUAL "
						+ "    ON (A.CUR_UNIT = ?) "
						+ "  WHEN MATCHED THEN "
						+ "UPDATE SET A.CUR_NM          = ? "
						+ "         , A.TTB             = ? "
						+ "         , A.TTS             = ? "
						+ "         , A.DEAL_BAS_R      = ? "
						+ "         , A.BKPR            = ? "
						+ "         , A.KFTC_DEAL_BAS_R = ? "
						+ "         , A.KFTC_BKPR       = ? "
						+ "  WHEN NOT MATCHED THEN "
						+ "INSERT (A.CUR_UNIT, A.CUR_NM, A.TTB, A.TTS, A.DEAL_BAS_R, A.BKPR, A.KFTC_DEAL_BAS_R, A.KFTC_BKPR) "
						+ "VALUES (?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i < arr.size(); i++) {
					
					pstmt.setString(1, arr.get(i).getCUR_UNIT());
					pstmt.setString(2, arr.get(i).getCUR_NM());
					pstmt.setString(3, arr.get(i).getTTB());
					pstmt.setString(4, arr.get(i).getTTS());
					pstmt.setString(5, arr.get(i).getDEAL_BAS_R());
					pstmt.setString(6, arr.get(i).getBKPR());
					pstmt.setString(7, arr.get(i).getKFTC_DEAL_BAS_R());
					pstmt.setString(8, arr.get(i).getKFTC_BKPR());
					pstmt.setString(9, arr.get(i).getCUR_UNIT());
					pstmt.setString(10, arr.get(i).getCUR_NM());
					pstmt.setString(11, arr.get(i).getTTB());
					pstmt.setString(12, arr.get(i).getTTS());
					pstmt.setString(13, arr.get(i).getDEAL_BAS_R());
					pstmt.setString(14, arr.get(i).getBKPR());
					pstmt.setString(15, arr.get(i).getKFTC_DEAL_BAS_R());
					pstmt.setString(16, arr.get(i).getKFTC_BKPR());
					
					pstmt.execute();
					pstmt.clearParameters();
				}
				
				conn.commit();
				
				Connect.setClose();
				pstmt.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "Exchange sucsess : " + formatter.format(new Date(System.currentTimeMillis()));
	}

}
