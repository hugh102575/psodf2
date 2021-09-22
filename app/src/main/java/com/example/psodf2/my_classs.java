package com.example.psodf2;

public class my_classs {
    public static String classs_name;
    public static int id;

    public my_classs(String classs_name, int id) {
        this.classs_name = classs_name;
        this.id = id;
    }

    public static String getClasss_name() {
        return classs_name;
    }

    public static int getId() {
        return id;
    }
}
