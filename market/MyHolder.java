package com.example.todo.market;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;

public class MyHolder extends RecyclerView.ViewHolder {
    //Views
    RelativeLayout mRelativeLayout;
    ImageView mGoodsImage;
    TextView mGoodsName, mGoodsPrice;
    public MyHolder(View itemView) {
        super(itemView);
        this.mRelativeLayout = itemView.findViewById(R.id.relative);
        this.mGoodsImage = itemView.findViewById((R.id.cardGoodsImage));
        this.mGoodsName = itemView.findViewById((R.id.cardGoodsName));
        this.mGoodsPrice = itemView.findViewById((R.id.cardGoodsPrice));
    }
}
