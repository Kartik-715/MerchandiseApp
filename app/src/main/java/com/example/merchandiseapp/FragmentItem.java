package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentItem extends Fragment
{
    final ArrayList<Merchandise> array_merchandise = new ArrayList<>();
    private HomeActivity mHomeActivity ;
    Bundle bundle ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeActivity = (HomeActivity) getActivity() ;
        bundle = this.getArguments() ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false) ;
        setupRecyclerView(rv) ;
        return rv ;
    }

    public FragmentItem()
    {

    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        ArrayList<String> array_test = new ArrayList<>();

        array_test.add("CSEA");
        array_test.add("CSEA");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mHomeActivity);
        recyclerView.setLayoutManager(layoutManager);

        /*DatabaseReference test = FirebaseDatabase.getInstance().getReference().child("Group");
        System.out.println("Test" + test);

        test.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    System.out.println("Fuck" + postSnapshot.getKey());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(((String) postSnapshot.getKey())).child("Merchandise").child(bundle.getString("category","none"));
                    System.out.println("Fuck2" + ref);

                    ref.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.child("Price").getValue() != null)
                                    System.out.println("Fuck_Loop  :  " + dataSnapshot1.child("Price").getValue().toString() );

                                System.out.println("Fuck3  :  " + dataSnapshot1.getValue(Merchandise.class) );
                                System.out.println("Fuck4  :  " + dataSnapshot1.getKey() );
                                System.out.println("Bakchodi  :  " + dataSnapshot1.getValue() );
                                Merchandise test = dataSnapshot1.getValue(Merchandise.class);
                                System.out.println("Fuck5 : " + test);
                                array_merchandise.add(test);
                            }

                            adapter();
                            System.out.println(array_merchandise + "5555555");
                            System.out.println(array_merchandise.get(0).getPrice() + "5555555");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                        }
                    });
                    System.out.println(array_merchandise + "233333333");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });*/

        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(array_test.get(0)).child("Merchandise").child(bundle.getString("category","none")) ;
        final Query queries = ProductsRef;

        FirebaseRecyclerOptions<Merchandise> options = new FirebaseRecyclerOptions.Builder<Merchandise>()
                .setQuery(queries, Merchandise.class)
                .build();

        FirebaseRecyclerAdapter<Merchandise, MerchandiseViewHolder> adapter = new FirebaseRecyclerAdapter<Merchandise, MerchandiseViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull MerchandiseViewHolder holder, int position, final Merchandise model)
            {
                holder.txtProductName.setText(model.getGroupName());
                holder.txtProductDescription.setText(model.getCategory());
                holder.txtProductPrice.setText("Price = " + model.getPrice() );

                if(model.getImage() != null)
                    Picasso.get().load(model.getImage().get(0)).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent;
                        intent = new Intent(mHomeActivity, productDetailActivity.class);
                        intent.putExtra("pid", model.getPID());
                        intent.putExtra("order_id", "empty");
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        intent.putExtra("groupName", model.getGroupName());
                        intent.putExtra("size_list", model.getSize());
                        mHomeActivity.startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MerchandiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
                return new MerchandiseViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public void adapter()
    {

    }


}
