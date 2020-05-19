package com.example.dorra.Category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*public class CategoryProductsAdapter extends RecyclerView.Adapter<CategoryProductsAdapter.ViewHolder> {
    private String[] namesArray;
    private int [] photo_array;
    private CategoryProductActivity context;

    public CategoryProductsAdapter(CategoryProductActivity context, String[] namesArray, int[] photo_array) {
        this.namesArray = namesArray;
        this.photo_array = photo_array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_products_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryProductsName.setText(namesArray[position]);
        holder.categoryProductsIcon.setImageResource(photo_array[position]);
    }

    @Override
    public int getItemCount() {
        return namesArray.length;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryProductsIcon;
        private TextView categoryProductsName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryProductsIcon = itemView.findViewById(R.id.category_product_item_image);
            categoryProductsName = itemView.findViewById(R.id.category_product_item_name);
        }
    }
}

 */