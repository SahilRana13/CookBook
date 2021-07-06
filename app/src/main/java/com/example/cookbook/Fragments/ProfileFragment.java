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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        getActivity().setTitle("Profile");

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

    }

    private void getData() {

        currentUser = firebaseAuth.getCurrentUser();

        DocumentReference docRef = db.collection("User Profile Information").document(currentUser.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    String resultEmail = documentSnapshot.getString("email");
                    String resultName = documentSnapshot.getString("name");

                    email.setText(resultEmail);
                    editName.setText(resultName);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImage() {

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
    }

    private void updateData() {

        String name = editName.getText().toString().trim();

        UserInfo userInfo = new UserInfo(name);



        db.collection("User Profile Information")
                .document(firebaseAuth.getUid())
                .update("name",name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        navController.navigate(R.id.profileFragment);
                        Toast toast = Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                       // progressDialog.dismiss();
                        //getActivity().recreate();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast toast = Toast.makeText(getActivity(),"Error : "+e.getMessage(),Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        //progressDialog.dismiss();
                    }
                });
    }


}
