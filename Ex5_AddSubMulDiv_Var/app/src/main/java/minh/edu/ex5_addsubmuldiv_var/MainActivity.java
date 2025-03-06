package minh.edu.ex5_addsubmuldiv_var;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edtSoA;
    EditText edtSoB;
    Button btnCong, btnTru, btnNhan, btnChia;
    TextView tvKetQua;

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
        TimView();
        btnCong.setOnClickListener(boLangNghe_XuLyCong);
        btnTru.setOnClickListener(boLangNghe_XuLyTru);
        btnNhan.setOnClickListener(boLangNghe_XuLyNhan);
        btnChia.setOnClickListener(boLangNghe_XuLyChia);
    }

    View.OnClickListener boLangNghe_XuLyCong = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strSo1 = edtSoA.getText().toString();
            String strSo2 = edtSoB.getText().toString();

            double soA = Double.parseDouble(strSo1);
            double soB = Double.parseDouble(strSo2);

            double tong = soA + soB;

            String strKQ = String.valueOf(tong);
            tvKetQua.setText(strKQ);

        }
    };

    View.OnClickListener boLangNghe_XuLyTru = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strSo1 = edtSoA.getText().toString();
            String strSo2 = edtSoB.getText().toString();

            double soA = Double.parseDouble(strSo1);
            double soB = Double.parseDouble(strSo2);

            double tong = soA - soB;

            String strKQ = String.valueOf(tong);
            tvKetQua.setText(strKQ);

        }
    };

    View.OnClickListener boLangNghe_XuLyNhan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strSo1 = edtSoA.getText().toString();
            String strSo2 = edtSoB.getText().toString();

            double soA = Double.parseDouble(strSo1);
            double soB = Double.parseDouble(strSo2);

            double tong = soA * soB;

            String strKQ = String.valueOf(tong);
            tvKetQua.setText(strKQ);

        }
    };

    View.OnClickListener boLangNghe_XuLyChia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strSo1 = edtSoA.getText().toString();
            String strSo2 = edtSoB.getText().toString();

            double soA = Double.parseDouble(strSo1);
            double soB = Double.parseDouble(strSo2);

            double tong = soA / soB;

            String strKQ = String.valueOf(tong);
            tvKetQua.setText(strKQ);

        }
    };

    void TimView(){
        edtSoA = (EditText) findViewById(R.id.edtSoA);
        edtSoB = (EditText) findViewById(R.id.edtSoB);
        btnCong = (Button) findViewById(R.id.btnCong);
        btnTru = (Button) findViewById(R.id.btnTru);
        btnNhan = (Button) findViewById(R.id.btnNhan);
        btnChia = (Button) findViewById(R.id.btnChia);
        tvKetQua = (TextView) findViewById(R.id.tvKetQua);
    }
}