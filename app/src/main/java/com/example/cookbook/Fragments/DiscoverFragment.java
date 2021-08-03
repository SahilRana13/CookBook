package com.example.cookbook.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cookbook.Adapters.RecipeListAdapter;
import com.example.cookbook.DatabaseLayout;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DiscoverFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference root;
    private FirebaseAuth firebaseAuth;
    private RecipeListAdapter adapter;
    private ArrayList<RecipeInfo> list;
    private RecipeInfo model,model_1;
    private int count = 1;


    //Sahil's
    private Button databasebutton;
    DatabaseReference mref;
    private ListView recipeList;
    private AutoCompleteTextView autoCompleteTextView;


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

        getActivity().setTitle("Discover Recipes");

        recyclerView = view.findViewById(R.id.recipeRecyclerView);
        db = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        adapter = new RecipeListAdapter(getContext(),list);

        //Sahil's
        mref = FirebaseDatabase.getInstance().getReference("recipe_chef's_names");
        recipeList = view.findViewById(R.id.autocomplete_search_list);
        autoCompleteTextView = view.findViewById(R.id.homeSearchBar);
        databasebutton = view.findViewById(R.id.database_button);



        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref.addListenerForSingleValueEvent(event);



//        databasebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), DatabaseLayout.class);
//                startActivity(intent);
//                Toast.makeText(getContext(), "Database Layout", Toast.LENGTH_SHORT).show();
//            }
//        });

        //


        root = db.getReference().child("User Recipe Details");

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {


                    for (DataSnapshot ds: dataSnapshot.getChildren()) {

                        model = ds.getValue(RecipeInfo.class);
                        list.add(model);

                    }

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());

            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete?");

            adb.setNegativeButton("No", new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {


                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());


                }});
            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                        RecipeInfo recipeInfo;

                        recipeInfo = list.get(viewHolder.getPosition());
                        DatabaseReference databaseReference1 = db.getReference();

                        DatabaseReference recipelistbranch1 = databaseReference1.child("User Recipe Details")
                                .child(firebaseAuth.getUid())
                                .child(recipeInfo.getRecipeName());

                        DatabaseReference recipelistbranch2 = databaseReference1.child("User Recipe Images")
                                .child(firebaseAuth.getUid())
                                .child(recipeInfo.getRecipeName());

                        recipelistbranch1.removeValue();
                        recipelistbranch2.removeValue();

                        list.remove(viewHolder.getAdapterPosition());
                        adapter.notifyDataSetChanged();




                }});


            adb.show();




        }
    };

    private void populateSearch(DataSnapshot snapshot) {

        ArrayList<String> nameList = new ArrayList<>();

        if (snapshot.exists())
        {
            for (DataSnapshot ds:snapshot.getChildren())
            {
                String name = ds.child("rName").getValue(String.class);
                String cname = ds.child("chefName").getValue(String.class);
                nameList.add(name);
                nameList.add(cname);
            }
            ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,nameList);
            autoCompleteTextView.setAdapter(adapter);
        }
        else
        {
            Log.d("Recipe or Chef's Names", "Data not found");
        }
    }
}