package com.example.dorra.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dorra.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button applychangesBtn,deleteBtn;
    private EditText name,description,price;
    private ImageView imageView;
    private String productId="";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        applychangesBtn = findViewById(R.id.apllay_changes_admin_btn);
        name = findViewById(R.id.product_item_name_edit_admin);
        description = findViewById(R.id.product_item_disc_edit_admin);
        price = findViewById(R.id.product_item_price_edit_admin);
        imageView = findViewById(R.id.product_item_image_edit_admin);
        deleteBtn=findViewById(R.id.delete_product_admin_btn);

        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        
        displaySpisefcProductInfo();
        applychangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteThisProduct();
            }
        });
    }

    private void DeleteThisProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductsActivity.this,"تم حذف المنتج بنجاح",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainProductsActivity.this, FirstPageToAdminActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ApplyChanges() {
        String productNewName= name.getText().toString();
        String productNewDescription= description.getText().toString();
        String productNewPrice= price.getText().toString();
        String productNewImage= name.getText().toString();
        //tell the admin if edittexts is empty
        if (productNewName.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"الرجاء ادخال اسم المنتج",Toast.LENGTH_SHORT).show();
        }else if (productNewDescription.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"الرجاء ادخال معلومات المنتج",Toast.LENGTH_SHORT).show();
        }else if (productNewPrice.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"الرجاء ادخال سعر المنتج",Toast.LENGTH_SHORT).show();
        }else {
            //store the new data
            HashMap<String,Object> productMap=new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("description",productNewDescription);
            productMap.put("price",productNewPrice);
            productMap.put("pname",productNewName);
            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,"تم تعديل المعلومات بنجاح",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainProductsActivity.this,FirstPageToAdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }


    }

    private void displaySpisefcProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pname =dataSnapshot.child("pname").getValue().toString();
                    String pdescription=dataSnapshot.child("description").getValue().toString();
                    String Pprcie=dataSnapshot.child("price").getValue().toString();
                    String pimage=dataSnapshot.child("image").getValue().toString();

                    name.setText(pname);
                    description.setText(pdescription);
                    price.setText(Pprcie);
                    Picasso.get().load(pimage).into(imageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
