package com.example.cookbook.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cookbook.Adapters.RecipeListAdapter;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchPageFragment extends Fragment {

    TextView tv;
    RecyclerView recyclerView;
    FirebaseDatabase db;
    DatabaseReference root;
    private FirebaseAuth firebaseAuth;
    private RecipeListAdapter adapter;
    private ArrayList<RecipeInfo> list;
    private RecipeInfo model;
    private int count = 1;
    private int searchNumber =0;

    private ProgressDialog progressDialog;



    public SearchPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Search Page");

        tv = view.findViewById(R.id.text_view_search_page);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.show();
        progressDialog.setContentView(R.layout.progressdialog);

        recyclerView = view.findViewById(R.id.recycle_view_search_page);
        db = FirebaseDatabase.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        adapter = new RecipeListAdapter(getContext(),list);

        String search = null;

        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            search = bundle.getString("recipe");
        }


        root = db.getReference().child("User Recipe Details");

        String finalSearch = search;
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        if(finalSearch.equals(ds.child("recipeName").getValue(String.class)) || finalSearch.equals(ds.child("chefName").getValue(String.class)))
                        {
                            model = ds.getValue(RecipeInfo.class);
                            list.add(model);
                            searchNumber++;
                        }
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                tv.setText(searchNumber+" "+getString(R.string.Search_Results_for)+"\n'"+finalSearch+"'");
                progressDialog.dismiss();
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
}