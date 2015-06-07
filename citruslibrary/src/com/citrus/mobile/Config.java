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
package com.citrus.mobile;

public class Config {
    private static String env, signinId, signinSecret, signupId, signupSecret, prepaid_cookie = "";

    public static void setEnv(String sip) {
        env = sip;
    }

    public static void setSigninId(String id) {
        signinId = id;
    }

    public static void setSigninSecret(String secret) {
        signinSecret = secret;
    }

    public static void setupSignupId(String id) {
        signupId = id;
    }

    public static void setupSignupSecret(String secret) {
        signupSecret = secret;
    }
    
    public static void setupPrepaidCookie(String cookie) {
    	prepaid_cookie = cookie;
    }
    
    public static String getEnv() {
        return env;
    }

    public static String getSigninId() {
        return signinId;
    }

    public static String getSigninSecret() {
        return signinSecret;
    }

    public static String getSignupId() {
        return signupId;
    }

    public static String getSignupSecret() {
        return signupSecret;
    }
    
    public static String getPrepaidCookie() {
    	return prepaid_cookie;
    }
}