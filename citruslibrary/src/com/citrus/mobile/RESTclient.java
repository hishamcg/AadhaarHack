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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class RESTclient {
    private JSONObject urls;

    private JSONObject params, headers;

    private String type, base_url;
    
    private HttpClient httpClient;

    private HttpResponse response;


     public RESTclient(String type, String base_url, JSONObject params, JSONObject headers) {

        this.params = params;
        this.headers = headers;
        this.type = type;
        urls = new JSONObject();

        try {
            urls.put("production", "https://admin.citruspay.com/");
            urls.put("oops", "https://oops.citruspay.com/");
            urls.put("sandbox", "https://sandboxadmin.citruspay.com/");
            urls.put("signup", "oauth/token");
            urls.put("bind", "service/v2/identity/bind");
            urls.put("signin", "oauth/token");
            urls.put("wallet", "service/v2/profile/me/payment");
            urls.put("struct", "service/moto/authorize/struct/payment");
            urls.put("prepaid", "prepaid/pg/_verify");
            urls.put("balance", "service/v2/mycard");
            urls.put("password", "service/v2/identity/me/password");
            urls.put("specialbalance", "service/v2/prepayment/balance");
            urls.put("resetpassword", "service/v2/identity/passwords/reset");
            urls.put("prepaidbill", "service/v2/prepayment/load");
            urls.put("paymentoptions", "service/v1/merchant/pgsetting");
            urls.put("umdetails", "service/um/profile/memberInfo");
        } catch (JSONException e) {
            return;
        }
        this.base_url = base_url;
     }


     public JSONObject makePostrequest() throws IOException {
    	 HttpParams redirectparams = new BasicHttpParams();
    	 redirectparams.setParameter("http.protocol.handle-redirects",false);
    	 
         httpClient = new DefaultHttpClient();
         HttpPost httpPost = null;
         try {
             httpPost = new HttpPost(urls.getString(base_url) + urls.getString(type));
             httpPost.setParams(redirectparams);
         } catch (JSONException e) {
             e.printStackTrace();
         }

         List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
         Iterator<String> iter = params.keys();
         while (iter.hasNext()) {
             String key = iter.next();
             try {
                 String value = params.getString(key);
                 postData.add(new BasicNameValuePair(key, value));
             } catch (JSONException e) {
                 Log.d("exception", e.toString());
             }
         }

         httpPost.setEntity(new UrlEncodedFormEntity(postData));

         Iterator<String> iterhead = headers.keys();
         while (iterhead.hasNext()) {
             String key = iterhead.next();
             try {
                 String value = headers.getString(key);
                 httpPost.addHeader(key, value);
             } catch (JSONException e) {
            	 return null;
             }
         }

         try {
             response = httpClient.execute(httpPost);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return parseResponse(response);
     }
    
     public JSONObject makePostrequest(JSONObject details) throws IOException {
    	 HttpParams redirectparams = new BasicHttpParams();
    	 redirectparams.setParameter("http.protocol.handle-redirects",false);
    	 
         httpClient = new DefaultHttpClient();
         HttpPost httpPost = null;
         try {
             httpPost = new HttpPost(urls.getString(base_url) + urls.getString(type));
             httpPost.setParams(redirectparams);
         } catch (JSONException e) {
             e.printStackTrace();
         }

         httpPost.setEntity(new StringEntity(details.toString()));
         
         Iterator<String> iterhead = headers.keys();
         while (iterhead.hasNext()) {
             String key = iterhead.next();
             try {
                 String value = headers.getString(key);
                 httpPost.addHeader(key, value);
             } catch (JSONException e) {
            	 return null;
             }
         }

         try {
             response = httpClient.execute(httpPost);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return parseResponse(response);
     } 
     
    public JSONObject makePutrequest() {
    	HttpClient client = new DefaultHttpClient();

        HttpPut put = null;
        try {
            put = new HttpPut(urls.getString(base_url) + urls.getString(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> iterhead = headers.keys();
        while (iterhead.hasNext()) {
            String key = iterhead.next();
            try {
                String value = headers.getString(key);
                put.addHeader(key, value);
            } catch (JSONException e) {
            	e.printStackTrace();
            }
        }
        
        List<NameValuePair> putdata = new ArrayList<NameValuePair>(2);
        Iterator<String> iter = params.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = params.getString(key);
                putdata.add(new BasicNameValuePair(key, value));
            } catch (JSONException e) {
                Log.d("exception", e.toString());
            }
        }
        
        try {
            put.setEntity(new UrlEncodedFormEntity(putdata));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            response = client.execute(put);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseResponse(response);
    }

    public JSONObject makePutrequest(JSONObject details) {
        HttpClient client = new DefaultHttpClient();

        HttpPut put = null;
        try {
            put = new HttpPut(urls.getString(base_url) + urls.getString(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> iterhead = headers.keys();
        while (iterhead.hasNext()) {
            String key = iterhead.next();
            try {
                String value = headers.getString(key);
                put.addHeader(key, value);
            } catch (JSONException e) {
                Log.d("exception", e.toString());
            }
        }

        try {
            put.setEntity(new StringEntity(details.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            response = client.execute(put);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseResponse(response);
    }

    public JSONObject makegetRequest() throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = null;
        try {
        		httpGet = new HttpGet(urls.getString(base_url) + urls.getString(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> iterhead = headers.keys();
        while (iterhead.hasNext()) {
            String key = iterhead.next();
            try {
                String value = headers.getString(key);
                httpGet.addHeader(key, value);
            } catch (JSONException e) {
            	return new JSONObject().put("error", "unable to find headers");
            }
        }

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseResponse(response);

    }

    public JSONObject postPayment(JSONObject payment) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(urls.getString(base_url) + urls.getString(type));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        httpPost.setHeader("Content-Type", "application/json");

        try {
            httpPost.setEntity(new StringEntity(payment.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return parseResponse(response);
    }

    private JSONObject parseResponse(HttpResponse response) {
        try {
        	
        	if (response == null) {
        		return formError(600, "Null response - is your internet connection functional?");
        	}
        	
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_OK:
                    return new JSONObject(EntityUtils.toString(response.getEntity()));
                case HttpStatus.SC_NO_CONTENT:
                    return null;
                case HttpStatus.SC_BAD_REQUEST:
                    return formError(400, "badrequest");
                case HttpStatus.SC_UNAUTHORIZED:
                    return formError(401, "unauthorized");
                case HttpStatus.SC_FORBIDDEN:
                    return formError(403, "access forbidden");
                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                    return formError(503, "unavailable");
                case HttpStatus.SC_GATEWAY_TIMEOUT:
                    return formError(504, "gatewaytimeout");
                case HttpStatus.SC_MOVED_TEMPORARILY:
                	return getCookies(response);

                default:
                    return formError(response.getStatusLine().getStatusCode(), "unknownerror");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return formError(600, "jsonexception");
        } catch (IOException e) {
            e.printStackTrace();
            return formError(600, "ioexception");
        }

    }
    
    private JSONObject getCookies(HttpResponse response) {
    	Header[] headers = response.getAllHeaders();
    	JSONObject cookies = new JSONObject();
    	for (int i = 0; i < headers.length; i++) {
    		Header h = headers[i];
    		try {
				cookies.put(h.getName(), h.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
    	} 
    	return cookies;
    }
    
    private JSONObject formError(int status, String message) {
        JSONObject error = new JSONObject();
        try {
            error.put("error", String.valueOf(status));
            error.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return error;
    }
}
