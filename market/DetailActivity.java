package com.example.todo.market;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.RegisterAndLogin.LoginActivity;
import com.example.todo.R;
import com.example.todo.cart.CartActivity;
import com.example.todo.cart.CartDatabase;
import com.example.todo.cart.UserSoundDao;

public class DetailActivity extends AppCompatActivity {
    ImageView mImage,back;
    TextView mName, mPrice, goCart, addCart, descriptionTextVIew;

    String gName,gPrice;
    int image;
    CartDatabase cartDatabase;
    UserSoundDao userSoundDao;
    boolean hasAdded = false;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cartDatabase = new CartDatabase(this);
        userSoundDao = new UserSoundDao(this);
        mImage= findViewById((R.id.image));
        mName = findViewById((R.id.nameItem));
        mPrice = findViewById((R.id.money));
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
        goCart = findViewById(R.id.go_cart);
        addCart = findViewById(R.id.add_cart);
        descriptionTextVIew = findViewById(R.id.description);
        // Action Bar
        ActionBar actionBar = getSupportActionBar();
        // Intent
        Intent intent = getIntent();
        gName = intent.getStringExtra("gName");
        gPrice = intent.getStringExtra("gPrice");
//        byte[] gByte = getIntent().getByteArrayExtra("gImage");
//         decode bytes array to bitmap
//        Bitmap bitmap = BitmapFactory.decodeByteArray(gByte, 0, gByte.length);
        image = intent.getIntExtra("gImage",1);
        // set title to actionbar
        actionBar.setTitle(gName);
        // Set data to the views
        mName.setText(gName);
        mPrice.setText(getString(R.string.unit) + gPrice);
//        mImage.setImageBitmap(bitmap);
        mImage.setImageResource(image);
        playSound();
    }

    public void goToCart(View view){
        Intent intent = new Intent(this, CartActivity.class);
        this.startActivity(intent);
        mediaPlayer.stop();
    }
    public void addToCart(View view){
        if (LoginActivity.userID.equals("-1")) {
            Toast.makeText(this, getString(R.string.invalid), Toast.LENGTH_SHORT).show();
        }
        else {
            if (hasAdded){
                Toast.makeText(this, getString(R.string.added), Toast.LENGTH_SHORT).show();
            }
            else {
                boolean isInserted = cartDatabase.insertData(gName, gPrice, image, LoginActivity.userID);
                if (isInserted){
                    Toast.makeText(this, getString(R.string.add_successful), Toast.LENGTH_SHORT).show();
                    hasAdded = true;
                }
                else
                    Toast.makeText(this, getString(R.string.add_failed), Toast.LENGTH_SHORT).show();
            }
        }
//        Intent intent = new Intent(this, CartActivity.class);
//        this.startActivity(intent);
    }

    public void payDirect(View view) {
        if (LoginActivity.userID.equals("-1")) {
            Toast.makeText(this, getString(R.string.invalid), Toast.LENGTH_SHORT).show();
        }
        else {
            boolean isPayed = userSoundDao.insertData(LoginActivity.userID,gName);
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(DetailActivity.this);
            builder.setTitle("Pay Directly");
            if (isPayed)
                builder.setMessage(getString(R.string.sth_message) + getString(R.string.unit) + gPrice);
            else builder.setMessage(getString(R.string.have_bought));
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        { }
                    });
            builder.create();
            builder.show();
        }
    }

    private void playSound() {
        String[] titles = getResources().getStringArray(R.array.title_array);
        String[] descriptions = getResources().getStringArray(R.array.description_array);
        if (mediaPlayer == null){
            if (gName.equals(titles[0])){
                mediaPlayer = MediaPlayer.create(this, R.raw.rain);
                descriptionTextVIew.setText(descriptions[0]);
            }

            if (gName.equals(titles[1])){
                mediaPlayer = MediaPlayer.create(this, R.raw.beach);
                descriptionTextVIew.setText(descriptions[1]);
            }

            if (gName.equals(titles[2])){
                mediaPlayer = MediaPlayer.create(this, R.raw.bell);
                descriptionTextVIew.setText(descriptions[2]);
            }

            if (gName.equals(titles[3])){
                mediaPlayer = MediaPlayer.create(this, R.raw.forest);
                descriptionTextVIew.setText(descriptions[3]);
            }

            if (gName.equals(titles[4])){
                mediaPlayer = MediaPlayer.create(this, R.raw.wind);
                descriptionTextVIew.setText(descriptions[4]);
            }

        }
        mediaPlayer.start();
    }
}