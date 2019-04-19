package com.example.merchandiseapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import static javax.mail.internet.InternetAddress.*;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class productsCourrier extends AppCompatActivity {

    private Session session =null;
    private ProgressDialog pdialog =null;
    private Context context =null;
    private String rec,Subject,msg_content;
    private Button login;
    private String mailhost ="smtp.gmail.com";
    private String username = "merchandiseiitg@gmail.com";
    private String password = "merchandise";


    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productName;
    private String productID = "";
    private String User_ID = "";
    private String OrderID = "";
    private String date = "";
    private String time = "";
    private String status = "";
    private TextView productQuantity;
    private TextView productBuyer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_courrier);


        Button  btn = (Button) findViewById(R.id.button1);

        productID = getIntent().getStringExtra("pid");
        date = getIntent().getStringExtra("pdate");
        time = getIntent().getStringExtra("ptime");

        User_ID = Prevalent.currentOnlineUser;
        System.out.println("\n123 "+ date);
        System.out.println("123 "+time);
        System.out.println("123 "+productID);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productBuyer = findViewById(R.id.productBuyer);
        productQuantity = findViewById(R.id.productQuantity);

        //getProductDetails(productID);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Packed and ready to be shipped"
                , "Shipped", "On Delivery" , "Delivered"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    /*private void getProductDetails(String productID)
    {
        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser);
        System.out.println(Prevalent.currentOnlineUser + Prevalent.currentEmail + " 123");

         productsRef.child(date+" "+time).addValueEventListener(new ValueEventListener()
         {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@2"+date+" "+time+"@@@@@@@@@@@@@@@@@@@@");
                Order order = dataSnapshot.getValue(Order.class);
                //System.out.println("hello" + order);
                status = order.getStatus();
                OrderID = order.getOrderid();
               productName.setText(order.getPname());
               productPrice.setText(order.getPrice());
                productQuantity.setText(order.getQuantity());
                productBuyer.setText(Prevalent.currentEmail);
                Spinner dropdown = findViewById(R.id.spinner1);
                System.out.println(status);

                if(status.toLowerCase().equals("packed and ready to be shipped"))
                {
                    dropdown.setSelection(0);
                }

                else if(status.toLowerCase().equals("shipped"))
                {
                    dropdown.setSelection(1);
                }

                else if (status.toLowerCase().equals("on delivery"))
                {
                    System.out.println("dsaaaaaaaaaaaaaaaaaaaa12#@#@!321132321dsa");
                    dropdown.setSelection(2);
                }

                if(status.toLowerCase().equals("delivered"))
                {
                    dropdown.setSelection(3);
                }

// Picasso.get().load(order.getStatus();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    public void doOnClick(View view)
    {
        System.out.println("dsaaaaaaaa");
        Spinner dropdown = findViewById(R.id.spinner1);
        String text = dropdown.getSelectedItem().toString();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("status", text);
        System.out.println("dsaaaaaaaa"+User_ID + OrderID);
        User_ID=Prevalent.currentOnlineUser;
        cartListRef.child(User_ID).child(OrderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Intent intent = new Intent(productsCourrier.this, CourierActivity.class);
                    startActivity(intent);
                }
            }
        });

            rec = "mayank343@gmail.com";
            Subject = "Delivery Status";
            msg_content = "Respected Sir, \nThis is with reference to your order places with our merchandise app.\n";
            msg_content+="We are happy to inform you that your order has been";

            if(text.toLowerCase().equals("shipped"))
            {
                msg_content+="shipped.";
            }
        else if(text.toLowerCase().equals("on delivery"))
        {
            msg_content+="on delivery and is expected to reach soon.";
        }
            else if(text.toLowerCase().equals("delivered"))
            {
                msg_content += "delivered";

            }
        msg_content += "\nThank You for shopping with us. Kindly contact us in case of querries.\nRegards,\nTeam Merchandise";


        Properties props = new Properties();
            //props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");

            session = javax.mail.Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            });

            RetrieveFeedTask task = new RetrieveFeedTask();
            task.execute();

    }

    class RetrieveFeedTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try{
                javax.mail.Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailhost));
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(Subject);
                message.setContent(msg_content,"text/html;charset=utf-8");

                Transport.send(message);

            }catch (MessagingException e){
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
        }
    }*/
}




























