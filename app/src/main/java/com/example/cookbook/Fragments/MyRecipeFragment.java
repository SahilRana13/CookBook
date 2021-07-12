package com.example.cookbook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MyRecipeFragment extends Fragment {

    ListView listView;
    ArrayList<String> arList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    RecipeInfo obj;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    public MyRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Recipes");


        obj = new RecipeInfo();

        listView = view.findViewById(R.id.myRecipeListView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        adapter = new ArrayAdapter<String>(getActivity(),R.layout.custom_my_recipe_list,R.id.myRecipeName,arList);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        final DatabaseReference recipelistbranch = databaseReference.child("User Recipe Details").child(firebaseAuth.getUid());

        recipelistbranch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                for (DataSnapshot ds: snapshot.getChildren())
                {

                    obj = ds.getValue(RecipeInfo.class);
                    arList.add(obj.getRecipeName());
                }

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

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