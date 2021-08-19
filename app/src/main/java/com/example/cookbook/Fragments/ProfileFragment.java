package com.example.cookbook.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.Models.UserInfo;
import com.example.cookbook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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


public class ProfileFragment extends Fragment {

    private TextView editTool;
    private Button saveButton;
    private EditText email,editName;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ImageView dp;

    private NavController navController;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.Profile);

        dp = view.findViewById(R.id.ProfileImage);
        editTool = view.findViewById(R.id.editNameTool);
        saveButton = view.findViewById(R.id.SaveBtn);
        editName = view.findViewById(R.id.EditProfileName);
        email = view.findViewById(R.id.ProfileEmail);
        db = FirebaseFirestore.getInstance();

        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment2);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        editName.setEnabled(false);
        email.setEnabled(false);

        getData();
        getImage();

        editTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editName.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData();
                editName.setEnabled(false);
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.expandProfilePictureFragment);
            }
        });

    }

    private void getData() {

        currentUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("User Profile Details");

        DatabaseReference userDetails = reference.child(firebaseAuth.getUid());

        userDetails.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                email.setText(snapshot.child("email").getValue().toString().trim());
                editName.setText(snapshot.child("name").getValue().toString().trim());
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

//
//        DocumentReference docRef = db.collection("User Profile Information").document(currentUser.getUid());
//
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if (documentSnapshot.exists())
//                {
//                    String resultEmail = documentSnapshot.getString("email");
//                    String resultName = documentSnapshot.getString("name");
//
//                    email.setText(resultEmail);
//                    editName.setText(resultName);
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void getImage() {


//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
//        if(signInAccount != null){
//
//            Picasso.get().load(signInAccount.getPhotoUrl()).into(dp);
//        }


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("User Profile Images")
                .child(firebaseAuth.getUid())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(dp);

                    }
                });

        dp.setBackground(null);
    }

    private void updateData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("User Profile Details");

        DatabaseReference userDetails = reference.child(firebaseAuth.getUid());


        String name = editName.getText().toString().trim();
        String email = firebaseAuth.getCurrentUser().getEmail().toString().trim();

        UserInfo userInfo = new UserInfo(name,email);




        userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userDetails.removeValue();

                userDetails.push().setValue(userInfo);

                Toast toast = Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        userDetails.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                email.setText(snapshot.child("email").getValue().toString().trim());
//                editName.setText(snapshot.child("name").getValue().toString().trim());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        db.collection("User Profile Information")
//                .document(firebaseAuth.getUid())
//                .update("name",name)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                        navController.navigate(R.id.profileFragment);
//                        Toast toast = Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//                        toast.show();
//                       // progressDialog.dismiss();
//                        //getActivity().recreate();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast toast = Toast.makeText(getActivity(),"Error : "+e.getMessage(),Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//                        toast.show();
//                        //progressDialog.dismiss();
//                    }
//                });
    }


}
