package com.example.cookbook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    Context context;
    ArrayList<RecipeInfo> mList;

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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView recipeTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.customTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RecipeInfo info = new RecipeInfo();
                    Toast.makeText(v.getContext(), info.getRecipeName()+" -> " + (getAdapterPosition()+1), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
