package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dorra.Admin.AdminMaintainProductsActivity;
import com.example.dorra.Model.Products;
import com.example.dorra.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {
    private ImageView searchBtn;
    private EditText inputText;
    private RecyclerView search_list;
    private RecyclerView.LayoutManager layoutManager;
    private String searchInput;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        searchBtn = findViewById(R.id.search_btn);
        inputText = findViewById(R.id.search_product_edit_text);
        search_list = findViewById(R.id.search_list);
        search_list.setHasFixedSize(true);
        layoutManager =new GridLayoutManager(this,2);
        search_list.setLayoutManager(new GridLayoutManager(this, 2));
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query firebaseSearchQuery = productRef.orderByChild("pname").startAt(searchInput).endAt(searchInput+"\uf8ff");

        FirebaseRecyclerOptions<Products> options=

                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(firebaseSearchQuery,Products.class)
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
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    Intent intent=new Intent(SearchProductActivity.this, ProductDetailActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);


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
        search_list.setAdapter(adapter);
        adapter.startListening();

    }
}
