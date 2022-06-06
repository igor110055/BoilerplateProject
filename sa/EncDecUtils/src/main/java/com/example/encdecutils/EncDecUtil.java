package com.example.encdecutils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import com.example.framework.utils.PjtUtil;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;

public class EncDecUtil extends SAProxy {

    public static String Salt() {
		
		String salt="";
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			random.nextBytes(bytes);
			salt = new String(Base64.getEncoder().encode(bytes));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return salt;
	}
    public static String SHA512(String password, String hash) {
		String salt = hash+password;
		String hex = null;
		try {
			MessageDigest msg = MessageDigest.getInstance("SHA-512");
			msg.update(salt.getBytes());
			
			hex = String.format("%128x", new BigInteger(1, msg.digest()));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hex;
	}

    public DataSet getSalt(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	DataSet OUT_DS = new DataSet();
        DataTable OUT_RESULT = OUT_DS.addTable("OUT_RESULT");
    	DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");
    	OUT_RESULT.addColumn("STATUS");  // E에러   S 성공
        OUT_RSET.addColumn("SALT");
    	    
        String SALT = EncDecUtil.Salt();
        DataRow drResult = OUT_RESULT.addRow();
        DataRow drRset = OUT_RSET.addRow();
		
        drResult.setString("STATUS", "S");
        drRset.setString("SALT", SALT);
 
        return OUT_DS;
    }

    public DataSet setEncrypt(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	DataSet OUT_DS = new DataSet();
    	DataTable OUT_RESULT = OUT_DS.addTable("OUT_RESULT");
    	DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");
    	OUT_RESULT.addColumn("STATUS"); 
        OUT_RESULT.addColumn("ERR_MSG"); 
        OUT_RESULT.addColumn("ERR_CODE"); 
        DataRow drResult = OUT_RESULT.addRow();

    	OUT_RSET.addColumn("PWD");
        OUT_RSET.addColumn("SALT");
    	OUT_RSET.addColumn("PWD_ENCRYPT");  

        try{

            DataTable IN_PSET =InDs.getTable("IN_PSET");
            String PWD ="";
            String SALT ="";
            if(IN_PSET.getRowCount()>0) {
                PWD = IN_PSET.getRow(0).getStringNullToEmpty("PWD");
                SALT = IN_PSET.getRow(0).getStringNullToEmpty("SALT");
            }
           
            DataRow drRset = OUT_RSET.addRow();
            if(PjtUtil.g().isEmpty(PWD)){
                throw new Exception("PWD가 인풋으로 넘어오지 않았습니다.");
            }
            String pw_encrypt = EncDecUtil.SHA512(PWD,SALT);
            
            
            drRset.setString("PWD", PWD);
            drRset.setString("PWD_ENCRYPT", pw_encrypt);
            drRset.setString("SALT", SALT);
        } catch(Exception e){
            drResult.setString("STATUS", "E");
            drResult.setString("ERR_MSG", e.getMessage());
            drResult.setString("ERR_CODE", "200");
        }
        drResult.setString("STATUS", "S");   
 
        return OUT_DS;
    }
}
