package minh.edu.ex6_intentdongian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button nutMH2;
    Button nutMH3;
    void TimDieuKHien(){
        nutMH2 = (Button) findViewById(R.id.btnMH2);
        nutMH3 = (Button) findViewById(R.id.btnMH3);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //Tim dieu khien nut bam
        TimDieuKHien();
        //Gan bo lang nghe su kien
        nutMH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xu ly chuyen man hinh
                //B1. Tao mot Intent   2 tham so: 1) man hinh hien tai.this; 2) man hinh chuyen toi.class
                Intent intentMH2 = new Intent(MainActivity.this, MH2Activity.class);
                //B2. Gui
                startActivity(intentMH2);
            }
        });

        nutMH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xu ly chuyen man hinh
                //B1. Tao mot Intent   2 tham so: 1) man hinh hien tai.this; 2) man hinh chuyen toi.class
                Intent intentMH3 = new Intent(MainActivity.this, MH2Activity.class);
                //B2. Gui
                startActivity(intentMH3);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}