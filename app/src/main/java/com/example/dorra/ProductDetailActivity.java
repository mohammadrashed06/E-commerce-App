package com.example.dorra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dorra.Model.Products;
import com.example.dorra.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName,productPrice,productDescrition;
    private Button addToCardBtn;
    private ElegantNumberButton numberButton;
    private String productId="",state="Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

            FindViewId();
            // to get the spicefic location data from home activity
            productId = getIntent().getStringExtra("pid");
            addToCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addingToCartList();
                    if (state.equals("Order Placed") || state.equals("Order Shipped")){
                       Toast.makeText(ProductDetailActivity.this,"يمكنك شراء المزيد من المنتجات بمجرد شحن طلبك السابق او تأكيده",Toast.LENGTH_LONG).show();
                   }else{
                        addingToCartList();

                    }
                }



                private void addingToCartList() {
                    String saveCurrentTime,saveCurrentDate;
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate=currentDate.format(calForDate.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime=currentTime.format(calForDate.getTime());
                    final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");


                    final HashMap<String,Object> cartMap=new HashMap<>();
                    cartMap.put("pid",productId);
                    cartMap.put("pname",productName.getText().toString());
                    cartMap.put("description",productDescrition.getText().toString());
                    cartMap.put("price",productPrice.getText().toString());
                    cartMap.put("date",saveCurrentDate);
                    cartMap.put("time",saveCurrentTime);
                    cartMap.put("quantity",numberButton.getNumber());
                    cartMap.put("discount","");
                    cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products")
                            .child(productId)
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                                .child("Products")
                                                .child(productId)
                                                .updateChildren(cartMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(ProductDetailActivity.this,"تم أضافة المنتج الى السلة.",Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(ProductDetailActivity.this,HomeeActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });



                }
            });
            getProductDetails(productId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void getProductDetails(String productId) {
        DatabaseReference productRef2= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef2.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescrition.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
                else{
                    Toast.makeText(ProductDetailActivity.this,"error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void checkOrderState(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //check the shipping state
                    String sheppingState = dataSnapshot.child("state").getValue().toString();
                    if (sheppingState.equals("shipped")){
                        state="Order Shipped";

                    }else if(sheppingState.equals("not shipped")){
                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void FindViewId(){
        productImage=findViewById(R.id.product_image_details);
        productName=findViewById(R.id.product_name_details);
        productPrice=findViewById(R.id.product_price_details);
        productDescrition=findViewById(R.id.product_details_discreption);
        addToCardBtn=findViewById(R.id.add_product_to_cart_btn);
        numberButton=findViewById(R.id.number_btn);
    }
}
