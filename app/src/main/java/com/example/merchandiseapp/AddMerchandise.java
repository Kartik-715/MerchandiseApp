package com.example.merchandiseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
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


    private String GroupName ;
    private String Category ;
    private ArrayList<String> Image;
    private String Material;
    private String PID;
    private String Price;
    private String OrderType;

    ArrayAdapter adapter;
    ArrayAdapter adapterSize_qty;
    DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchandise);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Footwear"
                , "T-Shirt", "Lowers" , "Hoodie","Others"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapterSpinner);

        add_accessgroup = (Button) findViewById(R.id.button_add_accessgroup);
        addSQ = (Button) findViewById(R.id.button_size_qty);
        addMerchandise = (Button) findViewById(R.id.button_submit);

        access_editText = (EditText) findViewById(R.id.editText_accessgroup);
        size_edt = (EditText) findViewById(R.id.editText_size);
        qty_edt = (EditText) findViewById(R.id.editText_qty);
        gpName = (EditText) findViewById(R.id.EditText_GroupName);
        mat = (EditText) findViewById(R.id.EditText_Material);
        price_edt = (EditText) findViewById(R.id.EditText_Price);
        prod_Id = (EditText) findViewById(R.id.EditText_prodID);

        accessGroupListView = (ListView) findViewById(R.id.listView_accessgroup);
        sizeQtyListView =(ListView) findViewById(R.id.listView_size_qty);







        AccessGroups.add("CSEA");

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AccessGroups);
        adapterSize_qty  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listSQ);

        add_accessgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessGroups.add(access_editText.getText().toString());
                adapter.notifyDataSetChanged();
                access_editText.setText("");

            }
        });
        accessGroupListView.setAdapter(adapter);

        addSQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (size_edt.getText().toString() == "")
                {
                    Toast.makeText(getApplicationContext(),"Enter the size", Toast.LENGTH_LONG);
                    return;
                }
                if (qty_edt.getText().toString() == "")
                {
                    Toast.makeText(getApplicationContext(),"Enter the quantity", Toast.LENGTH_LONG);
                    return;
                }
                listSQ.add(size_edt.getText().toString()+":"+qty_edt.getText().toString());
                size.add(size_edt.getText().toString());
                qty.add(qty_edt.getText().toString());
                size_edt.setText("");
                qty_edt.setText("");
            }
        });
        sizeQtyListView.setAdapter(adapterSize_qty);


        addMerchandise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner dropdown = findViewById(R.id.spinner1);
                GroupName = gpName.getText().toString();
                Category = (String) dropdown.getSelectedItem();
                Image = new ArrayList<>();
                Material = mat.getText().toString();
                PID = prod_Id.getText().toString();
                Price = price_edt.getText().toString();
                System.out.println(GroupName+Category+Image+Material+PID+Price);

                myRef = FirebaseDatabase.getInstance().getReference().child("Group").child("CSEA").child("Merchandise");
                Merchandise merchandise = new Merchandise(GroupName,Category,Image,Material,PID,Price,qty,size,AccessGroups,OrderType,"true");
                HashMap<String, Object> merchandiseValues = merchandise.toMap();

                HashMap<String, Object> childUpdates = new HashMap<>();
                childUpdates.put( Category +"/"+PID, merchandiseValues);


                myRef.updateChildren(childUpdates);

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
                    break;
            case R.id.radioButtonNO:
                if (checked)
                    OrderType = "2";
                    break;
        }
    }

}
