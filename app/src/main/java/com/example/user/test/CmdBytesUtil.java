package com.example.user.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdBytesUtil {
	    
   private static byte toByte(char c) {  
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);  
	    return b;  
   }  	

   public static byte hexStringToByte(String hex) {  
	    char[] achar = hex.toUpperCase().toCharArray(); 
	    if(1==hex.length()){
	    	return toByte(achar[0]);
	    }else{
	    	return (byte) (toByte(achar[0]) << 4 | toByte(achar[1])); 	
	    }
   }  

   public static String[] cmdBytes=new String[]
	{
		"NUL","SOH","STX","ETX","EOT","ENQ","ACK","BEL",
	    "BS","HT","LF","VT","CLR","CR","SO","SI",
	    "DEL","DC1","DC2","DC3","DC4","NAK","SYN","ETB",
	    "CAN","EM","SUB","ESC","FS","GS","RS","US",
	    "SP"
	};
	
    public static byte[] getGbk(String paramString)
    {
		byte[] arrayOfByte = null;
		try 
		{
			arrayOfByte = paramString.getBytes("GBK");
		}
		catch (Exception   ex) {
			ex.printStackTrace();
		}
		return arrayOfByte;
    }
	
	public static byte[] transCommandBytes(String s){
		for(byte i=0;i<cmdBytes.length;i++){
			if(cmdBytes[i].equals(s)){
				return new byte[]{i};
			}
		}
		
		Pattern p1 = Pattern.compile("(\\d{1,3})([Dd]$)");  
		Pattern p2 = Pattern.compile("([0-9a-fA-F]{1,2})([Hh]$)");  
		Pattern p3=Pattern.compile("^0x([0-9a-fA-F]{1,2})");
		Matcher m;
		
		m=p1.matcher(s);
		if(m.matches()){
			int i=Integer.parseInt(m.group(1));
			if(i>255){
				return getGbk(s);
			}else{
				return new byte[]{(byte)i};				
			}
		}
		
		m=p2.matcher(s);
		if(m.matches()){
			return new byte[]{hexStringToByte(m.group(1))};
		}
		
		m=p3.matcher(s);
		if(m.matches()){
			return new byte[]{hexStringToByte(m.group(1))};
		}
		
		return getGbk(s);
	}
	
	public static byte[] transToPrintText(String s){
		
		byte[] printText=new byte[4096];
		int iNum = 0;
		byte[] oldText;
		String[] tmp=s.split(" ");
		
		for(int i=0;i<tmp.length ;i++){
			if(tmp[i].length()>0){
				oldText=transCommandBytes(tmp[i]);
				System.arraycopy(oldText, 0,  printText,  iNum,  oldText.length);
				iNum += oldText.length;
			}
		}
		
		oldText=new byte[iNum];
		System.arraycopy(printText,0,oldText,0,iNum);
		
		return oldText;
	}
}
