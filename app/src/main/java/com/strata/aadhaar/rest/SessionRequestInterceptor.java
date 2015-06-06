package com.strata.aadhaar.rest;

import retrofit.RequestInterceptor;

class SessionRequestInterceptor implements RequestInterceptor
{
    private String authToken = "";
    private String latitude = null;
    private String longitude = null;
    public void setAuthToken(String auth){
        this.authToken = auth;
    }
    public void setLatLon(String lat,String lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    @Override
    public void intercept(RequestFacade request)
    {
        if(!this.authToken.isEmpty()){
            request.addHeader("auth-token", this.authToken);
        }
        if(latitude!=null){
            request.addHeader("x-latitude", this.latitude);
            request.addHeader("x-longitude", this.longitude);
        }

    }
}
