package com.example.psodf2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity3 extends AppCompatActivity {

    private String base_url;
    private String api_token;
    private int classs_id;
    private String classs_name;
    private TextView selected_classs;
    private  TextView location;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);
        setTitle("學生相片建檔");

        api_token=my_info.getApi_token();
        base_url = my_info.getBase_url();
        classs_id=my_classs.getId();
        classs_name=my_classs.getClasss_name();

        selected_classs=findViewById(R.id.selected_classs);
        selected_classs.setText(classs_name);
        location=findViewById(R.id.textView10);
        String location_str="學生相片建檔>"+my_batch.getBatch_name()+">"+classs_name;
        location.setText(location_str);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File

                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(ProfileActivity3.this,
                                        "com.example.android.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }


                        /*try {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }*/
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };


        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        retrofit2.Call<List<Student>> call = jsonPlaceHolderApi.class_find_student(classs_id,api_token);
        call.enqueue(new Callback<List<Student>>() {

            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                findViewById(R.id.loadingPanel3).setVisibility(View.GONE);
                if(!response.isSuccessful()){
                    return;
                }
                List<Student> students = response.body();
                if(students.size()!=0) {
                    for (Student student : students) {

                        TableLayout tl = (TableLayout) findViewById(R.id.student_table);
                        TableRow tr = new TableRow(ProfileActivity3.this);

                        TextView tv1= new TextView(ProfileActivity3.this);
                        tv1.setText(student.getName());
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setTextSize(15);
                        tv1.setTextColor(Color.BLACK);
                        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1));




                        Button b = new Button(ProfileActivity3.this);
                        b.setText("拍照");
                        b.setTextSize(15);
                        b.setTextColor(Color.parseColor("#6804ec"));
                        b.setGravity(Gravity.CENTER);

                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_camera);
                        img.setBounds(0, 0, 60, 60);
                        b.setCompoundDrawables(img, null, null, null);

                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                1
                        );
                        params.setMargins(0, 0, 0, 0);
                        b.setLayoutParams(params);


                        tr.addView(tv1);
                        tr.addView(b);
                        tl.addView(tr);

                        b.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {

                                int student_id= student.getId();

                                /*AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity3.this).create();
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "好",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                alertDialog.setMessage(Integer.toString(student_id));
                                alertDialog.show();*/

                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity3.this);
                                String alert_title="您是"+student.getName()+"嗎?";
                                builder.setTitle("請確認身分");
                                builder.setMessage(alert_title)
                                        .setPositiveButton("是", dialogClickListener)
                                        .setNegativeButton("否", dialogClickListener)
                                        .show();

                                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                try {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                } catch (ActivityNotFoundException e) {
                                    // display error state to the user
                                }*/

                            }
                        });
                    }


                }else{
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity3.this).create();
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "好",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setMessage("班級尚未新增學生喔! 請至後台網站新增");
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                findViewById(R.id.loadingPanel3).setVisibility(View.GONE);
            }
        });



    }
    public  File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}