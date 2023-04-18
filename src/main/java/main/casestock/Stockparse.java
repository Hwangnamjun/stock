package main.casestock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import util.Customexception;

public class Stockparse {

	public String getData(String stockUrl) throws Customexception {
		
	    URL url = null;
	    String readLine = null;
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    HttpURLConnection urlConnection = null;

		try {
			url = new URL(stockUrl);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json;");
			urlConnection.setRequestProperty("Accept", "*/*;q=0.9");
   
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer.toString();
	}

}
