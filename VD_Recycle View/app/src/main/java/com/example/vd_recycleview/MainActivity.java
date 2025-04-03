package com.example.vd_recycleview;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LandScapeAdapter landScapeAdapter;
    ArrayList<LandScape> recylerViewDatas;
    RecyclerView recyclerViewLandscape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recylerViewDatas = getDataForRecyclerView();
        recyclerViewLandscape = findViewById(R.id.recycleLand);
        RecyclerView.LayoutManager layoutLinear = new LinearLayoutManager(this);
        recyclerViewLandscape.setLayoutManager(layoutLinear);
        landScapeAdapter = new LandScapeAdapter(this, recylerViewDatas);
        recyclerViewLandscape.setAdapter(landScapeAdapter);

    }
    ArrayList<LandScape> getDataForRecyclerView(){
        ArrayList<LandScape> dsDulieu = new ArrayList<LandScape>();
        LandScape landScape1 = new LandScape("hinh1", "tranh 1");
        dsDulieu.add(landScape1);
        dsDulieu.add(new LandScape("hinh2", "tranh 2"));
        dsDulieu.add(new LandScape("hinh4", "tranh 4"));
        return dsDulieu;
    }
}