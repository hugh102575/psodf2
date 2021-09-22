package com.example.psodf2;

public class Login {
    private  String email;
    private  String password;
    private  boolean success;
    private  String api_token;
    private String school_name;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getApi_token() {
        return api_token;
    }

    public String getSchool_name() {
        return school_name;
    }
}
