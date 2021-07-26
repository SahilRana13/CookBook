package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Fragments.DiscoverFragment;
import com.example.cookbook.Fragments.MyRecipeDetailsFragment;
import com.example.cookbook.Fragments.MyRecipeFragment;
import com.example.cookbook.Fragments.RecipeDetailsFragment;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.example.cookbook.RecipeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyRecipeListAdapter extends RecyclerView.Adapter<MyRecipeListAdapter.MyViewHolder> {

    Context context;
    ArrayList<RecipeInfo> myList;
    public String recipetext;
    String recipeImage;
    public RecipeInfo model;

    //Context myContext = exampleViewHolder.itemView.getContext()


    public MyRecipeListAdapter() {
    }

    public MyRecipeListAdapter(Context context, ArrayList<RecipeInfo> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_my_recipe_list,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        model = myList.get(position);





        holder.recipeTitle.setText(model.getRecipeName());
        Picasso.get().load(model.getRecipeImageLink()).into(holder.recipeImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recipetext = myList.get(position).getRecipeName();

                Fragment fragment = new MyRecipeDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key",recipetext);
                fragment.setArguments(bundle);
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Host_Fragment2, fragment);
                ft.commit();

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                recipetext = myList.get(position).getRecipeName();



                return true;
            }
        });



    }




    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView recipeTitle;
        ImageView recipeImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.myRecipeName);
            recipeImage = itemView.findViewById(R.id.myRecipeImageView);


        }


    }
}
