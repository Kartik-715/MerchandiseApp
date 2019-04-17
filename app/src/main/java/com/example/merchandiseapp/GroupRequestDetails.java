package com.example.merchandiseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.merchandiseapp.R.layout.info;

public class GroupRequestDetails extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ImageView imageView;
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayList<String> Contact;
    ArrayList<String> Email;
    ArrayList<String> Quantity;
    ArrayList<String> Size;
    ArrayList<String> UserName;
    ArrayAdapter<String> adapter;
    private List<Request> objects;

    private TextView a;


    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_request_details);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        System.out.println(adapterViewPager);


        System.out.println(vpPager);
        vpPager.setAdapter(adapterViewPager);

//        vpPager.setPageTransformer(true, new RotateUpTransformer());


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                Toast.makeText(GroupRequestDetails.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                System.out.println("hello honey bunny");
               final  Fragment fragment =new FirstFragment();
                final Bundle bundle = new Bundle();

                database = FirebaseDatabase.getInstance();
                ref = database.getReference("Group").child("CSEA").child("Requests").child("F01").child("Requests");
                list = new ArrayList<>();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long ctr=0;

                        long count = dataSnapshot.getChildrenCount();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            System.out.println(ds.child("IsPaid").getValue().toString());
                            HashMap<String, Object> PaidMembers = (HashMap<String , Object>) dataSnapshot.getValue();

                            if (ds.child("IsPaid").getValue() != null && ds.child("IsPaid").getValue().toString().equals("true")) {
                                Context context = getApplicationContext();
                                if (ds.child("UserID").getValue() != null)
                                    list.add("UserID : " + ds.child("UserID").getValue().toString());
                                if (ds.child("Quantity").getValue() != null)
                                    list.add("Quantity : " + ds.child("Quantity").getValue().toString());
                                list.add("\n");

                                Contact.add(ds.child("Contact").toString());
                                Email.add(ds.child("Email").toString());
                                Quantity.add(ds.child("Quantity").toString());
                                Size.add(ds.child("Size").toString());
                                UserName.add(ds.child("UserName").toString());

                                    System.out.println( ds.child("UserID").toString());
                            }

                            ctr++;
                            if( ctr == count)
                            {
                                System.out.println(list.size()  );

                                bundle.putStringArrayList("Contact", Contact);
                                bundle.putStringArrayList("Email", Email);
                                bundle.putStringArrayList("Quantity", Quantity);
                                bundle.putStringArrayList("Size", Size);
                                bundle.putStringArrayList("UserName", UserName);
                                fragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                            }
                        }


/*
*
*
*
* HashMap<String, Object> All_merchandise = (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println(All_merchandise);


                for (Object o : All_merchandise.entrySet()) {
                    HashMap.Entry p1 = (HashMap.Entry) o;
                    FragmentItem fragment = new FragmentItem();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", (String) p1.getKey());
                    fragment.setArguments(bundle);
                    adaptor.AddFragment(fragment, (String) p1.getKey());

*
*
*
*
*
* */
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(GroupRequestDetails.this, "dsadsadas page position: " , Toast.LENGTH_SHORT).show();

                    }
                });
            }
//             This method will be invoked when the current page is scrolled
                @Override
                public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels){
                    // Code goes here
                }

                // Called when the scroll state changes:
                // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                @Override
                public void onPageScrollStateChanged ( int state){
                    // Code goes here
                }

        });
    }






    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            System.out.println("************122222222222222222222222222222");

        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            position=0;
            System.out.println("122222222222222222222222222222");
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FirstFragment.newInstance(0, "Paid");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FirstFragment.newInstance(1, "Not Paid");
                default:
                    return null;

            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

//
//
//        /* Tab Layout Setting */
//        tabLayout = findViewById(R.id.tabLayout);
//        viewPager = findViewById(R.id.viewPager_id);
//        final ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getSupportFragmentManager());
//
//
//        DatabaseReference allMerchandise;
//        allMerchandise = FirebaseDatabase.getInstance().getReference().child("Merchandise");
//        allMerchandise.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                HashMap<String, Object> All_merchandise = (HashMap<String, Object>) dataSnapshot.getValue();
//                System.out.println(All_merchandise);
//
//
////                for (Object o : All_merchandise.entrySet()) {
//                    HashMap.Entry p1 = (HashMap.Entry) o;
//                    ItemFragment1 fragment = new ItemFragment1();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("category", (String) p1.getKey());
//                    fragment.setArguments(bundle);
//                    adaptor.AddFragment(fragment, (String) p1.getKey());
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w("ErrMerchandise", "Couldn't Read All Merchandise");
//            }
//        });
//
//        viewPager.setAdapter(adaptor);
//        tabLayout.setupWithViewPager(viewPager);
//

        /* Tab Layout Setting */


    }




//
//    private FloatingActionButton addToCart;
//
//    private Button addToCartButton, buyNowButton;
//    private ImageView productImage;
//    private ElegantNumberButton numberButton;
//    private TextView productPrice, productName;
//    private String productID = "";
//    private String User_ID = "";
//    private String orderID = "";
//    private String image = "";
//    private String category = "";
//    private ArrayList<String> orderid_list;
//    private String image_src;
//    private Spinner SizeSpinner;
//    private String selecteditem;
//    private ArrayList<String> arraySpinner;
//    private int flag;
//
//    public interface MyCallback
//    {
//        void onCallback(ArrayList<String> value);
//    }
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//
//        productID = getIntent().getStringExtra("pid");
//        orderID = getIntent().getStringExtra("order_id");
//        image_src = getIntent().getStringExtra("image");
//        category = getIntent().getStringExtra("category");
//        User_ID = Prevalent.currentOnlineUser;
//        orderid_list = new ArrayList<>();
//
//        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
//        buyNowButton = findViewById(R.id.buy_now_Button);
//        numberButton = findViewById(R.id.numberBtn);
//        productImage = findViewById(R.id.productImage);
//        productName = findViewById(R.id.productName);
//        SizeSpinner = findViewById(R.id.size_spinner);
//        productPrice = findViewById(R.id.productPrice);
//
//
//        /*Spinner Display*/
//        arraySpinner = new ArrayList<>();
//
//        initializeSpinner(new MyCallback()
//        {
//            @Override
//            public void onCallback(ArrayList<String> value)
//            {
//                System.out.println("qqqqqqqqqq" + arraySpinner);
//                System.out.println("wwwwwwwwww" + value);
//
//                if(arraySpinner.isEmpty())
//                {
//                    flag = 0;
//                    getProductDetails(productID);
//                    addToCartButton.setVisibility(View.INVISIBLE);
//                    buyNowButton.setVisibility(View.INVISIBLE);
//                    numberButton.setVisibility(View.INVISIBLE);
//                    SizeSpinner.setVisibility(View.INVISIBLE);
//                    productPrice.setVisibility(View.INVISIBLE);
//                }
//
//                else
//                {
//                    flag = 1;
//                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(productDetailActivity.this, android.R.layout.simple_list_item_1, arraySpinner);
//                    myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    SizeSpinner.setAdapter(myAdapter);
//
//                    SizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//                    {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//                        {
//                            selecteditem = SizeSpinner.getSelectedItem().toString();
//                            getProductDetails(productID);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent)
//                        {
//
//                        }
//                    });
//                }
//
//            }
//        });
//
//        productImage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(productDetailActivity.this, FullPageImageActivity.class);
//                intent.putExtra("image", image_src);
//                startActivity(intent);
//            }
//        });
//
//
//        addToCartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addingToCartList();
//            }
//        });
//
//        buyNowButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                BuyNow();
//            }
//        });
//    }
//
//    private void BuyNow()
//    {
//        String saveCurrentTime, saveCurrentDate;
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
//        saveCurrentTime = currentTime.format(calForDate.getTime());
//
//        String neworderID;
//
//        if(orderID.equals("empty"))
//        {
//            neworderID = saveCurrentDate + " " + saveCurrentTime;
//            orderID = neworderID;
//        }
//
//        else
//        {
//            neworderID = saveCurrentDate + " " + saveCurrentTime;
//            orderID = orderID;
//
//        }
//
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");
//
//
//        final HashMap<String, Object> cartMap = new HashMap<>();
//        cartMap.put("pid",productID);
//        cartMap.put("pname",productName.getText().toString());
//        cartMap.put("price",productPrice.getText().toString());
//        cartMap.put("date",saveCurrentDate);
//        cartMap.put("time",saveCurrentTime);
//        cartMap.put("contact", "");
//        cartMap.put("address", "");
//        cartMap.put("email","");
//        cartMap.put("isplaced","false");
//        cartMap.put("status","incart");
//        cartMap.put("quantity",numberButton.getNumber());
//        cartMap.put("discount ","");
//        cartMap.put("uid", User_ID);
//        cartMap.put("orderid", neworderID);
//        cartMap.put("image", image);
//        cartMap.put("category", category);
//        cartMap.put("size", selecteditem);
//
//        //Removing the previous one and making new one
//        cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//                if(task.isSuccessful())
//                {
//
//                }
//            }
//        });
//
//        cartListRef.child(User_ID).child(neworderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//
//                if(task.isSuccessful())
//                {
//                    orderid_list.add(orderID);
//                    Intent intent = new Intent(productDetailActivity.this, DetailsActivity.class);
//                    intent.putExtra("orderid_list", orderid_list);
//                    startActivity(intent);
//                }
//            }
//        });
//
//    }
//    private void addingToCartList()
//    {
//        String saveCurrentTime, saveCurrentDate;
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
//        saveCurrentTime = currentTime.format(calForDate.getTime());
//        String neworderID;
//
//        if(orderID.equals("empty"))
//        {
//            neworderID = saveCurrentDate + " " + saveCurrentTime;
//            orderID = neworderID;
//        }
//
//        else
//        {
//            neworderID = saveCurrentDate + " " + saveCurrentTime;
//            orderID = orderID;
//
//        }
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");
//
//
//        final HashMap<String, Object> cartMap = new HashMap<>();
//        cartMap.put("pid",productID);
//        cartMap.put("pname",productName.getText().toString());
//        cartMap.put("price",productPrice.getText().toString());
//        cartMap.put("date",saveCurrentDate);
//        cartMap.put("time",saveCurrentTime);
//        cartMap.put("contact", "");
//        cartMap.put("address", "");
//        cartMap.put("email","");
//        cartMap.put("isplaced","false");
//        cartMap.put("status","incart");
//        cartMap.put("quantity",numberButton.getNumber());
//        cartMap.put("discount ","");
//        cartMap.put("uid", User_ID);
//        cartMap.put("orderid", neworderID);
//        cartMap.put("image", image);
//        cartMap.put("category", category);
//        cartMap.put("size", selecteditem);
//
//        //Removing the previous one and making new one
//        cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//                if(task.isSuccessful())
//                {
//
//                }
//            }
//        });
//
//        cartListRef.child(User_ID).child(neworderID).updateChildren(cartMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//
//                        if(task.isSuccessful())
//                        {
//                            Toast.makeText(productDetailActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(productDetailActivity.this, HomeActivity.class);
//                            startActivity(intent);
//
//                        }
//                    }
//                });
//    }
//
//    private void getProductDetails(String productID)
//    {
//
//        if(flag == 0)
//        {
//            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
//            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                {
//                    if(dataSnapshot.exists())
//                    {
////                        Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
////                        productName.setText(merchandises.getBrandName());
////                        Picasso.get().load(merchandises.getImage()).into(productImage);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError)
//                {
//
//                }
//            });
//
//            return;
//        }
//
//
//
//        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
//        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.exists())
//                {
//                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
//                    numberButton.setNumber("1");
//                    productName.setText(merchandises.getBrandName());
//                    image = merchandises.getImage();
//                    Picasso.get().load(merchandises.getImage()).into(productImage);
//
//                    if(selecteditem.equals("S"))
//                    {
//                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(0));
//                        numberButton.setRange(1,final_quantity);
//                        productPrice.setText(merchandises.getPrice().get(0));
//                    }
//
//                    if(selecteditem.equals("M"))
//                    {
//                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(1));
//                        numberButton.setRange(1,final_quantity);
//                        productPrice.setText(merchandises.getPrice().get(1));
//                    }
//
//                    if(selecteditem.equals("L"))
//                    {
//                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(2));
//                        numberButton.setRange(1,final_quantity);
//                        productPrice.setText(merchandises.getPrice().get(2));
//                    }
//
//                    if(selecteditem.equals("XL"))
//                    {
//                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(3));
//                        numberButton.setRange(1,final_quantity);
//                        productPrice.setText(merchandises.getPrice().get(3));
//                    }
//
//                    if(selecteditem.equals("XXL"))
//                    {
//                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(4));
//                        numberButton.setRange(1,final_quantity);
//                        productPrice.setText(merchandises.getPrice().get(4));
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
//    }
//
//    private void initializeSpinner(final MyCallback myCallback)
//    {
//        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
//        productsRef.child(productID).addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.exists())
//                {
//                    int idx = 0;
//                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
//                    ArrayList<String> quantity = merchandises.getQuantity();
//                    for(int i=0;i<quantity.size();i++)
//                    {
//
//                        if(quantity.get(i).equals("0") == false)
//                        {
//                            if(i == 0)
//                            {
//                                arraySpinner.add("S");
//                            }
//
//                            if(i == 1)
//                            {
//                                arraySpinner.add("M");
//                            }
//
//                            if(i == 2)
//                            {
//                                arraySpinner.add("L");
//                            }
//
//                            if(i == 3)
//                            {
//                                arraySpinner.add("XL");
//                            }
//
//                            if(i == 4)
//                            {
//                                arraySpinner.add("XXL");
//                            }
//
//
//                        }
//                    }
//
//                    myCallback.onCallback(arraySpinner);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
//
//    }
//
//}
