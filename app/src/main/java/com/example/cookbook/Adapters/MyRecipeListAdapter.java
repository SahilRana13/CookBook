package com.example.cookbook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.example.cookbook.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecipeListAdapter extends RecyclerView.Adapter<MyRecipeListAdapter.MyViewHolder> {

    Context context;
    ArrayList<RecipeInfo> myList;
    String recipetext;
    String recipeImage;


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

        RecipeInfo model = myList.get(position);

        holder.recipeTitle.setText(model.getRecipeName());
        Picasso.get().load(model.getRecipeImageLink()).into(holder.recipeImage);
        //holder.recipeImage.setImageResource(Integer.parseInt(model.getRecipeImageLink()));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                recipetext = model.getRecipeName();
//
//                //Toast.makeText(v.getContext(), recipetext+" -> " + (position+1), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(v.getContext(), RecipeActivity.class);
//                intent.putExtra("recipekey",recipetext);
//                context.startActivity(intent);
//
//            }
//        });
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
