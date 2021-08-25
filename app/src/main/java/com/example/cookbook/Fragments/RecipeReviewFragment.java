package com.example.cookbook.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class RecipeReviewFragment extends Fragment {

    RatingBar ratingBar;
    Button submitButton;
    float rateValue;
    private String rName;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private ProgressDialog progressDialog;



    public RecipeReviewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_review, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submitRecipeReviewBtn);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rName = this.getArguments().getString("key");


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                rateValue = ratingBar.getRating();
                if(rateValue<=1 && rateValue>0)
                    //ratecount.setText("Bad"+ rateValue+ "/5");
                    Toast.makeText(getActivity(), "Bad-"+rateValue, Toast.LENGTH_SHORT).show();
                else if(rateValue<=2 && rateValue>1)
                    //ratecount.setText("OK"+ rateValue+ "/5");
                    Toast.makeText(getActivity(), "Ok-"+rateValue, Toast.LENGTH_SHORT).show();
                else if(rateValue<=3 && rateValue>2)
                    //ratecount.setText("GOOD"+ rateValue+ "/5");
                    Toast.makeText(getActivity(), "Good-"+rateValue, Toast.LENGTH_SHORT).show();
                else if(rateValue<=4 && rateValue>3)
                    //ratecount.setText("VERY GOOD"+ rateValue+ "/5");
                    Toast.makeText(getActivity(), "Very Good-"+rateValue, Toast.LENGTH_SHORT).show();
                else if(rateValue<=5 && rateValue>4)
                    //ratecount.setText("BEST"+ rateValue+ "/5");
                    Toast.makeText(getActivity(), "Best-"+rateValue, Toast.LENGTH_SHORT).show();
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference().child("User Recipe Ratings");


                DatabaseReference childBranch = reference
                        .child(rName)
                        .child(firebaseAuth.getUid());

                String string = String.valueOf(rateValue);

                RecipeInfo recipeInfo = new RecipeInfo(string);


                recipeInfo.setRecipeName(rName);
                recipeInfo.setRecipeRatings(string);

                childBranch.setValue(recipeInfo);
                progressDialog.dismiss();

            }
        });
    }
}