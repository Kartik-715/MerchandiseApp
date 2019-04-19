package com.example.merchandiseapp;

import android.content.Intent;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity
{
    private Button Btn_Payment;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference UserData;
    private String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Btn_Payment = findViewById(R.id.Btn_Payment);
        radioGroup = findViewById(R.id.radiogroup);
        Prevalent.currentMoney = "50";
        Prevalent.currentWalletMoney = "75";
        amount = "2";

        updateWallet();

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

        if(radioButton.getText().toString().equals("Paytm"))
        {
            processPaytm();
        }

        if(radioButton.getText().toString().equals("UPI"))
        {
            Intent intent = new Intent(this, UPIActivity.class);
            startActivity(intent);
        }
        if(radioButton.getText().toString().equals("Wallet Money"))
        {

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


    private void processPaytm()
    {

        String custID = generateString();
        String orderID = generateString();
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID;

        final Paytm paytm = new Paytm
                (
                "uPliqe82544594658092",
                "WAP",
                amount,
                "WEBSTAGING",
                callBackurl,
                "Retail",
                orderID,
                custID
        );

        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new Callback<Checksum>()
        {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response)
            {
                if (response.isSuccessful())
                {
                    processToPay(response.body().getChecksumHash(),paytm);
                }
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t)
            {
                // This method gets called if transaction failed. //
                // Here in this case transaction is completed, but with
                // a failure. // Error Message describes the reason for
                // failure. // Response bundle contains the merchant
                // response parameters.
               /* Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            } */
            }
        });
    }

    private void processToPay(String checksumHash, Paytm paytm)
    {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , paytm.getOrderId());
        paramMap.put( "CUST_ID" , paytm.getCustId());
        paramMap.put( "CHANNEL_ID" , paytm.getChannelId());
        paramMap.put( "TXN_AMOUNT" , paytm.getTxnAmount());
        paramMap.put( "WEBSITE" , paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback()
        {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage)
            {
                // Some UI Error Occurred in Payment Gateway Activity.
                // // This may be due to initialization of views in
                // Payment Gateway Activity or may be due to //
                // initialization of webview. // Error Message details
                // the error occurred.
            }
            public void onTransactionResponse(Bundle inResponse)
            {
                Toast.makeText(PaymentActivity.this, inResponse.toString(), Toast.LENGTH_SHORT).show();

            }

            public void networkNotAvailable()
            {
                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

            }

            public void clientAuthenticationFailed(String inErrorMessage)
            {
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

            }

            public void onErrorLoadingWebPage(int inErrorCode, String inErrorMessage, String inFailingUrl)
            {
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();

            }

            public void onBackPressedCancelTransaction()
            {
                Toast.makeText(PaymentActivity.this, "Something pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                finish();
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse)
            {
                Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private String generateString()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
