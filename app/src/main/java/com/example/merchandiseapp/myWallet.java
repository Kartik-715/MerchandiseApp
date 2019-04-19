package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
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

        getInfo();
        Btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //transact();
            }
        });
    }

    //function to handle transactions by making a paytm api call
    public  void transact()
    {

        PaytmPGService Service = PaytmPGService.getStagingService();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "MID" , "vyZEyu61387796366188");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , "order1");
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7541818508");
        paramMap.put( "EMAIL" , "sparsh5008@gmail.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "1");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder(paramMap);



        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) { Toast.makeText(getApplicationContext(), "Some UI error Occured" , Toast.LENGTH_LONG).show();}
            public void onTransactionResponse(Bundle inResponse) {Toast.makeText(getApplicationContext(), "transaction succeeded"+ inResponse.toString() , Toast.LENGTH_LONG).show();}
            public void networkNotAvailable() {Toast.makeText(getApplicationContext(), "network not available " , Toast.LENGTH_LONG).show();}
            public void clientAuthenticationFailed(String inErrorMessage) {Toast.makeText(getApplicationContext(), "authentication failure " , Toast.LENGTH_LONG).show();}
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {Toast.makeText(getApplicationContext(), "Payment Transaction response " , Toast.LENGTH_LONG).show();}
            public void onBackPressedCancelTransaction() {Toast.makeText(getApplicationContext(), " back pressed " , Toast.LENGTH_LONG).show();}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {Toast.makeText(getApplicationContext(), "Cancelled transaction " , Toast.LENGTH_LONG).show();}
        });
    }


    public void getInfo()
    {
        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
        UserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Wallet_Money").exists())
                {
                    Current_Amount.setText(dataSnapshot.child("Wallet_Money").getValue().toString());
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

        return;

    }
}
