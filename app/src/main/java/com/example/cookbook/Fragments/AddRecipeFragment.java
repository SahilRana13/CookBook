package com.example.cookbook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class AddRecipeFragment extends Fragment {

    private EditText recipeName,chefName,recipeType,recipeDuration,countryName,recipeIngredients,recipeDirections;
    private Button submitRecipe;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add Recipe");


        recipeName = view.findViewById(R.id.enterRecipeName);
        chefName = view.findViewById(R.id.enterChefName);
        recipeType = view.findViewById(R.id.enterRecipeType);
        recipeDuration = view.findViewById(R.id.enterDuration);
        countryName = view.findViewById(R.id.enterCountry);
        recipeIngredients = view.findViewById(R.id.enterIngredients);
        recipeDirections = view.findViewById(R.id.enterDirection);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        submitRecipe = view.findViewById(R.id.submitRecipeBtn);

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recipeName.getText().toString().trim().length()==0 || chefName.getText().toString().trim().length()==0 || recipeType.getText().toString().trim().length()==0 || recipeDuration.getText().toString().trim().length()==0 || countryName.getText().toString().trim().length()==0 || recipeIngredients.getText().toString().trim().length()==0 || recipeDirections.getText().toString().trim().length()==0 )
                {
                    Toast toast = Toast.makeText(getActivity(),"Enter All Details",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    AddRecipe();
                }
            }
        });

    }


    private void AddRecipe() {


        String rName = recipeName.getText().toString().trim();
        String cName = chefName.getText().toString().trim();
        String rType = recipeType.getText().toString().trim();
        String rDuration = recipeDuration.getText().toString().trim();
        String country = countryName.getText().toString().trim();
        String rIngredients = recipeIngredients.getText().toString().trim();
        String rDirections = recipeDirections.getText().toString().trim();

        RecipeInfo recipeInfo = new RecipeInfo(rName,cName,rType,rDuration,country,rIngredients,rDirections);


        db.collection("User Recipe Details")
                .document(Objects.requireNonNull(firebaseAuth.getUid()))
                .collection("Recipe List")
                .document(""+rName)
                .set(recipeInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"Data Sent",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Error!",Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity().getApplicationContext(),"Something Wrong!",Toast.LENGTH_LONG).show();

                    }
                });


    }
}