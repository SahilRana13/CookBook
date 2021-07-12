package com.example.cookbook.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyRecipeFragment extends Fragment {

    ListView listView;
    ArrayList<String> arList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    RecipeInfo obj;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore db;


    private ImageView myImage;
    private TextView myName;


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

        myImage = view.findViewById(R.id.myImageView);
        myName = view.findViewById(R.id.myTextView);

        listView = view.findViewById(R.id.myRecipeListView);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getProfileData();
        getProfileImage();


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

        //myImage.setBackground(null);
    }
}