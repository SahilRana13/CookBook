package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookbook.Fragments.RecipeDetailsFragment;
import com.example.cookbook.Fragments.RecipeReviewFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        tabLayout = findViewById(R.id.recipeTabLayout);
        viewPager = findViewById(R.id.recipeViewPager);
        imageView = findViewById(R.id.recipeImageView);

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
    }

    private void getImageData() {




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

                            String name1 = String.valueOf(newPost.get("recipeImageLink"));

                            Picasso.get().load(name1).into(imageView);



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