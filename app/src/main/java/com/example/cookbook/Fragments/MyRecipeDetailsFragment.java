package com.example.cookbook.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyRecipeDetailsFragment extends Fragment {

    private EditText recipeName,chefName,recipeType,recipeDuration,countryName,recipeIngredients,recipeDirections;
    private ImageSlider imageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String strName;
    private List<SlideModel> images;
    private Button updateRecipeButton;

    String name1,name2,name3,name4,name5,name6,name7,name8;
    String rName,cName,rType,rDuration,country,rIngredients,rDirections;


    public MyRecipeDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_recipe_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        imageView = view.findViewById(R.id.receiveMyRecipeImageView);
        recipeName = view.findViewById(R.id.receiveMyRecipeName);
        chefName = view.findViewById(R.id.receiveMyChefName);
        recipeType = view.findViewById(R.id.receiveMyRecipeType);
        recipeDuration = view.findViewById(R.id.receiveMyDuration);
        countryName = view.findViewById(R.id.receiveMyCountry);
        recipeIngredients = view.findViewById(R.id.receiveMyIngredients);
        recipeDirections = view.findViewById(R.id.receiveMyDirection);
        updateRecipeButton = view.findViewById(R.id.saveUpdateMyRecipeBtn);

        images = new ArrayList<>();


        Bundle bundle = this.getArguments();
        strName = bundle.getString("key");




        updateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rName = recipeName.getText().toString().trim();
                cName = chefName.getText().toString().trim();
                rType = recipeType.getText().toString().trim();
                rDuration = recipeDuration.getText().toString().trim();
                country = countryName.getText().toString().trim();
                rIngredients = recipeIngredients.getText().toString().trim();
                rDirections = recipeDirections.getText().toString().trim();

                updateRecipe(rName,cName,rType,rDuration,country,rIngredients,rDirections);


            }
        });

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
                        if (area_value.contains(strName)) {


                            Map<String, Object> newPost = (Map<String, Object>) ds1.getValue();

                            name1 = String.valueOf(newPost.get("chefName"));
                            name2 = String.valueOf(newPost.get("countryName"));
                            name3 = String.valueOf(newPost.get("recipeDirections"));
                            name4 = String.valueOf(newPost.get("recipeDuration"));
                            name5 = String.valueOf(newPost.get("recipeIngredients"));
                            name6 = String.valueOf(newPost.get("recipeName"));
                            name7 = String.valueOf(newPost.get("recipeType"));
                            name8 = String.valueOf(newPost.get("recipeImageLink"));

                            String[] arrOfStr = name5.split("(\n)|(,)|(\\.)");
                            String[] arrOfStr1 = name3.split("(\\. )|(\n)|(\r)");


                            StringBuilder s1 = new StringBuilder();
                            StringBuilder s2 = new StringBuilder();

                            for (String a1 : arrOfStr)
                            {
                                s1.append("\u2022 ").append(a1).append("\n");
                            }

                            for (String a2 : arrOfStr1)
                            {
                                if (!a2.isEmpty())
                                {
                                    s2.append("\u2022 ").append(a2).append("\n\n");
                                }

                            }




                            recipeName.setText(name6);
                            chefName.setText(name1);
                            recipeType.setText(name7);
                            recipeDuration.setText(name4);
                            countryName.setText(name2);
                            recipeIngredients.setText(s1.toString().trim());
                            recipeDirections.setText(s2.toString().trim());




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


        DatabaseReference childreference1 = databaseReference.child("User Recipe Images");


        childreference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    for (DataSnapshot ds1: ds.getChildren())
                    {


                        DataSnapshot name = ds1.child("recipeName");

                        String area_value = name.getValue().toString();
                        if (area_value.contains(strName)) {

                            images.add(new SlideModel(ds1.child("recipeImageLink").getValue().toString(), ScaleTypes.FIT));
                            imageView.setImageList(images,ScaleTypes.FIT);

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

    private void updateRecipe(String rName, String cName, String rType, String rDuration, String country, String rIngredients, String rDirections) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("User Recipe Details");

        DatabaseReference recipeListBranch = reference.child(firebaseAuth.getUid());

        //RecipeInfo recipeInfo = new RecipeInfo(rName,cName,rType,rDuration,country,rIngredients,rDirections);

        RecipeInfo recipeInfo = new RecipeInfo(rName,cName,rType,rDuration,country,rIngredients,rDirections,name8);


        recipeListBranch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(strName)){

                    recipeListBranch.child(strName).removeValue();

                    recipeListBranch.child(strName).push().setValue(recipeInfo);
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

}
