package com.example.cookbook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


public class RecipeDetailsFragment extends Fragment {

    private EditText recipeName,chefName,recipeType,recipeDuration,countryName,recipeIngredients,recipeDirections;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private String fileName;


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        recipeName = view.findViewById(R.id.receiveRecipeName);
        chefName = view.findViewById(R.id.receiveChefName);
        recipeType = view.findViewById(R.id.receiveRecipeType);
        recipeDuration = view.findViewById(R.id.receiveDuration);
        countryName = view.findViewById(R.id.receiveCountry);
        recipeIngredients = view.findViewById(R.id.receiveIngredients);
        recipeDirections = view.findViewById(R.id.receiveDirection);


        fileName = this.getArguments().getString("key");

        //Toast.makeText(getActivity(), fileName, Toast.LENGTH_SHORT).show();


        DatabaseReference databaseReference = (DatabaseReference) firebaseDatabase.getReference();

        DatabaseReference childreference = databaseReference.child("User Recipe Details");

        childreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    for (DataSnapshot ds1: ds.getChildren())
                    {


                        DataSnapshot name = ds1.child("recipeName");

                        String area_value = name.getValue().toString();
                        if (area_value.contains(fileName)) {


                            Map<String, Object> newPost = (Map<String, Object>) ds1.getValue();

                            String name1 = String.valueOf(newPost.get("chefName"));
                            String name2 = String.valueOf(newPost.get("countryName"));
                            String name3 = String.valueOf(newPost.get("recipeDirections"));
                            String name4 = String.valueOf(newPost.get("recipeDuration"));
                            String name5 = String.valueOf(newPost.get("recipeIngredients"));
                            String name6 = String.valueOf(newPost.get("recipeName"));
                            String name7 = String.valueOf(newPost.get("recipeType"));


                            recipeName.setText(name6);
                            chefName.setText(name1);
                            recipeType.setText(name7);
                            recipeDuration.setText(name4);
                            countryName.setText(name2);
                            recipeIngredients.setText(name5);
                            recipeDirections.setText(name3);


                        }
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}