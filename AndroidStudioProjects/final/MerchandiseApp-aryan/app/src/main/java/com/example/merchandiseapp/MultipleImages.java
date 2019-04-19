package com.example.merchandiseapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MultipleImages extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE1 = 1;
    private Button SelectedButton;
    private RecyclerView UploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UplaodListAdapter uplaodListAdapter;
    private StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_images2);
        mStorage = FirebaseStorage.getInstance().getReference();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){

            if(data.getClipData() != null)
            {
                System.out.println("select more images");

                int totalItemSelected =data.getClipData().getItemCount();
                System.out.println(totalItemSelected);


                for(int i=0 ;i <totalItemSelected;i++)
                {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    System.out.println(fileNameList);

                    uplaodListAdapter.notifyDataSetChanged();

                    final int finalI=i;
                    StorageReference fileToUpload = mStorage.child("Merchandise").child(fileName);
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("image uplaoded");
                            fileDoneList.remove(finalI);
                            Toast.makeText(MultipleImages.this,"Done",Toast.LENGTH_SHORT).show();
                            fileDoneList.add(finalI,"Done");
                            uplaodListAdapter.notifyDataSetChanged();



                        }
                    });

                }
            }

            else if (data.getData() != null)
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Warning");
                alert.setMessage("Please Select at least two images...");
                System.out.println("yo baby");
                AlertDialog alertBox = alert.create();
                alertBox.show();
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
