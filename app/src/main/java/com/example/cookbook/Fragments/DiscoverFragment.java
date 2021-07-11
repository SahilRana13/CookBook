package com.example.cookbook.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.MainActivity;
import com.example.cookbook.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends Fragment {

    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    RecipeAdapter adapter;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Discover");

        dataList = view.findViewById(R.id.recipeRecyclerView);

        titles = new ArrayList<>();
        images = new ArrayList<>();


        titles.add("1");
        titles.add("2");
        titles.add("3");
        titles.add("4");
        titles.add("5");
        titles.add("6");
        titles.add("7");
        titles.add("8");
        titles.add("9");
        titles.add("10");
        titles.add("11");
        titles.add("12");
        titles.add("13");
        titles.add("14");
        titles.add("15");
        titles.add("16");


        images.add(R.drawable.ic_favorite_black_24dp);
        images.add(R.drawable.ic_format_list_bulleted_black_24dp);
        images.add(R.drawable.ic_power_settings_new_black_24dp);
        images.add(R.drawable.ic_perm_identity_black_24dp);
        images.add(R.drawable.ic_favorite_black_24dp);
        images.add(R.drawable.ic_format_list_bulleted_black_24dp);
        images.add(R.drawable.ic_power_settings_new_black_24dp);
        images.add(R.drawable.ic_perm_identity_black_24dp);
        images.add(R.drawable.ic_favorite_black_24dp);
        images.add(R.drawable.ic_format_list_bulleted_black_24dp);
        images.add(R.drawable.ic_power_settings_new_black_24dp);
        images.add(R.drawable.ic_perm_identity_black_24dp);
        images.add(R.drawable.ic_favorite_black_24dp);
        images.add(R.drawable.ic_format_list_bulleted_black_24dp);
        images.add(R.drawable.ic_power_settings_new_black_24dp);
        images.add(R.drawable.ic_perm_identity_black_24dp);

        adapter = new RecipeAdapter(getContext(),titles,images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);



    }
}