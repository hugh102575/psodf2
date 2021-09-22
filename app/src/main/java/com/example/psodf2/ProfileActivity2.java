package com.example.psodf2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity2 extends AppCompatActivity {

    private String base_url;
    private String api_token;
    private int batch_id;
    private String batch_name;
    private Spinner dropdown;
    private Integer[] classs_id_array;

    private TextView my_classs_id;
    private Button next_btn;
    private  TextView no_classs;
    private TextView selected_batch;
    private  TextView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        setTitle("學生相片建檔");

        api_token=my_info.getApi_token();
        base_url = my_info.getBase_url();
        batch_id=my_batch.getId();
        batch_name=my_batch.getBatch_name();
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            batch_id = extras.getInt("batch_id");
            batch_name = extras.getString("batch_name");
        }*/
        Spinner dropdown = findViewById(R.id.spinner3);
        dropdown.setVisibility(View. GONE);
        my_classs_id=findViewById(R.id.hidden_classs_id);
        my_classs_id.setVisibility(View. GONE);
        next_btn = findViewById(R.id.go_student_btn);
        next_btn.setVisibility(View. GONE);
        no_classs = findViewById(R.id.no_classs);
        findViewById(R.id.textView6).setVisibility(View. GONE);
        selected_batch=findViewById(R.id.selected_batch);
        selected_batch.setText("梯次: "+batch_name);
        //selected_batch.setTextColor(Color.parseColor("#c994e3"));
        location=findViewById(R.id.textView8);
        String location_str="學生相片建檔>"+batch_name+">"+"選擇班級";
        location.setText(location_str);

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        retrofit2.Call<List<Classs>> call = jsonPlaceHolderApi.batch_find_class(batch_id,api_token);
        call.enqueue(new Callback<List<Classs>>() {
            @Override
            public void onResponse(Call<List<Classs>> call, Response<List<Classs>> response) {
                findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                if(!response.isSuccessful()){
                    return;
                }
                List<Classs> classses = response.body();

                if(classses.size()!=0) {
                    String[] classs_array = new String[classses.size()];
                    Integer[] classs_id_array_tmp = new Integer[classses.size()];
                    int classs_index = 0;
                    for (Classs classs : classses) {
                        classs_array[classs_index] = classs.getClasss_Name();
                        classs_id_array_tmp[classs_index] = classs.getId();
                        classs_index++;
                    }
                    classs_id_array=classs_id_array_tmp;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity2.this,android.R.layout.simple_spinner_dropdown_item, classs_array);
                    dropdown.setAdapter(adapter);
                    dropdown. setVisibility(View. VISIBLE);
                }else{
                    no_classs.setText("查無資料");
                    my_classs_id.setText("null");
                }
                findViewById(R.id.textView6).setVisibility(View. VISIBLE);
                next_btn. setVisibility(View. VISIBLE);

            }

            @Override
            public void onFailure(Call<List<Classs>> call, Throwable t) {
                findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String classs_value=my_classs_id.getText().toString();

                if(classs_value=="null"){
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity2.this).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "好",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setMessage("您尚未新增班級喔! 請至後台網站新增");
                    alertDialog.show();
                }else{
                    String classs_name = dropdown.getSelectedItem().toString();
                    Intent intent = new Intent(ProfileActivity2.this, ProfileActivity3.class);
                    new my_classs(classs_name,Integer. parseInt(classs_value));
                    //intent.putExtra("classs_name",classs_name);
                    //intent.putExtra("classs_id",Integer. parseInt(classs_value));
                    startActivity(intent);
                }

            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int classs_id=classs_id_array[position];
                my_classs_id.setText(String.valueOf(classs_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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