package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CustomAdapter2 extends BaseAdapter {
    private Session session =null;
    private ProgressDialog pdialog =null;
    public String rec,Subject,msg_content;
    private Button login;
    private String mailhost ="smtp.gmail.com";
    private String username = "merchandiseiitg@gmail.com";
    private String password = "merchandise";
    Context context;
    ArrayList<String> Contact;
    ArrayList<String> Email;
    ArrayList<String> Quantity;
    ArrayList<String> Size;
    ArrayList<String> OrderID;
    ArrayList<String> UserName;
    ArrayList<String> UserID;
String PID;
    public CustomAdapter2()
    {
    }


    private LayoutInflater inflter;

    public CustomAdapter2(Context applicationContext, ArrayList<String> Contact,

                         ArrayList<String> Email,
                         ArrayList<String> Quantity,
                         ArrayList<String> Size,
                         ArrayList<String> UserName,String PID,
                          ArrayList<String> OrderID,ArrayList< String> UserID

    ) {
        this.context = applicationContext;


        this.Contact = Contact;
        this.Email = Email;
        this.Quantity = Quantity;
        this.Size = Size;
        this.UserName = UserName;
        this.OrderID = OrderID;
        this.UserID= UserID;
        this.PID=PID;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return UserName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_list_item2, null);
        TextView Contact1 = (TextView) view.findViewById(R.id.Contact);
        TextView Email1 = (TextView) view.findViewById(R.id.Email);
        TextView Quantity1 = (TextView) view.findViewById(R.id.Quantity);
        TextView Size1 = (TextView) view.findViewById(R.id.Size);
        TextView UserName1 = (TextView) view.findViewById(R.id.userName);
        ImageView icon1 = (ImageView) view.findViewById(R.id.icon);

        final Spinner dropdown = view.findViewById(R.id.status);
        String[] items = new String[]{"Packed ","Can collect"
                , "Delivered"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);




        System.out.println("hi" + UserName);

        Contact1.setText(Contact.get(i));
        Email1.setText(Email.get(i));
        Quantity1.setText(Quantity.get(i));
        Size1.setText(Size.get(i));

        System.out.println("hi" + UserName.size());

        UserName1.setText(UserName.get(i));
        Button mButton = (Button) view.findViewById(R.id.update);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = dropdown.getSelectedItem().toString();
//TO DO
                System.out.println(OrderID.get(i));

                String noti;
                noti = "We are happy to inform you that your order ";
                if (text.toLowerCase().equals("packed")) {
                    noti += "is packed.";
                } else if (text.toLowerCase().equals("can collect")) {
                    noti += "is now ready with an executive. You can collect it from him.";
                } else if (text.toLowerCase().equals("delivered")) {
                    noti += "has been delivered";
                }

                System.out.println(UserID+"DS");
                FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(UserID.get(i)).child("notiList").setValue(noti);


                FirebaseDatabase.getInstance().getReference().child("Group")
                        .child("CSEA").child("Orders").child(PID).child("Orders").child(OrderID.get(i)).child("Status").setValue(text)


                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Value has been succesfully updated", Toast.LENGTH_SHORT);

                            }
                        });




                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(UserID.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        rec= Email.get(i);
        System.out.println(rec + " )))");
        Subject = "Delivery Status";
        msg_content = "Respected Sir, \nThis is with reference to your order places with our merchandise app.\n";
        msg_content += "We are happy to inform you that your order ";
        String text1 = dropdown.getSelectedItem().toString();
        if (text1.toLowerCase().equals("packed")) {
            msg_content += "is packed.";
        } else if (text1.toLowerCase().equals("can collect")) {
            msg_content += "is now ready with an executive. You can collect it from him.";
        } else if (text1.toLowerCase().equals("delivered")) {
            msg_content += "has been delivered";

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
                return new PasswordAuthentication(username, password);
            }
        });

        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute();
            }
        });

        return view;
    }


    class RetrieveFeedTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                System.out.println(rec + " (((");

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
            Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
        }

    }
    }
