package com.example.todo.cart;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;

public class MyHolder extends RecyclerView.ViewHolder {
    //Views
    RelativeLayout mCartRelativeLayout;
    ImageView mItemImage,mSelect;
    TextView mItemName, mItemPrice;
    TextView deleteButton;
    public MyHolder(View itemView) {
        super(itemView);
        this.mCartRelativeLayout = itemView.findViewById(R.id.relativeItem);
        this.mItemImage = itemView.findViewById((R.id.imageItem));
        this.mItemName = itemView.findViewById((R.id.nameItem));
        this.mItemPrice = itemView.findViewById((R.id.spec_values));
        this.deleteButton = itemView.findViewById(R.id.delete);
        this.mSelect = itemView.findViewById(R.id.chooseAll);
    }
}
