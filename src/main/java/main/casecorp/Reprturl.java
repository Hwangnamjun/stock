package main.casecorp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.Infobean;
import connection.Connect;
import util.ConstantValue;
import util.Customexception;

public class Reprturl {

	public String chkUrl(Infobean bean) throws Customexception {
		

		String corpCode = bean.getCorpCode();
		String addr = null;				
		String serviceKey = ConstantValue.DART_SERVICE_KEY;
		
		String crnoAddr = "https://opendart.fss.or.kr/api/company.json?"
				+ "crtfc_key="+serviceKey
				+ "&corp_code="+corpCode;
		
		crnoChk(crnoAddr, corpCode, bean);

		addr = "https://opendart.fss.or.kr/api/fnlttSinglAcntAll.json?"
			 + "crtfc_key="+serviceKey
			 + "&corp_code="+corpCode
			 + "&bsns_year=2022"	
			 + "&reprt_code=11011"
			 + "&fs_div=CFS";

		return addr;
	}
	
	private void crnoChk(String addr, String corpCode, Infobean ibean) {
		
	    URL url = null;
	    String readLine = null;
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    HttpURLConnection urlConnection = null;
	    
	    try {
			url = new URL(addr);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept", "application/json;");
			
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
			    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
			    
			    while((readLine = bufferedReader.readLine()) != null) 
			    {
			        buffer.append(readLine).append("\n");
			    }
			    
				JSONParser parser = new JSONParser();
				JSONObject jsonParse = (JSONObject)parser.parse(buffer.toString());
				ibean.setCrno((String)jsonParse.get("jurir_no"));
				
				Connection conn = Connect.getInstance();
				PreparedStatement pstmt = null;
				String sql;

				  sql = "UPDATE DBA.UNIQUE_CORPCODE SET JURIR_NO=?, BIZR_NO=?, INDUTY_CODE=?"
					   + "WHERE CORP_CODE=?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, (String)jsonParse.get("jurir_no"));
				pstmt.setString(2, (String)jsonParse.get("bizr_no"));
				pstmt.setString(3, (String)jsonParse.get("induty_code"));
				pstmt.setString(4, corpCode);
				
				pstmt.execute();
				
				pstmt.close();
				Connect.setClose();
			}
			else 
			{
				throw new Customexception("message : "+urlConnection.getResponseMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
