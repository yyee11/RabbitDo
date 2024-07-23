package com.example.todo.RegisterAndLogin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.R;
import com.example.todo.RegisterAndLogin.LoginActivity;
import com.example.todo.RegisterAndLogin.MyDataBaseHelper;
import com.example.todo.RegisterAndLogin.UserDao;

public class RegisterActivity extends AppCompatActivity {

    private MyDataBaseHelper db;
    private TextView password_text;
    private TextView Repassword_text;
    private TextView username_text;
    private Button register;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        userDao=new UserDao(sd);
        password_text = findViewById(R.id.re_password);
        Repassword_text = findViewById(R.id.re_affirm);
        username_text=findViewById(R.id.re_username);

        register = (Button) findViewById(R.id.re_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = password_text.getText().toString().trim();
                String password2 = Repassword_text.getText().toString().trim();
                if (password1.equals("")||password2.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
                }else if(password1.equals(password2)){
                    String password = password_text.getText().toString();
                    String username= username_text.getText().toString();
                    boolean isRegister = userDao.addUser(username,password);
                    if (isRegister) {
                        Toast.makeText(RegisterActivity.this, R.string.Success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, R.string.Wrong, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please check your password", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}