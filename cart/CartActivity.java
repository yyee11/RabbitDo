package com.example.todo.cart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.RegisterAndLogin.LoginActivity;
import com.example.todo.market.Goods;
import com.example.todo.market.MarketMainActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    CartAdapter myAdapter;
    CartDatabase cartDB;
    UserSoundDao userSoundDao;
    TextView total;
    ImageView chooseAll;
    Boolean hasChoosed;
    int sum = 0;
    int result;
    String userId = LoginActivity.userID;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        total = findViewById(R.id.total);
        chooseAll = findViewById(R.id.chooseAll);
        // RecyclerView
        mRecyclerView = findViewById(R.id.items);
        // Set its Properties using LinearLayout
        mRecyclerView.setLayoutManager((new LinearLayoutManager(this)));
        // Set RecyclerView Adapter
        cartDB = new CartDatabase(this);
        userSoundDao = new UserSoundDao(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Shopping Cart");
        iniCart();

    }

    private void iniCart() {
        result = 0;
        hasChoosed = false;
        Cursor res = cartDB.getAllData(userId);
        if (res.getCount() == 0) {
            this.setContentView(R.layout.item_cart_empty);
        }
        else {
            myAdapter = new CartAdapter(this, this.getItems());
            mRecyclerView.setAdapter(myAdapter);
        }
    }

    private ArrayList<Goods> getItems() {
        Cursor res = cartDB.getAllData(userId);
        StringBuffer buffer = new StringBuffer();
        String index,title,price,image;
        ArrayList<Goods> models = new ArrayList<>();
        while (res.moveToNext()) {
            buffer.append(res.getString(0));
            index = buffer.toString();
            buffer.delete(0,buffer.length());
            buffer.append(res.getString(1));
            title = buffer.toString();
            buffer.delete(0,buffer.length());
            buffer.append(res.getString(2));
            price = buffer.toString();
            buffer.delete(0,buffer.length());
            buffer.append(res.getString(3));
            image = buffer.toString();
            buffer.delete(0,buffer.length());
            buffer.append(res.getString(4));
            buffer.delete(0,buffer.length());
            models.add(new Goods(Integer.parseInt(index),Integer.parseInt(image),title,price));
            sum += Integer.parseInt(price);
        }
        for (int i = 0; i<models.size();i++){
            for (int j = i+1;j<models.size();j++){
                if (models.get(i).getGoodsTitle().equals(models.get(j).getGoodsTitle())){
                    models.remove(j);
                    j--;
                }
            }
        }
        return models;
    }

    public void chooseAll(View view){
        if (!hasChoosed){
            chooseAll.setImageResource(R.mipmap.check_pressed);
            hasChoosed = true;
            total.setText(getString(R.string.unit) + String.valueOf(sum));
        }
        else {
            chooseAll.setImageResource(R.mipmap.check_normal);
            hasChoosed = false;
            total.setText("");
        }

    }

    public void pay(View view){
//        String[] titles = getResources().getStringArray(R.array.title_array);
        if (LoginActivity.userID.equals("-1")) {
            Toast.makeText(this, getString(R.string.invalid), Toast.LENGTH_SHORT).show();
        }
        else {
            boolean noDelete = true;
            if (hasChoosed){
                for (int i = 0;i<getItems().size();i++){
                    userSoundDao.insertData(LoginActivity.userID,getItems().get(i).getGoodsTitle());
                }
                Toast.makeText(this, getString(R.string.all_payed), Toast.LENGTH_SHORT).show();
                cartDB.deleteAll();
                result = sum;
                noDelete = false;
            }
            else {
                for (int i = 0; i<CartAdapter.chooseItems.size();i++){
                    int toBeDeleted = CartAdapter.chooseItems.get(i);
                    if (toBeDeleted != 0) {
                        String goodsName = cartDB.getName(String.valueOf(CartAdapter.chooseItems.get(i)));
                        userSoundDao.insertData(LoginActivity.userID,goodsName);
                        cartDB.deleteData(String.valueOf(CartAdapter.chooseItems.get(i)));
                        noDelete = false;
                    }

                }
                result =  CartAdapter.totalCost;
                CartAdapter.chooseItems.clear();
                CartAdapter.totalCost = 0;
            }

            if (noDelete) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Check Out");
                builder.setMessage(getString(R.string.nothing_chosen_message));
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            { }
                        });
                builder.create();
                builder.show();
                total.setText("");
            }
            else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(CartActivity.this);
                builder.setTitle(getString(R.string.check_out_title));
                builder.setMessage(getString(R.string.sth_message) + getString(R.string.unit) + result);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            { }
                        });
                builder.create();
                builder.show();
                total.setText(String.valueOf(result));
                iniCart();
            }
        }
//        Toast.makeText(this, "You have payed successfully. The total cost is"
//                + result, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_market:
                Intent intent = new Intent(this, MarketMainActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.goList:{
                Intent i = new Intent(this, MainActivity.class);
                this.startActivity(i);
                return true;
            }
        }
        return false;
    }

}