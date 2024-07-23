package com.example.todo.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.RegisterAndLogin.LoginActivity;
import com.example.todo.R;
import com.example.todo.market.DetailActivity;
import com.example.todo.market.Goods;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<MyHolder>{
    Context c;
    ArrayList<Goods> models;
    static int totalCost = 0;
    static ArrayList<Integer> chooseItems = new ArrayList() ;
    // Constructor of the MyAdapter Class
    public CartAdapter(Context c, ArrayList<Goods> models) {
        this.c = c;
        this.models = models;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // convert xml to view obj
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, null);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItemName.setText(models.get(position).getGoodsTitle());
        holder.mItemPrice.setText("Price: " +c.getResources().getString(R.string.unit) + models.get(position).getGoodsPrice());
        holder.mItemImage.setImageResource(models.get(position).getGoodsIcon());
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.userID.equals("-1")) {
                    Toast.makeText(c, c.getResources().getString(R.string.invalid), Toast.LENGTH_SHORT).show();
                }
                else {
                    CartDatabase cartDB = new CartDatabase(c);
                    cartDB.deleteData(String.valueOf(models.get(position).getGoodsId()));
                    Intent intent = new Intent(c, CartActivity.class);
                    c.startActivity(intent);
                }

            }
        });


        holder.mSelect.setOnClickListener(new View.OnClickListener() {
            Boolean isChoosed = false;
            @Override
            public void onClick(View v) {
                if (!isChoosed){
                    holder.mSelect.setImageResource(R.mipmap.check_pressed);
                    totalCost += Integer.parseInt(models.get(position).getGoodsPrice());
                    chooseItems.add(models.get(position).getGoodsId());
                    isChoosed = true;
                }
                else {
                    holder.mSelect.setImageResource(R.mipmap.check_normal);
                    totalCost -= Integer.parseInt(models.get(position).getGoodsPrice());
                    int index = chooseItems.indexOf(models.get(position).getGoodsId());
                    chooseItems.remove(index);
                    isChoosed = false;
                }
            }
        });

        holder.mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, DetailActivity.class);
                intent.putExtra("gName", models.get(position).getGoodsTitle());
                intent.putExtra("gPrice", models.get(position).getGoodsPrice());
                intent.putExtra("gImage", models.get(position).getGoodsIcon());
                c.startActivity(intent);
            }
        });

//        CartActivity.chooseAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!CartActivity.hasChoosed){
//                    holder.mSelect.setImageResource(R.mipmap.check_pressed);
//                    CartActivity.hasChoosed = true;
//                }
//                else {
//                    holder.mSelect.setImageResource(R.mipmap.check_normal);
//                    CartActivity.hasChoosed = false;
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}