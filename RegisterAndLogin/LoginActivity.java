package com.example.todo.RegisterAndLogin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.MainActivity;
import com.example.todo.R;

public class LoginActivity extends AppCompatActivity {

    private MyDataBaseHelper db;
    private TextView password_text;
    private TextView username_text;
    private Button login;
    private Button register;
    private UserDao userDao;

    Button Login,Register;

    public static String userID = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new MyDataBaseHelper(this, "study.db", null, 2);
        SQLiteDatabase sd = db.getWritableDatabase();
        userDao=new UserDao(sd);

        username_text=findViewById(R.id.Eusername);
        password_text = findViewById(R.id.Epw);

        Login = (Button) findViewById(R.id.LoginBtn);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = password_text.getText().toString();
                String username = username_text.getText().toString();
                int id = userDao.getUser(username, password);
                if (id != -1) {
                    Toast.makeText(LoginActivity.this, R.string.Wel, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_id", id);
                    intent.putExtra("user_name", username);
                    userID = username;
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.Wrong, Toast.LENGTH_LONG).show();
                }
            }
        });

        Register = (Button) findViewById(R.id.RetBtn);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}