package com.example.dorra.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dorra.CallBack.ItemClickListner;
import com.example.dorra.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //acsses all view in card view
    public TextView txtProductdesc,txtProductPrice,txtProductQuntityNumber,textOptionMenu;
    public ImageView productImage;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductdesc=itemView.findViewById(R.id.cart_product_description);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuntityNumber=itemView.findViewById(R.id.cart_quntitiy_number);
      //  productImage=itemView.findViewById(R.id.image_cart_layout);
        textOptionMenu=itemView.findViewById(R.id.textViewOptions);


    }
    private void setItemDetails(int resorce){
        productImage.setImageResource(resorce);
    }

    @Override
    public void onClick(View v) {
   itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
