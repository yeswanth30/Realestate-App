package com.realestateadmin;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Revenue_Fragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_item_1) {
                selectedFragment = new Revenue_Fragment();
            } else if (item.getItemId() == R.id.nav_item_2) {
                selectedFragment = new Brokers_Fragment();
            } else if (item.getItemId() == R.id.nav_item_4) {
                selectedFragment = new Profile_Fragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }

            return false;
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}


//else if (item.getItemId() == R.id.nav_item_3) {
//        selectedFragment = new Property_Fragment();
//        }