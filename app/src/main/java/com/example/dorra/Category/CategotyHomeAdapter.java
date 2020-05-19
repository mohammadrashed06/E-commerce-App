package com.example.dorra.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dorra.HomeeActivity;
import com.example.dorra.Model.Category;
import com.example.dorra.R;

import java.util.List;

public class CategotyHomeAdapter extends RecyclerView.Adapter<CategotyHomeAdapter.ViewHolder> {
    private List<Category> categoryList;
    private HomeeActivity context;
    private Context context2;

    public CategotyHomeAdapter(List<Category> categoryList, HomeeActivity context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategotyHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategotyHomeAdapter.ViewHolder holder, final int position) {
        final Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());
        holder.categoryImage.setImageResource(category.getCategoryImage(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    Intent intent = new Intent(v.getContext(), CategoryProductActivity.class);
                    intent.putExtra("CategoryName", category.getCategoryName());
                    v.getContext().startActivity(intent);
                }else if (position==0){
                    Toast.makeText(v.getContext(),"انت في الصفحة الرئيسية",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView categoryImage;
        private TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
        }


    }
}
