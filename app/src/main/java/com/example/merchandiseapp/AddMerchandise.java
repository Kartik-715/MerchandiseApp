package com.example.merchandiseapp;


import android.content.Intent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AddMerchandise extends AppCompatActivity {

    private ArrayList<String> AccessGroups = new ArrayList<String>();
    private ArrayList<String> size = new ArrayList<String>();
    private ArrayList<String> qty = new ArrayList<String>();
    private ArrayList<String> listSQ = new ArrayList<String>();

    private Button add_accessgroup;
    private Button addSQ;
    private Button addMerchandise;

    private EditText access_editText;
    private EditText size_edt;
    private EditText qty_edt;
    private EditText gpName;
    private EditText mat;
    private EditText price_edt;
    private EditText prod_Id;



    private ListView accessGroupListView;
    private ListView sizeQtyListView;
    private static final int RESULT_LOAD_IMAGE1 = 1;
    private Button SelectedButton;
    private RecyclerView UploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UplaodListAdapter uplaodListAdapter;
    private StorageReference mStorage;


    private String GroupName ;
    private String Category ;
    private ArrayList<String> Image;
    private String Material;
    private String PID;
    private String Price;
    private String OrderType="1";

    private RadioButton rbYes;

    ArrayAdapter adapter;
    ArrayAdapter adapterSize_qty;

    DatabaseReference myRef ;
    DatabaseReference myRef2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchandise);

        Image = new ArrayList<String>();
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Footwear"
                , "T-Shirt", "Lowers" , "Hoodie","Others"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapterSpinner);

        add_accessgroup = (Button) findViewById(R.id.button_add_accessgroup);
        addSQ = (Button) findViewById(R.id.button_size_qty);
        addMerchandise = (Button) findViewById(R.id.button_submit);
        addMerchandise.setEnabled(false);

        rbYes = (RadioButton) findViewById(R.id.radioButtonYes);
        rbYes.toggle();

        access_editText = (EditText) findViewById(R.id.editText_accessgroup);
        size_edt = (EditText) findViewById(R.id.editText_size);
        qty_edt = (EditText) findViewById(R.id.editText_qty);

        mat = (EditText) findViewById(R.id.EditText_Material);
        price_edt = (EditText) findViewById(R.id.EditText_Price);
        prod_Id = (EditText) findViewById(R.id.EditText_prodID);

        accessGroupListView = (ListView) findViewById(R.id.listView_accessgroup);
        sizeQtyListView =(ListView) findViewById(R.id.listView_size_qty);

        GroupName = Prevalent_Groups.currentGroupName ;






        AccessGroups.add(GroupName);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AccessGroups);
        adapterSize_qty  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listSQ);

        add_accessgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(access_editText.getText().toString().equals(""))
                {
                }
                else
                {
                    AccessGroups.add(access_editText.getText().toString());
                    adapter.notifyDataSetChanged();
                    access_editText.setText("");

                }

            }
        });
        accessGroupListView.setAdapter(adapter);

        addSQ.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if (size_edt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the size", Toast.LENGTH_LONG);
                    return;
                }
                else if (qty_edt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the quantity", Toast.LENGTH_LONG);
                    return;
                }
                else {
                    listSQ.add("Size=" + size_edt.getText().toString() + ":Qty=" + qty_edt.getText().toString());
                    size.add(size_edt.getText().toString());
                    qty.add(qty_edt.getText().toString());
                    adapterSize_qty.notifyDataSetChanged();
                    size_edt.setText("");
                    qty_edt.setText("");
                }

                }
        });
        sizeQtyListView.setAdapter(adapterSize_qty);


        addMerchandise.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                Spinner dropdown = findViewById(R.id.spinner1);
                Category = (String) dropdown.getSelectedItem();
                Material = mat.getText().toString();

                PID = prod_Id.getText().toString();
                Long pr =  Long.parseLong(price_edt.getText().toString());
                Price = pr.toString();


                System.out.println(GroupName+Category+Image+Material+PID+Price);

                if(listSQ.size()==0 && OrderType.equals("1"))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Quantity and Size",Toast.LENGTH_LONG).show();
                }
                else if(PID.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Product ID",Toast.LENGTH_LONG).show();
                }
                else if(Price.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter a Valid Price",Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intent = new Intent(AddMerchandise.this,GroupActivity.class);
                    startActivity(intent);

                    myRef = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupName).child("Merchandise");
                    myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise");
                    final Query queries = myRef2.child(Category).orderByKey().equalTo(PID);

                    queries.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {

                                   Toast.makeText(getApplicationContext(),"Enter Valid Product ID. This Id Already Exist",Toast.LENGTH_LONG).show();

                            } else {
                                    //Toast.makeText(CartActivity.this,"no data exists",Toast.LENGTH_SHORT).show();

                                System.out.println("I am 2nd" + Image);
                                Merchandise merchandise = new Merchandise(GroupName, Category, Image, Material, PID, Price, qty, size, AccessGroups, OrderType, "true");
                                HashMap<String, Object> merchandiseValues = merchandise.toMap();

                                HashMap<String, Object> childUpdates = new HashMap<>();

                                childUpdates.put(Category + "/" + PID, merchandiseValues);


                                myRef.updateChildren(childUpdates);
                                myRef2.updateChildren(childUpdates);

                                access_editText.setText("");
                                size_edt.setText("");
                                qty_edt.setText("");
                                mat.setText("");
                                price_edt.setText("");
                                prod_Id.setText("");
                                Toast.makeText(getApplicationContext(),"Merchandise Added Successfully",Toast.LENGTH_LONG).show();


                                }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });




                }



            }
        });


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
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonYes:
                if (checked)
                    OrderType = "1";
                sizeQtyListView.setEnabled(true);
                addSQ.setEnabled(true);
                size_edt.setEnabled(true);
                qty_edt.setEnabled(true);
                break;
            case R.id.radioButtonNO:
                if (checked)
                    OrderType = "2";
                    sizeQtyListView.setEnabled(false);
                    addSQ.setEnabled(false);
                    size_edt.setEnabled(false);
                    qty_edt.setEnabled(false);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){

            if(data.getClipData() != null)
            {
                System.out.println("select more images");

                final int totalItemSelected =data.getClipData().getItemCount();
                System.out.println(totalItemSelected);


                for(int i=0 ;i <totalItemSelected;i++)
                {
                    final Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    final String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    System.out.println(fileNameList);

                    uplaodListAdapter.notifyDataSetChanged();

                    final int finalI=i;
                    StorageReference fileToUpload = mStorage.child("Merchandise").child(fileName);
                    System.out.println(fileUri+"!!!!!\n");



                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("image uplaoded");
                            fileDoneList.remove(finalI);
                            Toast.makeText(AddMerchandise.this,"Done",Toast.LENGTH_SHORT).show();
                            fileDoneList.add(finalI,"Done");
                            uplaodListAdapter.notifyDataSetChanged();
//                            Image.add(fileUri.toString());
//                            System.out.println(fileUri.toString());
//                            Image.add(fileUri.toString());
//                            System.out.println("I am 1st" + Image);

                            StorageReference a =FirebaseStorage.getInstance().getReference().child("Merchandise").child(fileName);

                                    ((StorageReference) a).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {

                                    System.out.println(downloadUrl);
                                    Image.add(downloadUrl.toString());
                                    //do something with downloadurl

                                    if(Image.size()==totalItemSelected)
                                    {
                                        addMerchandise.setEnabled(true);
                                    }

                                }
                            });




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
