package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dorra.Admin.AdminMaintainProductsActivity;
import com.example.dorra.Model.Products;
import com.example.dorra.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ShowAllProductsActivity extends AppCompatActivity {
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String type="";
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_products);
        recyclerView =findViewById(R.id.allProductRecycler);


        Paper.init(this);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView.setHasFixedSize(true);
        layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("جميع المنتجات");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        setSupportActionBar(toolbar);




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=

                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productRef,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        //display the data in card
                        productViewHolder.txtProductName.setText(products.getPname());
                        productViewHolder.txtProductDescription.setText(products.getDescription());
                        productViewHolder.txtProductPrice.setText("السعر : "+products.getPrice()+" د.أ");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);
                        // to set up on user click in product open details activity

                        // to set up on user click in product open details activity
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")){
                                    // i have to send the admin to another activity
                                    Intent intent=new Intent(ShowAllProductsActivity.this, AdminMaintainProductsActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);

                                }else{
                                    Intent intent=new Intent(ShowAllProductsActivity.this, ProductDetailActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



}
}
