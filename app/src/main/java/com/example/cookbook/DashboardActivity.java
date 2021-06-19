package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cookbook.Fragments.AddRecipeFragment;
import com.example.cookbook.Fragments.DiscoverFragment;
import com.example.cookbook.Fragments.LoginFragment;
import com.example.cookbook.Fragments.MyRecipeFragment;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView navi;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navi = findViewById(R.id.nv);
        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this,dl,R.string.nav_drawer_open,R.string.nav_drawer_close);


        navController = Navigation.findNavController(DashboardActivity.this,R.id.Host_Fragment2);


        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.Host_Fragment2,new DiscoverFragment()).commit();
        }


        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                item.setCheckable(true);
                Fragment fragment = null;

                switch (item.getItemId())
                {

                    case R.id.Home :

                        navController.navigate(R.id.addRecipeFragment);
                        break;

                    case R.id.MyRecentRecipe :

                        navController.navigate(R.id.myRecipeFragment);
                        break;

                    case R.id.MyFavRecipe :

                        navController.navigate(R.id.recipeDescriptionFragment);
                        break;

                    case R.id.Profile :

                        navController.navigate(R.id.profileFragment);
                        break;


                    case R.id.Logout :

                        Toast toast = Toast.makeText(DashboardActivity.this,"Logged out",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
//                        firebaseAuth.signOut();
                        fragment = new LoginFragment();
                        finish();
                        break;

                }

                dl.closeDrawer(GravityCompat.START);
                return false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (t.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
