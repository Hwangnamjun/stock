package main.casecorp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.Customexception;

public class Reprtparse {

	public String getDate(String reprtUrl) {

	    URL url = null;
	    String readLine = null;
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    HttpURLConnection urlConnection = null;

		try {
			url = new URL(reprtUrl);
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

			}
			else 
			{
				throw new Customexception("message : "+urlConnection.getResponseMessage());
			}
			
			urlConnection.disconnect();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buffer.toString();
	}

}
