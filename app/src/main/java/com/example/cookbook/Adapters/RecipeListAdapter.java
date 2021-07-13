package com.example.cookbook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Fragments.RecipeDetailsFragment;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.example.cookbook.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    Context context;
    ArrayList<RecipeInfo> mList;
    String recipetext;
    String recipeImage;


    public RecipeListAdapter(Context context, ArrayList<RecipeInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_grid_layout,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecipeInfo model = mList.get(position);

        holder.recipeTitle.setText(model.getRecipeName());
        Picasso.get().load(model.getRecipeImageLink()).into(holder.recipeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recipetext = model.getRecipeName();

                Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                intent.putExtra("recipekey",recipetext);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView recipeTitle;
        ImageView recipeImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.customTextView);
            recipeImage = itemView.findViewById(R.id.customImageView);

        }


    }
}
