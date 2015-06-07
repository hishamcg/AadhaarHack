/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.citrus.otp;



public enum BankOTP {

	ICICI("ICICI"), 
	KOTAK("KOTAK"), 
	CITI("CITI"), 
	AXIS("AXIS"), 
	HDFC("HDFC"),
	AMEX("AMEX"),
	SBI("SBI");   
	
	private BankOTP(String pattern) {
	
	}
	
	public static BankOTP bankName(String sendername) {
		for (BankOTP type : values()) {
			if (sendername.toLowerCase().contains(type.toString().toLowerCase())) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}
}
