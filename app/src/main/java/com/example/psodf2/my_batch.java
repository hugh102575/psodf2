package com.example.psodf2;

public class my_batch {
    public static String batch_name;
    public  static int id;

    public my_batch(String batch_name, int id) {
        this.batch_name = batch_name;
        this.id = id;
    }

    public static String getBatch_name() {
        return batch_name;
    }

    public static int getId() {
        return id;
    }
}
