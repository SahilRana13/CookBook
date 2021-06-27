package com.example.cookbook.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.cookbook.MainActivity;
import com.example.cookbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SignupFragment extends Fragment {

    private Button btnSignup;
    private TextView txtLogin;
    private FirebaseAuth firebaseAuth;


    private EditText user_pwd1,user_pwd2,user_name,user_email;
    private TextView v1,v2,iv1,iv2;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Signup");

        btnSignup = view.findViewById(R.id.CreateBtn);
        txtLogin = view.findViewById(R.id.LoginText);

        user_email = view.findViewById(R.id.CreateEmail);
        user_name = view.findViewById(R.id.CreateName);
        user_pwd1 = view.findViewById(R.id.CreatePassword);
        user_pwd2 = view.findViewById(R.id.CreateRePassword);

        v1 = view.findViewById(R.id.visible1);
        iv1 = view.findViewById(R.id.notvisible1);
        v2 = view.findViewById(R.id.visible2);
        iv2 = view.findViewById(R.id.notvisible2);

        FirebaseApp.initializeApp(getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        View.OnClickListener navigate1 = Navigation.createNavigateOnClickListener(R.id.loginFragment);



        txtLogin.setOnClickListener(navigate1);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_name.getText().toString().trim().length()==0 || user_email.getText().toString().trim().length()==0 || user_pwd1.getText().toString().trim().length()==0 || user_pwd2.getText().toString().trim().length()==0)
                {
                    Toast toast = Toast.makeText(getActivity(),"Enter All Details",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else if (user_pwd1.getText().toString().trim().equals(user_pwd2.getText().toString().trim()))
                {
                    String name = user_name.getText().toString().trim();
                    String email=user_email.getText().toString().trim();
                    String password = user_pwd1.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                sendEmailVerification();

                            }

                            else
                            {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                    Toast toast = Toast.makeText(getActivity(),"User with this email already exist.",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                }
                                else
                                {
                                    Toast toast = Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                }

                            }
                        }
                    });



                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(),"Password not matched",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            }
        });


    }


    private void sendEmailVerification() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        //sendData();

                        Toast.makeText(getActivity().getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }

                    else
                    {
                        Toast toast = Toast.makeText(getActivity(),"Verification mail has not been sent",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }
            });

        }


    }
}
