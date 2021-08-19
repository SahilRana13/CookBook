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

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                            String name8 = String.valueOf(newPost.get("recipeImageLink"));



                            recipeName.setText(name6);
                            chefName.setText(name1);
                            recipeType.setText(name7);
                            recipeDuration.setText(name4);
                            countryName.setText(name2);

                            String[] arrOfStr = name5.split("\n");


                            StringBuilder s1 = new StringBuilder();

                            for (String a : arrOfStr)
                            {
                                s1.append("\u2022 ").append(a).append("\n");
                            }


                            recipeIngredients.setText(s1.toString());
                            recipeDirections.setText(name3);

                            sendHistory(name1,name2,name3,name4,name5,name6,name7,name8);



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


    private void sendHistory(String name1, String name2, String name3, String name4, String name5, String name6, String name7,String link) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = firebaseDatabase.getReference().child("Recent Viewed Item").child(firebaseAuth.getUid());

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                RecipeInfo recipeInfo = new RecipeInfo(name6,name1,name7,name4,name2,name5,name3,link);

                if(!snapshot.hasChild(name6)){

                    reference1.child(name6).push().setValue(recipeInfo);
                    return;
                }
                else if (snapshot.hasChild(name6)){
                    reference1.child(name6).removeValue();

                    reference1.child(name6).push().setValue(recipeInfo);
                    return;
                }
                else
                {
                    Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}