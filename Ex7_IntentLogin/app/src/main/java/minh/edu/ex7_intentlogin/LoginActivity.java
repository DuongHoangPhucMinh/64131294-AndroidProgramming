package minh.edu.ex7_intentlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Button btnXacNhan = (Button) findViewById(R.id.btnOK);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xu ly dang nhap
                //lay du lieu
                //B1. Tim tham chieu den dk
                EditText edTenDn = (EditText) findViewById(R.id.edtUsername);
                EditText edPass = (EditText) findViewById(R.id.edtPass);
                //B2.Lay du lieu
                String tenDangNhap = edTenDn.getText().toString();
                String mk = edPass.getText().toString();
                //Kiem tra mk
                if(tenDangNhap.equals("duonghoangphucminh") && mk.equals("123")) {
                    //chuyen sang man hinh home
                    Intent iQuiz = new Intent(LoginActivity.this, HomeActivity.class);
                    //gui du lieu vao iQuiz, dang key -value, key duoc dung de ben kia loc ra du lieu
                    iQuiz.putExtra("ten_dang_nhap",tenDangNhap);
                    iQuiz.putExtra("mat_khau",mk);
                    //Gui di
                    startActivity(iQuiz);

                }
                else {
                    Toast.makeText(LoginActivity.this,"BẠN NHẬP SAI MẬT KHẨU",Toast.LENGTH_LONG);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}