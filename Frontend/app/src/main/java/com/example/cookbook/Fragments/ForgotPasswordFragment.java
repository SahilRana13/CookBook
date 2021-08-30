package com.example.cookbook.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.example.cookbook.MainActivity;
import com.example.cookbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragment extends Fragment {

    private Button btnSubmit;
    private EditText forgotpwd;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Forgot Password?");

        forgotpwd = view.findViewById(R.id.ForgotEmail);
        btnSubmit = view.findViewById(R.id.ForgotSubmitBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                String email = forgotpwd.getText().toString().trim();



                if(email.length() == 0)
                {
                    Toast toast = Toast.makeText(getActivity(),R.string.Please_Enter_Your_Email,Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    progressDialog.dismiss();
                }
                else
                {


                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {

                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(getActivity(),R.string.Password_Reset_Email_sent,Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                                toast.show();

                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(getActivity(),R.string.Enter_Registered_Email, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL,0,0);
                                toast.show();

                            }
                        }
                    });

                }

            }
        });

    }
}
