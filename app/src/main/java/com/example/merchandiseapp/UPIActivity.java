package com.example.merchandiseapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class UPIActivity extends AppCompatActivity
{

    EditText noteEt;
    TextView amountEt, upiIdEt, nameEt ;
    Button send;
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list, product_list;
    private String mode;

    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi);

        initializeViews();
        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        group_list = getIntent().getStringArrayListExtra("group_list");
        product_list = getIntent().getStringArrayListExtra("product_list");

        amountEt.setText("Total Amount : " + getIntent().getStringExtra("amount"));
        mode = getIntent().getStringExtra("mode");

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Getting the values from the EditTexts
                String amount = getIntent().getStringExtra("amount");
                String note = noteEt.getText().toString();
                String name = nameEt.getText().toString();
                String upiId = upiIdEt.getText().toString();
                payUsingUpi(amount, upiId, name, note);
            }
        });
    }

    void initializeViews() {
        send = findViewById(R.id.send);
        amountEt = findViewById(R.id.amount_et);
        noteEt = findViewById(R.id.note);
        nameEt = findViewById(R.id.name);
        upiIdEt = findViewById(R.id.upi_id);
    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager()))
            startActivityForResult(chooser, UPI_PAYMENT);


        else
            Toast.makeText(UPIActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        //Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        //Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(UPIActivity.this))
        {
            String str = data.get(0);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");

            System.out.println("RESPONSE: " + response.toString());
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                System.out.println("equalStr: " + equalStr.toString());


                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                        status = equalStr[1].toLowerCase();

                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase()))
                        approvalRefNo = equalStr[1];
                } else
                    paymentCancel = "Payment cancelled by user.";
            }

            if (status.equals("success"))
            {
                if(mode.equals("Wallet"))
                {
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
                    String current_wallet_money = Prevalent.currentWalletMoney;
                    System.out.println("Initial " + Prevalent.currentWalletMoney);
                    int current_w_money = Integer.parseInt(current_wallet_money);
                    int added_w_money = Integer.parseInt(amountEt.getText().toString());

                    current_w_money += added_w_money;
                    current_wallet_money = Integer.toString(current_w_money);

                    userRef.child("Wallet_Money").setValue(current_wallet_money);
                    Prevalent.currentWalletMoney = current_wallet_money;

                    //Toast.makeText(this, "Congratulations, your order has been placed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateFirebase();

                }
                Toast.makeText(UPIActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
            }

            else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(UPIActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(UPIActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        }

        else
            Toast.makeText(UPIActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
    }


    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void updateFirebase()
    {
        for(int i=0;i<orderid_list.size();i++)
        {
            String orderid = orderid_list.get(i);
            String group_name = group_list.get(i);
            String product_name = product_list.get(i);

            final DatabaseReference cartListRef;
            final DatabaseReference cartListRef2;

            if(Prevalent.currentOrderType.equals("1"))
            {
                cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders_Temp").child(Prevalent.currentOnlineUser).child(orderid);
                cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Orders").child(product_name).child("Orders").child(orderid);

                cartListRef.child("IsPlaced").setValue("true");
                cartListRef2.child("IsPlaced").setValue("true");
                cartListRef.child("Status").setValue("packed");
                cartListRef2.child("Status").setValue("packed");
            }

            else
            {
                cartListRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderid);
                cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(product_name).child("Requests").child(orderid);

                cartListRef.child("IsPaid").setValue("true");
                cartListRef2.child("IsPaid").setValue("true");
                //cartListRef.child("Status").setValue("");
                //cartListRef2.child("Status").setValue("");
            }

        }
    }
}

