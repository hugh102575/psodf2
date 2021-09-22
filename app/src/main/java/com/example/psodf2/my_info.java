package com.example.psodf2;

public class my_info {
    public static String api_token;
    public static String base_url;
    public static String school_name;


    public my_info(String base_url, String api_token, String school_name) {
        this.base_url = base_url;
        this.api_token = api_token;
        this.school_name=school_name;
    }



    public static String getBase_url() {
        return base_url;
    }

    public static String getApi_token() {
        return api_token;
    }

    public static String getSchool_name() {
        return school_name;
    }
}
