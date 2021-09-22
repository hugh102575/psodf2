package com.example.psodf2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HomePageActivity extends AppCompatActivity {



    private String base_url;
    private String school_name;
    private Button eProfile;
    private String api_token;
    private TextView school_name_Result;
    //private Button logout_btn;
    private DialogInterface.OnClickListener dialogClickListener_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        api_token= my_info.getApi_token();
        base_url = my_info.getBase_url();
        school_name= my_info.getSchool_name();
        school_name_Result = findViewById(R.id.school_name);
        school_name_Result.setText(school_name);
        eProfile = findViewById(R.id.profile_button);
        //logout_btn = findViewById(R.id.logout_btn);
        setTitle("我的首頁");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        dialogClickListener_main=dialogClickListener;


        eProfile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        /*logout_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                builder.setMessage("確定要登出嗎?")
                        .setPositiveButton("是", dialogClickListener)
                        .setNegativeButton("否", dialogClickListener)
                        .show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.logoutmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout_button) {
            //Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
            //startActivity(intent);
            //finish();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
            builder.setMessage("確定要登出嗎?")
                    .setPositiveButton("是", dialogClickListener_main)
                    .setNegativeButton("否", dialogClickListener_main)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}