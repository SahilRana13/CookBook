package com.example.cookbook.Fragments;

import android.content.Intent;
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
import com.example.cookbook.R;
import com.example.cookbook.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private ImageView imageView1;
    private TextView textForgotPassword,textSignup;
    private Animation animation1,animation2,animation3;
    private Button loginButton;
    private EditText pwd,emailId;
    private TextView v1,iv1;
    private FirebaseAuth firebaseAuth;


    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
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
        textSignup = view.findViewById(R.id.signUpLogin);
        textForgotPassword = view.findViewById(R.id.forgotPasswordLogin);
        pwd = view.findViewById(R.id.enterPasswordLogin);
        emailId = view.findViewById(R.id.enterEmailLogin);
        v1 = view.findViewById(R.id.visible);
        iv1 = view.findViewById(R.id.notvisible);


        firebaseAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment1);


        animation1 = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomin);
        animation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.lefttoright);
        animation3 = AnimationUtils.loadAnimation(getActivity(),R.anim.righttoleft);



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
}
