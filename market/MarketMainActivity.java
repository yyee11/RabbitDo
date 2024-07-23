package com.example.todo.market;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.cart.CartActivity;

import java.util.ArrayList;

public class MarketMainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    GoodsAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_main);
        // RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set its Properties using LinearLayout
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        // Set RecyclerView Adapter
        myAdapter = new GoodsAdapter(this, this.getGoods());
        mRecyclerView.setAdapter(myAdapter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Market");
        getSupportActionBar().show();
    }

    private ArrayList<Goods> getGoods() {
        final String[] titles =
                getResources().getStringArray(R.array.title_array);
        final String[] prices =
                getResources().getStringArray(R.array.price_array);
        final TypedArray images =
                getResources().obtainTypedArray(R.array.image_array);
        ArrayList<Goods> models = new ArrayList<>();
        Goods p;
        for (int i = 0; i < titles.length; i++) {
            p = new Goods(i+1,images.getResourceId(i,-1),titles[i],prices[i]);
            models.add(p);
        }
        return models;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.market_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_cart:
                Intent intent = new Intent(this, CartActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.bake_to_main:{
                Intent i = new Intent(this, MainActivity.class);
                this.startActivity(i);
                return true;
            }
        }
        return false;
    }
}