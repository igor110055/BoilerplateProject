package com.example.gmail;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.example.framework.utils.PjtUtil;
import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import running.common.SAProxy;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;

public class GmailSend extends SAProxy {

    public DataSet SendMail(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	DataSet OUT_DS = new DataSet();
    	DataTable OUT_RST = OUT_DS.addTable("OUT_RST");
    	OUT_RST.addColumn("STATUS");  // E에러   S 성공
    	OUT_RST.addColumn("ERR_MSG");
    	OUT_RST.addColumn("ERR_CODE");  //100  외부 api 오류  200 내부오류
    	OUT_RST.addColumn("ERR_STACK_TRACE");
    	
    
    	DataTable IN_PSET =InDs.getTable("IN_PSET");
    	String MAIL_ID ="";
    	String MAIL_PWD =""; 
        String FROM =""; 
        String TO =""; 
        String SUBJECT="";
        String BODY="";
        String HTML_YN="";
        if(IN_PSET.getRowCount()>0) {
    		MAIL_ID = IN_PSET.getRow(0).getStringNullToEmpty("MAIL_ID");
    		MAIL_PWD = IN_PSET.getRow(0).getStringNullToEmpty("MAIL_PWD");
            FROM = IN_PSET.getRow(0).getStringNullToEmpty("FROM");
            TO = IN_PSET.getRow(0).getStringNullToEmpty("TO");
            SUBJECT = IN_PSET.getRow(0).getStringNullToEmpty("SUBJECT");
            BODY = IN_PSET.getRow(0).getStringNullToEmpty("BODY");
            HTML_YN = IN_PSET.getRow(0).getStringNullToEmpty("HTML_YN");
    	}
       
        DataRow drRst = OUT_RST.addRow();
	    if(PjtUtil.g().isEmpty(MAIL_ID)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "MAIL_ID가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        if(PjtUtil.g().isEmpty(MAIL_PWD)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "MAIL_PWD가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        if(PjtUtil.g().isEmpty(FROM)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "FROM가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        if(PjtUtil.g().isEmpty(TO)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "TO가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        if(PjtUtil.g().isEmpty(SUBJECT)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "SUBJECT가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        
        if(PjtUtil.g().isEmpty(BODY)){
			drRst.setString("STATUS", "E");
			drRst.setString("ERR_MSG", "BODY가 인풋으로 넘어오지 않았습니다.");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_STACK_TRACE", "");
			return OUT_DS;
	    }

        try {
            MailSend(MAIL_ID,MAIL_PWD,FROM,TO,SUBJECT,BODY,HTML_YN);
			drRst.setString("STATUS", "S");
        } catch(AddressException ae) {            
            StringWriter sw = new StringWriter();
			ae.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
            //System.out.println("AddressException : " + ae.getMessage());        
            drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", ae.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
        } catch(MessagingException me) {            
            StringWriter sw = new StringWriter();
			me.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
            //System.out.println("MessagingException : " + me.getMessage());
            drRst.setString("STATUS", "E");
			drRst.setString("ERR_CODE", "200");
			drRst.setString("ERR_MSG", me.getMessage());
			drRst.setString("ERR_STACK_TRACE", exceptionAsString);
        }
        return OUT_DS;
    }

    
    private void MailSend(final String mailId,final String mailPwd,String from,String to,String subject,String body,String HTML_YN) throws MessagingException, UnsupportedEncodingException {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587");       
        prop.put("mail.smtp.port", "587");       
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");       
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");       
        
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() { 
                return new PasswordAuthentication(mailId, mailPwd); 
            } 
        });

            MimeMessage msg = new MimeMessage(session);    
            msg.setSentDate(new Date());
            //msg.setFrom(new InternetAddress(from, "VISITOR"));
            msg.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(to);         
            msg.setRecipient(Message.RecipientType.TO, toAddress);           
            msg.setSubject(subject, "UTF-8");            
            if(HTML_YN.equals("Y")) {
                msg.setText(body, "UTF-8","html");          
            } else {
                msg.setText(body, "UTF-8","plain");          
            }            
            Transport.send(msg);
     
    }
}
