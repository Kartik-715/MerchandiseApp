package com.example.merchandiseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class myWallet extends AppCompatActivity {
    private String username;
    private DatabaseReference UserData;
    private TextView Current_Amount;
    private EditText Money_Added;
    private Button Btn_Add;
    private G_var global;
    private Button Btn_100, Btn_500, Btn_1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        global = (G_var) getApplicationContext();
        username = global.getUsername();

        Current_Amount = findViewById(R.id.cur_balance);
        Btn_Add = findViewById(R.id.Btn_Add_Money);
        Money_Added = findViewById(R.id.Text_Money);
        Btn_100 = findViewById(R.id.Btn_100);
        Btn_500 = findViewById(R.id.Btn_500);
        Btn_1000 = findViewById(R.id.Btn_1000);

        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
        UserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Wallet_Money").exists())
                {
                    Current_Amount.setText("Current Balance : " + dataSnapshot.child("Wallet_Money").getValue().toString());
                    Prevalent.currentWalletMoney = dataSnapshot.child("Wallet_Money").getValue().toString();
                }
                else //Just add the Money as "0" in the data
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Wallet_Money", "0");

                    UserData.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            Current_Amount.setText("Current Balance : 0");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        Btn_Add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(Money_Added.getText().toString().trim()))
                {
                    Toast.makeText(myWallet.this, "Please Add Some Amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                Prevalent.currentMoney = Money_Added.getText().toString();
                Intent intent = new Intent(myWallet.this, PaymentWalletActivity.class);
                startActivity(intent);
            }
        });

        Btn_100.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Money_Added.setText("100");
            }
        });

        Btn_500.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Money_Added.setText("500");
            }
        });

        Btn_1000.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Money_Added.setText("1000");
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(myWallet.this, HomeActivity.class);
        Prevalent_Intent.setIntent(intent);
        intent.putExtra("orderType", Prevalent.currentOrderType);
        startActivity(intent);
    }
}
