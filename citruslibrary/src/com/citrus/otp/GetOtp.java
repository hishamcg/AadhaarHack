package com.citrus.otp;

import java.util.Locale;


public class GetOtp {
	
	public static final String extractOTP(String sms, String length) {
		
        String[] nbs = sms.split("\\D+");
        
        if (nbs.length != 0) {
        	for (String number : nbs) {
        		if (number.length() == Integer.valueOf(length)) return number;
        	}
        }
        
        throw new IllegalArgumentException();    
    
	}
	
	public static final String getJS(String otp, String bankname) {
		
		if (bankname.toLowerCase(Locale.ENGLISH).contains("citi")) {
			String js = "javascript:document.optInForm.otp.value='"+otp+ "'; validateOTP(1);";
			return js;
		}
		
		if (bankname.toLowerCase(Locale.ENGLISH).contains("sbi")) {
			String js = "javascript:document.passwdForm.pin1.value='"+otp+ "'; OnSubmitHandler1();";
			return js;
		}
		
		if (bankname.toLowerCase(Locale.ENGLISH).contains("amex")) {
			String js = "javascript:document.mainForm.OTC.value='"+otp+ "'; validateOTC();";
			return js;
		}
		
		String js = "javascript: " +
				 "var inputs = document.querySelectorAll('input[type=password]');" +
				 "var forms = document.getElementsByTagName('form');" +
				 "inputs[inputs.length - 1].value='"+otp+"';" +
				 "forms[forms.length - 1].submit();";
		return js;
	}
	
}