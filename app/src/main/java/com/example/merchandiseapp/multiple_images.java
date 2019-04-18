package com.example.merchandiseapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class multiple_images extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE1 = 1;
    private Button SelectedButton;
    private RecyclerView UploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UplaodListAdapter uplaodListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_images);

        SelectedButton = (Button) findViewById(R.id.addImg);
        UploadList = (RecyclerView) findViewById( R.id.recyclerview);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uplaodListAdapter = new UplaodListAdapter(fileNameList , fileDoneList );
        UploadList.setLayoutManager(new LinearLayoutManager(this));
//        UploadList.hasFixedSize("true");
        UploadList.setAdapter(uplaodListAdapter);

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
                System.out.println("select more images");

                int totalItemSelected =data.getClipData().getItemCount();

                for(int i=0 ;i <totalItemSelected;i++)
                {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    uplaodListAdapter.notifyDataSetChanged();
                }
            }

            else if (data.getData() != null)
            {
                System.out.println("yo baby");

            }



        }


    }

public String getFileName(Uri uri){

        String result = null;

        if(uri.getScheme().equals("content")){

            Cursor cursor= getContentResolver().query(uri,null,null,null,null);
            try{

                if (cursor!=null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }

            finally {
                cursor.close();
            }



        }

        if(result==null)
        {
            result=uri.getPath();
            int cut = result.lastIndexOf('/');

            if(cut != -1){

                result = result.substring(cut+1);

            }
        }

        return result;
}

}
