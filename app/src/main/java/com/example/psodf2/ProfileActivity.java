package com.example.psodf2;


import android.content.DialogInterface;
import android.content.Intent;
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

public class ProfileActivity extends AppCompatActivity {

    private String base_url;
    private String api_token;
    private Spinner dropdown;
    private TextView my_batch_id;
    private Button next_btn;
    private Integer[] batch_id_array;
    private  TextView no_batch;
    private TextView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("學生相片建檔");

        api_token=my_info.getApi_token();
        base_url = my_info.getBase_url();
        Spinner dropdown = findViewById(R.id.spinner);
        dropdown.setVisibility(View. GONE);
        my_batch_id=findViewById(R.id.hidden_batch_id);
        my_batch_id.setVisibility(View. GONE);
        next_btn = findViewById(R.id.go_class_btn);
        next_btn.setVisibility(View. GONE);
        no_batch = findViewById(R.id.no_batch);
        findViewById(R.id.textView2).setVisibility(View. GONE);
        location=findViewById(R.id.textView11);
        location.setText("學生相片建檔>選擇梯次");

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        retrofit2.Call<List<Batch>> call= jsonPlaceHolderApi.get_batch(api_token);
        call.enqueue(new Callback<List<Batch>>() {
            @Override
            public void onResponse(Call<List<Batch>> call, Response<List<Batch>> response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if(!response.isSuccessful()){
                    return;
                }
                List<Batch> batches = response.body();

                if(batches.size()!=0) {
                    String[] batch_array = new String[batches.size()];
                    Integer[] batch_id_array_tmp = new Integer[batches.size()];
                    int batch_index = 0;
                    for (Batch batch : batches) {
                        batch_array[batch_index] = batch.getBatch_Name();
                        batch_id_array_tmp[batch_index] = batch.getId();
                        batch_index++;
                    }
                    batch_id_array=batch_id_array_tmp;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,android.R.layout.simple_spinner_dropdown_item, batch_array);
                    dropdown.setAdapter(adapter);
                    dropdown. setVisibility(View. VISIBLE);
                }else{
                    no_batch.setText("查無資料");
                    my_batch_id.setText("null");
                }
                findViewById(R.id.textView2).setVisibility(View. VISIBLE);
                next_btn. setVisibility(View. VISIBLE);

            }

            @Override
            public void onFailure(Call<List<Batch>> call, Throwable t) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String batch_value=my_batch_id.getText().toString();

                if(batch_value=="null"){
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "好",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setMessage("您尚未新增梯次喔! 請至後台網站新增");
                    alertDialog.show();
                }else{
                    String batch_name = dropdown.getSelectedItem().toString();
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity2.class);
                    new my_batch(batch_name,Integer. parseInt(batch_value));
                    //intent.putExtra("batch_name",batch_name);
                    //intent.putExtra("batch_id",Integer. parseInt(batch_value));
                    startActivity(intent);
                }

            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int batch_id=batch_id_array[position];
                my_batch_id.setText(String.valueOf(batch_id));
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