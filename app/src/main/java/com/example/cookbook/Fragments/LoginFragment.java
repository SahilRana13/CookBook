package com.example.cookbook.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookbook.DashboardActivity;
import com.example.cookbook.MainActivity;
import com.example.cookbook.R;
import com.example.cookbook.SplashScreen;


public class LoginFragment extends Fragment {

    private ImageView imageView1;
    private TextView textView1,textView2,textForgotPassword,textSignup;
    private Animation animation1,animation2,animation3;
    private Button loginButton;

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

        animation1 = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomin);
        animation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.lefttoright);
        animation3 = AnimationUtils.loadAnimation(getActivity(),R.anim.righttoleft);




        imageView1.startAnimation(animation1);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}
