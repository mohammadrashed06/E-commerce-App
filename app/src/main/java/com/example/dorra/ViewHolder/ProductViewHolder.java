package com.example.dorra.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dorra.CallBack.ItemClickListner;
import com.example.dorra.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductDescription,txtProductPrice;
    public ImageView imageView,addItemImage;
    public ItemClickListner listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_item_image);
        txtProductName=itemView.findViewById(R.id.product_item_name);
        txtProductDescription=itemView.findViewById(R.id.product_item_disc);
        txtProductPrice=itemView.findViewById(R.id.product_item_price);
        addItemImage = itemView.findViewById(R.id.add_to_card_small);


    }
    public void setItemClickListener(ItemClickListner listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
       listener.onClick(v,getAdapterPosition(),false);
    }
}
