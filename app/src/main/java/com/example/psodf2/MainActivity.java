package com.example.psodf2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private String base_url="https://49ce-61-220-205-150.ngrok.io";
    //public String base_url="http://yes-shop.yesinfo.com.tw/";
    private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;
    private TextView mTextViewResult;
    private TextView app_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eEmail = findViewById(R.id.login_email);
        ePassword = findViewById(R.id.login_password);
        eLogin = findViewById(R.id.login_submit);
        mTextViewResult = findViewById(R.id.text_view_result);
        app_title= findViewById(R.id.app_title);
        //app_title.setText("安親班點名系統");
        setTitle("安親班點名系統");

        eEmail.setText("danny102575@gmail.com");
        ePassword.setText("Neko!123");

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        retrofit2.Call<TestApi> call = jsonPlaceHolderApi.api_test();
        call.enqueue(new Callback<TestApi>() {
            @Override
            public void onResponse(Call<TestApi> call, Response<TestApi> response) {
                if(!response.isSuccessful()){
                    mTextViewResult.setText("伺服器連線錯誤 "+"Code: "+response.code());
                    mTextViewResult.setTextColor(Color.RED);
                    return;
                }
                TestApi test=response.body();
                if(test.getApi_test().equals("ok")){
                    mTextViewResult.setText("伺服器連線正常");
                    mTextViewResult.setTextColor(Color.parseColor("#4CAF50"));
                }
            }

            @Override
            public void onFailure(Call<TestApi> call, Throwable t) {
                mTextViewResult.setText("伺服器連線錯誤 "+t.getMessage());
                mTextViewResult.setTextColor(Color.RED);
            }
        });





        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = eEmail.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if(inputEmail.isEmpty() || inputPassword.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "好",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setTitle("");
                    alertDialog.setMessage("請輸入帳號密碼");
                    alertDialog.show();
                }else{

                    Login login =new Login(inputEmail,inputPassword);
                    retrofit2.Call<Login> call = jsonPlaceHolderApi.app_login(login);
                    call.enqueue(new retrofit2.Callback<Login>() {
                        @Override
                        public void onResponse(retrofit2.Call<Login> call, retrofit2.Response<Login> response) {
                            if(!response.isSuccessful()){
                                return;
                            }
                            Login status = response.body();
                            if(status.isSuccess()){
                                String api_token = status.getApi_token();
                                String school_name=status.getSchool_name();

                                new my_info(base_url,api_token,school_name);
                                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }else{
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "好",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setTitle("登入失敗");
                                alertDialog.setMessage("帳號或密碼錯誤");
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<Login> call, Throwable t) {

                        }
                    });



                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }





}