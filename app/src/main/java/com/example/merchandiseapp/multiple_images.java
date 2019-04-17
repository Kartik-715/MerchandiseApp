package com.example.merchandiseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class multiple_images extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE1 = 1;
    private Button SelectedButton;
    private RecyclerView UploadList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_images);

        SelectedButton = (Button) findViewById(R.id.addImg);
        UploadList = (RecyclerView) findViewById( R.id.recyclerview);

        SelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent , "Select Picture"), RESULT_LOAD_IMAGE1 );

            }
        });

    }

    public void OnActivityResult(int requestCode , int resultCode , Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){

            if(data.getClipData() != null)
            {

            }

            else if (data.getData() != null)
            {}

            else
            {



            }

        }


    }



}
