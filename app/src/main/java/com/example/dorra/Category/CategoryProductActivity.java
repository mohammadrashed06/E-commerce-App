package com.example.dorra.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.dorra.Admin.AdminMaintainProductsActivity;
import com.example.dorra.HomeeActivity;
import com.example.dorra.Model.Cart;
import com.example.dorra.Model.Products;
import com.example.dorra.ProductDetailActivity;
import com.example.dorra.R;
import com.example.dorra.ViewHolder.CartViewHolder;
import com.example.dorra.ViewHolder.CategoryProductViewHolder;
import com.example.dorra.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class CategoryProductActivity extends AppCompatActivity {

    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String catName;
    FirebaseRecyclerAdapter<Products, CategoryProductViewHolder> adapter;
    private ImageView imgEmbtyMassege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catName = getIntent().getStringExtra("CategoryName");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(catName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        imgEmbtyMassege = findViewById(R.id.msg1_category_empty);
        recyclerView=findViewById(R.id.category_products_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoryProductActivity.this,HomeeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query firebaseCategoryQuery = productRef.orderByChild("category").startAt(catName).endAt(catName + "\uf8ff");
        FirebaseRecyclerOptions<Products> options =

                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(firebaseCategoryQuery, Products.class)
                        .build();

            adapter =
                new FirebaseRecyclerAdapter<Products, CategoryProductViewHolder>(options) {
                    @NonNull
                    @Override
                    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_products_layout,parent,false);
                        CategoryProductViewHolder holder=new CategoryProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull CategoryProductViewHolder categoryProductViewHolder, int i, @NonNull final Products products) {
                        //display the data in card
                        categoryProductViewHolder.txtProductCategoryName.setText(products.getPname());
                        categoryProductViewHolder.txtProductCategoryDescription.setText(products.getDescription());
                        categoryProductViewHolder.txtProductCategoryPrice.setText("السعر : " + products.getPrice() + " د.أ");
                        Picasso.get().load(products.getImage()).into(categoryProductViewHolder.imageCategoryView);
                        categoryProductViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    Intent intent=new Intent(CategoryProductActivity.this, ProductDetailActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);


                            }
                        });

                    }

                    //check if the recycler view is embty or not
                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        if (adapter.getItemCount() != 0){
                            imgEmbtyMassege.setVisibility(View.GONE);

                        }
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     //   int id =item.getItemId();
      //  if (id==R.id.categor_action_search){
        //    return true;
      //  }
        return super.onOptionsItemSelected(item);
    }
}
