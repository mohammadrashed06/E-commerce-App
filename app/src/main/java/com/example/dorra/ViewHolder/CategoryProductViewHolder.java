package com.example.dorra.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dorra.CallBack.ItemClickListner;
import com.example.dorra.R;

public class CategoryProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductCategoryName,txtProductCategoryDescription,txtProductCategoryPrice;
    public ImageView imageCategoryView,addItemCategoryImage;
    public ItemClickListner listenerCategory;
    public CategoryProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageCategoryView=itemView.findViewById(R.id.category_product_item_image);
        txtProductCategoryName=itemView.findViewById(R.id.category_product_item_name);
        txtProductCategoryDescription=itemView.findViewById(R.id.category_product_item_disc);
        txtProductCategoryPrice=itemView.findViewById(R.id.category_product_item_price);
        addItemCategoryImage = itemView.findViewById(R.id.category_add_to_card_small);


    }
    public void setItemClickListener(ItemClickListner listener){
        this.listenerCategory=listener;
    }

    @Override
    public void onClick(View v) {
        listenerCategory.onClick(v,getAdapterPosition(),false);
    }
}
