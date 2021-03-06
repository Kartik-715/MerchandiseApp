package com.example.merchandiseapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemFragment1 extends Fragment {

        private GroupRequestDetails mGroupRequestDetails;
        Bundle bundle ;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mGroupRequestDetails= (GroupRequestDetails) getActivity() ;
            bundle = this.getArguments() ;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false) ;
            setupRecyclerView(rv) ;
            return rv ;
        }

        public ItemFragment1()
        {

        }


        private void setupRecyclerView(RecyclerView recyclerView)
        {
            DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(bundle.getString("category","none")) ;
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mGroupRequestDetails);
            recyclerView.setLayoutManager(layoutManager);

            FirebaseRecyclerOptions<Merchandise> options = new FirebaseRecyclerOptions.Builder<Merchandise>()
                    .setQuery(ProductsRef, Merchandise.class)
                    .build();

            FirebaseRecyclerAdapter<Merchandise, MerchandiseViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Merchandise, MerchandiseViewHolder>(options)
                    {
                        @Override
                        protected void onBindViewHolder(@NonNull MerchandiseViewHolder holder, int position, final Merchandise model)
                        {
//
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


    }
