package com.example.merchandiseapp;

import android.content.Intent;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity
{
    private Button Btn_Payment;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference UserData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Btn_Payment = findViewById(R.id.Btn_Payment);
        radioGroup = findViewById(R.id.radiogroup);

        //updateWallet();

        Btn_Payment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Payment();
            }
        });
    }

    public void Payment()
    {
        int selectedID = radioGroup.getCheckedRadioButtonId();
        radioButton = ((RadioButton) findViewById(selectedID));

        if(radioButton.getText().toString().equals("Wallet Money"))
        {
            Prevalent.currentMoney = "50";
            Prevalent.currentWalletMoney = "55";
            int walletMoney = Integer.parseInt(Prevalent.currentWalletMoney);
            int currentMoney = Integer.parseInt(Prevalent.currentMoney);

            if(currentMoney <= walletMoney)
            {
                walletMoney -= currentMoney;
                System.out.println("First : " + Prevalent.currentWalletMoney);
                final String Wallet_Money = Integer.toString(walletMoney);
                Prevalent.currentWalletMoney = Wallet_Money;
                System.out.println("First : " + Prevalent.currentWalletMoney);


                final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
                userRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.child("Wallet_Money").exists())
                        {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("Wallet_Money", Wallet_Money);

                            userRef.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                }
                            });
                        }

                        else //Just add the Money as "0" in the data
                        {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("Wallet_Money", "0");

                            userRef.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }

            else
                Toast.makeText(PaymentActivity.this, "Not Sufficient Money in Wallet, Add Money in Wallet to Proceed to Payment", Toast.LENGTH_SHORT).show();


        }
    }

    public void updateWallet()
    {
        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
        UserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Wallet_Money").exists())
                {
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

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
