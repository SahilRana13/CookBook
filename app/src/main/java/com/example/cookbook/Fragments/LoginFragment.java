package com.example.cookbook.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookbook.DashboardActivity;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.UserInfo;
import com.example.cookbook.R;
import com.example.cookbook.SplashScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class LoginFragment extends Fragment {

    private ImageView imageView1;
    private TextView textForgotPassword,textSignup;
    private Animation animation1,animation2,animation3;
    private Button loginButton,googleSignInButton;
    private EditText pwd,emailId;
    private TextView v1,iv1;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;




    private NavController navController;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Login");

        imageView1 = view.findViewById(R.id.iconLogin);
        loginButton = view.findViewById(R.id.MainLoginBtn);
        googleSignInButton = view.findViewById(R.id.google_signIn);
        textSignup = view.findViewById(R.id.signUpLogin);
        textForgotPassword = view.findViewById(R.id.forgotPasswordLogin);
        pwd = view.findViewById(R.id.enterPasswordLogin);
        emailId = view.findViewById(R.id.enterEmailLogin);
        v1 = view.findViewById(R.id.visible);
        iv1 = view.findViewById(R.id.notvisible);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();


        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment1);


        animation1 = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomin);
        animation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.lefttoright);
        animation3 = AnimationUtils.loadAnimation(getActivity(),R.anim.righttoleft);


        createGoogleRequest();

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                v1.setVisibility(View.INVISIBLE);
                iv1.setVisibility(View.VISIBLE);
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iv1.setVisibility(View.INVISIBLE);
                v1.setVisibility(View.VISIBLE);
            }
        });


        imageView1.startAnimation(animation1);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (emailId.getText().toString().trim().length() == 0)
                {
                    emailId.setError("Email Id Required");
                    Toast toast = Toast.makeText(getActivity(),"Enter Email",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else if (pwd.getText().toString().trim().length() == 0)
                {
                    emailId.setError(null);
                    pwd.setError("Password Required");
                    Toast toast = Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    emailId.setError(null);
                    pwd.setError(null);

                    String email = emailId.getText().toString().trim();
                    String password = pwd.getText().toString().trim();


                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                checkEmailVerification();

                            }
                            else
                            {

                                Toast toast = Toast.makeText(getActivity(),"Enter Valid Email and Password",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }

                        }
                    });

                }
            }
        });

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.signupFragment);
            }
        });


        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.forgotPasswordFragment);
            }
        });

    }


    private void checkEmailVerification() {


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag)
        {

            Toast.makeText(getActivity(), "Login SuccessFull", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(),DashboardActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getActivity(), "Please verify your Email", Toast.LENGTH_SHORT).show();
        }

    }


    private void createGoogleRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    sendData();



                } else {
                    Toast.makeText(getActivity(), "Sorry auth failed.", Toast.LENGTH_SHORT).show();


                }

            }
        });

    }

    private void sendData()
    {


        String name = null;
        String email = null;
        Uri imagePath = null;

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());

        if(signInAccount != null){

            name = signInAccount.getDisplayName();
            email = signInAccount.getEmail();
            imagePath = signInAccount.getPhotoUrl();
        }

        StorageReference ref = storageReference.child("User Profile Images").child(firebaseAuth.getUid());

        ref.putFile(imagePath)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {

                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        Toast toast = Toast.makeText(getActivity(),"Upload Failed",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                    }
                });



        UserInfo obj1 = new UserInfo(name,email);

        db.collection("User Profile Information")
                .document(firebaseAuth.getUid())
                .set(obj1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getContext(),DashboardActivity.class);
                            startActivity(intent);

                        }else
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"FireStore Error!",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                });



    }
}
