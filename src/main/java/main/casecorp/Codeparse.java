package main.casecorp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.Customexception;

public class Codeparse {

	public String getData(String codeUrl) {
		
        String spec = codeUrl;
        String outputDir = System.getProperty("user.dir");
        InputStream is = null;
        FileOutputStream os = null;
	    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
	    
        try{
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = conn.getHeaderField("Content-Disposition");
                
                String target = "filename=";
                int index = disposition.indexOf(target);
                fileName = disposition.substring(index + target.length());

                is = conn.getInputStream();
                os = new FileOutputStream(new File(outputDir, fileName));

                final int BUFFER_SIZE = 4096;
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            } else {
            	throw new Customexception("download fail: "+responseCode);
            }
            conn.disconnect();
        } catch (Exception e){
            e.printStackTrace();
            try {
                if (is != null){
                    is.close();
                }
                if (os != null){
                    os.close();
                }
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
		return "Download sucsess : "+ formatter.format(new Date(System.currentTimeMillis()));
	}

}
