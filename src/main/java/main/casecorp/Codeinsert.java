package main.casecorp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import connection.Connect;
import util.Customexception;

public class Codeinsert {
	

	public String insertData() {
		
		Connection conn = Connect.getInstance();
		PreparedStatement pstmt = null;
		
		File zipFile = new File(System.getProperty("user.dir")+"\\CORPCODE.zip");
		File targetDir = new File(System.getProperty("user.dir"));
		
		unzip(zipFile, targetDir);
		return getxml(conn,pstmt);
	}
	
	private static void unzip(File zipFile, File targetDir){
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;

        try {
			fis = new FileInputStream(zipFile);
			zis = new ZipInputStream(fis);

			while ((zentry = zis.getNextEntry()) != null) {
			    String fileNameToUnzip = zentry.getName();

			    File targetFile = new File(targetDir, fileNameToUnzip);

		    	FileUtils.forceMkdir(targetFile.getParentFile());
		        unzipEntry(zis, targetFile);
			}
			if (zis != null)
				zis.close(); 
			
			if (fis != null)
				fis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }

    private static String getxml(Connection conn, PreparedStatement pstmt) {
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    
	    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
	    ResultSet rs = null;
	    int count = 0;
	    StringJoiner joiner = new StringJoiner(",","(",")");
    	try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(System.getProperty("user.dir")+"\\CORPCODE.xml");
			
			Element root = document.getDocumentElement();
			NodeList childList = root.getChildNodes();
			
			String sql = "INSERT INTO HWANG.UNIQUE_CORPCODE_T(CORP_CODE, CORP_NAME, STOCK_CODE, MODIFY_YMD)"
					   + "VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql); 
			
			for(int i = 0; i < childList.getLength(); i++) {
				
				Node item = childList.item(i);

				if(item.getNodeType() == Node.ELEMENT_NODE) {
					
					if(item.getChildNodes().item(5).getTextContent().trim().equals("") ? false : true) {
						pstmt.setString(1, item.getChildNodes().item(1).getTextContent());
						pstmt.setString(2, item.getChildNodes().item(3).getTextContent().toUpperCase());
						pstmt.setString(3, item.getChildNodes().item(5).getTextContent());
						pstmt.setString(4, item.getChildNodes().item(7).getTextContent());
						
						pstmt.addBatch();
			            pstmt.clearParameters();
						}
					}
				if((i % 1000) == 0) {
					  pstmt.executeBatch();
					  pstmt.clearBatch();
					  conn.commit() ;
				}
			}
		    pstmt.executeBatch();
		    
		    pstmt.clearBatch();
		    pstmt.clearParameters();
		    
		    sql = "SELECT CORP_NAME FROM ( "
		    		+ "SELECT CORP_CODE "
		    		+ "     , STOCK_CODE "
		    		+ "     , CORP_NAME "
		    		+ "  FROM UNIQUE_CORPCODE_T "
		    		+ " MINUS "
		    		+ "SELECT CORP_CODE "
		    		+ "     , STOCK_CODE "
		    		+ "     , CORP_NAME "
		    		+ "  FROM UNIQUE_CORPCODE )";
		    
		    pstmt = conn.prepareStatement(sql);
		    
		    rs = pstmt.executeQuery();

		    while(rs.next()) {
		    	joiner.add(rs.getString(1));
		    	count++;
		    }
		    
		    if(count > 0) {
			    pstmt.clearBatch();
			    pstmt.clearParameters();
			    
		    	sql =     " MERGE "
			    		+ "  INTO HWANG.UNIQUE_CORPCODE A "
			    		+ " USING ( "
			    		+ "SELECT * FROM UNIQUE_CORPCODE_T WHERE CORP_CODE IN ( "
			    		+ "SELECT CORP_CODE FROM ( "
			    		+ "SELECT CORP_CODE "
			    		+ "     , STOCK_CODE "
			    		+ "     , CORP_NAME "
			    		+ "  FROM UNIQUE_CORPCODE_T "
			    		+ " MINUS "
			    		+ "SELECT CORP_CODE "
			    		+ "     , STOCK_CODE "
			    		+ "     , CORP_NAME "
			    		+ "  FROM UNIQUE_CORPCODE))) B "
			    		+ "    ON (A.CORP_CODE = B.CORP_CODE) "
			    		+ "  WHEN MATCHED THEN "
			    		+ "UPDATE SET A.STOCK_CODE = B.STOCK_CODE "
			    		+ "         , A.CORP_NAME  = B.CORP_NAME "
			    		+ "         , A.MODIFY_YMD = B.MODIFY_YMD "
			    		+ "  WHEN NOT MATCHED THEN "
			    		+ "INSERT (A.CORP_CODE, A.STOCK_CODE, A.CORP_NAME, A.MODIFY_YMD) "
			    		+ "VALUES (B.CORP_CODE, B.STOCK_CODE, B.CORP_NAME, B.MODIFY_YMD) ";
			    
			    pstmt = conn.prepareStatement(sql);
			    pstmt.execute();
		    }
		    
		    pstmt.clearBatch();
		    pstmt.clearParameters();
		    
		    sql = "TRUNCATE TABLE HWANG.UNIQUE_CORPCODE_T";
		    
		    pstmt = conn.prepareStatement(sql);
		    pstmt.execute();
		    
		    pstmt.clearParameters();
		    
		    sql = "UPDATE UNIQUE_CORPCODE A "
	    		+ "   SET A.INDUTY_NAME = (SELECT B.INDUTY_NAME FROM HWANG.INDUTY_CODE_LIB B WHERE B.INDUTY_CODE = SUBSTR(A.INDUTY_CODE,0,2)) "
	    		+ "     , A.INDUTY_CODE_SIMPLE = (SELECT C.INDUTY_CODE_SIMPLE FROM HWANG.INDUTY_CODE_LIB C WHERE C.INDUTY_CODE = SUBSTR(A.INDUTY_CODE,0,2))"
	    		+ " WHERE A.BIZR_NO IS NOT NULL";
		    
		    pstmt = conn.prepareStatement(sql);
		    pstmt.execute();
		    
		    conn.commit();
		    
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new Customexception("insert fail : "+e);
		} finally {
			try {
				pstmt.close();
				Connect.setClose();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    return "Insert sucsess : "+ formatter.format(new Date(System.currentTimeMillis())) + (count == 0 ? "" : " -> " + count + joiner.toString());
    }
}
