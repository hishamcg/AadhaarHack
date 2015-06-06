package com.strata.aadhaar.model;

/**
 * Created by hisham on 6/6/15.
 */
public class ProfileDetail {
    private String phone_no,email,biz_name,tan_no,image,merchant_id;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiz_name() {
        return biz_name;
    }

    public void setBiz_name(String biz_name) {
        this.biz_name = biz_name;
    }

    public String getTan_no() {
        return tan_no;
    }

    public void setTan_no(String tan_no) {
        this.tan_no = tan_no;
    }
}
