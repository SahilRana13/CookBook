package com.example.cookbook.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.Adapters.MyRecipeListAdapter;
import com.example.cookbook.Adapters.RecipeListAdapter;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.ArrayList;


public class MyRecipeFragment extends Fragment {

    private RecyclerView recyclerView;

    RecipeInfo obj;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore db;

    private MyRecipeListAdapter newAdapter;
    private ArrayList<RecipeInfo> list;
    private RecipeInfo model;



    private ImageView myImage;
    private TextView myName;

    private AutoCompleteTextView autoCompleteTextView;
    private ListView recipeList;
    DatabaseReference mref;
    public String recipe;
    private EditText myRecipeSearch;


    public MyRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_recipe, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Recipes");


        obj = new RecipeInfo();

        myImage = view.findViewById(R.id.myImageView);
        myName = view.findViewById(R.id.myTextView);

        recyclerView = view.findViewById(R.id.myRecipeListView);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        list = new ArrayList<>();
        newAdapter = new MyRecipeListAdapter(getContext(),list);

        getProfileData();
        getProfileImage();

        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference recipelistbranch = databaseReference.child("User Recipe Details").child(firebaseAuth.getUid());

        recipelistbranch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                for (DataSnapshot ds: snapshot.getChildren())
                {
                    model = ds.getValue(RecipeInfo.class);
                    list.add(model);
                }

                recyclerView.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();

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

        /*autoCompleteTextView = view.findViewById(R.id.mySearchBarView);
        recipeList = view.findViewById(R.id.myRecipe_list);*//*
        recipe = autoCompleteTextView.getText().toString();*/
        mref = FirebaseDatabase.getInstance().getReference("User Recipe Details").child(firebaseAuth.getUid());
        /*myRecipeSearch = view.findViewById(R.id.mySearchBarView);*/

        /*ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref.addListenerForSingleValueEvent(event);*/
    }

    /*private void populateSearch(DataSnapshot snapshot) {

        ArrayList<String> nameList = new ArrayList<>();

        if (snapshot.exists())
        {
            for (DataSnapshot ds:snapshot.getChildren())
            {
                for (DataSnapshot ds1:ds.getChildren())
                {
                    for (DataSnapshot ds2:ds1.getChildren())
                    {
                        String name = ds2.child("recipeName").getValue(String.class);
                        nameList.add(name);
                    }
                }

            }
            ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,nameList);
            autoCompleteTextView.setAdapter(adapter);
        }
        else
        {
            Log.d("Recipe or Chef's Names", "Data not found");
        }
    }*/

    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());

            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete?");

            adb.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {


                    newAdapter.notifyItemChanged(viewHolder.getAdapterPosition());


                }});
            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    RecipeInfo recipeInfo;

                    recipeInfo = list.get(viewHolder.getPosition());
                    DatabaseReference databaseReference1 = firebaseDatabase.getReference();

                    DatabaseReference recipelistbranch1 = databaseReference1.child("User Recipe Details")
                            .child(firebaseAuth.getUid())
                            .child(recipeInfo.getRecipeName());

                    DatabaseReference recipelistbranch2 = databaseReference1.child("User Recipe Images")
                            .child(firebaseAuth.getUid())
                            .child(recipeInfo.getRecipeName());

                    recipelistbranch1.removeValue();
                    recipelistbranch2.removeValue();

                    list.remove(viewHolder.getAdapterPosition());
                    newAdapter.notifyDataSetChanged();




                }});


            adb.show();




        }
    };

    private void getProfileData() {

        currentUser = firebaseAuth.getCurrentUser();

        DocumentReference docRef = db.collection("User Profile Information").document(currentUser.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    String sName = documentSnapshot.getString("name");
                    myName.setText(sName);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getProfileImage() {


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        if(signInAccount != null){

            Picasso.get().load(signInAccount.getPhotoUrl()).into(myImage);
        }


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("User Profile Images")
                .child(firebaseAuth.getUid())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(myImage);

                    }
                });

    }

}