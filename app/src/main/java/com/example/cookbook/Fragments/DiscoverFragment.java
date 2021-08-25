package com.example.cookbook.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference root;
    private FirebaseAuth firebaseAuth;
    private RecipeListAdapter adapter;
    private ArrayList<RecipeInfo> list;
    private RecipeInfo model,model_1;
    private int count = 1;
    private Button btnAll, btnBreakfast, btnBrunch, btnLunch, btnDinner, btnSnacks, btnAppetisers, btnDesserts, btnBaking, btnDrinks, btnOther;

    private NavController navController;
    private TextView searchButton,nullText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.DiscoverRecipes);

        navController = Navigation.findNavController(getActivity(),R.id.Host_Fragment2);
        searchButton = view.findViewById(R.id.button_search_discover);
        nullText = view.findViewById(R.id.nullText);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.show();
        progressDialog.setContentView(R.layout.progressdialog);

        btnAll = view.findViewById(R.id.r1);
        btnBreakfast = view.findViewById(R.id.r2);
        btnBrunch = view.findViewById(R.id.r3);
        btnLunch = view.findViewById(R.id.r4);
        btnDinner = view.findViewById(R.id.r5);
        btnSnacks = view.findViewById(R.id.r6);
        btnAppetisers = view.findViewById(R.id.r7);
        btnDesserts = view.findViewById(R.id.r8);
        btnBaking = view.findViewById(R.id.r9);
        btnDrinks = view.findViewById(R.id.r10);
        btnOther = view.findViewById(R.id.r11);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        
        Drawable drawable = getResources().getDrawable(R.drawable.button_background);

        recyclerView = view.findViewById(R.id.recipeRecyclerView);
        db = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        Log.e(firebaseAuth.getCurrentUser().getEmail(),"--(Admin)--");

        if (firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("tjp083@gmail.com"))
        {
            new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        }



        swipeRefreshLayout.setOnRefreshListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        adapter = new RecipeListAdapter(getContext(),list);


        //Sahil's
        mref = FirebaseDatabase.getInstance().getReference("recipe_chef's_names");
        //recipeList = view.findViewById(R.id.autocomplete_search_list);
        autoCompleteTextView = view.findViewById(R.id.homeSearchBar);
        //databasebutton = view.findViewById(R.id.database_button);

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


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (autoCompleteTextView.getText().toString().trim().length()==0)
                {
                    Toast toast = Toast.makeText(getActivity(),"Enter Recipe/Chef Name",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("recipe",autoCompleteTextView.getText().toString());
                    SearchPageFragment fragment = new SearchPageFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Host_Fragment2,fragment).commit();


                }
            }
        });

        //

        btnBreakfast.setBackground(null);
        btnAppetisers.setBackground(null);
        btnBaking.setBackground(null);
        btnBrunch.setBackground(null);
        btnDesserts.setBackground(null);
        btnDinner.setBackground(null);
        btnDrinks.setBackground(null);
        btnLunch.setBackground(null);
        btnOther.setBackground(null);
        btnSnacks.setBackground(null);

        btnBreakfast.setTextColor(Color.parseColor("#193566"));
        btnAppetisers.setTextColor(Color.parseColor("#193566"));
        btnBaking.setTextColor(Color.parseColor("#193566"));
        btnBrunch.setTextColor(Color.parseColor("#193566"));
        btnDesserts.setTextColor(Color.parseColor("#193566"));
        btnDinner.setTextColor(Color.parseColor("#193566"));
        btnDrinks.setTextColor(Color.parseColor("#193566"));
        btnLunch.setTextColor(Color.parseColor("#193566"));
        btnOther.setTextColor(Color.parseColor("#193566"));
        btnSnacks.setTextColor(Color.parseColor("#193566"));

        getRecipeData();


        btnAll.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                btnAll.setBackground(drawable);
                btnAll.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));


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
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnBreakfast.setBackground(drawable);
                btnBreakfast.setTextColor(Color.parseColor("#ECF0F3"));

                btnAll.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnAll.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("breakfast"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnBrunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnBrunch.setBackground(drawable);
                btnBrunch.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnAll.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("brunch"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);


                list.clear();
                adapter.notifyDataSetChanged();

                btnLunch.setBackground(drawable);
                btnLunch.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnAll.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("lunch"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnDinner.setBackground(drawable);
                btnDinner.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnAll.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("dinner"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);


                list.clear();
                adapter.notifyDataSetChanged();

                btnSnacks.setBackground(drawable);
                btnSnacks.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnAll.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("snacks"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnAppetisers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);


                list.clear();
                adapter.notifyDataSetChanged();

                btnAppetisers.setBackground(drawable);
                btnAppetisers.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAll.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("Appetisers"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnDesserts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnDesserts.setBackground(drawable);
                btnDesserts.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnAll.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("desserts"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnBaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnBaking.setBackground(drawable);
                btnBaking.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnAll.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("baking"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);


                list.clear();
                adapter.notifyDataSetChanged();

                btnDrinks.setBackground(drawable);
                btnDrinks.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnAll.setBackground(null);
                btnLunch.setBackground(null);
                btnOther.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnOther.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("Drinks"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                list.clear();
                adapter.notifyDataSetChanged();

                btnOther.setBackground(drawable);
                btnOther.setTextColor(Color.parseColor("#ECF0F3"));

                btnBreakfast.setBackground(null);
                btnAppetisers.setBackground(null);
                btnBaking.setBackground(null);
                btnBrunch.setBackground(null);
                btnDesserts.setBackground(null);
                btnDinner.setBackground(null);
                btnDrinks.setBackground(null);
                btnLunch.setBackground(null);
                btnAll.setBackground(null);
                btnSnacks.setBackground(null);

                btnBreakfast.setTextColor(Color.parseColor("#193566"));
                btnAppetisers.setTextColor(Color.parseColor("#193566"));
                btnBaking.setTextColor(Color.parseColor("#193566"));
                btnBrunch.setTextColor(Color.parseColor("#193566"));
                btnDesserts.setTextColor(Color.parseColor("#193566"));
                btnDinner.setTextColor(Color.parseColor("#193566"));
                btnDrinks.setTextColor(Color.parseColor("#193566"));
                btnLunch.setTextColor(Color.parseColor("#193566"));
                btnAll.setTextColor(Color.parseColor("#193566"));
                btnSnacks.setTextColor(Color.parseColor("#193566"));

                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {


                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                if (ds.child("recipeType").getValue().toString().equalsIgnoreCase("Other"))
                                {

                                    model = ds.getValue(RecipeInfo.class);
                                    list.add(model);
                                }

                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                        if (list.size() == 0)
                        {
                            nullText.setVisibility(View.VISIBLE);
                        }else
                        {
                            nullText.setVisibility(View.INVISIBLE);
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
        });



    }

    private void getRecipeData() {

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
                    progressDialog.dismiss();

                    swipeRefreshLayout.setRefreshing(false);
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

                        Toast.makeText(getActivity(), recipeInfo.getRecipeName(), Toast.LENGTH_SHORT).show();
                        DatabaseReference databaseReference1 = db.getReference();

                        DatabaseReference recipelistbranch1 = databaseReference1.child("User Recipe Details");

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
            autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = autoCompleteTextView.getText().toString();
                    search(name);
                }
            });
        }
        else
        {
            Log.d("Recipe or Chef's Names", "Data not found");
        }
    }

    private void search(String name) {

        Query query = mref.orderByChild("rname").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRefresh() {

        list.clear();
        adapter.notifyDataSetChanged();
        getRecipeData();
    }
}