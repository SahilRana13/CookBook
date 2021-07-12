package com.example.cookbook;

import androidx.annotation.NonNull;
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
    }
}