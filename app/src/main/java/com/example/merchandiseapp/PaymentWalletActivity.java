package com.example.merchandiseapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentWalletActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_payment_wallet);

        System.out.println(Prevalent.currentWalletMoney + "qwerty");

        Btn_Payment = findViewById(R.id.Btn_Payment);
        radioGroup = findViewById(R.id.radiogroup);
        amount = Prevalent.currentMoney ;

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
            ArrayList<String> orderid_list = new ArrayList<>();
            ArrayList<String> group_list = new ArrayList<>();
            ArrayList<String> product_list = new ArrayList<>();

            Intent intent = new Intent(this, UPIActivity.class);
            intent.putExtra("amount", amount) ;
            intent.putExtra("orderid_list", orderid_list);
            intent.putExtra("group_list", group_list);
            intent.putExtra("product_list", product_list);
            intent.putExtra("mode", "Wallet");
            startActivity(intent);
        }
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
                final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
                String current_wallet_money = Prevalent.currentWalletMoney;
                System.out.println("Initial " + Prevalent.currentWalletMoney);
                System.out.println("Initial " + amount);
                int current_w_money = Integer.parseInt(current_wallet_money);
                int added_w_money = Integer.parseInt(amount);

                current_w_money += added_w_money;
                current_wallet_money = Integer.toString(current_w_money);

                userRef.child("Wallet_Money").setValue(current_wallet_money);
                Prevalent.currentWalletMoney = current_wallet_money;

                Toast.makeText(PaymentWalletActivity.this, inResponse.toString(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(PaymentWalletActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                //finish();
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

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(PaymentWalletActivity.this, myWallet.class);
        startActivity(intent);

    }
}
