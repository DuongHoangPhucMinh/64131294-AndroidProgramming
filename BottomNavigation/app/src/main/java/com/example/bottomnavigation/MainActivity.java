package com.example.bottomnavigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav;
        bottomNav = findViewById(R.id.bot_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mnuItemDuocChonId = item.getItemId();
                if (mnuItemDuocChonId ==R.id.mnu_home){
                    Toast.makeText(MainActivity.this , "Thay HOME", Toast.LENGTH_LONG).show();}
                else if (mnuItemDuocChonId == R.id.mnu_search){
                    Toast.makeText(MainActivity.this , "Thay SEARCH", Toast.LENGTH_LONG).show();}
                else if (mnuItemDuocChonId == R.id.mnu_profile){
                    Toast.makeText(MainActivity.this , "Thay PROFILE", Toast.LENGTH_LONG).show();}
                else
                    return false;
                return  true;
            }
        });
    }
}