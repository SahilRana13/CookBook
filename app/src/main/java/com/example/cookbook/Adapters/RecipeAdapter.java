package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends BaseAdapter {

    ArrayList<RecipeInfo> namelist;

    public RecipeAdapter(ArrayList<RecipeInfo> namelist) {
        this.namelist = namelist;
    }

    @Override
    public int getCount() {
        return namelist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_layout,parent,false);
        TextView name = convertView.findViewById(R.id.customTextView);

        name.setText(namelist.get(position).getRecipeName());

        return convertView;

    }
}
