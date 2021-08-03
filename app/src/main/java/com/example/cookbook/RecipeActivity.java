package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookbook.Fragments.RecipeDetailsFragment;
import com.example.cookbook.Fragments.RecipeReviewFragment;
import com.example.cookbook.Models.RecipeInfo;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageSlider imageView;
    private String str;
    private List<SlideModel> images;
    private RatingBar getRatingBar;
    private TextView userCount;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        tabLayout = findViewById(R.id.recipeTabLayout);
        viewPager = findViewById(R.id.recipeViewPager);
        imageView = findViewById(R.id.recipeImageView);
        getRatingBar = findViewById(R.id.avgRatingBar);
        userCount = findViewById(R.id.numOfUsers);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        images = new ArrayList<>();

        tabLayout.addTab(tabLayout.newTab().setText("Recipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Review"));


        str = getIntent().getExtras().get("recipekey").toString();



        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                switch (position)
                {
                    case 0:
                        RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", str);
                        detailsFragment.setArguments(bundle);
                        return detailsFragment;

                    case 1:
                        RecipeReviewFragment reviewFragment = new RecipeReviewFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("key", str);
                        reviewFragment.setArguments(bundle1);
                        return reviewFragment;

                    default:
                        return null;


                }

            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getImageData();
        getRatingData();
    }


    private void getRatingData() {




        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();

        DatabaseReference childreference = databaseReference.child("User Recipe Ratings").child(str);


        childreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;

                for (DataSnapshot ds: snapshot.getChildren())
                {

                    double rating = Double.parseDouble(ds.child("recipeRatings").getValue().toString());
                    total = total + rating;
                    count = count + 1.0;
                    average = total / count;

//                    if (ds.getKey().equals("recipeRatings"))
//                    {
//
//                        arrayList.add(ds.getValue().toString());
//
//
//                    }



                }


                Toast.makeText(RecipeActivity.this, String.valueOf(average), Toast.LENGTH_SHORT).show();



                getRatingBar.setRating(Float.valueOf(String.valueOf(average)));


                int value = (int)Math.round(count);
                userCount.setText("("+value+")");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getImageData() {




        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();

        DatabaseReference childreference = databaseReference.child("User Recipe Images");

        childreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    for (DataSnapshot ds1: ds.getChildren())
                    {


                        DataSnapshot name = ds1.child("recipeName");

                        String area_value = name.getValue().toString();
                        if (area_value.contains(str)) {

                            images.add(new SlideModel(ds1.child("recipeImageLink").getValue().toString(),ScaleTypes.FIT));
                            imageView.setImageList(images,ScaleTypes.FIT);

                        }
                    }

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}