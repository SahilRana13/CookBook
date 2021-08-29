package com.example.cookbook.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private ProgressDialog progressDialog;

    private String fileName;


    ImageButton arrow,arrow2,arrow3;
    LinearLayout hiddenView,hiddenView2,hiddenView3;
    CardView cardView,cardView2,cardView3;


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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.show();
        progressDialog.setContentView(R.layout.progressdialog);

        cardView = view.findViewById(R.id.base_cardview);
        arrow = view.findViewById(R.id.arrow_button);
        hiddenView = view.findViewById(R.id.hidden_view);

        cardView2 = view.findViewById(R.id.base_cardview2);
        arrow2 = view.findViewById(R.id.arrow_button2);
        hiddenView2 = view.findViewById(R.id.hidden_view2);

        cardView3 = view.findViewById(R.id.base_cardview3);
        arrow3 = view.findViewById(R.id.arrow_button3);
        hiddenView3 = view.findViewById(R.id.hidden_view3);


        fileName = this.getArguments().getString("key");

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (hiddenView.getVisibility() == View.VISIBLE) {

                   TransitionManager.beginDelayedTransition(cardView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }
               else {

                    TransitionManager.beginDelayedTransition(cardView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });

        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hiddenView2.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.GONE);
                    arrow2.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }
                else {

                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.VISIBLE);
                    arrow2.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });

        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hiddenView3.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(cardView3,
                            new AutoTransition());
                    hiddenView3.setVisibility(View.GONE);
                    arrow3.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }
                else {

                    TransitionManager.beginDelayedTransition(cardView3,
                            new AutoTransition());
                    hiddenView3.setVisibility(View.VISIBLE);
                    arrow3.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
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


                            recipeIngredients.setText(s1.toString().trim());
                            recipeDirections.setText(s2.toString().trim());

                            progressDialog.dismiss();
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