package com.example.todo.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;

import java.util.ArrayList;

public class GoodsAdapter extends RecyclerView.Adapter<MyHolder>{
    Context c;
    ArrayList<Goods> models;
    // Constructor of the MyAdapter Class
    public GoodsAdapter(Context c, ArrayList<Goods> models) {
        this.c = c;
        this.models = models;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // convert xml to view obj
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_layout, null);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        //bind data to our views
        holder.mGoodsName.setText(models.get(position).getGoodsTitle());
        holder.mGoodsPrice.setText(c.getResources().getString(R.string.price) + " " + c.getResources().getString(R.string.unit) + models.get(position).getGoodsPrice());
        holder.mGoodsImage.setImageResource(models.get(position).getGoodsIcon());
        // Use when you want to put each item data to same activity
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data from item click
                String goodsName = models.get(position).getGoodsTitle();
                String goodsPrice = models.get(position).getGoodsPrice();
//                BitmapDrawable bitmapDrawable = (BitmapDrawable)
//                        holder.mGoodsImage.getDrawable();
                // get bitmap from drawable
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                // compress image
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                convert to bytes array
//                byte[] bytes = stream.toByteArray();
                // intent, put data to intent, start activity
                int image = models.get(position).getGoodsIcon();
                Intent intent = new Intent(c, DetailActivity.class);
                intent.putExtra("gName", goodsName);
                intent.putExtra("gPrice", goodsPrice);
//                intent.putExtra("gImage", bytes);
                intent.putExtra("gImage", image);
                c.startActivity(intent);
            }
        });
        // Animation
        Animation animation = AnimationUtils.loadAnimation(c,
                android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
