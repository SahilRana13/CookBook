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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    int i = 1;

    String name1,name2,name3,name4,name5,name6,name7,name8;



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

        tabLayout.addTab(tabLayout.newTab().setText(R.string.recipee));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.REVIEW));


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



        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();

        DatabaseReference childreference = databaseReference.child("User Recipe Details");

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


                            Map<String, Object> newPost = (Map<String, Object>) ds1.getValue();

                            name1 = String.valueOf(newPost.get("chefName"));
                            name2 = String.valueOf(newPost.get("countryName"));
                            name3 = String.valueOf(newPost.get("recipeDirections"));
                            name4 = String.valueOf(newPost.get("recipeDuration"));
                            name5 = String.valueOf(newPost.get("recipeIngredients"));
                            name6 = String.valueOf(newPost.get("recipeName"));
                            name7 = String.valueOf(newPost.get("recipeType"));
                            name8 = String.valueOf(newPost.get("recipeImageLink"));

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





                }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);

        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();

        DatabaseReference childreference2 = databaseReference.child("User Favourite Recipes")
                .child(firebaseAuth.getUid());

        childreference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(str))
                {

                    menu.findItem(R.id.addFavourite).setIcon(R.drawable.ic_favorite_black_24dp);
                    i++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference();


        if (item.getItemId() == R.id.addFavourite) {

            i++;

            DatabaseReference childreference1 = databaseReference.child("User Favourite Recipes")
                    .child(firebaseAuth.getUid());


            if (i% 2 == 0)
            {
                item.setIcon(R.drawable.ic_baseline_favorite_24);


                RecipeInfo recipeInfo = new RecipeInfo(name6,name1,name7,name4,name2,name5,name3,name8);

                childreference1.child(str).push().setValue(recipeInfo);

            }
            else
            {
                item.setIcon(R.drawable.ic_baseline_favorite_border_24);

                childreference1.child(str).removeValue();

            }


        }



        return super.onOptionsItemSelected(item);
    }
}