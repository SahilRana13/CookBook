package com.example.cookbook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.cookbook.Adapters.RecipeListAdapter;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DiscoverFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference root;
    private RecipeListAdapter adapter;
    private ArrayList<RecipeInfo> list;
    private RecipeInfo model,model_1;
    private int count = 1;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Discover Recipes");

        recyclerView = view.findViewById(R.id.recipeRecyclerView);
        db = FirebaseDatabase.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        adapter = new RecipeListAdapter(getContext(),list);


        root = db.getReference().child("User Recipe Details");

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {

                   // model_1 = dataSnapshot.getValue(RecipeInfo.class);
//                    list.add(model);

                    for (DataSnapshot ds: dataSnapshot.getChildren()) {



                        model = ds.getValue(RecipeInfo.class);

//                        if (model.getRecipeName().equalsIgnoreCase(model_1.getRecipeName()))
//                        {
//                            count++;
//
//                        }
//
//                        if (count==2)
//                        {
//
//                            list.add(model);
//                        }

                        list.add(model);



                    }

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}