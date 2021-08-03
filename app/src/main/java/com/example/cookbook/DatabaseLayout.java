package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cookbook.Models.RecipeSearchInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseLayout extends AppCompatActivity {

    DatabaseReference mref;
    private ListView listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_layout);

        listdata = (ListView)findViewById(R.id.database_layout_list_view);
        mref = FirebaseDatabase.getInstance().getReference("recipe_chef's_names");

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                readData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref.addListenerForSingleValueEvent(event);
    }

    private void readData(DataSnapshot snapshot) {

        if (snapshot.exists())
        {
            ArrayList<String> names = new ArrayList<>();
            for (DataSnapshot ds : snapshot.getChildren())
            {
                RecipeSearchInfo recipeSearchInfo = new RecipeSearchInfo(ds.child("rName").getValue(String.class),
                        ds.child("chefName").getValue(String.class));
                names.add(recipeSearchInfo.getrName() + "\n" + recipeSearchInfo.getChefName());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
            listdata.setAdapter(adapter);
        }
        else
        {
            Log.d("Recipe or Chef's name", "Data not found");
        }
    }
}